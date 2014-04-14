package com.hacktics.diviner.analyze;

import java.util.TreeSet;

import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HtmlParameter;

import com.hacktics.diviner.gui.RESULT_TYPE;

/**
 * 
 * @author Eran Tamari
 *
 */

public class LoginFirstAnalyzer extends AbstractAnalyzer {

	private static boolean IGNORE_LOGIN_SESSION = true;
	private static boolean LOGIN_TWICE = true;
	private static boolean REPERFORM_LOGIN = true;

	public LoginFirstAnalyzer() {
		this.scenario = SCENARIO_MODE.LOGIN_FIRST;
		this.runOnlyOnePlugin = false;
	}

	public LoginFirstAnalyzer(RESULT_TYPE selectedPlugin) {
		this.scenario = SCENARIO_MODE.LOGIN_FIRST;
		this.runOnlyOnePlugin = true;
		this.selectedPlugin = selectedPlugin;
	}
	
	/**
	 * Implements the SOURCE to TARGET direct scenario.
	 * 
	 * @param sourceMsg the source entry point pulled from ZAP DB
	 * @param targetMsg the target entry point pulled from ZAP DB
	 * @param historyFlag an ENUM flag that signifies whether or not to access the history
	 * @param sessionCookie contains the session id and other cookie parameters
	 * @param param the parameter to test
	 * @param token the random token to send in the parameter 
	 * @param isOutputCheck a boolean that is true when the token should be searched in the source page response
	 * @param flow the flow documentation object
	 * @param skipSourceAccess causes the method to skip the source page access (useful when reused by loginfirst method/db verification)
	 */
	public void analyzeParamter(RecordHistory source, RecordHistory target,HISTORY_MODE histroyFlag, TreeSet<HtmlParameter> sessionCookie, HtmlParameter param, String token, FlowDocumentation flow,
			boolean skipSourceAccess, boolean ignoreLoginSession, boolean rePerformLogin, boolean isDBWrite, boolean loginTwice, boolean isTokenAppendMode, TOKEN_TYPE t_type) {

		this.isTokenAppendMode = isTokenAppendMode;
		this.histMode = histroyFlag;
		this.tokenType = t_type;

		//a storage to contain any new cookies that are generated in the process
		TreeSet<HtmlParameter> newSession = null;
		FlowDocumentation newSessionFlow = null; //a local copy of the current flow object
		TreeSet<HtmlParameter> loginSession = null;
		TreeSet<HtmlParameter> loginTwiceSession = null;
		FlowDocumentation loginSessionFlow = null; //a local copy of the current flow object
		//Stores combined values from 2 cookie
		TreeSet<HtmlParameter> combinedSession = null;
		//Stores combined values from 2 cookie
		TreeSet<HtmlParameter> combinedSessionLogin = null;
		//generate a new public session cookie if not received

		if (sessionCookie == null){
			sessionCookie = AnalyzerUtils.getNewPublicSession();
		}

		//generate a new token if token not received
		if (token == null) {
			token = generateToken(param.getValue());
		}
	
		if (flow == null) {
			flow = new FlowDocumentation();
			flow.setHistory(histroyFlag);
			flow.setScenario(SCENARIO_MODE.DIRECT);//CUSTOMIZE
			flow.setInitialToken(token);
			flow.setSourceId(source.getHistoryId());
			flow.setTargetId(target.getHistoryId());
		}
		flow.setToken(token); //override token in case one was sent to method

		if(ignoreLoginSession == false) {
			loginSession = AnalyzerUtils.getNewLoginSession(sessionCookie);
		}
		//In case both the login page and the source page replace the session (and invalidate the previous) we need -> Login->Source->Login-> Target, even though it's called in recursion
		if(loginTwice == true) {
			loginTwiceSession = AnalyzerUtils.getNewLoginSession(sessionCookie);
			sessionCookie = AnalyzerUtils.getCombindCookies(sessionCookie, loginTwiceSession);
		}
		if (loginSession != null) {

			combinedSessionLogin = AnalyzerUtils.getCombindCookies(sessionCookie, loginSession);

			loginSessionFlow = flow.clone();
			loginSessionFlow.addResponseDocumentation(token, sessionCookie, loginSession, "0", "LOGIN_FIRST_METHOD-LOGIN_SESSION"); //response code is unknown here
			loginSessionFlow.setLoginFirstLoginAccessSessionChange(true);

			analyzeParamter(source, target, histroyFlag, combinedSessionLogin, param, token, loginSessionFlow, skipSourceAccess, IGNORE_LOGIN_SESSION, !REPERFORM_LOGIN, isDBWrite, !LOGIN_TWICE, isTokenAppendMode, t_type);
		}
		
		if (!skipSourceAccess) {
			newSession = sourcePageAccess(source, target, histroyFlag, sessionCookie, param, token, flow, !SOURCE_AND_TARGET_IDENTICAL, isDBWrite, isTokenAppendMode);
			//newSession = sourcePageAccess(sourceMsg, targetMsg, histroyFlag, sessionCookie, param, token, !SOURCE_AND_TARGET_IDENTICAL, isDBWrite);

			if (newSession != null) {

				combinedSession = AnalyzerUtils.getCombindCookies(sessionCookie, newSession);

				newSessionFlow = flow.clone();
				newSessionFlow.addResponseDocumentation(token, sessionCookie, newSession, "0", "LOGIN_FIRST_METHOD-NEW_SESSION"); //response code is unknown here
				newSessionFlow.setLoginFirstSourceAccessSessionChange(true);

				if (rePerformLogin == true) {
					newSessionFlow.setReLoginFlag(true);

					LoginAfterAnalyzer loginAfterAnalyzer = new LoginAfterAnalyzer(SCENARIO_MODE.LOGIN_FIRST);

					loginAfterAnalyzer.analyzeParamter(source, target, histroyFlag, combinedSession, param, token, newSessionFlow, SKIP_SOURCE, isDBWrite, isTokenAppendMode, t_type);
					loginAfterAnalyzer.analyzeParamter(source, target, histroyFlag, sessionCookie, param, token, newSessionFlow, SKIP_SOURCE, isDBWrite, isTokenAppendMode, t_type);
				}
				//In case reLogin == false
				else {
					newSessionFlow.setReLoginFlag(false);

					targetAccess(source, target, combinedSession, token, param, newSessionFlow, isDBWrite);

					targetAccess(source, target, sessionCookie, token, param, newSessionFlow, isDBWrite);
					//send null in session and token so all will be redone, including session generation & token generation - ONLY HERE 
					analyzeParamter(source, target, histroyFlag, null, param, null, newSessionFlow, skipSourceAccess, IGNORE_LOGIN_SESSION, REPERFORM_LOGIN, isDBWrite, LOGIN_TWICE, isTokenAppendMode, t_type);
				}
			}
			//In case newSession == null
		}
		else { //SkipSource 
			sendHistory(histMode, sessionCookie, source, target);
			//targetAccess(source, target, sessionCookie, token, param.getName(), flow, isDBWrite);
		}


		flow.addResponseDocumentation(token, sessionCookie, newSession, "0", "LOGIN_FIRST_METHOD-ORIGINAL_SESSION"); //response code is unknown here
		//access the target entry point with the original session, without any modifications
		targetAccess(source, target, sessionCookie, token, param, flow, isDBWrite);
		//targetAccess(sourceMsg, targetMsg, sessionCookie, token, param.getName(), isDBWrite);

	}
}