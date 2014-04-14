package com.hacktics.diviner.gui.scanwizard;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Paper;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hacktics.diviner.gui.DivinerTitleBorder;

/**
 * 
 * @authors Eran Tamari, Shay Chen
 *
 */


public class PluginTabContent extends GenericTabContent implements ActionListener{
	/*
	 * Constants
	 */

	/*
	 * Data members
	 */
	private static final long serialVersionUID = 1978117581877038047L;
	private static final String HEADLINE =  "Available Analysis Plugins:";
	private static final String DESCRIPTION =  "<html><b>The plugins selected will be used during the test.</b></html>";
	private JCheckBox checkBoxReflectionPlugin;
	private JCheckBox checkBoxDiffPlugin;
	private JCheckBox checkBoxDOSPlugin;
	private JCheckBox checkBoxExceptionPlugin; 
	private JCheckBox appendModeBox;
	private JCheckBox replaceModeBox;
	private JCheckBox simpleRandom;
	private JCheckBox errorRandom; //error random
	private JCheckBox validRandom;
	private JButton btnConfigException;
	private JButton btnPayloads;
	private JPanel panelExcetions;
	private Dialog parent;
	private static final String REFLECTION_PLUGIN = "Input Reflection Detection";
	private static final String EXCEPTION_PLUGIN = "Exception Tracking";
	private static final String DIFF = "<html>Content Differentiation Analysis<BR>(Experimental)</html>";
	private static final String DOS = "<html>Code Sorting via DOS<BR>(Coming Soon)</html>";
	private static final String CONFIG = "Configuration";
	private static final String CONFIG_EXCEPTIONS = "Exception Tracking Configuration";
	private static final String CONFIG_PAYLOADS = "Payloads Configuration";
	private static final String PAYLOADS = "<html>Error Random<br>(Number or String)</html>";

	/*
	 * Constructor
	 */
	public PluginTabContent(String backButtonCommand, JPanel panel, ActionListener eventHandler)
	{
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  panel,  eventHandler);
		this.parent = (Dialog) eventHandler;
		panel.setLayout(new GridLayout(1, 3, 0 , 0));

		//Right panel
		checkBoxReflectionPlugin = new JCheckBox(REFLECTION_PLUGIN);
		checkBoxReflectionPlugin.setSelected(true);
		checkBoxExceptionPlugin = new JCheckBox(EXCEPTION_PLUGIN);
		checkBoxExceptionPlugin.setSelected(true);
		checkBoxExceptionPlugin.addActionListener(this);
		btnConfigException = new JButton(CONFIG);
		btnConfigException.setActionCommand(CONFIG_EXCEPTIONS);
		btnConfigException.addActionListener(this);
		btnConfigException.setEnabled(true);
		panelExcetions = new JPanel(new FlowLayout(FlowLayout.LEFT));
		checkBoxDiffPlugin = new JCheckBox(DIFF);
		checkBoxDiffPlugin.setSelected(false);
		checkBoxDiffPlugin.setToolTipText("Experimental Feature (In Development)");
		checkBoxDOSPlugin = new JCheckBox(DOS);
		checkBoxDOSPlugin.setEnabled(false);
		checkBoxDOSPlugin.setToolTipText("Experimental Feature (In Development)");
		
		//Exceptions panel
		panelExcetions.add(checkBoxExceptionPlugin);
		panelExcetions.add(btnConfigException);
		
		//Middle panel
		appendModeBox = new JCheckBox("Append Mode");
		replaceModeBox = new JCheckBox("Replace Mode");
		appendModeBox.setSelected(true);
		replaceModeBox.setSelected(true);

		//Left panel
		JPanel panelPayloads = new JPanel(new FlowLayout(FlowLayout.LEFT));
		simpleRandom = new JCheckBox("Simple Random");
		errorRandom = new JCheckBox(PAYLOADS);
		validRandom = new JCheckBox("<html>Valid Random<BR>(Coming Soon)</html>");
		
		btnPayloads = new JButton(CONFIG);
		btnPayloads.setActionCommand(CONFIG_PAYLOADS);
		btnPayloads.addActionListener(this);
		btnPayloads.setEnabled(true);
		
		simpleRandom.setSelected(true);
		errorRandom.addActionListener(this);
		errorRandom.setSelected(true);
		validRandom.setEnabled(false);

		panelPayloads.add(errorRandom);
		panelPayloads.add(btnPayloads);
		
		JPanel leftPanel = new JPanel(new GridLayout(3, 1));
		JPanel midPanel = new JPanel(new GridLayout(2, 1));
		JPanel rightPanel = new JPanel(new GridLayout(4, 1));

		leftPanel.setBorder(new DivinerTitleBorder("Random Mode"));
		midPanel.setBorder(new DivinerTitleBorder("Token Mode"));
		rightPanel.setBorder(new DivinerTitleBorder("Plugins"));

		leftPanel.add(simpleRandom);
		leftPanel.add(panelPayloads);
		leftPanel.add(validRandom);

		midPanel.add(appendModeBox);
		midPanel.add(replaceModeBox);

		rightPanel.add(checkBoxReflectionPlugin);
		rightPanel.add(panelExcetions);
		rightPanel.add(checkBoxDiffPlugin);
		rightPanel.add(checkBoxDOSPlugin);

		panel.add(leftPanel);
		panel.add(midPanel);
		panel.add(rightPanel);
	}

	public void showExceptionsConfigDialog(boolean isInvalidCharsDialog) {
		ExceptionsConfigDialog configDialog = new ExceptionsConfigDialog(parent, isInvalidCharsDialog);
		configDialog.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case EXCEPTION_PLUGIN:
			if (((JCheckBox)e.getSource()).isSelected()) {
				btnConfigException.setEnabled(true);
			}
			else {
				btnConfigException.setEnabled(false);
			}
			break;
		case PAYLOADS:
			if (((JCheckBox)e.getSource()).isSelected()) {
				btnPayloads.setEnabled(true);
			}
			else {
				btnPayloads.setEnabled(false);
			}
			break;
		case CONFIG_EXCEPTIONS://Open Exceptions dialog
			showExceptionsConfigDialog(false);
			break;
			
		case CONFIG_PAYLOADS://Open payloads dialog
			showExceptionsConfigDialog(true);
			break;
		}
		
	}
	
	/*
	 * Getters & Setters
	 */

	//Plugins
	public boolean isReflectionPluginEnabled() { return checkBoxReflectionPlugin.isSelected(); };
	public boolean isContentDiffPluginEnabled() { return checkBoxDiffPlugin.isSelected(); };
	public boolean isExceptionPluginEnabled() { return checkBoxExceptionPlugin.isSelected(); };
	//Token Append/Replace
	public boolean isAppendMode() {	return appendModeBox.isSelected(); }
	public boolean isReplaceMode() { return replaceModeBox.isSelected(); }
	//Token Mode
	public boolean isSimpleRandom() { return simpleRandom.isSelected(); }
	public boolean isErrorRandom() { return errorRandom.isSelected();	}
	public boolean isValidRandom() { return validRandom.isSelected();	}
	
	


}
