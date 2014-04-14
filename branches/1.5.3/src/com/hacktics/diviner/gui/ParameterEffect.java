package com.hacktics.diviner.gui;


import com.hacktics.diviner.analyze.HISTORY_MODE;
import com.hacktics.diviner.analyze.SCENARIO_MODE;
import com.hacktics.diviner.analyze.TOKEN_TYPE;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ParameterEffect {

	public static final int DATABASE_REFLECTION = 0;
	public static final int OUTPUT_REFLECTION = 1;
	public static final int SESSION_REFLECTION = 2;
	public static final int DATABASE_EXCEPTION = 3;
	public static final int OUTPUT_EXCEPTION= 4;
	public static final int SESSION_EXCEPTION = 5;
	public static final int DIFF = 6;

	private int sourceID;
	private int targetID;
	private SCENARIO_MODE scenario;
	private HISTORY_MODE histMode;
	private TOKEN_TYPE tokenType;
	private boolean isTokenAppendMode;
	private String originalValue;
	private String tokenValue;
	private int effectType;
	private String outputPage;
	private boolean isDiff;
	private boolean isReflection;
	private boolean isException;
	private int diffPercentage;
	private String paramName;
	
	public String getOutputPage()
	{
		return outputPage;
	}

	public ParameterEffect(int sourceID, int targetID, SCENARIO_MODE scenario,
			HISTORY_MODE histMode, TOKEN_TYPE tokenType, boolean isTokenAppendMode,
			String originalValue, String tokenValue, int effectType,
			String outputPage, boolean isDiff, boolean isReflection,
			boolean isException, int diffPercentage, String paramName) {

		this.sourceID = sourceID;
		this.targetID = targetID;
		this.scenario = scenario;
		this.histMode = histMode;
		this.tokenType = tokenType;
		this.isTokenAppendMode = isTokenAppendMode;
		this.originalValue = originalValue;
		this.tokenValue = tokenValue;
		this.effectType = effectType;
		this.outputPage = outputPage;
		this.isDiff = isDiff;
		this.isReflection = isReflection;
		this.isException = isException;
		this.diffPercentage = diffPercentage;
		this.paramName = paramName;
	}

	
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public int getType()
	{
		return effectType;
	}

	public boolean isDiff() {
		return isDiff;
	}

	public int getDiffPercentage() {
		return diffPercentage;
	}

	public int getSourceID() {
		return sourceID;
	}

	public int getTargetID() {
		return targetID;
	}

	public SCENARIO_MODE getScenario() {
		return scenario;
	}

	public HISTORY_MODE getHistMode() {
		return histMode;
	}

	public TOKEN_TYPE getTokenType() {
		return tokenType;
	}

	public boolean isTokenAppendMode() {
		return isTokenAppendMode;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public boolean isReflection() {
		return isReflection;
	}

	public boolean isException() {
		return isException;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + diffPercentage;
		result = prime * result + effectType;
		result = prime * result
				+ ((histMode == null) ? 0 : histMode.hashCode());
		result = prime * result + (isDiff ? 1231 : 1237);
		result = prime * result + (isException ? 1231 : 1237);
		result = prime * result + (isReflection ? 1231 : 1237);
		result = prime * result + (isTokenAppendMode ? 1231 : 1237);
		result = prime * result
				+ ((outputPage == null) ? 0 : outputPage.hashCode());
		result = prime * result
				+ ((scenario == null) ? 0 : scenario.hashCode());
		result = prime * result + sourceID;
		result = prime * result + targetID;
		result = prime * result
				+ ((tokenType == null) ? 0 : tokenType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterEffect other = (ParameterEffect) obj;
		if (diffPercentage != other.diffPercentage)
			return false;
		if (effectType != other.effectType)
			return false;
		if (histMode != other.histMode)
			return false;
		if (isDiff != other.isDiff)
			return false;
		if (isException != other.isException)
			return false;
		if (isReflection != other.isReflection)
			return false;
		if (isTokenAppendMode != other.isTokenAppendMode)
			return false;
		if (outputPage == null) {
			if (other.outputPage != null)
				return false;
		} else if (!outputPage.equals(other.outputPage))
			return false;
		if (scenario != other.scenario)
			return false;
		if (sourceID != other.sourceID)
			return false;
		if (targetID != other.targetID)
			return false;
		if (tokenType != other.tokenType)
			return false;
		return true;
	}
	
	 
}
