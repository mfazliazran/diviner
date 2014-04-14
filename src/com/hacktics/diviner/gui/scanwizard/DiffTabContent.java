
package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parosproxy.paros.network.HttpMessage;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.diffutil.Response_Diffs.Diff;
import com.hacktics.diviner.diffutil.Response_Manager;
import com.hacktics.diviner.gui.DivinerTitleBorder;
import com.hacktics.diviner.gui.ProgressBarDialog;
import com.hacktics.diviner.zapDB.ZapHistoryDB;
/**
 * 
 * @author Eran Tamari
 *
 */
public class DiffTabContent extends GenericTabContent {

	private	ArrayList<DiffCheckBox> diffCheckBoxList;

	private static final long serialVersionUID = -8850590282311437256L;
	private static final String HEADLINE =  "Diviner noticed the following pattern inconsitencies in the server's responses: ";
	private static final String DESCRIPTION =  "<html><b>Please instruct Diviner which difference should be overlooked.</b></html>";
	private static final int OFFSET_STRING = 20;
	private Dialog parent;
	public DiffTabContent(String backButtonCommand, JPanel mainContentPanel, ActionListener eventHandler) {
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  mainContentPanel,  eventHandler);
		 diffCheckBoxList = new ArrayList<DiffCheckBox>();
		 parent = (Dialog) eventHandler;
	}

	public JScrollPane showDiffs() {
		
		//Show Progress bar
		ProgressBarDialog progressBarDiffs = new ProgressBarDialog(parent, "Content Differences","<html>Looking for content differences</html>");
		progressBarDiffs.start();
		
		HashMap<Integer, Response_Manager> diffsMap = AnalyzerUtils.createDiffList();
		int mapSize = diffsMap.size();
		JPanel diffsPanel = new JPanel(new GridLayout(mapSize, 1));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(diffsPanel, BorderLayout.CENTER);
		JScrollPane scrollDiffs = new JScrollPane(mainPanel);
		scrollDiffs.setPreferredSize(new Dimension(650, 400));
		for (Integer requestID : diffsMap.keySet()) {
		
			List<Diff> diffList = ((Response_Manager) diffsMap.get(requestID)).getResponses(0).getDiffs();
			
			if (diffList.size() == 0) {
				continue;
			}
			
			JPanel singleRequestDiffPanel = new JPanel(new GridLayout(diffList.size(), 1, 20, 20));
			JScrollPane singleRequestDiffScroll = new JScrollPane(singleRequestDiffPanel); 
			HttpMessage zapRequest = null;
			try {
				zapRequest = ZapHistoryDB.getZapHistoryTable().read(requestID).getHttpMessage();	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			singleRequestDiffScroll.setBorder(new DivinerTitleBorder("Request " + requestID + ": " + zapRequest.getRequestHeader().getURI().toString()));

			for (Diff diff : diffList) {
				String diffText = "";
				String start =  diff.start;
				String end = diff.end;
				
				if (start.length() > OFFSET_STRING) {
					start = start.substring(start.length() - OFFSET_STRING);
				}
				
				if (end.length() > OFFSET_STRING) {
					end = end.substring(end.length() - OFFSET_STRING);
				}
				
				start =  AnalyzerUtils.encodeHTML(start);
				end = AnalyzerUtils.encodeHTML(end);
				
				
				if (diff.originalValue != "") {
					diffText = "<html><body>" + start + "<br/>[DELETED:]<b>" + AnalyzerUtils.encodeHTML(diff.originalValue) + "</b><br/> [INSERTED:] <b>" + AnalyzerUtils.encodeHTML(diff.between) + " </b>" + end + "<body></html>";
				}
				else {
					diffText = "<html><body>" + start + "<br/>[" + diff.betweenType + ":] " + " <b>" + AnalyzerUtils.encodeHTML(diff.between) + "</b> " + end  +  "</body></html>";
				}
				
				DiffCheckBox diffBox = new DiffCheckBox(requestID, diff, diffText);
				diffCheckBoxList.add(diffBox);
				singleRequestDiffPanel.add(diffBox);
			}
			diffsPanel.add(singleRequestDiffScroll);
		}
		
		//End progress bar
		progressBarDiffs.setVisible(false);
		progressBarDiffs.dispose();
		return scrollDiffs;
	}
	
	
	//Go through all the checkboxes and set the diffs which are marked 
	public void setSelectedDiffs(){
		ArrayList<DiffCheckBox> diffList = new ArrayList<DiffCheckBox>();
		
		for (DiffCheckBox diffBox : diffCheckBoxList) {
			if (diffBox.isSelected()) {
				diffList.add(diffBox);
			}
		}
		AnalyzerUtils.setDiffsList(diffList);

	}
}
