package com.hacktics.diviner.csrf;

/**
 * 
 * @author Eran Tamari
 *
 */

public class CsrfToken {

	private String name;
	private String value;
	private Url url;
	private CSRF_TOKEN_TYPE type;

	

	public CsrfToken(String name, String value, Url url, CSRF_TOKEN_TYPE type) {
		this.name = name;
		this.value = value;
		this.url = url;
		this.type = type;
	}
	
	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public static enum CSRF_TOKEN_TYPE {
		PER_PAGE, PER_SESSION
	};


	
	public CsrfToken clone() {
		return new CsrfToken(this.name, this.value, this.url, this.type);
	}
	
	public String getName() {
		return name;
	}

	public CSRF_TOKEN_TYPE getType() {
		return type;
	}

	public void setType(CSRF_TOKEN_TYPE type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
