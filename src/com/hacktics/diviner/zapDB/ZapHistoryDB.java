package com.hacktics.diviner.zapDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.db.RecordParam;
import org.parosproxy.paros.db.TableHistory;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpMessage;

import com.hacktics.diviner.database.DivinerDatabase;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.database.DivinerTableFlow;
import com.hacktics.diviner.database.DivinerTableHistory;
import com.hacktics.diviner.database.DivinerTableParam;
import com.hacktics.diviner.database.DivinerTableResults;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ZapHistoryDB {

	private static ArrayList<RecordHistory> recordHistoryList;
	private static ArrayList<RecordHistory> recordHistoryListSelectedHost;

	private static ArrayList<RecordParam> recordParamList;
	private static long zapSessionID;
	private static ArrayList<Long> scopeIdRequests = new ArrayList<Long>();
	private static DivinerDatabase database;
	public static DivinerTableParam parameterTable;
	public static DivinerTableResults resultTable;
	public static DivinerTableHistory historyTable;
	public static DivinerTableFlow flowTable;

	public static void Initilize() {
		recordHistoryList = new ArrayList<RecordHistory>();
		recordHistoryListSelectedHost = new ArrayList<RecordHistory>(); // /Store
																		// only
																		// records
																		// of
																		// selected
																		// host
		recordParamList = new ArrayList<RecordParam>();
		zapSessionID = Model.getSingleton().getSession().getSessionId();// Read
																		// only
																		// current
																		// session's
																		// history
		database = new DivinerDatabase();
		parameterTable = database.getDivinerTableParam();
		resultTable = database.getDivinerTableResult();
		historyTable = database.getDivinerTableHistory();
		flowTable = database.getDivinerTableFlow();
	}
	
	public static ArrayList<DivinerRecordResult> getAllResults()
	{
		ArrayList<DivinerRecordResult> result = null;
		try{
		result = resultTable.getAll();
		}
		catch(SQLException e){e.printStackTrace();}
		
		return result;
	}

	public static boolean isAffecting(HttpMessage inputPage, HttpMessage outputPage ) throws SQLException, URIException{
		return resultTable.isAffecting(getURI(inputPage), getURI(outputPage));
	}
	
	private static String getURI(HttpMessage msg) throws URIException
	{
		
		return msg.getRequestHeader().getURI().getPath().toString();
	}
	
	public static void setHistoryFromZapDB() {
		Initilize();

		try {
			Vector<Integer> historyIDList;
			TableHistory tableHistory = Model.getSingleton().getDb()
					.getTableHistory();
			historyIDList = tableHistory.getHistoryList(zapSessionID);

			for (Integer historyID : historyIDList) {
				recordHistoryList.add(tableHistory.read(historyID));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static ArrayList<RecordHistory> getHistoryFromZapDB() {
		return recordHistoryList;
	}

	public static ArrayList<RecordHistory> getScope() {
		ArrayList<RecordHistory> result = null;
		try {
			result = historyTable.getScopeRecords();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	//Get scope without temporary requests
	public static ArrayList<RecordHistory> getCleanScope() {
		ArrayList<RecordHistory> result = null;
		try {
			result = historyTable.getCleanScopeRecords();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static void addURLToScope(String uri) {
		try {
			historyTable.setScope(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setHost(String hostname) {
		try {
			resetHistory();
			historyTable.setHost(hostname);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetHistory() {
		try {
			historyTable.resetHistory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getScopeUrls() {
		ArrayList<String> result = null;
		try {
			result = historyTable.getScopeUrlsAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	//Gets the first Post or last Get requests of the input url
	public static HttpMessage getRequestFromZapDBByURL(String url) {
		HttpMessage msg = null;
		for (RecordHistory record : getHistoryFromZapDB()) {
			if (record.getHttpMessage().getRequestHeader().getURI().toString()
					.equals(url)) {

				msg = record.getHttpMessage().cloneRequest();
				if (record.getHttpMessage().getRequestHeader().getMethod()
						.toString().equals("POST")) // Prefer POST over GET
				{
					break;
				}
			}
		}
		return msg;
	}
	
	public static TableHistory getZapHistoryTable()
	{
		return Model.getSingleton().getDb().getTableHistory();
	}

	 public static ArrayList<DivinerRecordResult> getResultsPerPageAndParam (String page, long paramId) throws SQLException
	 {
		 return resultTable.getResultsPerPageAndParam(page, paramId);
	 }
	 
	 public static ArrayList<DivinerRecordResult> getUniqueResultsPerPageAndParam (String page, long paramId) throws SQLException
	 {
		 return resultTable.getResultsPerPageAndParamUniqueSource(page, paramId);
	 }
	 
	 public static ArrayList<DivinerRecordResult> getLeads () throws SQLException
	 {
		 return resultTable.getLeads();
	 }
	 
	 public static ArrayList<DivinerRecordResult> getAllDistinctResults () throws SQLException
	 {
		 return resultTable.getAllDistinctResults();
	 }
}
