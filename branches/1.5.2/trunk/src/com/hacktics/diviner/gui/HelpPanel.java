package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpPanel extends JDialog {

	private static final String HEADER = "Glossary";
	private static final String STAR_TEXT = "Payload Manager";
	private static final String EYE_TEXT = "Source Code Available";
	private static final String INJECTION_TEXT = "Resend Request";
	private static final String MAGNIFY_TEXT = "Focus on Entry Point";
	private static final String RED_TEXT = "Selected Input Parameter";
	private static final String WHITE_TEXT = "Input Parameter";
	private static final String YELLOW_TEXT = "Interesting Parameter";
	private static final String LOCK_TEXT = "Require Authentication";
	private static final String DOOR_TEXT = "Public Entry Point";
	private static final String NO_TEXT = "";
	private static final String PM_TOOLTIP = "Activate the Payload Manager for the selected parameter";
	private static final String CLAIRVOYANCE_TOOLTIP = "Source code divination yeilded results";
	private static final String REPEATER_TOOLTIP = "Open the Resend dialog";
	private static final String MAGNIFY_TOOLTIP = "Show the entry points affected by the selected entry point";
	private static final String LOCK_TOOLTIP = "Some behaviours in the entry point require authentication";
	private static final String DOOR_TOOLTIP = "The detected behaviours in the enrty point do not require authentication";
	private static final String SESSION_TOOLTIP = "Parameter stored in session";
	private static final String DATABASE_TOOLTIP = "Parameter stored in database";
	private static final String OUTPUT_TOOLTIP = "Parameter printed to the screen in the same page";
	private static final String SESSION_TEXT = "Session Storage";
	private static final String DATABASE_TEXT = "Database Storage";
	private static final String OUTPUT_TEXT = "Output Storage";
	
	public HelpPanel(JFrame parent){
		super(parent,"Glossary", ModalityType.DOCUMENT_MODAL);	//The wizard is the top frame

		this.setLayout(new GridLayout(3, 4, 2, 2));
		this.setSize(700, 180);
		this.setLocation(parent.getWidth() / 2 - this.getWidth() / 2, parent.getHeight() / 2 - this.getHeight() / 2);
		JLabel starIcon = new JLabel(STAR_TEXT, GuiUtils.getGuiUtils().getStarIcon(), JLabel.LEFT);
		starIcon.setToolTipText(PM_TOOLTIP);

		JLabel clairvoyanceIcon = new JLabel(EYE_TEXT, GuiUtils.getGuiUtils().getEyeIcon(), JLabel.LEFT);
		clairvoyanceIcon.setToolTipText(CLAIRVOYANCE_TOOLTIP);


		JLabel injectionIcon = new JLabel(INJECTION_TEXT, GuiUtils.getGuiUtils().getRepeaterIcon(), JLabel.LEFT);
		injectionIcon.setToolTipText(REPEATER_TOOLTIP);

		JLabel lockIcon = new JLabel(LOCK_TEXT, GuiUtils.getGuiUtils().getAuthenticatedIcon(), JLabel.LEFT);
		lockIcon.setToolTipText(LOCK_TOOLTIP);

		JLabel doorIcon = new JLabel(DOOR_TEXT, GuiUtils.getGuiUtils().getUnauthenticatedIcon(), JLabel.LEFT);
		doorIcon.setToolTipText(DOOR_TOOLTIP);
		
		JLabel magnifyIcon = new JLabel(MAGNIFY_TEXT, GuiUtils.getGuiUtils().getMagnifyEnabledIcon(), JLabel.LEFT);
		magnifyIcon.setToolTipText(MAGNIFY_TOOLTIP);

		JLabel sessionIcon = new JLabel(SESSION_TEXT, GuiUtils.getGuiUtils().getSessionIcon(), JLabel.LEFT);
		sessionIcon.setToolTipText(SESSION_TOOLTIP);

		JLabel dbIcon = new JLabel(DATABASE_TEXT, GuiUtils.getGuiUtils().getDBIcon(), JLabel.LEFT);
		dbIcon.setToolTipText(DATABASE_TOOLTIP);
		
		JLabel outputIcon = new JLabel(OUTPUT_TEXT, GuiUtils.getGuiUtils().getOutputIcon(), JLabel.LEFT);
		outputIcon.setToolTipText(OUTPUT_TOOLTIP);
		
		
		JButton selectColor = new JButton(NO_TEXT);
		JButton noEffectColor = new JButton(NO_TEXT);
		JButton effectColor = new JButton(NO_TEXT);

		selectColor.setBackground(Parameter.BLINK_COLOR);
		noEffectColor.setBackground(Parameter.NO_EFFECT_COLOR);
		effectColor.setBackground(Parameter.EFFECT_COLOR);

		JPanel noEffectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		noEffectPanel.add(noEffectColor);
		noEffectPanel.add(new JLabel(WHITE_TEXT, JLabel.LEFT));

		JPanel affectingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		affectingPanel.add(effectColor);
		affectingPanel.add(new JLabel(YELLOW_TEXT, JLabel.LEFT));

		JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		selectedPanel.add(selectColor);
		selectedPanel.add(new JLabel(RED_TEXT, JLabel.LEFT));

		
		this.add(noEffectPanel);
		this.add(affectingPanel);
		this.add(selectedPanel);
		this.add(injectionIcon);
		this.add(starIcon);
		this.add(magnifyIcon);
		this.add(lockIcon);
		this.add(doorIcon);
		this.add(outputIcon);
		this.add(sessionIcon);
		this.add(dbIcon);
		
	}
}
