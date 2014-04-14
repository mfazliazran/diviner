package com.hacktics.diviner.analyze;

import java.util.TreeSet;

import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HtmlParameter;
import com.hacktics.diviner.gui.OUTPUT_TYPE;

/**
 * 
 * @author Eran Tamari
 *
 */

public class MirrorAnalyzer extends AbstractAnalyzer {

	private static int lastTestedScenarioId = SCENARIO_MODE.values().length + 1; //init to a number larger than the number of scenarios
	private TreeSet<HtmlParameter> sessionCookie;
	private String token;

	public void analyzeParamter(RecordHistory source, HtmlParameter param, boolean isDBWrite, int scenarioId, boolean isTokenAppendMode, TOKEN_TYPE t_type, HISTORY_MODE histMode) {

		//Makes sure that only one of the running scenario will run the mirror tests, since they identical for every scenario
		if (scenarioId <= lastTestedScenarioId) { 
			lastTestedScenarioId = scenarioId;
		}
		//This record was already tested - so result == false automatically
		else {
			return;	
		}


		boolean result = false;
		tokenType = t_type;
		this.histMode = histMode;
		FlowDocumentation flow = new FlowDocumentation();
		flow.setOutputType(OUTPUT_TYPE.OUTPUT_REFLECTION);

		//Login session 
		this.scenario = SCENARIO_MODE.LOGIN_FIRST;
		token = generateToken(param.getValue());
		sessionCookie = AnalyzerUtils.getNewLoginSession(null);
		sourcePageAccess(source, source, histMode, sessionCookie, param, token, flow, SOURCE_AND_TARGET_IDENTICAL, isDBWrite, isTokenAppendMode);
		if (isFound() == FOUND) {
			result = FOUND;

		}

		//Public session 
		this.scenario = SCENARIO_MODE.DIRECT;
		token = generateToken(param.getValue());
		sessionCookie = AnalyzerUtils.getNewPublicSession();
		sourcePageAccess(source, source, histMode, sessionCookie, param, token, flow, SOURCE_AND_TARGET_IDENTICAL, isDBWrite, isTokenAppendMode);
		if (isFound() == FOUND) {
			result = FOUND;
		}

		isFound = result;
	}
}
