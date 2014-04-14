package com.hacktics.diviner.zapDB;

/**
 * 
 * @author Eran Tamari
 *
 */

public class HttpParameter {

	private String name;
	private String value;
	
	
	public HttpParameter(String i_name, String i_value)
	{
		name = i_name;
		value = i_value;
	}
	public String GetValue()
	{
		return value;
	}
	
	public String GetName()
	{
		return name;
	}
	
	@Override
	public String toString() {
		return ("name: " + name + " value: " + value);
	}
}
