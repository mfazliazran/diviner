package com.hacktics.diviner.zapDB;

import java.net.URL;
import java.util.ArrayList;

/**
 * 
 * @author Eran Tamari
 *
 */

public class Page {
	
	private URL url;
	private int lastHistoryRef;
	private ArrayList<Integer> historyRefs;
	
	public Page(URL i_url)
	{
		url = i_url;
	}
	
	public URL getURL()
	{
		return url;
	}
	
	
	public String getURLWithoutParams()
	{
		return url.toString().split("\\?")[0];
	}
	
	public int getLastHistoryRef()
	{
		return lastHistoryRef;
	}
	
	public ArrayList<Integer> getHistoryRefList()
	{
		return historyRefs;
	}
	
	public void SetURL(URL i_url)
	{
		url = i_url;
	}
	
	public void SetLastHistoryRef(int i_lastRef)
	{
		lastHistoryRef = i_lastRef;
	}
	
	public void SetHistoryRefList(ArrayList<Integer> i_lastRefList)
	{
		historyRefs = i_lastRefList;
	}
	
}
