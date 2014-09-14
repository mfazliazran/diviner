package com.hacktics.diviner.gui;

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class LicenseDialog extends JDialog {
	public LicenseDialog() {
	}
	private static final String TITLE = "License";
	private static final String OK = "OK";
	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 */
	public LicenseDialog(Frame parent, boolean modal) {
		super(parent,"License", ModalityType.DOCUMENT_MODAL);	//The wizard is the top frame
		initialize();
	}

	private void initialize() {
		this.setTitle(TITLE);
		this.setSize(720, 800);
		JScrollPane scroll = new JScrollPane(new LicensePanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
		this.add(scroll);
	}

	
}
