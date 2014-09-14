package com.hacktics.diviner.gui.scanwizard;

import javax.swing.JCheckBox;
import com.hacktics.diviner.analyze.HISTORY_MODE;
import com.hacktics.diviner.analyze.SCENARIO_MODE;

/**
 * 
 * @author Eran Tamari
 *
 */

public class Scenarios {
	
	public static final String MULTITHREAD_TEXT ="Multi-threading";
	public static final String MULTITHREAD ="Run On Multi-thread Mode";
	public static final String SCENARIOS_TEXT = "Analyzing Scenarios";
	public static final String VERIFY_MODE_TEXT = "Verify Mode";
	private static final String HISTORY_MODE_TEXT = "History Modes";
	private static final String[] SCENARIOS = {"Login First", "Public Direct", "Login After"};
	private static final String[] HIST_MODE = {"No History", "Partial History", "Full History", "Custom History"};
	private static final String CUSTOM_HISTORY_MODE = "Custom History";

	private static JCheckBox [] scenarios;
	private static JCheckBox [] historyModes;
	private static JCheckBox verifyModeCheckbox;
	private static final boolean ENABLED = true;
	public static final String TESTING_MODE = " Testing Mode\n";

	
	//Add checkbox MULTITHRED
	private static JCheckBox  multithread;
	
	public Scenarios()
	{
		initScenarios();
	}

	private void initScenarios()
	{
		scenarios = new JCheckBox[SCENARIOS.length];
		historyModes = new JCheckBox[HIST_MODE.length];
		
		int i = 0;
		for (String scenario : SCENARIOS)
		{
			scenarios[i] = new JCheckBox("<html><i>" + scenario + "</i></html>");
			scenarios[i].setSelected(true);
			i++;
		}
		i = 0;
		for (String hitMode : HIST_MODE)
		{
			historyModes[i] = new JCheckBox("<html><i>" + hitMode + "</i></html>");
			if (hitMode != CUSTOM_HISTORY_MODE) {
				historyModes[i].setSelected(true);
			}
			i++;
		}
		multithread = new JCheckBox("<html><i>" + MULTITHREAD + "</i></html>");
		multithread.setSelected(true);
		
		verifyModeCheckbox = new JCheckBox("<html><i>Safer and More Accurate scan</i></html>");
		verifyModeCheckbox.setSelected(true);
	}

	public boolean isVerifyMode()
	{
		return verifyModeCheckbox.isSelected();
	}
	
	public JCheckBox getVerifyBox()
	{
		return verifyModeCheckbox;
	}
	
	public JCheckBox[] getAllScenarios()
	{
		return scenarios;
	}
	
	public JCheckBox[] getAllHistModes()
	{
		return historyModes;
	}
	
	public int getNumOfScenarios()
	{
		return scenarios.length;
	}
	
	public int getNumOfHistiryModes()
	{
		return historyModes.length;
	}
	
	
	public boolean isScenarioSelectedAt(int index)
	{
		return scenarios[index].isSelected();
	}

	public boolean isHistorySelectedAt(int index)
	{
		return historyModes[index].isSelected();
	}

	
	public static String[] getScenariosText(){
		return SCENARIOS;
	}
	
	public boolean isLoginFirstScenario()
	{
		if (scenarios[SCENARIO_MODE.LOGIN_FIRST.ordinal()].isSelected())
			return ENABLED;
		return !ENABLED;
	}
	
	public boolean isPublicDirectScenario()
	{
		if (scenarios[SCENARIO_MODE.DIRECT.ordinal()].isSelected())
			return ENABLED;
		return !ENABLED;
	}
		
	public boolean isLoginAfterScenario()
	{
		if (scenarios[SCENARIO_MODE.LOGIN_AFTER.ordinal()].isSelected())
			return ENABLED;
		return !ENABLED;
	}
	
	public int getEnabledScenariosCount()
	{
		int counter = 0;
		for (JCheckBox scenario : getAllScenarios())
		{
			if (scenario.isSelected())
			{
				counter++;
			}
		}
		return counter;
	}
	
	public static boolean isAllScenariosEnabled() {
		
		boolean result = true;
		for (JCheckBox scenario : scenarios)
		{
			if (! scenario.isSelected())
			{
				result = false;
				break;
			}
		}
		return result;
	}
	
	public int getEnabledHistoryModeCount()
	{
		int counter = 0;
		for (JCheckBox histMode : getAllHistModes())
		{
			if (histMode.isSelected())
			{
				counter++;
			}
		}
		return counter;
	}
	
	public boolean isCustomHistoryEnabled() {
		
		return historyModes[HISTORY_MODE.CUSTOM_HISTORY.ordinal()].isSelected();
	}
	public String getHistoryTitle() {
		return HISTORY_MODE_TEXT;
	}
	
	public String getVerifyTitle() {
		return VERIFY_MODE_TEXT;
	}
	
	public String getScenariosTitle() {
		return SCENARIOS_TEXT;
	}
	
	public String getMultithreadTitle() {
		return  MULTITHREAD_TEXT;
	}
	
	///add
	
	
	public boolean isMultithreadingEnabled(){
		return multithread.isSelected();
	}

	public JCheckBox getMultithread() {
		return multithread;
	}
}

