/**
 * 
 */
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


public class LoginAfterAnalyzer extends AbstractAnalyzer {

	public LoginAfterAnalyzer() {
		this.scenario = SCENARIO_MODE.LOGIN_AFTER;
	}
	
	//When other scenarios are using this class as a sub scenario - it needs to run under their title
	public LoginAfterAnalyzer(SCENARIO_MODE scenario) {
		this.scenario = scenario;
	}
	
	public LoginAfterAnalyzer(RESULT_TYPE selectedPlugin) {
		this.scenario = SCENARIO_MODE.LOGIN_AFTER;
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

	public void analyzeParamter(RecordHistory source, RecordHistory target, HISTORY_MODE histroyFlag, TreeSet<HtmlParameter> sessionCookie, HtmlParameter param, String token, FlowDocumentation flow,
			boolean skipSourceAccess, boolean isDBWrite, boolean isTokenAppendMode, TOKEN_TYPE t_type){

		this.isTokenAppendMode = isTokenAppendMode;
		this.histMode = histroyFlag;
		this.tokenType = t_type;
		//a storage to contain any new cookies that are generated in the process
		TreeSet<HtmlParameter> newSession = null;
		FlowDocumentation newSessionFlow = null; //a local copy of the current flow object
		TreeSet<HtmlParameter> loginSession = null;
		FlowDocumentation loginSessionFlow = null; //a local copy of the current flow object
		//Stores combined values from 2 cookie
		TreeSet<HtmlParameter> combinedSession = null;
		//Stores combined values from 2 cookie
		TreeSet<HtmlParameter> combinedSessionLogin = null;
		//generate a new public session cookie if not received

		if (sessionCookie == null) {
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

		if (!skipSourceAccess) {

			newSession = sourcePageAccess(source, target, histroyFlag, sessionCookie, param, token, flow, !SOURCE_AND_TARGET_IDENTICAL, isDBWrite, isTokenAppendMode);

			//newSession = sourcePageAccess(sourceMsg, targetMsg, histroyFlag, sessionCookie, param, token, !SOURCE_AND_TARGET_IDENTICAL, isDBWrite);

			if (newSession != null) {

				newSessionFlow = flow.clone();
				newSessionFlow.addResponseDocumentation(token, sessionCookie, newSession, "0", "LOGIN_AFTER_METHOD-NEW_SESSION"); //response code is unknown here
				newSessionFlow.setLoginAfterSourceAccessSessionChange(true);

				combinedSession = AnalyzerUtils.getCombindCookies(sessionCookie, newSession);
				loginSession = AnalyzerUtils.getNewLoginSession(combinedSession);
				if (loginSession != null) {

					loginSessionFlow = newSessionFlow.clone();
					loginSessionFlow.addResponseDocumentation(token, newSession, loginSession, "0", "LOGIN_AFTER_METHOD-NEW_SESSION-LOGIN_SESSION"); //response code is unknown here
					loginSessionFlow.setLoginAfterLoginAccessSessionChange(true);

					combinedSessionLogin =  AnalyzerUtils.getCombindCookies(combinedSession, loginSession);

					targetAccess(source, target, combinedSessionLogin, token, param, loginSessionFlow, isDBWrite);
					//targetAccess(sourceMsg, targetMsg, combinedSessionLogin, token, param.getName(), isDBWrite);
				}

				//Currently - we do not handle a scenario in which ONLY partial cookie content is required
				targetAccess(source, target, combinedSession, token, param, newSessionFlow, isDBWrite);
				//targetAccess(sourceMsg, targetMsg, combinedSession, token, param.getName(), isDBWrite);
			}
		}
		//SkipSource == true
		else {
			sendHistory(histMode, sessionCookie, source, target);
		}
		loginSession = AnalyzerUtils.getNewLoginSession(sessionCookie);
		combinedSessionLogin =  AnalyzerUtils.getCombindCookies(sessionCookie, loginSession);

		if (loginSession != null) {

			loginSessionFlow = flow.clone();
			loginSessionFlow.addResponseDocumentation(token, sessionCookie, loginSession, "0", "LOGIN_AFTER_METHOD-ORIGINAL_SESSION-LOGIN_SESSION"); //response code is unknown here
			loginSessionFlow.setLoginAfterLoginAccessSessionChange(true);

			targetAccess(source, target, combinedSessionLogin, token, param, loginSessionFlow, isDBWrite);

			//targetAccess(sourceMsg, targetMsg, combinedSessionLogin, token, param.getName(), isDBWrite);
		}

		flow.addResponseDocumentation(token, sessionCookie, newSession, "0", "LOGIN_AFTER_METHOD-ORIGINAL_SESSION"); //response code is unknown here
		//access the target entry point with the original session, without any modifications
		targetAccess(source, target, sessionCookie, token, param, flow, isDBWrite);
		//targetAccess(sourceMsg, targetMsg, sessionCookie, token, param.getName(), isDBWrite);

	}
}
