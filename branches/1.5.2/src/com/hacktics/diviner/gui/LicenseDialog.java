package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.parosproxy.paros.extension.AbstractDialog;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
