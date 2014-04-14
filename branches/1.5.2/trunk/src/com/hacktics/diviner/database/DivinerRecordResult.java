package com.hacktics.diviner.database;

import com.hacktics.diviner.analyze.HISTORY_MODE;
import com.hacktics.diviner.analyze.OUTPUT_LOCATION;
import com.hacktics.diviner.gui.OUTPUT_TYPE;
import com.hacktics.diviner.gui.RESULT_TYPE;

/**
 * 
 * @author Eran Tamari
 *
 */

public class DivinerRecordResult {

	private long resultId;
    private long paramID;
    private String inpage;
    private String outpage;
    private int type;
    private String name;
    private String value;
    private int scenario;
    private int histMode;
    private int inputPageID;
    private int outputPageID;
    private int diffPercent;
    private int tokenType;
    private boolean isTokenAppendMode;
    private String tokenValue;
    private int outputPlugin; 
    
    public DivinerRecordResult(long resultId, long paramID, String inpage,String outpage, int type, String name, String value, int scenario, int histMode, int inputPageID, int outputPageID, int outputPlugin, int diffPercent, int tokenType, boolean isTokenAppendMode, String tokenValue) {
		this.resultId = resultId;
		this.paramID = paramID;
		this.inpage = inpage;
		this.outpage = outpage;
		this.type = type;	//session/DB/reflection
		this.name = name;
		this.value = value;
		this.scenario = scenario;
		this.histMode = histMode;
		this.inputPageID = inputPageID;
		this.outputPageID = outputPageID;
		this.diffPercent = diffPercent;
		this.tokenType = tokenType;
		this.isTokenAppendMode = isTokenAppendMode;
		this.tokenValue = tokenValue;
		this.outputPlugin = outputPlugin;
	}
	
	public long getResultId() {
		return resultId;
	}
	
	public String getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	
	
	public int getInputID()
	{
		return inputPageID;
	}
	
	public int getOutputID()
	{
		return outputPageID;
	}

	public int getHistMode()
	{
		return histMode;
	}

	public int getScenario()
	{
		return scenario;
	}
	public String getInPage() {
		return inpage;
	}

	public void setInPage(String page) {
		this.inpage = page;
	}
	
	public String getOutPage() {
		return outpage;
	}

	public void setHistMode(HISTORY_MODE histMode) {
		this.histMode = histMode.ordinal();
	}
	
	public void setOutPage(String outpage) {
		this.outpage = outpage;
	}

	public long getParamID() {
		return paramID;
	}

	public void setParamID(long paramID) {
		this.paramID = paramID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDiffPercent() {
		return diffPercent;
	}

	public int getTokenType() {
		return tokenType;
	}

	public boolean isTokenAppendMode() {
		return isTokenAppendMode;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public int getOutputPlugin() {
		return outputPlugin;
	}

	public void setOutputPlugin(int outputPlugin) {
		this.outputPlugin = outputPlugin;
	}

	public boolean isParamReflected() {
		boolean result = false;
		if (outputPlugin == RESULT_TYPE.REFLECTION.ordinal()) {
			result = true;
		}
		return result;
	}
	
	public boolean isParamException() {
		boolean result = false;
		if (outputPlugin == RESULT_TYPE.EXCEPTION.ordinal()) {
			result = true;
		}
		return result;
	}
	
	public boolean isParamDiff() {
		boolean result = false;
		if (outputPlugin == RESULT_TYPE.DIFF.ordinal()) {
			result = true;
		}
		return result;
	}
	
	
}

