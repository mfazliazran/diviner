package com.hacktics.diviner.analyze;

public enum TOKEN_TYPE {

	RANDOM_VALUE , VALID_VALUE, INVALID_VALUE;
	private boolean validEnabled = false;
	private boolean invalidEnabled = false;
	private boolean randomEnabled = false;
	
	private String validToken = "";
	private String invalidToken = "";
	private String randomToken = "";
	
	public boolean isEnabled(TOKEN_TYPE type) {
		boolean result = true; 
		switch (type) {
		case INVALID_VALUE:
			result = invalidEnabled;
			break;
		case RANDOM_VALUE:
			result = randomEnabled;
			break;
		case VALID_VALUE:
			result = validEnabled;
			break;
		}
		return result;
	}

	public void setEnabled(TOKEN_TYPE type) {
		switch (type) {
		case INVALID_VALUE:
			invalidEnabled = true;
			break;
		case RANDOM_VALUE:
			randomEnabled = true;
			break;
		case VALID_VALUE:
			validEnabled = true;
			break;
		}
	}

	public String getToken(TOKEN_TYPE type) {
		String result = ""; 
		switch (type) {
		case INVALID_VALUE:
			result = invalidToken;
			break;
		case RANDOM_VALUE:
			result = randomToken;
			break;
		case VALID_VALUE:
			result = validToken;
			break;
		}
		return result;
	}
	
	public void setToken(TOKEN_TYPE type, String tokenValue) {
		switch (type) {
		
		case INVALID_VALUE:
			invalidToken = tokenValue;
			break;
		case RANDOM_VALUE:
			randomToken = tokenValue;;
			break;
		case VALID_VALUE:
			validToken = tokenValue;;
			break;
		}
	}

}
