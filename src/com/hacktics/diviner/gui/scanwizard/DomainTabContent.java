package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.gui.GuiUtils;

/**
 * 
 * @authors Eran Tamari, Shay Chen
 *
 */

public class DomainTabContent extends GenericTabContent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 886265384705844353L;
	private static final String HEADLINE =  "Choose the Target Domain:";
	private static final String DESCRIPTION =  "<html><b>The various tests and content analysis processes will be restricted to the selected domain.</b></html>";
	private JComboBox <String> domainsList ;
	private ArrayList<JCheckBox> urlsListCheckBox;
	private ArrayList<String> urlsList;
	private JScrollPane urlsScroll;
	private JCheckBox urlCheckbox;
	private boolean isSuccessful;
	private static String selectedHost;
	private static final boolean SELECTED = true;

	public DomainTabContent(String backButtonCommand, JPanel panel, ActionListener eventHandler)
	{
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  panel,  eventHandler);
	

		panel.setLayout(new GridLayout(7, 1, 20 , 20));
		urlsListCheckBox = new ArrayList<JCheckBox>();
		ArrayList<String> result = GuiUtils.getGuiUtils().getDomainsFromZap();
		String [] str = new String[result.size()];
		result.toArray(str);
		domainsList = new JComboBox<String>(str);
		panel.add(domainsList, BorderLayout.CENTER);

	}
	
	public JScrollPane handleDomainTabOK()
	{
		Object selectedItem = domainsList.getSelectedItem();
		urlsScroll = null;
		
		if (selectedItem != null)
		{
			isSuccessful = SUCCESSFUL;
			selectedHost = domainsList.getSelectedItem().toString();
			urlsListCheckBox.clear();
			AnalyzerUtils.setHostForAnalyze(selectedHost);
			urlsList = AnalyzerUtils.getSitesOfSelectedHostForAnalyze();
			JPanel urlsPanel = new JPanel(new GridLayout(urlsList.size() , 1 , 0 , 0));
			urlsPanel.setMinimumSize(new Dimension(200,200));
			urlsScroll = new JScrollPane(urlsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			for (String url : urlsList)
			{
				urlCheckbox = new JCheckBox(url);
				urlCheckbox.setSelected(SELECTED);
				urlsPanel.add(urlCheckbox);
				urlsListCheckBox.add(urlCheckbox);
			}
		}

		//Show warning if no host is selected
		else
		{
			isSuccessful = !SUCCESSFUL;
		}
		return urlsScroll;
	}
		
	public boolean isSuccessful()
	{
		return isSuccessful;
	}
	public ArrayList<JCheckBox>  getSelectedHostsUrls()
	{
		return urlsListCheckBox;
	}

	public static String getSelectedDomain() {
		return selectedHost;
	}
}
