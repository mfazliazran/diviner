package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.gui.GuiUtils;

/**
 * 
 * @author Eran Tamari
 *
 */
public class ScopeTabContent extends GenericTabContent implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2219206338459517142L;
	private static final String HEADLINE =  "Analysis Scope:";
	private static final String DESCRIPTION =  "<html><b>The various tests and content analysis processes will be limited to the selected URLs.<br/>It is recommended to remove static files such as static pages, images, and media files from scope, in order to make the analysis process faster.</b></html>";
//	private JScrollPane selectedUrlsScroll;
	private ArrayList<String> selectedUrlsList;
	private ButtonGroup group;
	private ArrayList<JCheckBox> scopeURLs = null;
	private boolean isSuccessful;
	private static final String CHECKALL = "Check / Uncheck  All";
	private static final String FILTER = "Filter";
	private static final String RECOMMEDED = "Recommended";
	private static final boolean SELECTED = true;
	public ScopeTabContent (String backButtonCommand, JPanel panel, ActionListener eventHandler)
	{
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  panel,  eventHandler);
		
		JButton checkAllButton = new JButton(CHECKALL);
		checkAllButton.setActionCommand(CHECKALL);
		JButton filterButton = new JButton(FILTER);
		filterButton.setActionCommand(FILTER);
		JButton recommendedButton = new JButton(RECOMMEDED);
		filterButton.addActionListener(this);
		checkAllButton.addActionListener(this);
		recommendedButton.addActionListener(this);
		
		JPanel northButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 0));
		northButtonsPanel.add(checkAllButton);
		northButtonsPanel.add(recommendedButton);
		northButtonsPanel.add(filterButton);
		panel.add(northButtonsPanel, BorderLayout.NORTH);
		
	}
	
	public void setScopeURLs(ArrayList<JCheckBox> scopeURLs){
		this.scopeURLs = scopeURLs;	
		
		//Initially remove the recommended filters
		for (String filter : RecommendedFilter.getFilters()) {
			removeFromListByRegex(filter);
		}
	}
	
	public void setSelectedUrlsInScope() {
		selectedUrlsList = new ArrayList<String>();
		AnalyzerUtils.resetHistory();

		//Get only the selected checkboxes and store them in a radio button list for session and login tabs
		for (JCheckBox checkbox : scopeURLs)
		{
			if (checkbox.isSelected()) {
				String url = checkbox.getText();
				selectedUrlsList.add(url);

				try
				{
					AnalyzerUtils.addURLToScope(url);	//Add selected URL to scope in DB
				}
				catch(Exception e){e.printStackTrace();}
			}
		}
		if (selectedUrlsList.size() != 0) {
			AnalyzerUtils.updateAnalyzeScope();
//			selectedUrlsList.get(0).setSelected(true);
			isSuccessful = SUCCESSFUL;
		}
		else{	isSuccessful = !SUCCESSFUL; }
//		return selectedUrlsList;			
	}
	
	public boolean isSuccessful(){
		return isSuccessful;
	}
	
	public String getSelectedLogin()
	{
		return group.getSelection().getActionCommand();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()){
		case CHECKALL:
			boolean setSelected = SELECTED;
			if ((scopeURLs.size() != 0) && (scopeURLs.get(0).isSelected())) {
				setSelected = !SELECTED;
			}
					
			for (JCheckBox checkbox : scopeURLs) {
				checkbox.setSelected(setSelected);
			}
			break;
		case FILTER:
			String regex = (String) JOptionPane.showInputDialog(this, "Enter a Regular Expression","Filter", JOptionPane.INFORMATION_MESSAGE ,GuiUtils.getGuiUtils().getQuestionIcon(), null ,"");
			removeFromListByRegex(regex);
			break;
		case RECOMMEDED:
			RecommendedFilter recommendedFilter = new RecommendedFilter();
			recommendedFilter.setVisible(true);
			for (String recommendedRegex : recommendedFilter.getItems()) {
				removeFromListByRegex(recommendedRegex);
			}
			break;
		
		}
		
		
	}
	
	private void removeFromListByRegex (String regex) {
		if (regex != null){
			try{
			Pattern uriRegex = Pattern.compile(regex);
			
			for (JCheckBox checkbox : scopeURLs) {
				Matcher matcher = uriRegex.matcher(checkbox.getText());
				if (matcher.find()) {
					checkbox.setSelected(!SELECTED);
				}
			}
			}
			catch (PatternSyntaxException syntaxExcetion){}//Do nothing
		}
	}
	
	public ArrayList<String> getScopeUrlList() {
		return selectedUrlsList;
	}
}
