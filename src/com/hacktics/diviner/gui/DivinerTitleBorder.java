package com.hacktics.diviner.gui;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Eran Tamari
 *
 */

public class DivinerTitleBorder extends TitledBorder{
	private static final long serialVersionUID = -5783858494628289605L;
	private static Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

	public DivinerTitleBorder(String title) {
		super(loweredetched, title);
	}
	
}
