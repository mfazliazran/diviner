package com.hacktics.diviner.gui;

/**
 * 
 * @author Eran Tamari
 *
 */
public class Statistics {

	private int numOfParams;
	private int requestId;
	private String reponseCode;
	private boolean isSSL;
	private String method;
	
	public Statistics(int numOfParams, int requestId, String reponseCode,
			boolean isSSL, String method) {
		super();
		this.numOfParams = numOfParams;
		this.requestId = requestId;
		this.reponseCode = reponseCode;
		this.isSSL = isSSL;
		this.method = method;
	}
	
	public Statistics (int numOfParams){
		this.numOfParams = numOfParams;
	}
	
	public int getNumOfParams() {
		return numOfParams;
	}
	public void setNumOfParams(int numOfParams) {
		this.numOfParams = numOfParams;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getReponseCode() {
		return reponseCode;
	}
	public void setReponseCode(String reponseCode) {
		this.reponseCode = reponseCode;
	}
	public boolean isSSL() {
		return isSSL;
	}
	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
}
