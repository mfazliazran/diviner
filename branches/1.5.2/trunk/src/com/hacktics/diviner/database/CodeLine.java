package com.hacktics.diviner.database;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.analyze.CodeBehaviour;

public class CodeLine {

	private String paramName;
	private String pageName;
	private int behaviour;
	private String codeLine;
	private int sourceId;
	int probability; 
	
	public CodeLine(int behaviourId, String pageName, String paramName, int codeId, int sourceId, int probability) {
		
		this.behaviour = behaviourId;
		this.pageName = pageName;
		this.paramName = paramName;
		this.sourceId = sourceId;
		this.probability = probability;
		String temp1;
		String temp2;
		String temp3;
		
		this.codeLine = CodeBehaviour.values()[behaviourId].getJavaSourceCode();
		temp1 = this.codeLine.replace(AnalyzerUtils.ANCHOR1, "<b>" + codeId + "</b>");
		temp2 = temp1.replace(AnalyzerUtils.ANCHOR2, "<b>" + paramName + "</b>");
		
		temp3 = temp2.replace(AnalyzerUtils.ANCHOR3, "<b>SessionAttribute" + codeId + "</b>");
		this.codeLine = "<html>" + temp3 +"</html>";
	}
	
	public String getSourceCode() {
		return codeLine;
	}
	
	public int getProbability () {
		return probability;
	}
	
	public String getParamName() {
		return paramName;
	}

	public String getPageName() {
		return pageName;
	}
	
	public int getSourceId() {
		return this.sourceId;
	}

	public int getBehaviour() {
		return behaviour;
	}
}
