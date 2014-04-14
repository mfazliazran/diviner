package com.hacktics.diviner.analyze;

import java.util.ArrayList;
import java.util.regex.Pattern;
import com.hacktics.diviner.zapDB.HttpParameter;

/**
 * Zap Doesn't offer an easy way to extract parameters from a request, so I wrote it myself. (ZAP database stores parameters without their request ID)
 * @author Eran Tamari
 *
 */
public class ParameterParser {

	/**
	 * 
	 * @param RequestParameters - A string of request parameters without the "?" sign.
	 * @return A list of parameter objects 
	 */
	public static ArrayList<HttpParameter> Parse(String i_RequestParameters)
	{
		ArrayList<HttpParameter> parameterList = new ArrayList<HttpParameter>();
		if (i_RequestParameters != null)
		{
			String RequestParameters = Pattern.quote(i_RequestParameters); //Escape Regex dangerous chars
			String [] parameters = RequestParameters.split("&");
			for (String parameterStr : parameters)
			{
				String name = parameterStr.split("=")[0];
				String value = "";
				if (parameterStr.split("=").length == 2) //The parameter has a value
				{
					value = parameterStr.split("=")[1]; 
				}
				HttpParameter parameter = new HttpParameter(name , value);
				parameterList.add(parameter);
			}
		}
		return parameterList;
		
	}
	
	
}
