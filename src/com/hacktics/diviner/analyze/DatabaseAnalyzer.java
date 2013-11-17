package com.hacktics.diviner.analyze;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JTextArea;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HtmlParameter.Type;
import org.parosproxy.paros.network.HttpMessage;

import com.hacktics.diviner.constants.DivinerFlags;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.database.DivinerTableFlow;
import com.hacktics.diviner.gui.ParameterEffect;
import com.hacktics.diviner.gui.RESULT_TYPE;
import com.hacktics.diviner.gui.scanwizard.ScanWizard;
import com.hacktics.diviner.zapDB.ZapHistoryDB;

/**
 * 
 * @author Eran Tamari
 *	The database analyzer currently does not support token append mode
 */

public class DatabaseAnalyzer extends AbstractAnalyzer{

	private RecordHistory source;
	private RecordHistory target;
	private JTextArea detailsPanel;
	private RESULT_TYPE outputPlugin;
	private ArrayList<DivinerRecordResult> results;
	private boolean isfullHistory;
	private long resultId;
	private String token;

	public DatabaseAnalyzer(JTextArea detailsPanel){
		this.detailsPanel = detailsPanel;
		results = AnalyzerUtils.getAllResults();
		this.isIntanceOfDbAnalyzer = true;
	}


	/**
	 * Iterate all results of session/output parameters and check which is a database parameter
	 */
	public void analyze() {

		detailsPanel.append("\nStarting Database Analysis\n");

		long globalStartTime = System.currentTimeMillis();
		long startTime = 0;
		long endTime = 0;

		if(DivinerFlags.DEBUG_FLAG == true) {
			detailsPanel.append("\nCurrently Verifying " + results.size() + " Results in the Database Analysis\n");
		}

		for (DivinerRecordResult resultRecord : results) {
			resultId = resultRecord.getResultId();
			source = AnalyzerUtils.readZapHistory(resultRecord.getInputID());
			target = AnalyzerUtils.readZapHistory(resultRecord.getOutputID());

			HtmlParameter param = new HtmlParameter(getType(resultRecord.getType()), resultRecord.getName(), resultRecord.getValue());
			if (plantTokenInAppDB(resultRecord)) {	//"Plant" a token in the application

				this.histMode = HISTORY_MODE.values()[resultRecord.getHistMode()];
				this.scenario = SCENARIO_MODE.values()[resultRecord.getScenario()];
				this.tokenType = TOKEN_TYPE.values()[resultRecord.getTokenType()];
				this.outputPlugin = RESULT_TYPE.values()[resultRecord.getOutputPlugin()];

				startTime = System.currentTimeMillis();
				ResultItem resultAnalysis = this.analyzeParameter(resultRecord); //execute the local analyzeParameter method, which executes the entire scenario - WITHOUT the parameter entry point
				endTime = System.currentTimeMillis();

				if(DivinerFlags.DEBUG_FLAG == true) {
					detailsPanel.append("\nExecuted history up to target " + resultRecord.getOutPage() + 
							", for the source page " + resultRecord.getInPage() + ", parameter " + resultRecord.getName() + ", in: " + (startTime-endTime) + "\n");
				}

				//TODO: !!!INCOMPLETE IMPLEMENTATION!!!
				//currently, we intentionally miss "insert" scenarios in order to avoid diff false positives when the source page in necessary to the target page proper execution
				//will be mitigated once we manage to conclude which pages are necessary for each execution, and once we perform the initial history potential "insert" detection
				//OR if we execute the database verification with NO_history first immediately after an issue detection
				if ((resultAnalysis.isTokenFound()) || (resultRecord.getDiffPercent() > 30 && resultAnalysis.getDiffPercetage() == resultRecord.getDiffPercent())) {	//If token found or Diff == Diff found at session/output analysis 
					try {
						startTime = System.currentTimeMillis();
						writeToDB(source, target, token, param, AnalyzerUtils.getMsgParams(source.getHttpMessage()), DBWRITE, resultAnalysis.getDiffPercetage(), tokenType, outputPlugin);
						endTime = System.currentTimeMillis();

						if(DivinerFlags.DEBUG_FLAG == true) {
							detailsPanel.append("\nCreated database row for the target page " + resultRecord.getOutPage() + 
									", for the source page " + resultRecord.getInPage() + ", parameter " + resultRecord.getName() + ", in: " + (startTime-endTime) + "\n");
						}

						int sourceId = source.getHistoryId();
						String sourceURI = source.getHttpMessage().getRequestHeader().getURI().getPath() == null ? "/" : source.getHttpMessage().getRequestHeader().getURI().getPath();
						String targetURI = target.getHttpMessage().getRequestHeader().getURI().getPath() == null ? "/" : target.getHttpMessage().getRequestHeader().getURI().getPath();

						if (outputPlugin.equals(RESULT_TYPE.REFLECTION)) {
							CodeBehaviour.databaseReflection(sourceURI, targetURI, param.getName(), CodeBehaviour.getCodeId(), null, DivinerTableFlow.DEFAULT_ORDER, sourceId, WRITE_DB_FLOW);
						}
					}
					catch (Exception e) { e.printStackTrace(); } 
				}
			}
		}

		long globalEndTime = System.currentTimeMillis();

		if(DivinerFlags.DEBUG_FLAG == true) {
			detailsPanel.append("\nDatabase Analysis executed in " + (globalEndTime-globalStartTime) + "\n");
		}
	}

	private Type getType(int typeOrdinal) {
		if (typeOrdinal == Type.cookie.ordinal()) {
			return Type.cookie;
		}
		else if (typeOrdinal == Type.form.ordinal()) {
			return Type.form;
		}
		else {
			return Type.url;
		}
	}

	private boolean plantTokenInAppDB(DivinerRecordResult resultRecord) {

		boolean result = !FOUND;
		HttpMessage sourceMsg = source.getHttpMessage();
		HtmlParameter testedParameter = getParamByName(sourceMsg, resultRecord.getName());
		SCENARIO_MODE reproduceScenario = SCENARIO_MODE.values()[resultRecord.getScenario()];
		HISTORY_MODE reproduceHistMode = HISTORY_MODE.values()[resultRecord.getHistMode()];
		TOKEN_TYPE reproduceTokenType = TOKEN_TYPE.values()[resultRecord.getTokenType()]; 

		switch (reproduceScenario) {
		case DIRECT:
			DirectAccessAnalyzer directAnalyzer = new DirectAccessAnalyzer();
			directAnalyzer.analyzeParamter(source, target, reproduceHistMode, null, testedParameter, null, null, false, false, false, reproduceTokenType);
			result = directAnalyzer.isFound();
			token = directAnalyzer.getToken();
			break;

		case LOGIN_FIRST:
			LoginFirstAnalyzer loginFirstAnalyzer = new LoginFirstAnalyzer();
			loginFirstAnalyzer .analyzeParamter(source, target, reproduceHistMode, null, testedParameter, null, null, false, false, false, false, false, false, reproduceTokenType);
			result = loginFirstAnalyzer.isFound();
			token = loginFirstAnalyzer.getToken();

			break;

		case LOGIN_AFTER:
			LoginAfterAnalyzer loginAfterAnalyzer = new LoginAfterAnalyzer();
			loginAfterAnalyzer.analyzeParamter(source, target, reproduceHistMode, null, testedParameter, null, null, false, false, false, reproduceTokenType);
			result = loginAfterAnalyzer.isFound();
			token = loginAfterAnalyzer.getToken();

			break;
		}
		return result;
	}


	private HtmlParameter getParamByName(HttpMessage msg, String paramName)
	{
		HtmlParameter result = null;
		String method = msg.getRequestHeader().getMethod().toString();
		TreeSet<HtmlParameter> params = method.equals(POST) ? msg.getFormParams() : msg.getUrlParams();
		for (HtmlParameter param : params){
			if (param.getName().equals(paramName)){
				result = param;
				break;
			}
		}

		return result;
	}


	protected void sendRequestUntilPage(TreeSet<HtmlParameter> sessionID) {
		AnalyzerUtils.sendNotAffectingRequestUntilPage(source, target, sessionID, isfullHistory, ScanWizard.getHistoryLimit());
	}


	private ResultItem analyzeParameter(DivinerRecordResult resultRecord) {

		ResultItem resultItem = null;

		HtmlParameter testedParameter = getParamByName(source.getHttpMessage(), resultRecord.getName());

		switch (scenario) {
		case DIRECT:
			DirectAccessAnalyzer directAnalyzer = new DirectAccessAnalyzer(outputPlugin);
			directAnalyzer.analyzeParamter(source, target, histMode, null, testedParameter, token, null, true, false, false, tokenType);
			resultItem = new ResultItem(directAnalyzer.isFound(), directAnalyzer.getDiffPercentage());
			break;

		case LOGIN_FIRST:
			LoginFirstAnalyzer loginFirstAnalyzer = new LoginFirstAnalyzer(outputPlugin);
			loginFirstAnalyzer.analyzeParamter(source, target, histMode, null, testedParameter, token, null, true, false, false, false, false, false, tokenType);
			resultItem = new ResultItem(loginFirstAnalyzer.isFound(), loginFirstAnalyzer.getDiffPercentage());
			break;

		case LOGIN_AFTER:
			LoginAfterAnalyzer loginAfterAnalyzer = new LoginAfterAnalyzer(outputPlugin);
			loginAfterAnalyzer.analyzeParamter(source, target, histMode, null, testedParameter, token, null, true, false, false, tokenType);
			resultItem = new ResultItem(loginAfterAnalyzer.isFound(), loginAfterAnalyzer.getDiffPercentage());
			break;
		}

		return resultItem;
	}

	@Override
	protected long writeToDB(RecordHistory source, RecordHistory target, String token , HtmlParameter suspectParam, TreeSet<HtmlParameter> params, boolean writeMode, int diffPercent, TOKEN_TYPE TokenType, RESULT_TYPE outputPlugin) throws SQLException {
		int effect = 0;

		if (DivinerFlags.DEBUG_FLAG) {
			System.out.println("WIN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
		}
		switch (outputPlugin) {
		case REFLECTION:
			effect = ParameterEffect.DATABASE_REFLECTION;
			break;

		case DIFF:
			effect = ParameterEffect.DIFF;
			break;

		case EXCEPTION:
			effect = ParameterEffect.DATABASE_EXCEPTION;
			break;

		}

		ZapHistoryDB.resultTable.updateResultType(resultId, effect, outputPlugin.ordinal());
		ZapHistoryDB.flowTable.deleteFlow(resultId);	//The session flow should be replaced with database flow
		return WRITE_DB_FLOW;	//If DB analyzer writes to DB we don't care about the rowID, since we don't have to change it in the future. (unlike in case of session parameter) 
	}
}
