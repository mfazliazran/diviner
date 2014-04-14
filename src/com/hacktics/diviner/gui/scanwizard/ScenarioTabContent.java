package com.hacktics.diviner.gui.scanwizard;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @authors Eran Tamari, Shay Chen
 *
 */
public class ScenarioTabContent extends GenericTabContent{

	private static final String HEADLINE =  "Choose Analysis Scenarios:";
	private static final String DESCRIPTION =  "<html><b>At least one analysis process and one history mode must be selected.</b><html>";


	public ScenarioTabContent(String backButtonCommand, JPanel panel, ActionListener eventHandler)
	{
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  panel,  eventHandler);
	
	}
	
	
}
