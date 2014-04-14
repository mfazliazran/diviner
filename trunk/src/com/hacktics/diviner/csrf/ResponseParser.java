package com.hacktics.diviner.csrf;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HttpMessage;
import com.hacktics.diviner.analyze.AnalyzerUtils;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ResponseParser {

	private static HashMap<Url, HashMap<String, CsrfToken>> perPageTokens = new HashMap<Url, HashMap<String, CsrfToken>>();
	private static CsrfToken perAppToken = null;
	private static final String NAME_ATTRIBUTE = "NAME";
	private static final String ID_ATTRIBUTE = "ID";

	/**
	 * Allows to store the CSRF tokens sent in Zap history 
	 */
	public static void loadCsrfTokensFromHistory() {
		ArrayList<CsrfToken> csrfTokens = AnalyzerUtils.getCsrfTokens();
		if (csrfTokens != null) {
			for (RecordHistory record : AnalyzerUtils.getHistoryOfSelectedHostToAnalyze()) {
				try{
					HttpMessage msg = record.getHttpMessage();
					Source  response = new Source(msg.getResponseHeader().toString() +
							msg.getResponseBody().toString());
					ResponseParser.parseTokens(msg.getRequestHeader().getURI().toString(), response);
				}
				catch (Exception e) {
					//If a request has no response, continue
				}
			}
		}
	}

	/**
	 * Gets the list of CSRF tokens and their values to be set in the request
	 * @param url
	 * @return
	 */
	public static HashMap<String,CsrfToken> getTokens(Url url) {
		HashMap<String,CsrfToken> result;
		//Add the per page Tokens (viewstate, etc.)
		result = perPageTokens.get(url);
		if (result == null) {
			result =  new HashMap<String,CsrfToken>();
		}
		//Add the per app tokens - only if the original request contains the CSRF token 
		if (perAppToken != null) {
			for (String paramName : url.getParameters()) {
				if (paramName.equals(perAppToken)) {
					result.put(perAppToken.getName(), perAppToken);
				}
			}
			
		}
		return result;
	}



	/**
	 * Parses the HTML from the HTTP response and maps the CSRF tokens found in the forms and links 
	 * @param requestUrlStr
	 * @param source
	 */
	public static void parseTokens(String requestUrlStr, Source source) {

		List<Element> formElements = source.getAllElements(HTMLElementName.FORM);
		URL requestUrl = null;
		try {
			requestUrl = new URL(requestUrlStr);
		}
		catch(MalformedURLException e) { e.printStackTrace(); }
		String uri = requestUrlStr;
		if (formElements != null && formElements.size() > 0) {
			// Loop through all of the FORM tags

			for (Element formElement : formElements) {
				List<Element> inputElements = formElement.getAllElements(HTMLElementName.INPUT);
				String actionUri = (formElement.getAttributeValue("action"));	//TODO: take the source uri if no action is found
				String method = (formElement.getAttributeValue("method"));	

				if (actionUri == null) {
					actionUri = requestUrlStr;
				}
				else{
					try{
						new URL(actionUri);	//If the uri is relative - an exception will be thrown
					}
					catch(MalformedURLException e) {	//need to construct the full uri path
						if (requestUrl == null) { return; }		//The initial request url isn't OK
						uri = requestUrlStr.split(requestUrl.getFile())[0] + actionUri;
					}
				}
				if (inputElements != null && inputElements.size() > 0) {

					lookForCSRFTokenInForm(inputElements, uri, method);

				}
			}
		}

		//This block handles URL CSRF Tokens
		List<Element> linkElements = source.getAllElements(HTMLElementName.A);
		lookForCSRFTokenInLinks(linkElements, requestUrlStr);
	}

	private static void lookForCSRFTokenInForm(List<Element> inputElements,String url, String method) {
		HashMap<String, String> parameters = new HashMap<String, String>();

		//Adding all the parameters in the form to an array of parameters
		for (Element inputElement : inputElements) {

			String name = inputElement.getAttributeValue(NAME_ATTRIBUTE);
			if (name != null) {
				parameters.put(name, inputElement.getAttributeValue("VALUE"));
			}
			else {
				String aId = inputElement.getAttributeValue(ID_ATTRIBUTE);
				if (aId != null) {//Put the parameter ID in the name list because the NAME does not exist
					parameters.put(aId, inputElement.getAttributeValue("VALUE"));
				}
			}
		}
		//Comparing the parameters' names to the CSRF tokens the user defined
		for (String paramName : parameters.keySet()) {

			for (CsrfToken token : AnalyzerUtils.getCsrfTokens()) {
				if (token.getName().toString().equalsIgnoreCase(paramName)) {
					Url TokenUrl = new Url(url, method, parameters.keySet());
					CsrfToken csrfToken = new CsrfToken(token.getName(), parameters.get(paramName), TokenUrl, token.getType());
					registerToken(csrfToken);
					break;
				}
			}

		}
	}

	/**
	 * 
	 * @param linkElements
	 * @param pageUrlStr Refers to the URL of the page that contains the form
	 */
	private static void lookForCSRFTokenInLinks(List<Element> linkElements, String pageUrlStr) {
		for (Element linkElement : linkElements) {
			String href=linkElement.getAttributeValue("href");
			URL pageUrl = null;

			if (href==null) {
				continue;
			}
			else {
				try{
					new URL(href);	//If href is relative - will throw an exception

				}
				catch(MalformedURLException e) {	//Need to construct full path of href
					try{
						pageUrl = new URL(pageUrlStr);
						href = pageUrlStr.split(pageUrl.getFile())[0] + href;
					}
					catch(MalformedURLException ex) {
						e.printStackTrace();
					}
				}
			}

			// A element can contain other tags so need to extract the text from it:
			try {
				URI hrefURI = new URI(href);
				String queryString = hrefURI.getQuery();
				if (queryString == null) { continue; }
				String [] params = queryString.split("&");
				for (String urlParam : params) {
					for (CsrfToken token : AnalyzerUtils.getCsrfTokens()) {
						String[] splitUri = urlParam.split(token.getName() + "=");
						if (splitUri.length > 1) {	//The token was found in the url
							HashMap<String, String> parameters = parseQueryString(queryString);
							Url tokenURL = new Url(href, "GET", parameters.keySet());
							CsrfToken csrfToken = new CsrfToken(token.getName(), splitUri[1], tokenURL, token.getType());
							registerToken(csrfToken);
						}
					}
				}
			}
			catch(Exception e) { e.printStackTrace(); }
		}
	}

	private static HashMap<String, String> parseQueryString(String query) {

		String[] params = query.split("&");  
		HashMap<String, String> map = new HashMap<String, String>();  
		for (String param : params)  
		{  
			String [] parsedParam = param.split("=");
			String name = parsedParam[0];  
			String value = parsedParam[1];  
			map.put(name, value);  
		}  
		return map;  
	}  

	private static void registerToken(CsrfToken csrfToken) {

		switch(csrfToken.getType()) {
		case PER_SESSION:
			perAppToken = csrfToken.clone();
			break;
		case PER_PAGE:
			HashMap<String, CsrfToken> tokensMap;
			tokensMap = perPageTokens.get(csrfToken.getUrl());
			if (tokensMap == null) {
				tokensMap = new HashMap<String, CsrfToken>();
			}
			tokensMap.put(csrfToken.getName(), csrfToken.clone());
			perPageTokens.put(csrfToken.getUrl(), tokensMap);
			break;
		}
	}

	//Removes CSRF tokens from the list of parameters
//	private static Set<String> removeTokenFromParams (Set<String> paramNames, String tokenName) {
//		Set<String> result = new HashSet<String>();
//		for (String name : paramNames) {
//			if (!name.equals(tokenName)) {
//				result.add(name);
//			}
//		}
//		return result;
//	}
}
