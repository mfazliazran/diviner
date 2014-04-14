package com.hacktics.diviner.database;

/**
 * 
 * @author Eran Tamari
 *
 */

public class DivinerRecordParam {

	private long paramId;
    private String page;
    private String method;
    private String name;
    private String value;
    
   
	public DivinerRecordParam(long paramId, String page, String method, String name, String value) {
		this.paramId = paramId;
		this.page = page;
		this.method = method;
		this.name = name;
		this.value = value;
	}
	
	public long getParamId() {
		return paramId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getName() {
		return name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

