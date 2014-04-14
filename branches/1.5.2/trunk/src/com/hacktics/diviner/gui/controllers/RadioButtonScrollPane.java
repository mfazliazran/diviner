package com.hacktics.diviner.gui.controllers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import com.hacktics.diviner.gui.DivinerTitleBorder;

/**
 * 
 * @author Eran Tamari
 *
 */

public class RadioButtonScrollPane extends JScrollPane {


	private static final long serialVersionUID = -411979368299715056L;
	private JPanel payloadsPanel;
	private ButtonGroup group;
	private String firstSelectedButton;
	private Set <AbstractButton> items;
	private boolean isMultiSelection;
	private JButton btnAddNewItem;
	private boolean isVertical;
	private JPanel mainPanel;
	/**
	 * JtoggleButton is the father of JCheckBox and JradioButton, so this class will fit both components
	 * @param title
	 * @param itemList
	 * @param isVertical
	 * @param multiSelection
	 */
	public RadioButtonScrollPane(String title, Set <AbstractButton> itemList, boolean isVertical, boolean multiSelection) {


		setBorder(new DivinerTitleBorder(title));
		isMultiSelection = multiSelection;
		this.items = itemList;
		this.isVertical = isVertical;
		mainPanel = new JPanel(new BorderLayout()); 
		if (isVertical) { 
			payloadsPanel = new JPanel(new GridLayout(itemList.size() + 1, 1));
		}
		else {
			payloadsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
			setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		}
		group = new ButtonGroup();

		boolean firstInLine = true;

		//The AddNew button
//		if (addNewItem != null) {
//			btnAddNewItem = addNewItem;
//			mainPanel.add(addNewItem, BorderLayout.NORTH);
//		}
		for (AbstractButton radioItem : itemList) {

			if (firstInLine) {
				radioItem.setSelected(true);
				firstInLine = false;
				firstSelectedButton = radioItem.getText();
			}

			if (! multiSelection) {
				group.add(radioItem);
			}
			payloadsPanel.add(radioItem);
		}
		
		mainPanel.add(payloadsPanel, BorderLayout.CENTER);
		setViewportView(mainPanel);
		
	}



	public void setNewView(Set<AbstractButton> items) {

		payloadsPanel.removeAll();
		group = new ButtonGroup();
		this.items = items;

		if (isVertical) {
			payloadsPanel.setLayout(new GridLayout(items.size() + 1, 1));
		}
//		if (btnAddNewItem != null) {
////			payloadsPanel.add(btnAddNewItem);
//			mainPanel.add(btnAddNewItem, BorderLayout.NORTH);
//		}
		for (AbstractButton radioItem : items) {

			if (! isMultiSelection) {
				group.add(radioItem);
			}
			payloadsPanel.add(radioItem);

		}		

		payloadsPanel.repaint();
		payloadsPanel.revalidate();
	}

	public void setRecommended(String platform, boolean isRadioButton) {

		//Recommend radiobutton
		if (isRadioButton) {
			for (AbstractButton item : items) {
				((PayloadRadioButton) item).checkPlatform(platform);
			}
		}
		//Recommend button 
		else {
			for (AbstractButton item : items) {
				try {
					((PayloadButton) item).checkPlatform(platform);
				}
				catch (ClassCastException e) {
					//The "Add New" button should be ignored 
				}
			}
		}
	}


	public AbstractButton getSelected() {
		AbstractButton result = null;
		for (AbstractButton item : items) {
			if (item.isSelected()) {
				result = item;
				break;
			}
		}
		return result;
	}

	//Add new attack to GUI
	public void addNewAttack(AttackItem attackRadioButton) {
		group.add(attackRadioButton);
		payloadsPanel.add(attackRadioButton);
		attackRadioButton.setSelected(true);
		items.add(attackRadioButton);
		payloadsPanel.setLayout(new GridLayout(items.size() + 1, 1));
	}
	
	public String getFirstSelected() {
		return firstSelectedButton;
	}
}
