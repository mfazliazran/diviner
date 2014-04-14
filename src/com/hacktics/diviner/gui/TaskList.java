package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.gui.controllers.JCheckBoxTree;

/**
 * 
 * @authors Eran Tamari, Shay Chen
 *
 */
public class TaskList extends JScrollPane implements TreeSelectionListener{

	private static final long serialVersionUID = -1747633449199762560L;
	private JPanel mainPanel;
	private JLabel lblTasks;
	private static final String HEADLINE = "Tasks / Leads";
	private static final String SESSION_REFLECTION_MSG = "Session Input Reflections (Potential XSS, CRLFi, Redirect, Etc)";
	private static final String PERSISTENT_REFLECTION_MSG = "Persistent Input Reflections (Potential XSS, CRLFi, Redirect, Etc)";
	private static final String DIRECT_REFLECTION_MSG = "Direct Input Reflections (Potential XSS, CRLFi, Redirect, Etc)";
	private static final String SESSION_EXCEPTION_MSG = "Session Exceptions (Potential Injections, Etc)";
	private static final String PERSISTENT_EXCEPTION_MSG = "Persistent Exceptions (Potential Injections, Etc)";
	private static final String DIRECT_EXCEPTION_MSG = "Direct Exceptions (Potential Injections, Etc)";
	private static final String OTHER_MSG = "Additional Leads (Content Diff Patterns)";

	private static boolean isDirectReflectionResultFound = false;
	private static boolean isSessionReflectionResultFound = false;
	private static boolean isPersistentReflectionResultFound = false;
	private static boolean isDirectExceptionResultFound = false;
	private static boolean isSessionExceptionResultFound = false;
	private static boolean isPersistentExceptionResultFound = false;
	
	private static boolean isOtherResults = false;
	
	private DefaultMutableTreeNode parent;
	private DefaultMutableTreeNode persistentReflectionCheckbox;
	private DefaultMutableTreeNode sessionReflectionCheckbox;
	private DefaultMutableTreeNode directReflectionCheckbox;
	private DefaultMutableTreeNode persistentExceptionCheckbox;
	private DefaultMutableTreeNode sessionExceptionCheckbox;
	private DefaultMutableTreeNode directExceptionCheckbox;
	private DefaultMutableTreeNode otherCheckbox;
	
	public TaskList() {
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
		parent = new DefaultMutableTreeNode(HEADLINE, true);
		persistentReflectionCheckbox = new DefaultMutableTreeNode(PERSISTENT_REFLECTION_MSG);
		sessionReflectionCheckbox = new DefaultMutableTreeNode(SESSION_REFLECTION_MSG);
		directReflectionCheckbox = new DefaultMutableTreeNode(DIRECT_REFLECTION_MSG);
		persistentExceptionCheckbox = new DefaultMutableTreeNode(PERSISTENT_EXCEPTION_MSG);
		sessionExceptionCheckbox = new DefaultMutableTreeNode(SESSION_EXCEPTION_MSG);
		directExceptionCheckbox = new DefaultMutableTreeNode(DIRECT_EXCEPTION_MSG);
		otherCheckbox = new DefaultMutableTreeNode(OTHER_MSG);

		lblTasks = new JLabel(HEADLINE, JLabel.CENTER);
		lblTasks.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));
		
		JPanel topPanel = new JPanel(new GridLayout(3, 1));
		topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		topPanel.setBackground(Color.LIGHT_GRAY);
		topPanel.add(lblTasks);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		setViewportView(mainPanel);

	}
	
	public void showTasks() {
		
		for (DivinerRecordResult record : AnalyzerUtils.getLeads()) {
			
			String itemText = "<html>(<b>" + record.getInputID() + "</b>) " + record.getInPage() + "--> (<b>" + record.getOutputID() + "</b>)" + record.getOutPage() + " [<b>" + record.getName() + "</b>]</html>";
			TaskItem taskItem = new TaskItem(record.getInPage(), record.getName(), itemText);
			if (record.isParamReflected()) {
				switch (OUTPUT_TYPE.values()[record.getType()]) {
				
				case DATABASE_REFLECTION:
					if (! isPersistentReflectionResultFound) {
						parent.add(persistentReflectionCheckbox);
						isPersistentReflectionResultFound = true;
					}
					persistentReflectionCheckbox.add(taskItem);
					break;
					
				case OUTPUT_REFLECTION:
					if (! isDirectReflectionResultFound) {
						parent.add(directReflectionCheckbox);
						isDirectReflectionResultFound = true;
					}
					directReflectionCheckbox.add(taskItem);
					break;
					
				case SESSION_REFLECTION:
					if (! isSessionReflectionResultFound) {
						parent.add(sessionReflectionCheckbox);
						isSessionReflectionResultFound = true;
					}
					sessionReflectionCheckbox.add(taskItem);
					break;
					
				}
			} else if (record.isParamException()) {
				switch (OUTPUT_TYPE.values()[record.getType()]) {
				case DATABASE_EXCEPTION:
					if (! isPersistentExceptionResultFound) {
						parent.add(persistentExceptionCheckbox);
						isPersistentExceptionResultFound = true;
					}
					persistentExceptionCheckbox.add(taskItem);
					break;
					
				case OUTPUT_EXCEPTION:
					if (! isDirectExceptionResultFound) {
						parent.add(directExceptionCheckbox);
						isDirectExceptionResultFound = true;
					}
					directExceptionCheckbox.add(taskItem);
					break;
					
				case SESSION_EXCEPTION:
					if (! isSessionExceptionResultFound) {
						parent.add(sessionExceptionCheckbox);
						isSessionExceptionResultFound = true;
					}
					sessionExceptionCheckbox.add(taskItem);
					break;
				}
			} else {
				if (! (isOtherResults)) {
					isOtherResults = true;
					parent.add(otherCheckbox);
				}
				otherCheckbox.add(taskItem);
			}
		}
		JCheckBoxTree leadsTree = new JCheckBoxTree(parent);
		leadsTree.addTreeSelectionListener(this);
		mainPanel.add(leadsTree, BorderLayout.CENTER);
		
		revalidate();
		repaint();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		try {
			((TaskItem)e.getPath().getPathComponent(2)).doClick();
				
		} catch (Exception ex) {
			//Thrown when root is selected - ignore
			System.out.println("Root Selection");
		}
	}

} //end of class
