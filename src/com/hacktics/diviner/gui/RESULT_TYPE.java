package com.hacktics.diviner.gui;

/**
 * This ENUM stores a list of potential classifications for a behaviors, which are used by the advisor tree. 
 *
 * @authors Eran Tamari, Shay Chen
 * @since 1.0
 *
 */

public enum RESULT_TYPE {
	REFLECTION("Reflected Input Values (Potential XSS,CRLFi,Redirect)"), DIFF("Content Differentiation Effects"), EXCEPTION("Exception-Generating Scenarios (Potential Injection)");
	
	private String text;
	private RESULT_TYPE(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public static RESULT_TYPE getTypeByName(String text) {
		RESULT_TYPE result = null;
		for (RESULT_TYPE resultType : RESULT_TYPE.values()) {
			if (resultType.text.equals(text)) {
				result = resultType;
				break;
			}
		}
		return result;
	}
	
} //end of class
