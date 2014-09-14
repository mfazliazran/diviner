package com.hacktics.diviner.analyze;

import java.util.TreeSet;
import javax.swing.JTextArea;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

/**
 * 
 * @author Eran Tamari
 *
 */

public abstract class GenericAnalyzer {

	protected HISTORY_MODE histMode;
	protected JTextArea detailsPanel;
	protected TreeSet<HtmlParameter> sessionID ;
	protected boolean isVerifyMode;
	protected String token;
	protected boolean isFound;
	protected boolean isTokenAppendMode;
	protected SCENARIO_MODE scenario;
	protected boolean isSourceTargetIdentical = false; //Flags output test type
	protected static final String LINE_BREAK = "\n-------------------------------------------------------------------------------------------\n";
	protected static final String POST = "POST";
	protected static final String GET = "GET";
	protected static final String HEAD = "HEAD";
	protected static final String TOKENIZED_PAGE = "\nSource Page: ";
	protected static final String ANALYZED_PAGE = "  Target Page: ";
	protected static final String TESTED_PARAM = "\tTested Source Parameter: ";
	protected static final String FOUND_TEXT = " This may be interesting...\n";
	protected static final String SET_COOKIE = "Set-Cookie";
	protected static final String NO_HIST = "No History Mode";
	protected static final String FULL_HIST = " Full History Mode";
	protected static final String PARTIAL_HIST = "Partial History Mode";

	protected static final boolean FOUND = true;
	protected static final boolean DBWRITE = true;
	
	protected void initAnalyzer(HISTORY_MODE histMode, SCENARIO_MODE scenario, JTextArea detailsPanel, boolean isVerifyMode){
		this.histMode = histMode;
		this.detailsPanel = detailsPanel;
		this.scenario = scenario;
		this.isVerifyMode = isVerifyMode;
	}
	
	public abstract void analyze(boolean isTokenAppendMode);
	

	
	protected boolean verifyAnalyze(RecordHistory source, RecordHistory target, HtmlParameter param, String method)
	{
		
		if (analyzeParameter(source, target, param, !DBWRITE))
		{
			return analyzeParameter(source, target, param, DBWRITE);
		}
		return !FOUND;
	}
	
	protected abstract boolean  analyzeParameter(RecordHistory source , RecordHistory target , HtmlParameter parameter, boolean isWriteMode);

	protected String getTestModeDetails(SCENARIO_MODE typeOfTest)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(typeOfTest.text);
		switch (histMode) {
			case NO_HISTORY:
				sb.append("\t\"" + NO_HIST +"\" " + LINE_BREAK);
				break;
			case PARTIAL_HISTORY:
				sb.append("\t\"" + PARTIAL_HIST + "\" " + LINE_BREAK);
				break;
			case FULL_HISTORY:
				sb.append("\t\"" + FULL_HIST + "\" " + LINE_BREAK);
				break;
		case CUSTOM_HISTORY:
			break;
		default:
			break;
		}
		return sb.toString();
	}


	protected abstract void sendRequestUntilPage(HttpMessage tokenizedMsg, TreeSet<HtmlParameter> sessionID);

	protected String getToken()
	{
		return token;
	}
}
