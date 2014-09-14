package com.hacktics.diviner.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.analyze.HISTORY_MODE;
import com.hacktics.diviner.analyze.SCENARIO_MODE;
import com.hacktics.diviner.analyze.TOKEN_TYPE;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.gui.scanwizard.ScanWizard;

//TODO:Divide the HTML to reflection/exception...
public class ReportGenerator {

	private static HashSet<ParameterEffect> results;
	private static final String HTML_PAGE_START = "<html><title>Diviner Results</title><body><h1>Diviner Results for " + ScanWizard.getAnalyzedDomain() + "</h1>";
	private static final String HTML_PAGE_END = "</body></html>";
	private static final String TABLE_START = "<p></p><table width=\"100%\" border=\"0\">";
	private static final String TABLE_END = "</table>";
	private static final String TITLE_REFCLETION = "<h2>Reflection Results</h2>"; 
	private static final String TITLE_DIFF = "<h2>Content Diff Results</h2>";
	private static final String TITLE_EXCEPTION = "<h2>Exception Results</h2>";
	
	public static void setResultsInReport(HashSet<ParameterEffect> results) {

		ReportGenerator.results = results;
	}

	/*Since token value is distinct, create a custom comparator which will check for duplicates based on predefined fields*/
	public static ArrayList<DivinerRecordResult> getDistinct(ArrayList<DivinerRecordResult> fullList)
	{		
		recordCompare reCompare = new recordCompare(); //Create a custom comparator
		
		//Assign the comparator and override the compare method
		SortedSet<DivinerRecordResult> tree = new TreeSet<DivinerRecordResult>(reCompare);
		
		//Add the results to a tree set while comparing and removing duplicated pairs
		for (DivinerRecordResult record: fullList) {
			DivinerRecordResult element  = record;
			tree.add(new DivinerRecordResult(element));
		}
		
		//Copy the tree set content to a new list
		ArrayList<DivinerRecordResult> dList = new ArrayList<DivinerRecordResult>(tree);
		
		//Remove duplicated records on unsorted the final unsorted list of results
		for (int i=0; i<dList.size(); i++) {
			for (int j=i+1; j<dList.size(); j++) {
				if (reCompare.isRecordEqual(dList.get(i),dList.get(j))) {
					dList.remove(j); //Remove the duplicated record from list
					j--; //Decrement j case removal so we won't miss potential duplicates on the former j index
				}
			}
		}
		return dList;
	}
	
	public static void generateReport(File HTMLReport) {
		int resultCounter = 0;
		StringBuffer htmlText = new StringBuffer(HTML_PAGE_START);
		StringBuffer htmlTextReflection = new StringBuffer(TITLE_REFCLETION);
		StringBuffer htmlTextException = new StringBuffer(TITLE_EXCEPTION);
		StringBuffer htmlTextDiff = new StringBuffer(TITLE_DIFF);
		StringBuffer htmlTextTemp = new StringBuffer();

		//Get results from DB
		ArrayList<DivinerRecordResult> resultsList = null;
		
		try {
			resultsList = getDistinct(AnalyzerUtils.getAllDistinctResults());
		} 
		catch(Exception ex) { GuiUtils.getGuiUtils().showErrorDialog(new JDialog(), "No results to show in report");};

		if (resultsList != null) {
			for (DivinerRecordResult record : resultsList) {
				
				resultCounter++;
				
				switch  (OUTPUT_TYPE.values()[record.getType()]) {
				
					case DATABASE_REFLECTION:
						htmlTextTemp = htmlTextReflection;
						break;
					case DATABASE_EXCEPTION:
						htmlTextTemp = htmlTextException;
						break;
					case DIFF:
						htmlTextTemp = htmlTextDiff;
						break;
					case OUTPUT_EXCEPTION:
						htmlTextTemp = htmlTextException;
						break;
					case OUTPUT_REFLECTION:
						htmlTextTemp = htmlTextReflection;
						break;
					case SESSION_EXCEPTION:
						htmlTextTemp = htmlTextException;
						break;
					case SESSION_REFLECTION:
						htmlTextTemp = htmlTextReflection;
						break;
				}
				
				//Append to html report
				htmlTextTemp.append(TABLE_START);
				
				htmlTextTemp.append(getTableTitle(resultCounter + ". " + record.getName(), record.getInPage() + " --> " + record.getOutPage()));
				htmlTextTemp.append(getTableLine(Advisor.SERVER_STORAGE, OUTPUT_TYPE.values()[record.getType()].name()));
				htmlTextTemp.append(getTableLine(Advisor.ORIGINAL_VAL, record.getValue()));
				htmlTextTemp.append(getTableLine(Advisor.TOKEN_VAL, record.getTokenValue()));
				htmlTextTemp.append(getTableLine(Advisor.SCENARIO, SCENARIO_MODE.values()[record.getScenario()].name()));
				htmlTextTemp.append(getTableLine(Advisor.HISTORY, HISTORY_MODE.values()[record.getHistMode()].name()));
				htmlTextTemp.append(getTableLine(Advisor.ZAP_SOURCE, Integer.toString(record.getInputID())));
				htmlTextTemp.append(getTableLine(Advisor.ZAP_TARGET, Integer.toString(record.getOutputID())));
				htmlTextTemp.append(getTableLine(Advisor.TOKEN_TYPE, TOKEN_TYPE.values()[record.getTokenType()].name()));

				htmlTextTemp.append(TABLE_END);

				
			}
			htmlText.append(htmlTextReflection);
			htmlText.append(htmlTextException);
			htmlText.append(htmlTextDiff);

			htmlText.append(HTML_PAGE_END);
			try {
				FileWriter fstream = new FileWriter(HTMLReport);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(htmlText.toString());
				out.close();
				
				JOptionPane.showMessageDialog(new JDialog(), "Report file has been saved");
			}
			catch (Exception ex) {	/* no log file was selected */}
		}
		
		
	}

	private static String getTableTitle(String resultId, String title) {
		return "<tr bgcolor=\"blue\" height=\"24\"><td width=\"20%\" valign=\"top\"><strong><font color=\"#FFFFFF\" size=\"2\" face=\"Arial, Helvetica, sans-serif\"><a name=\"info\"></a>Result "+ resultId + "</font></strong></td><td width=\"80%\"><strong><font color=\"#FFFFFF\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">" + title + "</font></strong></td></tr>";
	}
	
	private static String getTableLine(String critria, String value) {
		return "<tr bgcolor=\"#e8e8e8\" valign=\"top\"><td width=\"20%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><p>" + critria + "</p></font></td><td width=\"80%\"><font size=\"2\" face=\"Arial, Helvetica, sans-serif\"><p align=\"justify\">" + value + "</p></font></td></tr><TR vAlign=\"top\"></TR>";

	}

}
