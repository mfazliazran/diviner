package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.hacktics.diviner.analyze.SCENARIO_MODE;
/**
 * 
 * @author Eran Tamari
 *
 */
//TODO: Each parameter should store its effects ready for presentation rather than constructed on every click
public class AdvisorTree extends JScrollPane{

	private static final long serialVersionUID = 6477143668198390485L;
	private JPanel mainPanel;
	private JLabel lblAdvisor;
	private String paramName;
	private String pageName;
	private ParameterInfo info;
	private JPanel topPanel;
	private JLabel lblParamName;
	private JLabel lblSourcePageName;
	private JTree tree;
	private DefaultTreeModel treeModel;
	private Advisor advisor;
	private static final String ADVISOR = "Advisor";

	public AdvisorTree(Advisor advisor) {

		this.advisor = advisor;
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setToolTipText(ADVISOR);

		setViewportView(mainPanel);
		setBackground(Color.WHITE);

		lblAdvisor = new JLabel(ADVISOR, JLabel.CENTER);
		lblAdvisor.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));
		lblParamName = new JLabel("", JLabel.CENTER);
		lblParamName.setFont(new Font("Serif", Font.BOLD, 19));
		lblSourcePageName = new JLabel("", JLabel.CENTER);
		lblParamName.setFont(new Font("Serif", Font.BOLD, 15));

		topPanel = new JPanel(new GridLayout(3, 1));
		topPanel.setBackground(Color.LIGHT_GRAY);
		topPanel.add(lblAdvisor);
		topPanel.add(lblParamName);
		topPanel.add(lblSourcePageName);
		topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		mainPanel.add(topPanel, BorderLayout.NORTH);
		tree = new JTree();

	}

	public void showParameterInfo(Parameter param) {

		pageName = param.getPageName();
		paramName = param.getParamName();
		info = param.getInfo();
		
		lblParamName.setText(paramName);
		lblSourcePageName.setText(pageName);
		topPanel.repaint();
		
		mainPanel.remove(tree);
		
		//Parameter with no effect - empty main panel
		if (info == null) {
			advisor.clearView();
			revalidate();
			repaint();
			return;
		}
		
		this.tree = param.getAdvisorTree();
		mainPanel.add(tree, BorderLayout.CENTER);
		
		tree.expandRow(0); //expand the first row of the tree
	
		mainPanel.repaint();
		tree.addTreeSelectionListener(SelectionListener());

		advisor.showParameterInfo(paramName, pageName, info, tree.getSelectionPath());

	}

	private TreeSelectionListener SelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				advisor.showParameterInfo(paramName, pageName, info, e.getPath());
			}
		};
	}

	
}
