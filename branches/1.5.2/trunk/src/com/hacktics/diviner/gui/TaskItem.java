package com.hacktics.diviner.gui;

import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;

public class TaskItem extends DefaultMutableTreeNode{
	
	private Parameter parameter;
	
	public TaskItem(String pageName, String paramName, String text) {
		super(text);
		parameter = EntryPointsArray.getParameterByName(pageName, paramName);
	}
	
	public void doClick() {
		EntryPointsArray.doClick(parameter);
	}
}
