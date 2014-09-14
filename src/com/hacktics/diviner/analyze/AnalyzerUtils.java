package com.hacktics.diviner.analyze;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.StringEscapeUtils;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.zaproxy.zap.network.HttpRequestBody;

import com.hacktics.diviner.csrf.CsrfToken;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.database.DivinerTableHistory;
import com.hacktics.diviner.diffutil.Response_Manager;
import com.hacktics.diviner.diffutil.UnRelevantTag;
import com.hacktics.diviner.gui.scanwizard.DiffCheckBox;
import com.hacktics.diviner.gui.scanwizard.ScanWizard;
import com.hacktics.diviner.tokens.TokenGenerator;
import com.hacktics.diviner.zapDB.ZapHistoryDB;

/**
 * 
 * @author Eran Tamari
 *
 */

public class AnalyzerUtils {

	private static ArrayList<RecordHistory> historyListToDisplayAllRequests;
	private static ArrayList<RecordHistory> historyListToAnalyze;
	private static ArrayList<String> historyListSelectedHost;
	private static int startIndex = 0;
	private static int previousStartIndex = 0;
	private static boolean isPagingSuccessful;
	private int displayedRequestsCounter = 0;
	private static Stack<Integer> previousIndexes = new Stack<Integer>();
	private static final Boolean NUMERIC = true;
	private static int currentPageCounter = 0;
	private static ArrayList<DiffCheckBox> diffsList;
	public static DivinerTableHistory historyTable = ZapHistoryDB.historyTable;
	private static ArrayList<CsrfToken> csrfTokens = new ArrayList<CsrfToken>();
	public static final String ANCHOR1 = "!!!1";
	public static final String ANCHOR2 = "!!!2";
	public static final String ANCHOR3 = "!!!3";
	public static final int NO_LIMIT = -1;
	private static final int PAGE_BLOCK = 10;

	/**
	 * Read the history record from ZAP DB
	 * @param historyId the record to read
	 * @return
	 */
	public static RecordHistory readZapHistory(int historyId)
	{
		RecordHistory result = null;
		try{
			result = ZapHistoryDB.getZapHistoryTable().read(historyId);
		}
		catch(Exception e){	e.printStackTrace(); }

		return result;
	}

	/**
	 * Sets the parameter with a new value and returns true if successful
	 * 
	 * @param request
	 * @param parameterName
	 * @param ParameterValue
	 * @return
	 */
	public static void setParameterGetRequest(HttpMessage msg, String parameterName, String ParameterValue) {

		StringBuffer query = new StringBuffer();

		for (HtmlParameter param : msg.getUrlParams()) {
			if (param.getName().equals(parameterName)) {
				param.setValue(ParameterValue);
			}

			query.append(param.getName() + "=" + param.getValue() + "&");

		}
		try{
			query.deleteCharAt(query.length() - 1);
		}
		catch(Exception ex) { ex.printStackTrace();}
		try {
			msg.getRequestHeader().getURI().setQuery(query.toString());
		} catch (URIException e) {	e.printStackTrace();	}
	}

	public static void setParameterPostRequest(HttpMessage msg,
			String parameterName, String ParameterValue) {

		StringBuffer query = new StringBuffer();
		for (HtmlParameter param : msg.getFormParams()) {
			if (param.getName().equals(parameterName)) {
				param.setValue(ParameterValue);
			}
			query.append(param.getName() + "=" + param.getValue() + "&");
		}
		query.deleteCharAt(query.length() - 1);
		msg.setRequestBody(new HttpRequestBody(query.toString()));
		msg.getRequestHeader().setContentLength(query.length());

	}


	/**
	 * Extracts all parameters from requests. Takes care of the POST requests which include URL params
	 * @param msg
	 * @return
	 */
	public static TreeSet<HtmlParameter> getMsgParams(HttpMessage msg) {
		TreeSet<HtmlParameter> params = new TreeSet<HtmlParameter>();
		params = msg.getUrlParams();
		if (msg.getRequestHeader().getMethod().toString().equalsIgnoreCase("POST")) {
			params.addAll(msg.getFormParams());
		}
		return params;
	}

	/**
	 * Gets all HTTP msgs in scope
	 * @return
	 */
	public static ArrayList<RecordHistory> getRequestsForSelectedHost(int sourceId, int limit) {
		ArrayList<RecordHistory> tempList = new ArrayList<RecordHistory>();
		ArrayList<RecordHistory> requestsList = new ArrayList<RecordHistory>();

		//Go through all requests to selected host
		for (RecordHistory record : historyListToAnalyze) {

			//Stop at source request
			if (record.getHistoryId() == sourceId){
				break;
			}

			//0 in history type means the page was not visited - so we don't add those pages
			if (record.getHistoryType() != 0) {	
				tempList.add(record);
			}


		}

		//There's a limit of requests set by the user
		int numOfRequsts = tempList.size();

		if (limit <= numOfRequsts) {
			for (; limit > 0; limit--) {
				requestsList.add(tempList.get(numOfRequsts - limit));
			}
		}
		else {
			requestsList = tempList;
		}

		return requestsList;
	}

	/**
	 * Gets all analyzing results from the diviner result table
	 * @return
	 */
	public static ArrayList<DivinerRecordResult> getAllResults()
	{
		return ZapHistoryDB.getAllResults();
	}

	public static ArrayList<DivinerRecordResult> getAllDistinctResults()
	{
		ArrayList<DivinerRecordResult> results = null;
		try{
			results = ZapHistoryDB.getAllResults();
			//results = ZapHistoryDB.getAllDistinctResults();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	/**
	 * Sends partial or full history until reaches the source page
	 * @param sourceMsg
	 * @param targetMsg
	 * @param sessionID
	 * @param fullHistory
	 */
	public static void sendNotAffectingRequestUntilPage(RecordHistory source, RecordHistory target, TreeSet<HtmlParameter> sessionID, boolean fullHistory, int limit) {
		try {
			HttpMessage sourceMsg = source.getHttpMessage().cloneRequest();
			HttpMessage targetMsg = target.getHttpMessage().cloneRequest();

			String sourceURI = (targetMsg.getRequestHeader().getURI().getPath() != null) ? targetMsg.getRequestHeader().getURI().getPath().toString() : "/";
			String targetURI = (sourceMsg.getRequestHeader().getURI().getPath() != null) ? sourceMsg.getRequestHeader().getURI().getPath().toString() : "/";

			//Run through all the requests in history and send only the relevant ones
			for (RecordHistory record : getRequestsForSelectedHost(source.getHistoryId(), limit)) {

				//Stop when all messages up to the source were sent
				if (record.getHistoryId() == source.getHistoryId()){
					break;
				}

				HttpMessage msg = record.getHttpMessage().cloneRequest();
				String msgURI = (msg.getRequestHeader().getURI().getPath() != null) ? msg.getRequestHeader().getURI().getPath().toString() : "/";

				//If equals to the injection page uri or equals to login request- don't send it
				if (isEqualToLogin(msg) || sourceURI.equals(msgURI)) { 
					continue;
				}
				// If page is affecting analyzed page or page has the same URI as the analyzed page- don't send it
				if (!fullHistory && (ZapHistoryDB.isAffecting(sourceMsg, targetMsg) || targetURI.equals(msgURI))) { 
					continue;
				}

				msg.setCookieParams(sessionID);

				RequestSender.send(msg);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the String URLs of sites in scope
	 * @return
	 */
	public static ArrayList<String> getSitesOfSelectedHostForAnalyze() {
		return historyListSelectedHost;
	}
	
	public static int getPagesPerBlock() {
		return PAGE_BLOCK;
	}

	public static int getStartIndex () {
		return startIndex;
	}

	public static void restartStartIndex () {
		startIndex = 0;
	}


	public static void setPreviousIndex () {
		if (startIndex >= currentPageCounter) {
			startIndex -= (currentPageCounter);
		}
		
		if (startIndex >= PAGE_BLOCK) {
			startIndex -= (PAGE_BLOCK);
		}
		
	}

	public static boolean isPagingSuccessful() {
		return isPagingSuccessful;
	}

	public static int getTotalNumOfPages() {
		int result = 0;
		if (historyListToDisplayAllRequests != null) {
			
			if (historyListToDisplayAllRequests.size() % PAGE_BLOCK > 0) {
				result ++;
			}
			result += (historyListToDisplayAllRequests.size() / PAGE_BLOCK);
		}
		return result;
	}

//	public static void saveLastIndex() {
//		previousIndexes.push(startIndex);
//	}

	public static ArrayList<RecordHistory> getHistoryOfSelectedHostForRequestsDisplay(ArrayList<String> filters) {

		ArrayList <RecordHistory> result = new ArrayList<RecordHistory>();
		int displayCounter = 0;
		for (int i = startIndex; i < historyListToDisplayAllRequests.size(); i++) {
			RecordHistory request = historyListToDisplayAllRequests.get(i);
			//Zap's history type "temp" means that the page was not visited - we don't need to display them
			if (request.getHistoryType() != HistoryReference.TYPE_TEMPORARY) {
				boolean isFiltered = false;
				for (String filter : filters) {
					Pattern uriRegex = Pattern.compile(filter);
					String uri = "/";
					try {
						uri = request.getHttpMessage().getRequestHeader().getURI().getPath();
					}
					catch (Exception e) { e.printStackTrace(); }
					Matcher matcher = uriRegex.matcher(uri);
					if (matcher.find()) {
						isFiltered = true;
					}
				}

				//If request is filtered - continue to next request
				if (isFiltered) {
					continue;
				}

				result.add(historyListToDisplayAllRequests.get(i));
				displayCounter++;
				if (PAGE_BLOCK <= displayCounter){
					break;
				}
			}

		}

		//If there are new entry points to show in the new page - need to show them
		if (displayCounter > 0) {
			isPagingSuccessful = true;
			currentPageCounter = displayCounter;
		}
		else {
			isPagingSuccessful = false;
		}

		startIndex += displayCounter;
		return result;
	}

	/**
	 * Adds a URL to scope
	 * @param uri
	 */
	public static void addURLToScope(String uri) {
		ZapHistoryDB.addURLToScope(uri);
	}



	/**
	 * Sets a new host in the diviner DB and stores its scope in a list designated to be used for analyzing process scope
	 * @param hostname
	 */
	public static void setHostForAnalyze(String hostname) {
		ZapHistoryDB.setHost(hostname);
		historyListSelectedHost = ZapHistoryDB.getScopeUrls();
		historyListToAnalyze = ZapHistoryDB.getScope();
	}

	/**
	 * Sets a new host in the diviner DB and stores its scope in a list designated to be displayed in the Requests panel
	 * @param hostname
	 */
	public static void setHostForRequestsDisplay(String hostname) {
		ZapHistoryDB.setHost(hostname);
		historyListToDisplayAllRequests = ZapHistoryDB.getCleanScope();
	}

	/**
	 * Resets the urls in scope of the selected domain
	 */
	public static void resetHistory() {
		ZapHistoryDB.resetHistory();
	}

	public static void updateAnalyzeScope() {
		historyListToAnalyze = ZapHistoryDB.getScope();
	}

	public static ArrayList<RecordHistory> getHistoryOfSelectedHostToAnalyze() {
		return historyListToAnalyze;
	}

	/**
	 * Get the scope of the actual scan, after the user selected a host and
	 * included/excluded pages
	 * 
	 * @return
	 */

	/**
	 * features 2 modes - 
	 * replacement : token will replace the original parameter value
	 * append : token will be appended to the original value
	 * @param paramValue
	 * @param isTokenAppendMode
	 * @return
	 */
	public static String getToken(String paramValue, boolean isTokenAppendMode) {

		String result = paramValue;
		String token;
		if (isNumeric(paramValue)) {
			token = generateToken(NUMERIC);
		} else {
			token = generateToken(!NUMERIC);
		}
		//Token should be appended to origianl value
		if (isTokenAppendMode) {
			result = result.concat(token);
		}

		//Token should be replaced with original value
		else {
			result = token;
		}

		return result;

	}

	private static boolean isNumeric(String str) {
		return str.matches("\\d+");
	}

	//Generates a numeric token or Alpha numeric - depending on the original value
	private static String generateToken(Boolean isNumericParam) {
		if (isNumericParam == NUMERIC) {
			return TokenGenerator.GenerateTokens(TokenGenerator.INT_TOKEN_TYPE);
		}

		return TokenGenerator.GenerateTokens(TokenGenerator.STRING_TOKEN_TYPE);
	}

	/**
	 * A message is "login equal" if it contains the same params as the login (and has the same uri of course) 
	 * @param msg
	 * @return
	 */
	public static boolean isEqualToLogin(HttpMessage msg)
	{
		boolean equal = false;
		HttpMessage loginRequest = ScanWizard.getLoginRequest();
		//The messages are equal in URI and the message contains the same parameters as the login request
		if (msg.getRequestHeader().getURI().toString().equals(loginRequest.getRequestHeader().getURI().toString()) && equalInParameters(loginRequest, msg)){
			equal = true;
		}
		return equal;
	}

	private static boolean equalInParameters(HttpMessage msg1, HttpMessage msg2){
		for (String paramName : msg1.getParamNames()){
			if (!containsParam(msg2, paramName))
				return false;
		}
		return true;
	}

	private static boolean containsParam(HttpMessage msg, String paramName){
		for (String currentName : msg.getParamNames()){
			if (paramName.toString().equals(currentName))
				return true;
		}
		return false;
	}

	/**
	 * Two messages are equal if they have the same uri,port,http/https,method and request body
	 * @param msg1
	 * @param msg2
	 * @return
	 */
	public static boolean httpMessageEqual(HttpMessage msg1, HttpMessage msg2) {

		// compare method
		if (!msg1.getRequestHeader().getMethod().equalsIgnoreCase(msg2.getRequestHeader().getMethod())) {
			return false;
		}

		// compare host, port and URI
		//TODO: REMOVE REPLAYABLE PARAMETERS - timestamp + antiCSRF
		URI uri1 = msg1.getRequestHeader().getURI();
		URI uri2 = msg2.getRequestHeader().getURI();

		if (uri1.getPort() != uri2.getPort()) {
			return false;
		}

		if (msg1.getRequestHeader().getSecure() != msg2.getRequestHeader().getSecure()) {
			return false;
		}

		//Currently - different replayable parameters will cause identical pages to be tested twice - but that will enable us to test pages in a dispatcher application model
		if (!uri1.toString().equals(uri2.toString())) {
			return false;
		}

		//the HttpBody.STORAGE_CHARSET was replaced with DEFAULT_CHARSET in 1.4, but can
		//still be changed via the setCharset method instead of the toString method. Currently - we don't change it.
		//Currently - different replayable parameters will cause identical pages to be tested twice - but that will enable us to test pages in a dispatcher application model
		if (msg1.getRequestHeader().getMethod().equalsIgnoreCase(HttpRequestHeader.POST)) {
			if(!(msg1.getRequestBody().toString(/*HttpBody.STORAGE_CHARSET*/).equalsIgnoreCase(msg2.getRequestBody().toString(/*HttpBody.STORAGE_CHARSET*/)))) {
				return false;
			}

		} 

		//If all OK, return true
		return true;
	}


	public static String getParamValueByName (HttpMessage msg, String paramName) {

		String value = "";
		for (HtmlParameter param : AnalyzerUtils.getMsgParams(msg)) {
			if (param.getName().toString().equals(paramName)) {
				value = param.getValue();
				break;
			}
		}
		return value;
	}




	/**
	 * This method generates a new session identifier (and any accompanying cookie parameters).
	 * 
	 * @return a collection of cookie parameters which is returned from the public session generation page 
	 */
	public static TreeSet<HtmlParameter> getNewPublicSession(){
		
		TreeSet<HtmlParameter> result = null;
		HttpMessage publicSessionRequest = ScanWizard.getPublicSessionRequest().cloneRequest();
		
		try{			
			RequestSender.send(publicSessionRequest);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			result = publicSessionRequest.getResponseHeader().getCookieParams();
		}
		//Will throw exception when no set-cookie was sent by server - Cannot proceed
		catch(Exception e) {
			e.printStackTrace();
		}
	
		return result;
	}  //end of method


	public static TreeSet<HtmlParameter> getNewLoginSession(TreeSet<HtmlParameter> sessionCookies){

		TreeSet<HtmlParameter> result = null;
		HttpMessage loginSessionRequest = ScanWizard.getLoginRequest().cloneRequest();
		
		//Send the request with supplied cookies
		if (sessionCookies != null) {
			loginSessionRequest.setCookieParams(sessionCookies);
		}

		RequestSender.send(loginSessionRequest);

		try {
			result = loginSessionRequest.getResponseHeader().getCookieParams();
		}
		//Will throw exception when no set-cookie was sent by server- take previous cookie
		catch(Exception e) {
			result = loginSessionRequest.getRequestHeader().getCookieParams();
			e.printStackTrace();
		}
		return result;
	}  //end of method


	public static void setCsrfTokens(ArrayList<CsrfToken> csrfTokens) {
		AnalyzerUtils.csrfTokens = csrfTokens; 

	}

	public static ArrayList<CsrfToken>  getCsrfTokens() {
		return csrfTokens; 
	}

	//Gets the combined cookies from two lists of cookies, while updating cookie values to new ones
	public static TreeSet<HtmlParameter> getCombindCookies( TreeSet<HtmlParameter> oldCookies, TreeSet<HtmlParameter> newCookies) {

		TreeSet<HtmlParameter> cookieClone = new TreeSet<HtmlParameter>();
		//Add old cookies that their names don't appear in the new cookie
		for (HtmlParameter oldCookie : oldCookies){
			if (!isCookieFound(newCookies, oldCookie.getName())) {
				cookieClone.add(new HtmlParameter(HtmlParameter.Type.cookie, oldCookie.getName(), oldCookie.getValue()));
			}
		}

		//Add all the new cookies to the final combined cookie list
		for (HtmlParameter newCookie : newCookies){
			cookieClone.add(new HtmlParameter(HtmlParameter.Type.cookie, newCookie.getName(), newCookie.getValue()));
		}
		return cookieClone;
	}

	//Checks if cookieName appears in the the given list of cookies 
	private static boolean isCookieFound(TreeSet<HtmlParameter> Cookies, String cookieName) {
		boolean isFound = false;
		for (HtmlParameter cookie : Cookies) {
			if (cookie.getName().equals(cookieName)) {
				isFound = true;
				break;
			}
		}
		return isFound;

	}

	public static ArrayList<DivinerRecordResult> getResultsPerPageAndParam (String page, long paramId) {
		ArrayList<DivinerRecordResult> results = null;
		try {
			results =  ZapHistoryDB.getResultsPerPageAndParam(page, paramId);
		}
		catch(SQLException e) { e.printStackTrace(); }
		return results;
	}

	public static ArrayList<DivinerRecordResult> getUniqueResultsPerPageAndParam (String page, long paramId) {
		ArrayList<DivinerRecordResult> results = null;
		try {
			results =  ZapHistoryDB.getUniqueResultsPerPageAndParam(page, paramId);
		}
		catch(SQLException e) { e.printStackTrace(); }
		return results;
	}

	private static List<UnRelevantTag> getUnrelevantTags() {

		ArrayList<UnRelevantTag> tagsList = new ArrayList<UnRelevantTag>();
		for (CsrfToken csrfToken : getCsrfTokens()) {

			tagsList.add(new UnRelevantTag("<input", csrfToken.getName(), ">"));
			tagsList.add(new UnRelevantTag("\\?", csrfToken.getName(), ">"));
			tagsList.add(new UnRelevantTag("&", csrfToken.getName(), "/>"));

		}
		return tagsList;
	}
	public static HashMap<Integer, Response_Manager> createDiffList() {

		HashMap<Integer, Response_Manager> diffsMap = new HashMap<Integer, Response_Manager>();

		//Starting with public session and update session in each request if changes
		TreeSet<HtmlParameter> currentSession = getNewPublicSession();
		//Iterate scope requests
		for (RecordHistory record : getHistoryOfSelectedHostToAnalyze()) {
			//Temporary Zap history should not be compared
			if (record.getHistoryType() == HistoryReference.TYPE_TEMPORARY) {
				continue;
			}

			HttpMessage originalMsg = record.getHttpMessage();
			HttpMessage replicatedMsg = record.getHttpMessage().cloneRequest();

			//Set cookies in the first request
			TreeSet<HtmlParameter> replicatedMsgCookies = replicatedMsg.getCookieParams();
			TreeSet<HtmlParameter> combinedSession = getCombindCookies(replicatedMsgCookies, currentSession);
			replicatedMsg.setCookieParams(combinedSession);

			//Send request
			RequestSender.send(replicatedMsg);
			
			//Update current session if a new cookie was added
			currentSession = getCombindCookies(currentSession,replicatedMsg.getResponseHeader().getCookieParams());

			//Add to diffs map
			ArrayList<String> originalResponseList = new ArrayList<String>();
			originalResponseList.add(originalMsg.getResponseBody().toString());
			Response_Manager ResponseManager = new Response_Manager(replicatedMsg.getResponseBody().toString(), originalResponseList, getUnrelevantTags());
			//Add to map only when there are differences in responses
			if (ResponseManager.getResponses(0).getDiffs().size() > 0) {
				diffsMap.put(new Integer(record.getHistoryId()), ResponseManager);

			}
		}

		return diffsMap;
	}

	public static ArrayList<DiffCheckBox> getDiffsList() {
		return diffsList;
	}

	public static void setDiffsList(ArrayList<DiffCheckBox> diffsList) {
		AnalyzerUtils.diffsList = diffsList;
	}

	public static ArrayList<DivinerRecordResult> getLeads () {
		ArrayList<DivinerRecordResult> result = null;
		try {
			result = ZapHistoryDB.getLeads();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}

	
	public static String encodeHTML(String str) {
		if (str == null) {
			str = "";
		}
		return StringEscapeUtils.escapeHtml(str).replace("\r", "\\r").replace("\n", "\\n");
		
	}
}
