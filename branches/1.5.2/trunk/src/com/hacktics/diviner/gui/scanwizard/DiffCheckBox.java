package com.hacktics.diviner.gui.scanwizard;

import javax.swing.JCheckBox;

import com.hacktics.diviner.diffutil.Response_Diffs.Diff;

public class DiffCheckBox extends JCheckBox {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6492997659088532273L;
	private Diff diff;
	private int reqID;
	
	public DiffCheckBox(int reqID, Diff diff, String text) {
		super(text);
		this.diff = diff;
		this.reqID = reqID;
	}

	public Diff getDiff() {
		return diff;
	}

	public int getReqID() {
		return reqID;
	}
	
	

}
