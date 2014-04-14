package com.hacktics.diviner.csrf;

import java.net.URL;
import java.util.Set;

/**
 * 
 * @author Eran Tamari
 *
 */

public class Url {

	private Set<String> parameters;
	private String path;
	private String method;

	public Url(String urlStr, String method, Set<String> params) {
		try {
			URL url = new URL(urlStr);
			this.path = url.toString();
			this.method = method;
			this.parameters = params;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Set<String> getParameters() {
		return parameters;
	}
	public String getPath() {
		return path;
	}
	public String getMethod() {
		return method;
	}

	//TODO: compare by parameters
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Url)) {
			return false;
		}
		Url url = (Url) obj;
		if (! this.method.toString().equalsIgnoreCase(url.getMethod())) {
			return false;
		}
		
		if (! this.path.toString().equals(url.getPath())) {
			return false;
		}
		
		for (String paramName : this.parameters) {
			if (! url.getParameters().contains(paramName)) {
				return false;
			}
		}
		
		for (String paramName : url.getParameters()) {
			if (! this.parameters.contains(paramName)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = this.path.hashCode() + this.method.hashCode();
		for (String paramName : parameters) {
			hash += paramName.hashCode();
		}
		return hash;
	}
}
