package com.hacktics.diviner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ParameterList extends JPanel  {
	
	private static final long serialVersionUID = -4114101426854919992L;
	private static final int MIN_PARAMS_IN_PAGE = 6;
	private ArrayList <Parameter> parameterList = new ArrayList <Parameter>(); 
	private static final Border border =  new CompoundBorder(new EtchedBorder(),new LineBorder(Color.black, 0));
	private static final Color ENTRY_POINT_COLOR = Color.LIGHT_GRAY;
	private static final int ENTRY_POINT_WIDTH = 120;
	private static final int ENTRY_POINT_HEIGHT = 170;
	private JPanel paramPanel;
	private JScrollPane scrollPane;
	
	public ParameterList(int i_numberOfParamters){
		paramPanel = new JPanel();
		//Set proper layout for parameters 
		if (i_numberOfParamters < MIN_PARAMS_IN_PAGE)
		{
			paramPanel.setLayout(new GridLayout(MIN_PARAMS_IN_PAGE ,1 ,5 ,5));
		}
		else
		{
			paramPanel.setLayout(new GridLayout(i_numberOfParamters ,1 ,5 ,5));

		}
		
		setBackground(ENTRY_POINT_COLOR);
		paramPanel.setBackground(ENTRY_POINT_COLOR);
		
		
		
		scrollPane = new JScrollPane(paramPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(new Dimension(ENTRY_POINT_WIDTH , ENTRY_POINT_HEIGHT));
		scrollPane.setPreferredSize(new Dimension(ENTRY_POINT_WIDTH , ENTRY_POINT_HEIGHT));
		scrollPane.setBackground(ENTRY_POINT_COLOR);
		TitledBorder inputPanelBorder=new TitledBorder(border , "Input Parameters");
		inputPanelBorder.setTitleJustification(TitledBorder.CENTER);
		scrollPane.setBorder(inputPanelBorder);
		add(scrollPane);
	}
	
	public void addParameter(long id, int requestId, String pageName, String paramName, String paramValue, boolean isAffecting, ParameterInfo info){
		Parameter parameter = new Parameter(id, requestId, pageName,  paramName, paramValue, isAffecting, info);
		
		parameterList.add(parameter);
		paramPanel.add(parameter);
		
	}
	
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}
	
	public void restoreDefaultColors(){
		for (Parameter param : parameterList){
			param.RestoreDefaultColor();
		}
	}
	
	public ArrayList<Parameter> getParamList() {
		return parameterList;
	}
	public Parameter getParamByName(String paramName) {
		Parameter result = null;
		for (Parameter param : parameterList) {
			if (param.getParamName().equals(paramName)) {
				result = param;
				break;
			}
		}
		return result;
	}
	
}
