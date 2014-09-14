package com.hacktics.diviner.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.analyze.HISTORY_MODE;
import com.hacktics.diviner.analyze.SCENARIO_MODE;
import com.hacktics.diviner.analyze.TOKEN_TYPE;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.database.DivinerTableResults;
import com.hacktics.diviner.gui.controllers.BlinkingButton;
import com.hacktics.diviner.zapDB.ZapHistoryDB;

/**
 * 
 * @author Eran Tamari
 *
 */

public class EntryPointsArray implements ActionListener{

	private static Advisor advisor;
	private static TaskList taskList;
	private static AdvisorTree advisorTree;
	private static HashMap<String, EntryPointPanel>  entryMap;
	private static ArrayList<String> filters;
	public EntryPointsArray(Advisor advisor, TaskList taskList, AdvisorTree advisorTree) {
		entryMap =  new HashMap<String, EntryPointPanel>();
		filters = new ArrayList<String>();
		EntryPointsArray.advisor = advisor;
		EntryPointsArray.taskList = taskList;
		EntryPointsArray.advisorTree = advisorTree;
	}

	private void addEntryPoint(EntryPointPanel entryPoint){

		//Don't add entry points that were already added in previous run
		if (!entryMap.containsKey(entryPoint.getName()))
		{
			entryMap.put(entryPoint.getName(), entryPoint);
			for (Parameter param : entryPoint.getParameterList().getParamList())	{
				param.addActionListener(this);
			}
		}
	}

	/**
	 * Set output types visible in GUI
	 * Can be Session,Output or Database
	 */
	public static void setAffectedOutputVisible(EntryPointPanel entryPoint) {

		if (entryPoint != null) {
			for (Parameter param : entryPoint.getParameterList().getParamList())	{
				if (param.getAffectedPages() != null) {

					//Set the affected pages' effects
					for (ParameterEffect affectedPage : param.getAffectedPages()) {
						switch(affectedPage.getType())	{
						case ParameterEffect.DATABASE_REFLECTION:
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.DATABASE_REFLECTION);
							break;
						case ParameterEffect.OUTPUT_REFLECTION:
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.OUTPUT_REFLECTION);
							break;
						case ParameterEffect.SESSION_REFLECTION:
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.SESSION_REFLECTION);
							break;
						case ParameterEffect.DATABASE_EXCEPTION:
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.DATABASE_EXCEPTION);
							break;
						case ParameterEffect.OUTPUT_EXCEPTION:
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.OUTPUT_EXCEPTION);
							break;
						case ParameterEffect.SESSION_EXCEPTION:
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.SESSION_EXCEPTION);
							break;
						}
						if (affectedPage.isDiff()) {
							entryMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.DIFF);
						}
					}	
				}
			}

		}

	}

	private ArrayList<EntryPointPanel> getAllEntryPoints() {
		return  new ArrayList<EntryPointPanel>(entryMap.values());
	}

	public void actionPerformed(ActionEvent e) {
		Parameter clickedParam = (Parameter)e.getSource();
		doClick(clickedParam);
	}

	public static void doClick(Parameter clickedParam) {
		
		//Show selected parameter's info in Advisor panel 
		advisorTree.showParameterInfo(clickedParam);
		
		//Do the blinking effect in all related pages and parameters
		ArrayList <BlinkingButton> blinkButtonsArray =  new ArrayList <BlinkingButton>();
		ArrayList<ParameterEffect> affectedPages = clickedParam.getAffectedPages();

		for (EntryPointPanel entryPoint : entryMap.values()) {

			//Restore all default colors once a parameter is clicked
			entryPoint.restoreAllDefaultColors(); 
			entryPoint.restoreColor();
		}		

		//Blinking Action when clicked
		if (affectedPages != null) {
			for (ParameterEffect affectedPage : affectedPages)	{

				EntryPointPanel entryPoint = entryMap.get(affectedPage.getOutputPage());
				entryPoint.changeColor();
				entryMap.get(clickedParam.getPageName()).changeColor();
				blinkButtonsArray.add(clickedParam);
				switch(affectedPage.getType()) {
				case ParameterEffect.DATABASE_REFLECTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.DATABASE_REFLECTION.ordinal()]);
					break;
				case ParameterEffect.OUTPUT_REFLECTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.OUTPUT_REFLECTION.ordinal()]);
					break;
				case ParameterEffect.SESSION_REFLECTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.SESSION_REFLECTION.ordinal()]);
					break;
				case ParameterEffect.DATABASE_EXCEPTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.DATABASE_EXCEPTION.ordinal()]);
					break;
				case ParameterEffect.OUTPUT_EXCEPTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.OUTPUT_EXCEPTION.ordinal()]);
					break;
				case ParameterEffect.SESSION_EXCEPTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.SESSION_EXCEPTION.ordinal()]);
					break;

				}	
				if (affectedPage.isDiff()) {
					entryPoint.getOutputPanel().setDiffPercentage(affectedPage.getDiffPercentage());
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.DIFF.ordinal()]);

				}

			}	

			//Implemented blinking in another loop in order to keep the blinks timing
			for (BlinkingButton blinkingButton : blinkButtonsArray) {
				blinkingButton.Blink();


			}
		}
	}

	/**
	 * Gets a list of Entry point panels that represent the analyze results
	 * @return
	 */
	public ArrayList<EntryPointPanel> getScanResults() {

		try{

			DivinerTableResults resultsTable = ZapHistoryDB.resultTable;
			ArrayList<String> pagesToShow = resultsTable.getPages();


			//Add pages to the screen
			for (String pageName : pagesToShow)
			{
				ArrayList<DivinerRecordResult> affectingParamsInPage = resultsTable.getResultsPerPage(pageName);
				ArrayList<String> otherParams = resultsTable.getOtherParams(pageName);
				ArrayList<Long> paramIdList = new ArrayList<Long>();
				int totalParamsCount = affectingParamsInPage.size() + otherParams.size();
				EntryPointPanel entryPoint = new EntryPointPanel(pageName ,true, new Statistics(totalParamsCount), resultsTable.isPageAuthenticated(pageName), -1);

				//Add parameters with effect on any type of output
				for (DivinerRecordResult affectingParam : affectingParamsInPage)
				{
					ArrayList<ParameterEffect> affectedPages;
					if (!paramIdList.contains(affectingParam.getParamID()))
					{
						paramIdList.add(affectingParam.getParamID());
						affectedPages = convertResultsToEffects(AnalyzerUtils.getResultsPerPageAndParam(pageName, affectingParam.getParamID()));
						ParameterInfo info = new ParameterInfo(affectingParam.getName(), affectingParam.getInPage(), affectedPages);
						entryPoint.getParameterList().addParameter(affectingParam.getParamID(), affectingParam.getInputID(), pageName, affectingParam.getName(), affectingParam.getValue() , Parameter.AFFECTTING, info);
						entryPoint.addMagnifyOption();	//The page has affect, so we need to add magnify option

					}
				}

				//Add parameters with no effect on output
				for (String noEffectParam : otherParams)
				{
					entryPoint.getParameterList().addParameter(-1, -1, pageName, noEffectParam, "", !Parameter.AFFECTTING, null);
				}

				addEntryPoint(entryPoint);

			}

			
			//Display the parameters and their effect in the corresponding page
			for (EntryPointPanel entry : entryMap.values()) {
				setAffectedOutputVisible(entry);
			}
			
			for (EntryPointPanel entry : entryMap.values()) {
				entry.setEffects();
			}
			
			
			//Required for the entry point clones which appear in the magnifying glass option
			for (EntryPointPanel entry : entryMap.values()) {	
				entry.setAffectedPages();
			}
			//Required for the entry point clones which appear in the magnifying glass option
			for (EntryPointPanel entry : entryMap.values()) {	
				entry.showParameters();
			}

			////////////////////////////



		}
		catch(SQLException ex){ex.printStackTrace();}

		//Update Tasks/Leads List
		taskList.showTasks();

		return  getAllEntryPoints();

	}


	private ArrayList<ParameterEffect> convertResultsToEffects(ArrayList<DivinerRecordResult> resultRecords)
	{
		ArrayList<ParameterEffect> effects = new ArrayList<ParameterEffect>();
		for (DivinerRecordResult record : resultRecords)
		{
			SCENARIO_MODE scenario = SCENARIO_MODE.values()[record.getScenario()];
			HISTORY_MODE histMode = HISTORY_MODE.values()[record.getHistMode()];
			TOKEN_TYPE tokenType = TOKEN_TYPE.values()[record.getTokenType()];

			effects.add(new ParameterEffect(record.getInputID(), record.getOutputID(), scenario, histMode, tokenType, record.isTokenAppendMode(), record.getValue(), record.getTokenValue(), record.getType(), record.getOutPage(), record.isParamDiff(), record.isParamReflected(), record.isParamException(), record.getDiffPercent(), record.getName()));                                     

		}
		return effects;
	}

	public ArrayList<EntryPointPanel> getZapRequests(String domain) {
		
		AnalyzerUtils.setHostForRequestsDisplay(domain);
		ArrayList<EntryPointPanel> result = new ArrayList<EntryPointPanel>();
	
		int requestIndex = AnalyzerUtils.getStartIndex() + 1; //Start from 1
		ArrayList<RecordHistory> requests = AnalyzerUtils.getHistoryOfSelectedHostForRequestsDisplay(filters);
		try {

			//Add pages to the screen
			for (RecordHistory request : requests)
			{

				String pageName = request.getHttpMessage().getRequestHeader().getURI().getPath();
				int numOfParams = getParamCount(request.getHttpMessage());
				String method = request.getHttpMessage().getRequestHeader().getMethod().toUpperCase();
				String responseCode = Integer.toString(request.getHttpMessage().getResponseHeader().getStatusCode());	//Zap bug not getting code for directories
				boolean isSSL =  request.getHttpMessage().getRequestHeader().getSecure();
				EntryPointPanel entryPoint = new EntryPointPanel(pageName, false, new Statistics(numOfParams, request.getHistoryId(), responseCode, isSSL, method), false, requestIndex);
				requestIndex ++;
				//Add parameters with no effect on output
				for (HtmlParameter param : AnalyzerUtils.getMsgParams(request.getHttpMessage())) {
					entryPoint.getParameterList().addParameter(-1, -1, pageName, param.getName(), param.getValue(), !Parameter.AFFECTTING, null);
				}
				result.add(entryPoint);
			}
		}
		catch(URIException e) { e.printStackTrace(); }
		return  result;
	}


	private int getParamCount(HttpMessage msg) {
		int result = 0;
		String [] names = msg.getParamNames();
		for (String name : names) {
			if (name != "") {
				result++;
			}
		}
		return result;
	}


	public static EntryPointPanel getClone(String pageName) {
		return entryMap.get(pageName).clone();
	}

	public static void addRequestFilter(String filter) {
		filters.add(filter);
	}

	public static void cleanFilters() {
		filters.clear();
	}
	
	public static Parameter getParameterByName(String pageName, String paramName) {
		return entryMap.get(pageName).getParameterList().getParamByName(paramName);
	}
}
