package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.hacktics.diviner.gui.DivinerTitleBorder;

/**
 * 
 * @author Eran Tamari
 *
 */

public class GenericTabContent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6342548449168432007L;
	private static final String OK = "OK";
	private static final String CANCEL = "Cancel";
	private static final String BACK = "Back";
	protected static final boolean SUCCESSFUL = true;
	private JLabel title;
	private JLabel desc;
	private DivinerTitleBorder titleBorder;

	public GenericTabContent(String backButtonCommand, String title, JLabel description, JPanel mainContent, ActionListener eventHandler)
	{
		setLayout(new BorderLayout(0,30));
		titleBorder  = new DivinerTitleBorder(title);
		titleBorder.setTitleFont(new Font("Serif", Font.ITALIC, 18));
		titleBorder.setTitleJustification(TitledBorder.CENTER);
		setBorder(titleBorder);
	
		this.desc = description;
		JPanel headPanel =  new JPanel(new BorderLayout(10, 10));
		headPanel.add(description, BorderLayout.CENTER);
		add(headPanel, BorderLayout.NORTH);
		add(mainContent, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100 , 0));
		JButton okButton = new JButton(OK);
		okButton.setActionCommand(OK);
		okButton.addActionListener(eventHandler);
		JButton cancelButton = new JButton(backButtonCommand);
		cancelButton.addActionListener(eventHandler);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		add(buttonsPanel, BorderLayout.SOUTH);

	}

	public void setHeadline(String text) {
		titleBorder.setTitle(text);
	}

	public JLabel getDescription() {
		return this.desc;
	}
}
