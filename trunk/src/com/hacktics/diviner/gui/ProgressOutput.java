package com.hacktics.diviner.gui;

import javax.swing.JTextArea;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ProgressOutput extends JTextArea {

	private static final long serialVersionUID = 6590727055986796175L;
	public ProgressOutput(int rows, int cols) {
		super(rows,cols);
	}
	
	public void append(String text) {
		super.append(text);
        this.setCaretPosition( this.getText().length() );
		}
}
