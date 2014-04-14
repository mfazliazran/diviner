package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.TreePath;

import com.hacktics.diviner.analyze.SCENARIO_MODE;

/**
 * 
 * @authors Eran Tamari, Shay Chen
 * @since 1.0
 *
 */

public class Advisor extends JScrollPane{

	private JPanel mainPanel;
	private JLabel lblAdvisor;
	private static final String ADVISOR = "Advisor";
	public static final String OUTPUT_PAGE = "OutPut Page:";
	public static final String ORIGINAL_VAL = "Original Value: ";
	public static final String SCENARIO = "Found in Scenario: ";
	public static final String HISTORY = "Found in History Mode: ";
	public static final String TOKEN_VAL = "Token Value: ";
	public static final String SERVER_STORAGE = "Server-Side Storage/Event: ";
	public static final String DIFF_PERCENT = "Diff Percentage: ";
	public static final String TOKEN_TYPE = "Token Type: ";
	public static final String ZAP_SOURCE = "ZAP Source Request ID: ";
	public static final String ZAP_TARGET = "ZAP Target Request ID: ";


	public Advisor() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setToolTipText(ADVISOR);

		setViewportView(mainPanel);
		setBackground(Color.WHITE);

		lblAdvisor = new JLabel(ADVISOR, JLabel.CENTER);
		lblAdvisor.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));
	}

	public void clearView() {
		mainPanel.removeAll();
		repaint();
	}

	public void showParameterInfo(String paramName, String pageName, ParameterInfo info, TreePath filters) {
		mainPanel.removeAll();

		mainPanel.setLayout(new BorderLayout());

		//Parameter with no effect - empty main panel
		if (info == null) {
			revalidate();
			repaint();
			return;
		}

		HashSet<ParameterEffect> effectsList = new HashSet<ParameterEffect>();

		RESULT_TYPE plugin = null;
		SCENARIO_MODE scenario = null;
		String targetPage = null;

		try {
			plugin = RESULT_TYPE.getTypeByName(filters.getPathComponent(Parameter.PLUGIN_TREE_INDEX).toString());
			scenario = SCENARIO_MODE.getNameByText(filters.getPathComponent(Parameter.SCENARIO_TREE_INDEX).toString());
			targetPage = filters.getPathComponent(Parameter.TARGET_PAGE_INDEX).toString();
		}
		catch (Exception ex) {
			//Exception is thrown every time pluginName or targetPage are null - happens when targetPage is not selected 

		}

		//Screen the results and show only the results according to the selected node in Advisor Tree
		for (ParameterEffect effect : info.getEffectsListView()) {
			if (plugin != null) {
				if (scenario != null) {
					//Target page node was selected in tree
					if (targetPage != null) {
						if (targetPage.equals(effect.getOutputPage()) && scenario.equals(effect.getScenario()) && isMatchingPlugin(plugin, effect)) {
							effectsList.add(effect);
						}
					}
					//Scenario node was selected in tree
					else {
						if (isMatchingPlugin(plugin, effect) && scenario.equals(effect.getScenario())) {
							effectsList.add(effect);
						}
					}
				}
				//Plugin node was selected in tree
				else {
					if (isMatchingPlugin(plugin, effect)) {
						effectsList.add(effect);
					}
				}
			}
			//When plugin is null - just show everything without filters
			else {
				effectsList = info.getEffectsListView();
			}
		}

		JPanel centerPanel = new JPanel(new GridLayout(effectsList.size(), 1));

		int resultCounter = 0;
				
		//Parse results for adivsor
		for (ParameterEffect effect : effectsList) {

			resultCounter++;

			JPanel effectPanel = new JPanel(new GridLayout(10, 2, 2 , 2));
			effectPanel.setBorder(new DivinerTitleBorder("Result " + resultCounter));

			effectPanel.add(new JLabel(setBold(OUTPUT_PAGE)));
			effectPanel.add(new JLabel(effect.getOutputPage()));

			effectPanel.add(new JLabel(setBold(SERVER_STORAGE)));
			effectPanel.add(new JLabel(setUnderline(OUTPUT_TYPE.values()[effect.getType()].name())));

			effectPanel.add(new JLabel(setBold(TOKEN_TYPE)));
			effectPanel.add(new JLabel(effect.getTokenType().name()));

			effectPanel.add(new JLabel(setBold(TOKEN_VAL)));
			effectPanel.add(new JLabel(effect.getTokenValue()));

			effectPanel.add(new JLabel(setBold(ORIGINAL_VAL)));
			effectPanel.add(new JLabel(effect.getOriginalValue()));

			effectPanel.add(new JLabel(setBold(SCENARIO)));
			effectPanel.add(new JLabel(effect.getScenario().name()));

			effectPanel.add(new JLabel(setBold(HISTORY)));
			effectPanel.add(new JLabel(effect.getHistMode().name()));

			effectPanel.add(new JLabel(setBold(DIFF_PERCENT)));
			effectPanel.add(new JLabel(Integer.toString(effect.getDiffPercentage())));

			effectPanel.add(new JLabel(setBold(ZAP_SOURCE)));
			effectPanel.add(new JLabel(Integer.toString(effect.getSourceID())));

			effectPanel.add(new JLabel(setBold(ZAP_TARGET)));
			effectPanel.add(new JLabel(Integer.toString(effect.getTargetID())));

			effectPanel.setBackground(Color.WHITE);
			centerPanel.add(effectPanel);
		}

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.WHITE);

		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		revalidate();
		repaint();


	}

	private boolean isMatchingPlugin(RESULT_TYPE pluginName, ParameterEffect effect) {

		boolean result = false;
		switch (pluginName) {
		case DIFF:
			if (effect.isDiff()) {
				result = true;
			}
			break;
		case EXCEPTION:
			if (effect.isException()) {
				result = true;
			}
			break;
		case REFLECTION:
			if (effect.isReflection()) {
				result = true;
			}
			break;
		}

		return result;
	}

	private String setBold(String str) {
		return "<html><b>" + str + "</b></html>";
	}

	private String setUnderline(String str) {
		return "<html><u>" + str + "</u></html>";
	}
}
