package com.hacktics.diviner.analyze;

import com.hacktics.diviner.gui.scanwizard.Scenarios;

/**
 * 
 * @author Eran Tamari
 *
 */

public enum SCENARIO_MODE {
	
	LOGIN_FIRST(Scenarios.getScenariosText()[0]), DIRECT(Scenarios.getScenariosText()[1]), LOGIN_AFTER(Scenarios.getScenariosText()[2]);
	public String text;
	
	private SCENARIO_MODE(String text){
		this.text = text;
	}
	
	public static SCENARIO_MODE getNameByText(String text) {
		SCENARIO_MODE result = null;
		
		for (SCENARIO_MODE scenario : SCENARIO_MODE.values()) {
			if (scenario.text.equals(text)) {
				result = scenario;
			}
		}
		return result;
	}
}
