package com.hacktics.diviner.analyze;

public class ResultItem {

	private boolean isTokenFound = false;
	private int diffPercetage = 0;
	
	public ResultItem(boolean isTokenFound, int diffPercentage) {
		this.diffPercetage = diffPercentage;
		this.isTokenFound = isTokenFound;
	}

	public boolean isTokenFound() {
		return isTokenFound;
	}

	public void setTokenFound(boolean isTokenFound) {
		this.isTokenFound = isTokenFound;
	}

	public int getDiffPercetage() {
		return diffPercetage;
	}

	public void setDiffPercetage(int diffPercetage) {
		this.diffPercetage = diffPercetage;
	}
	
	
	
}
