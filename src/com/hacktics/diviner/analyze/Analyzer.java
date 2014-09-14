package com.hacktics.diviner.analyze;

import java.util.TreeSet;

import javax.swing.JTextArea;

import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import com.hacktics.diviner.gui.scanwizard.ScanWizard;

/**
 * 
 * @author Eran Tamari
 *
 */

public  class Analyzer extends GenericAnalyzer {

	private int inputPageID;
	private int outputPageID;

	public Analyzer(HISTORY_MODE histMode, SCENARIO_MODE scenario , JTextArea detailsPanel, boolean isVerifyMode) {
		initAnalyzer(histMode, scenario, detailsPanel, isVerifyMode);
	}

	@Override
	public void analyze(boolean isTokenAppendMode) {

		detailsPanel.append(getTestModeDetails(scenario));
		this.isTokenAppendMode = isTokenAppendMode;

		for (RecordHistory sourcePageHistoryRecord : AnalyzerUtils.getHistoryOfSelectedHostToAnalyze())	{
			TreeSet<HtmlParameter> params = null;
			TreeSet<HtmlParameter> tempparams = null;
			HttpMessage sourcePage = sourcePageHistoryRecord.getHttpMessage(); 

			//Login pages are not to be tested when userLock is enabled
			if (ScanWizard.isUserLock() && AnalyzerUtils.isEqualToLogin(sourcePage)) {
				continue;
			}

			String sourcePageMethod = sourcePage.getRequestHeader().getMethod();
			detailsPanel.append(TOKENIZED_PAGE + sourcePage.getRequestHeader().getURI() + " (" + sourcePageMethod + ")" + "\n");
			inputPageID = sourcePageHistoryRecord.getHistoryId();	//????????????????????used for Mirror analyzer
			//TODO: ADD SUPPORT FOR COOKIE/AJAX PARAMS
			//test URL params for GET & HEAD, URL & BODY params for POST, NONE for other methods
			if(sourcePageMethod.equals(GET) || sourcePageMethod.equals(HEAD)) {
				params = sourcePage.getUrlParams();
			} 
			else if(sourcePageMethod.equals(POST)) {

				tempparams = sourcePage.getUrlParams(); //in case the post request also has URL params  
				params = sourcePage.getFormParams();
				params.addAll(tempparams);


			} 
			else {
				continue; //skip current loop since we don't support HTTP method
			}

			//If request has no parameters - go to the next source page in history
			if (!isRequestContainsParams(params)){	continue;	}	 

			for(RecordHistory targetPageHistoryRecord :  AnalyzerUtils.getHistoryOfSelectedHostToAnalyze())
			{
				HttpMessage targetPage = targetPageHistoryRecord.getHttpMessage();
				String targetPageMethod = targetPage.getRequestHeader().getMethod();
				outputPageID = targetPageHistoryRecord.getHistoryId();

				// When the source msg and target msg are equal - test for mirror output
				if (AnalyzerUtils.httpMessageEqual(targetPage, sourcePage)) {
					isSourceTargetIdentical = true;	
				} else	{ 
					isSourceTargetIdentical = false;	
				}
				detailsPanel.append(ANALYZED_PAGE + targetPage.getRequestHeader().getURI() + " (" + targetPageMethod + ")" + "\n");


				for (HtmlParameter param : params)
				{
					//Leave antiCSRF tokens untouched
					if (AnalyzerUtils.getCsrfTokens().contains(param)) {
						continue;
					}
					detailsPanel.append(TESTED_PARAM + param.getName() + " (" + param.getType() + ")" + "\n");

					if (isVerifyMode){
						verifyAnalyze(sourcePageHistoryRecord, targetPageHistoryRecord, param, sourcePageMethod);
					}
					else {
						analyzeParameter(sourcePageHistoryRecord, targetPageHistoryRecord, param, DBWRITE);
					}

					if (isFound)	//isFound value is set in isTokenFound function
					{
						detailsPanel.append(LINE_BREAK + FOUND_TEXT + param.getName()  + LINE_BREAK);
					}
				}

			}

		}		
	}

	public boolean isFound()
	{
		return isFound;
	}

	@Override
	protected boolean analyzeParameter(RecordHistory source, RecordHistory target, HtmlParameter parameter, boolean isWriteMode) {
		isFound = false;
		TOKEN_TYPE tokenType = TOKEN_TYPE.RANDOM_VALUE;

		if (Plugins.isRandomTokenEnabled()) {
			tokenType.setEnabled(TOKEN_TYPE.RANDOM_VALUE);
		}
		if (Plugins.isInvalidTokenEnabled()) {
			tokenType.setEnabled(TOKEN_TYPE.INVALID_VALUE);
		}
		if (Plugins.isValidTokenEnabled()) {
			tokenType.setEnabled(TOKEN_TYPE.VALID_VALUE);
		}

		try
		{
			if (! isSourceTargetIdentical){
				switch (scenario) {

				case DIRECT:
					for (TOKEN_TYPE type : TOKEN_TYPE.values()) {
						if (tokenType.isEnabled(type)){
							DirectAccessAnalyzer directAccessAnalyzer = new DirectAccessAnalyzer();
							directAccessAnalyzer.analyzeParamter(source, target, histMode, null, parameter, null,  null, false, isWriteMode, isTokenAppendMode, type);

							if (! isFound) {
								isFound = directAccessAnalyzer.isFound();
							}
						}
					}
					break;

				case LOGIN_FIRST:
					for (TOKEN_TYPE type : TOKEN_TYPE.values()) {
						if (tokenType.isEnabled(type)){
							LoginFirstAnalyzer loginFirstAnalyzer = new LoginFirstAnalyzer();
							loginFirstAnalyzer.analyzeParamter(source, target, histMode, null, parameter, null, null, false, false, false, isWriteMode, false, isTokenAppendMode, type);
							if (! isFound) {

								isFound = loginFirstAnalyzer.isFound();
							}
						}
					}
					break;

				case LOGIN_AFTER:
					for (TOKEN_TYPE type : TOKEN_TYPE.values()) {
						if (tokenType.isEnabled(type)){
							LoginAfterAnalyzer loginAfterAnalyzer = new LoginAfterAnalyzer();
							loginAfterAnalyzer.analyzeParamter(source, target, histMode, null, parameter, null, null, false, isWriteMode, isTokenAppendMode, type);
							if (! isFound) {

								isFound = loginAfterAnalyzer.isFound();
							}
						}
					}
					break;

				}
			}
			//Source and Target are Identical = Mirror parameter
			else {
				for (TOKEN_TYPE type : TOKEN_TYPE.values()) {
					if (tokenType.isEnabled(type)){
						MirrorAnalyzer mirrorAnalyzer = new MirrorAnalyzer();
						mirrorAnalyzer.analyzeParamter(source, parameter, isWriteMode, scenario.ordinal(), isTokenAppendMode, type, histMode);
						if(isFound==false) isFound = mirrorAnalyzer.isFound();
					}

				}
			}
		}
		catch (Exception e){	e.printStackTrace();	}

		return isFound;
	}

	//Returns true if request has parameters, else false
	private boolean isRequestContainsParams (TreeSet<HtmlParameter> params){

		boolean foundParams = FOUND;

		if (params.size() == 0){
			foundParams = !FOUND;	//No parameters in request
		}
		return foundParams;
	}

	@Override
	protected void sendRequestUntilPage(HttpMessage tokenizedMsg,
			TreeSet<HtmlParameter> sessionID) {
		// TODO Auto-generated method stub

	}

}
