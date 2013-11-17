package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hacktics.diviner.gui.Diviner;

/**
 * 
 * @author Eran Tamari
 *
 */
public class RecommendedFilter extends JDialog implements ActionListener{


	private static final long serialVersionUID = 5318967014979961901L;

	private static String[] defaultRecommended = {"\\.css$", "\\.js$", "logout", "\\.jpg$", "\\.png$"};
	private boolean successFlag;
	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;
	private static final String ADD = "ADD";
	private static final String REMOVE = "REMOVE";
	private static final String OK = "APPLY";
	private static final String CANCEL = "CANCEL";
	private JList<String> recommendedList;
	private DefaultListModel<String> recommendedListModel;
	
	public RecommendedFilter() {
		super(Diviner.getMainFrame(),"Recommended Filter", ModalityType.DOCUMENT_MODAL);
		setLocation(Diviner.getWindowWidth() / 2, Diviner.getWindowHeight() / 2);
		setLayout(new BorderLayout());
		setSize(WIDTH, HEIGHT);
		
		recommendedListModel = new DefaultListModel<String>();
		successFlag = false;
		
		for (String str : defaultRecommended) {
			recommendedListModel.addElement(str);
		}

		//Add and Remove buttons
		JPanel buttonsPanel = new JPanel(new GridLayout(5, 1));
		JButton addButton = new JButton(ADD);
		JButton removeButton = new JButton(REMOVE);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);

		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);

		//Bottom panel - OK and Cancel
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
		JButton okButton = new JButton(OK);
		JButton cancelButton = new JButton(CANCEL);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		southPanel.add(okButton);
		southPanel.add(cancelButton);

		//Create default list of regex and put it in the main panel
		recommendedList = new JList<String>(recommendedListModel);
		JScrollPane listScroll = new JScrollPane(recommendedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(listScroll, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case OK:
			successFlag = true;
			dispose();
			break;
		case REMOVE:
			try {
				recommendedListModel.remove(recommendedList.getSelectedIndex());
				recommendedList.repaint();
			}
			catch (Exception ex) {
				//No element was selected when trying to remove element
			}
			break;
		case ADD:
			String newItem = JOptionPane.showInputDialog("Create a new regex to be removed from the analysis scope:");
			recommendedListModel.addElement(newItem);
			recommendedList.repaint();
			break;
		case CANCEL:
			successFlag = false;
			dispose();
			break;
		}

	}
	
	public String[] getItems() {
		
		int itemsCount = recommendedListModel.getSize();
		String[] result = new String [itemsCount];
		
		if (successFlag) {
			int index = 0;
			
		
			for (; index < itemsCount; index++) {
				result[index] = recommendedListModel.get(index);
				
			}
		}
		return result;
	}

	public static String[] getFilters() {
		return defaultRecommended;
	}
}
