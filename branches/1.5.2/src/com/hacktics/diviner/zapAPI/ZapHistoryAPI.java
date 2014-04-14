package com.hacktics.diviner.zapAPI;

import java.io.InputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.InputSource;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.zaproxy.zap.extension.api.API;

public class ZapHistoryAPI {

	private static API zapAPI = new API();
	private static URL HOSTS_URL;
	private static URL SITES_URL;
	private static URL ALERTS_URL;
	private static URL HTTP_URL;
	private static URL URLS_URL;
	private static String [] result = null;
	private ArrayList <String> SelectedHostURLs;

	public ZapHistoryAPI(){
		
		try{
			HOSTS_URL = new URL("http://zap/XML/core/view/hosts/");
			ALERTS_URL = new URL("http://zap/XML/core/view/alerts/");
			SITES_URL = new URL("http://zap/XML/core/view/sites/");
			HTTP_URL = new URL("http://zap/XML/core/view/http/");
			URLS_URL = new URL("http://zap/XML/core/view/urls/");
								
		}
		catch (MalformedURLException e){
			e.printStackTrace();
		}
	}
	public static String []  GetZapHistoryXML()
	{
		requestAPI(HOSTS_URL);
		return result;
	}
	
	public static ArrayList<String> GetSitesPerHost(String host)
	{
		ArrayList <String> SelectedHostURLs = new ArrayList<String>();
		Pattern hostRegex = Pattern.compile("^" + "https?://" +  host);
		requestAPI(URLS_URL);
		for (String site : result)
		{
			Matcher matcher = hostRegex.matcher(site);
			
			//The site belongs to the selected host
			if (matcher.find())
			{
				SelectedHostURLs.add(site);
			}
		}
		return SelectedHostURLs;
	}
	
	//Extracts elements from the XML API of ZAP
	private static void requestAPI(URL API_URL){
				
		try {
		    // Create a URLConnection object for a URL with a proxy
			
			URL url = API_URL;
		    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8899));
		    URLConnection conn = url.openConnection(proxy);
		
		    Object contents = conn.getContent();
		    InputStream is = (InputStream) contents;
		    StringBuffer buf = new StringBuffer();
		    int c;
		    while (( c = is.read() ) != -1 ) {
		      buf.append( (char) c );
		    }
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource ins = new InputSource(new StringReader(buf.toString()));
	        org.w3c.dom.Document doc = builder.parse(ins);
	        NodeList hostsList = doc.getDocumentElement().getChildNodes();
	        int numOfNodes = hostsList.getLength();
	        result = new String[numOfNodes];
	        
	        for (int nodeIndex = 0; nodeIndex < numOfNodes; nodeIndex++)
	        { 
		
	        	Element node = (Element) hostsList.item(nodeIndex);
	        	result[nodeIndex] = node.getTextContent();
		    }
			   		
			} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
