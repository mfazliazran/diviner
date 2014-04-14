package com.hacktics.diviner.gui.controllers;

import javax.swing.JRadioButton;

public class AttackItem extends JRadioButton implements Comparable<JRadioButton>{

	private static final long serialVersionUID = -7661820475516864450L;

	public AttackItem(String text) {
		super(text);
	}
	@Override
	public int compareTo(JRadioButton o) {		
		return this.getText().compareTo(o.getText());
	}

}
