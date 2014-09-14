package com.hacktics.diviner.gui;

import java.util.Comparator;

import com.hacktics.diviner.analyze.HISTORY_MODE;
import com.hacktics.diviner.analyze.SCENARIO_MODE;
import com.hacktics.diviner.analyze.TOKEN_TYPE;
import com.hacktics.diviner.database.DivinerRecordResult;

//Customer comparator which check for duplicated records based on predefined fields
public class recordCompare implements Comparator<DivinerRecordResult> {

	@Override
	public int compare(DivinerRecordResult r1, DivinerRecordResult r2) {
		if (isRecordEqual(r1,r2))
			return 0;
		return 1;
	}

	public Boolean isRecordEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (isZapSessionEqual(r1,r2) && isEventEqual(r1,r2) && isSrcTargetEqual(r1,r2) && isScenarioEqual(r1,r2) && isOriginalValEqual(r1,r2) && isHistoryModeEqual(r1,r2) && isTokenTypeEqual(r1,r2))
			return true;
		return false;		
	}
	
	public Boolean isZapSessionEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if ((r1.getInputID() == r2.getInputID()) && (r1.getOutputID() == r2.getOutputID()))
			return true;
		return false;
	}
	
	public Boolean isEventEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getType() == r2.getType())
			return true;
		return false;
	}
	
	public Boolean isTokenValEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getTokenValue().equals(r2.getTokenValue()))
			return true;
		return false;
	}
	
	public Boolean isSrcTargetEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getInPage().equals(r2.getInPage()) && r1.getOutPage().equals(r2.getOutPage()))
			return true;
		return false;
	}
	
	public Boolean isScenarioEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getScenario() == r2.getScenario())
			return true;
		return false;
	}
	
	public Boolean isOriginalValEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getValue().equals(r2.getValue()))
			return true;
		return false;
	}
	
	public Boolean isHistoryModeEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getHistMode() == r2.getHistMode())
			return true;
		return false;
	}
	
	public Boolean isTokenTypeEqual(DivinerRecordResult r1, DivinerRecordResult r2)
	{
		if (r1.getTokenType() == r2.getTokenType())
			return true;
		return false;
	}
}
