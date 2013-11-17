package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import com.hacktics.diviner.gui.controllers.BlinkingButton;

/**
 * 
 * @author Eran Tamari
 *
 */

public class OutputPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8148910969585793993L;
	private static final int ENTRY_POINT_WIDTH = 122;
	private static final int ENTRY_POINT_HEIGHT = 170;
	private static final Color ENTRY_POINT_COLOR = Color.LIGHT_GRAY;
	
	private static final Border border =  new CompoundBorder(new EtchedBorder(),new LineBorder(Color.black, 0));
	
	//Reflection output
	private JPanel mainPanel;
	private BlinkingButton sessionReflectionButton;
	private BlinkingButton databaseReflectionButton;
	private BlinkingButton outputReflectionButton;
	
	//Exception output
	private BlinkingButton sessionExceptionButton;
	private BlinkingButton databaseExceptionButton;
	private BlinkingButton outputExceptionButton;
	
	//Diff output
	private BlinkingButton diffButton;
	
	private BlinkingButton  parameterLocation[] = new BlinkingButton[7];

	public OutputPanel(boolean isStatiticsPanel){

		setBackground(ENTRY_POINT_COLOR);
		setMinimumSize(new Dimension(ENTRY_POINT_WIDTH, ENTRY_POINT_HEIGHT));
		setPreferredSize(new Dimension(ENTRY_POINT_WIDTH, ENTRY_POINT_HEIGHT));
		
		TitledBorder tb;

		if (isStatiticsPanel) {
			tb = new TitledBorder(border,"Statistics");
		}
		else {
			tb = new TitledBorder(border,"Location");
			initOutputTypes();
		}

		tb.setTitleJustification(TitledBorder.CENTER);
		setBorder(tb);
	}

	public void setStatistics(Statistics stats) {
		setLayout(new GridLayout(5 ,1 ,0 ,0));
		
		JButton btnMethod = new JButton("<html><b>Method: " + stats.getMethod() + "</b></html>");
		JButton btnReqId = new JButton("<html><b>Zap ID: " + stats.getRequestId() + "</b></html>");
		JButton btnNumParams = new JButton("<html><b># Params: " + stats.getNumOfParams() + "</b></html>");
		JButton btnRespcode = new JButton("<html><b>Code: " + stats.getReponseCode() + "</b></html>");
		JButton btnSSL = new JButton("<html><b>SSL: " + stats.isSSL() + "</b></html>");
		
		btnMethod.setBackground(Color.YELLOW);
		btnReqId.setBackground(Color.YELLOW);
		btnNumParams.setBackground(Color.YELLOW);
		btnRespcode.setBackground(Color.YELLOW);
		btnSSL.setBackground(Color.YELLOW);
		
		add(btnMethod);
		add(btnReqId);
		add(btnNumParams);
		add(btnRespcode);
		add(btnSSL);
	}


	private void initOutputTypes() {
		setLayout(new BorderLayout());
		
		mainPanel = new JPanel(new GridLayout(6, 1, 5, 5));
		mainPanel.setBackground(ENTRY_POINT_COLOR);
		mainPanel.setMinimumSize(new Dimension(ENTRY_POINT_WIDTH, ENTRY_POINT_HEIGHT));
		mainPanel.setPreferredSize(new Dimension(ENTRY_POINT_WIDTH, ENTRY_POINT_HEIGHT));
		
		add(mainPanel, BorderLayout.CENTER);

		ImageIcon icon = GuiUtils.getGuiUtils().getSessionIcon();
		sessionReflectionButton = new BlinkingButton(Color.GRAY , Color.RED , "Output");
		sessionReflectionButton.setIcon(icon);
		sessionReflectionButton.setHorizontalAlignment(AbstractButton.LEFT);
		sessionReflectionButton.setVisible(false);
		sessionReflectionButton.setToolTipText("Session Reflection");
		
		icon = GuiUtils.getGuiUtils().getDBIcon();
		databaseReflectionButton = new BlinkingButton(Color.GRAY , Color.RED , "Output");
		databaseReflectionButton.setIcon(icon);
		databaseReflectionButton.setHorizontalAlignment(AbstractButton.LEFT);
		databaseReflectionButton.setVisible(false);
		databaseReflectionButton.setToolTipText("Database Reflection");

		icon = GuiUtils.getGuiUtils().getOutputIcon();
		outputReflectionButton = new BlinkingButton(Color.GRAY , Color.RED , "Output");
		outputReflectionButton.setIcon(icon);
		outputReflectionButton.setHorizontalAlignment(AbstractButton.LEFT);
		outputReflectionButton.setVisible(false);
		outputReflectionButton.setToolTipText("Output Reflection");
		
		icon = GuiUtils.getGuiUtils().getSessionIcon();
		sessionExceptionButton = new BlinkingButton(Color.GRAY , Color.RED , "Exception");
		sessionExceptionButton.setIcon(icon);
		sessionExceptionButton.setHorizontalAlignment(AbstractButton.LEFT);
		sessionExceptionButton.setVisible(false);
		sessionExceptionButton.setToolTipText("Exception from Session");

		icon = GuiUtils.getGuiUtils().getDBIcon();
		databaseExceptionButton = new BlinkingButton(Color.GRAY , Color.RED , "Exception");
		databaseExceptionButton.setIcon(icon);
		databaseExceptionButton.setHorizontalAlignment(AbstractButton.LEFT);
		databaseExceptionButton.setVisible(false);
		databaseExceptionButton.setToolTipText("Exception from Database");

		icon = GuiUtils.getGuiUtils().getOutputIcon();
		outputExceptionButton = new BlinkingButton(Color.GRAY , Color.RED , "Exception");
		outputExceptionButton.setIcon(icon);
		outputExceptionButton.setHorizontalAlignment(AbstractButton.LEFT);
		outputExceptionButton.setVisible(false);
		outputExceptionButton.setToolTipText("Exception from Output");
		
		diffButton = new BlinkingButton(Color.BLUE , Color.GREEN , "Diff");
		diffButton.setHorizontalAlignment(AbstractButton.LEFT);
		diffButton.setVisible(false);
		
		parameterLocation[0] = databaseReflectionButton;
		parameterLocation[1] = outputReflectionButton;
		parameterLocation[2] = sessionReflectionButton;
		parameterLocation[3] = databaseExceptionButton;
		parameterLocation[4] = outputExceptionButton;
		parameterLocation[5] = sessionExceptionButton;
		parameterLocation[6] = diffButton;

	}

	public BlinkingButton[] getOutputTypeList() {
		return parameterLocation;
	}
	
	//Add the effects that were identified
	public void addEffects() {
		for (BlinkingButton effect : parameterLocation) {
			//Effects that are visible - should be added the the panel
			if (effect.isVisible()) {
				mainPanel.add(effect);
			}
		}
	}
	
	//Identify which affects are relevant to the entry point
	public void setVisible(OUTPUT_TYPE outputType) {
	
		if (outputType.equals(OUTPUT_TYPE.DIFF)) {
			diffButton.setVisible(true);
			JPanel diffLocatorPanel = new JPanel(new BorderLayout());
			diffLocatorPanel.setBackground(ENTRY_POINT_COLOR);
			diffLocatorPanel.add(diffButton, BorderLayout.WEST);
			add(diffLocatorPanel, BorderLayout.SOUTH);
		}
		
		if (!parameterLocation[outputType.ordinal()].isVisible()) {	//If output is not visible it means it wasn't added before 
			switch (outputType) {
			case DATABASE_EXCEPTION:
				databaseExceptionButton.setVisible(true);
				break;
			case DATABASE_REFLECTION:
				databaseReflectionButton.setVisible(true);
				break;
			case DIFF:
				break;
			case OUTPUT_EXCEPTION:
				outputExceptionButton.setVisible(true);
				break;
			case OUTPUT_REFLECTION:
				outputReflectionButton.setVisible(true);
				break;
			case SESSION_EXCEPTION:
				sessionExceptionButton.setVisible(true);
				break;
			case SESSION_REFLECTION:
				sessionReflectionButton.setVisible(true);
				break;
			}
		}
	}
	
	public void setDiffPercentage(int percentage) {
		if (percentage > 0) {
			diffButton.setVisible(true);
			diffButton.setText(Integer.toString(percentage));
			diffButton.repaint();
		}
	}
	

	
	public void restoreDefaultColors(){
		for (BlinkingButton location : parameterLocation){
			location.RestoreDefaultColor();
			diffButton.setVisible(false);
		}
	}

}
