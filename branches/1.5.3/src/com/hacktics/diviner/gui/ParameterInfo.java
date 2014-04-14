package com.hacktics.diviner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.tree.TreePath;

import com.hacktics.diviner.analyze.HISTORY_MODE;

public class ParameterInfo {

	private String paramName;
	private String sourcePage;
	private ArrayList <ParameterEffect> effectsList;
	private HashSet<ParameterEffect> effectsListView;
	
	public ParameterInfo(String paramName, String sourcePage,
			ArrayList<ParameterEffect> effectsList) {
		super();
		this.paramName = paramName;
		this.sourcePage = sourcePage;
		this.effectsList = effectsList;
		this.effectsListView = new HashSet<ParameterEffect>();
		
		if (effectsList != null) {
			for (ParameterEffect effect : effectsList) {
				effectsListView.add(effect);
			}
		}
	}
	
	
	public String getParamName() {
		return paramName;
	}
	public ArrayList<ParameterEffect> getEffectsList() {
		return effectsList;
	}
	public String getSourcePage() {
		return sourcePage;
	}
	
	public HashSet<ParameterEffect> getEffectsListView() {
		
		return effectsListView;
	}
	
}
