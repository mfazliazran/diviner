package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;


public class ExceptionsConfigDialog extends JDialog implements ActionListener{


	private static final long serialVersionUID = -3561792759898126132L;
	private JPanel mainPanel; 
	private JPanel payloadsPanel; 
	private JPanel errorPanel;
	private JPanel southPanel; 
	private JScrollPane payloadsScroll;
	private JScrollPane errorScroll;
	private boolean isInvalidCharsDialog;
	private static JTable payloads;
	private static JTable errorsRegex;
	private static DefaultTableModel payloadsModel;
	private static DefaultTableModel errorModel;
	private static String payloadsConcatenation = "";
	public static ArrayList<String> errorsList = new ArrayList<String>();
	private static final String headerText = "<html><b>Define the payloads and patterns that will be used in the exception triggering and recognition processes.<br>Diviner will update the task list / advisor views if an erroneous response that was caused from the input will appear in the application responses.</b></html>";
	private static final String ADD = "ADD";
	private static final String REMOVE = "REMOVE";
	private static final String PAYLOAD_REMOVE = "P_REMOVE";
	private static final String ERROR_REMOVE = "E_REMOVE";
	private static final String PAYLOAD_ADD = "P_ADD";
	private static final String ERROR_ADD = "E_ADD";
	private static final String OK = "OK";


	public ExceptionsConfigDialog(Dialog parent, boolean isInvalidCharsDialog) {
		super(parent,"Exception Configuration", ModalityType.DOCUMENT_MODAL);

		setSize(new Dimension(600, 500));
		setLayout(new BorderLayout());

		this.isInvalidCharsDialog = isInvalidCharsDialog;
		
		mainPanel = new JPanel(new FlowLayout());
		payloadsPanel = new JPanel(new BorderLayout());
		errorPanel = new JPanel(new BorderLayout());

		JLabel lblHeader = new JLabel(headerText);

		Object [] payloadsHeader = {"Payloads"};
		Object [] errorsHeader = {"Response Regex"};

		//Use saved items if items are not null
		if (payloadsModel == null) {
			payloadsModel = new DefaultTableModel(ExceptionConstants.getPayloads(), payloadsHeader);
		}
		if (errorModel == null) {
			errorModel = new DefaultTableModel(ExceptionConstants.getResponseRegex(), errorsHeader);
		}

		if (isInvalidCharsDialog) {
			payloads = new JTable(payloadsModel);
			payloads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			payloads.setPreferredSize(new Dimension(200, 350));
			payloadsScroll = new JScrollPane(payloads);
			payloadsScroll.setPreferredSize(new Dimension(200, 350));
			mainPanel.add(payloadsPanel,BorderLayout.CENTER);
			//Bottom panels for buttons REMOVE/ADD
			JPanel bottomPayloadsPanel = new JPanel();

			//Creating buttons  - payloads
			JButton btnAddPayload = new JButton(ADD);
			btnAddPayload.addActionListener(this);
			JButton btnRemovePayload = new JButton(REMOVE);
			btnRemovePayload.addActionListener(this);
			btnAddPayload.setActionCommand(PAYLOAD_ADD);
			btnRemovePayload.setActionCommand(PAYLOAD_REMOVE);
			bottomPayloadsPanel.add(btnAddPayload);
			bottomPayloadsPanel.add(btnRemovePayload);
			payloadsPanel.add(bottomPayloadsPanel, BorderLayout.SOUTH);
			payloadsPanel.add(payloadsScroll, BorderLayout.CENTER);
		}
		else {
			errorsRegex = new JTable(errorModel);
			errorsRegex.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			errorScroll = new JScrollPane(errorsRegex);
			errorsRegex.setPreferredSize(new Dimension(200, 350));
			errorScroll.setPreferredSize(new Dimension(200, 350));
			mainPanel.add(errorPanel, BorderLayout.CENTER);

			JPanel bottomErrorPanel = new JPanel();

			//Creating buttons  - Errors
			JButton btnAddError = new JButton(ADD);
			btnAddError.addActionListener(this);
			btnAddError.setActionCommand(ERROR_ADD);
			JButton btnRemoveError = new JButton(REMOVE);
			btnRemoveError.addActionListener(this);
			btnRemoveError.setActionCommand(ERROR_REMOVE);
			bottomErrorPanel.add(btnAddError);
			bottomErrorPanel.add(btnRemoveError);
			
			errorPanel.add(bottomErrorPanel, BorderLayout.SOUTH);
			errorPanel.add(errorScroll, BorderLayout.CENTER);
		}
		

		//South Panel
		JButton btnOK = new JButton(OK);
		btnOK.addActionListener(this);
		southPanel = new JPanel(new BorderLayout());
		southPanel.add(btnOK, BorderLayout.EAST);
		add(lblHeader, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case PAYLOAD_REMOVE:
			int indexPayload = payloads.getSelectedRow();
			if (indexPayload != -1) {
				payloadsModel.removeRow(indexPayload);	
			}
			break;

		case PAYLOAD_ADD:
			Object [] rowPayload = {};
			payloadsModel.addRow(rowPayload);

			break;

		case ERROR_REMOVE:
			int indexError = errorsRegex.getSelectedRow();
			if (indexError != -1) {
				errorModel.removeRow(indexError);	
			}
			break;

		case ERROR_ADD:
			Object [] rowError = {};
			errorModel.addRow(rowError);
			break;

		case OK:
			this.dispose();
			break;
		}

		//Paint the correct dialog
		if (isInvalidCharsDialog) {
			payloadsScroll.repaint();	
		}
		else {
			errorScroll.repaint();	
		}
		

	}

	public JTable getErrorsRegex() {
		return errorsRegex;
	}

	public static ArrayList<String> getPayloads() {
		ArrayList<String> result = new ArrayList<String>();
		for (int i =0; i < payloads.getRowCount(); i++) {
			String payload = payloads.getValueAt(i, 0).toString();
			if (payload != "") {
				result.add(payload);
			}
		}
		return result;
	}

	public static void setErrorRegex() {
		if (errorsRegex != null) {
			for (int i =0; i < errorsRegex.getRowCount(); i++) {
				String regex = errorsRegex.getValueAt(i, 0).toString();
				if (regex != "") {
					errorsList.add(regex);
				}
			}	
		}
		else {
			for (int i = 0; i < ExceptionConstants.getResponseRegex().length; i++) {
				errorsList.add(ExceptionConstants.getResponseRegex()[i][0].toString());
			}
		}
	}

	public static void setConcatenatedPayloads() {
		payloadsConcatenation = "";
		if (payloads != null) {
			for (int i =0; i < payloads.getRowCount(); i++) {
				String payload = payloads.getValueAt(i, 0).toString();
				if (payload != null && payload != "") {
					payloadsConcatenation += payloads.getValueAt(i, 0).toString();	
				}
			}
		}
		else {	//Take the payloads from the constants list
			for (int i = 0; i < ExceptionConstants.getPayloads().length; i++) {
				payloadsConcatenation += ExceptionConstants.getPayloads()[i][0];	
			}
		}
	}
	
	public static String getConcatenatedPayloads() {
		return payloadsConcatenation;
	}

	public static ArrayList<String> getErrorsList() {
		return errorsList;
	}

}
