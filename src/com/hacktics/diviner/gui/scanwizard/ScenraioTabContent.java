package com.hacktics.diviner.gui.scanwizard;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author Eran Tamari
 *
 */
public class ScenraioTabContent extends GenericTabContent{

	private static final String HEADLINE =  "Scan scenarios:";
	private static final String DESCRIPTION =  "<html><b>At least one analyzing process and one history mode need to be selected</b><html>";


	public ScenraioTabContent(String backButtonCommand, JPanel panel, ActionListener eventHandler)
	{
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  panel,  eventHandler);
	
	}
	
	
}
