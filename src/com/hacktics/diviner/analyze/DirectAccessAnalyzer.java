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
public class DirectAccessAnalyzer extends AbstractAnalyzer {

	public DirectAccessAnalyzer() {
		this.scenario = SCENARIO_MODE.DIRECT;
		this.runOnlyOnePlugin = false;
	}

	public DirectAccessAnalyzer(RESULT_TYPE selectedPlugin) {
		this.scenario = SCENARIO_MODE.DIRECT;
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
			boolean skipSourceAccess, boolean isDBWrite, boolean isTokenAppendMode, TOKEN_TYPE t_type) {
		
		this.tokenType = t_type;

		//Login should not be the source page in this scenario
		if (AnalyzerUtils.isEqualToLogin(source.getHttpMessage())) {
			return;
		}
		this.isTokenAppendMode = isTokenAppendMode; 
		this.histMode = histroyFlag;
		//a storage to contain any new cookies that are generated in the process
		TreeSet<HtmlParameter> newSession = null;
		FlowDocumentation newSessionFlow = null; //a local copy of the current flow object
		
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

		if (!skipSourceAccess) {
			//The token should be searched in the target page response
			newSession = sourcePageAccess(source, target, histroyFlag, sessionCookie, param, token, flow, !SOURCE_AND_TARGET_IDENTICAL, isDBWrite, isTokenAppendMode);

			//if isSourceTargetIdentical - need to search token in source page response, if not - in target page 

			//if any set-cookie header returned, merge/override cookie variables and send in addition
			//to the original session, to cover an optional flow.
			if (newSession != null) {

				newSessionFlow = flow.clone();
				newSessionFlow.addResponseDocumentation(token, sessionCookie, newSession, "0", "DIRECT_METHOD-NEW_SESSION"); //response code is unknown here
				newSessionFlow.setDirectSourceAccessSessionChange(true);
				
				//if new param - append, if replaces param - replace
				TreeSet<HtmlParameter> updatedSession = AnalyzerUtils.getCombindCookies(sessionCookie, newSession);
				//access the target page
				targetAccess(source, target, updatedSession, token, param, newSessionFlow, isDBWrite);
			}

		}
		//SkipSource == true
		else {
			sendHistory(histMode, sessionCookie, source, target);
		}
		flow.addResponseDocumentation(token, sessionCookie, newSession, "0", "DIRECT_METHOD-ORIGINAL_SESSION"); //response code is unknown here
		//access the target entry point with the original session, without any modifications
		targetAccess(source, target, sessionCookie, token, param, flow, isDBWrite);
	}



}
