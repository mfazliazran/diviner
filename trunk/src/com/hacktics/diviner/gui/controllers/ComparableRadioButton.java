package com.hacktics.diviner.gui.controllers;

import javax.swing.AbstractButton;
import javax.swing.JRadioButton;

public class ComparableRadioButton extends JRadioButton implements Comparable<AbstractButton>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComparableRadioButton(String text) {
		super(text);
	}
	
	@Override
	public int compareTo(AbstractButton o) {
		return this.getText().compareTo(o.getText());
	}

}
