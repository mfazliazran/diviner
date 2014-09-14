package com.hacktics.diviner.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.parosproxy.paros.db.AbstractTable;
import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author Eran Tamari
 *
 */

public class DivinerTableHistory extends AbstractTable {

	private static final String	ZAPHISTORYID	= "HISTORYID";
	private static final String SESSIONID	= "SESSIONID";
	private static final String HISTTYPE	= "HISTTYPE";
	private static final String URI			= "URI";
	private static final String TIMESENTMILLIS = "TIMESENTMILLIS";
	private static final String TIMEELAPSEDMILLIS = "TIMEELAPSEDMILLIS";
	private static final String REQHEADER	= "REQHEADER";
	private static final String REQBODY		= "REQBODY";
	private static final String RESHEADER	= "RESHEADER";
	private static final String RESBODY		= "RESBODY";
	private static final String TAG         = "TAG";
	// Alex: Added NOTE field to history table
	private static final String NOTE        = "NOTE";
	// Alex: Added RESPONSEFROMTARGETHOST field to history table
	private static final String RESPONSEFROMTARGETHOST = "RESPONSEFROMTARGETHOST";

	private static final String	HISTORYID	= "HISTID";
	private static final String INSCOPE	= "INSCOPE";
	private static long zapHistId = 0;
	private PreparedStatement psRead = null;
	private PreparedStatement psReadUrls = null;
	private PreparedStatement psInsert = null;
	private PreparedStatement psScopeReset = null;
	private PreparedStatement psSetScope = null;
	private PreparedStatement psSetSelectedHost = null;


	/**
	 * A table that maintains references to ZAP history ID in order to include/exclude URLs in scope
	 */
	@Override
	protected void reconnect(Connection conn) throws SQLException {
		ResultSet rs = conn.getMetaData().getTables(null, null, "DIVINERHISTORY", null);
		if ( ! rs.next()) {
			// Need to create the table

			PreparedStatement stmt = conn.prepareStatement(
					"CREATE cached TABLE DIVINERHISTORY(histid bigint , inscope boolean default false not null )");
			stmt.execute();
		}
		else {
			//table exists, clear the table:
			PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE DIVINERHISTORY");
			stmt.execute();
		}
		rs.close();

		psRead	= conn.prepareStatement("SELECT * FROM DIVINERHISTORY JOIN HISTORY ON " + HISTORYID + "=" + ZAPHISTORYID + " WHERE " + INSCOPE + " = true AND " + ZAPHISTORYID + "= ?");
		psScopeReset = conn.prepareStatement("UPDATE DIVINERHISTORY SET " + INSCOPE + "= false");
		psSetSelectedHost = conn.prepareStatement("UPDATE DIVINERHISTORY SET " + INSCOPE + "= true WHERE " + HISTORYID + " IN (SELECT "  + ZAPHISTORYID + " FROM HISTORY WHERE "+ URI +" LIKE ? OR " + URI + " LIKE ? OR " + URI + " LIKE ? OR " + URI + " LIKE ?)");
		psSetScope = conn.prepareStatement("UPDATE DIVINERHISTORY SET " + INSCOPE + "= true WHERE " + HISTORYID + " IN (SELECT "  + ZAPHISTORYID + " FROM HISTORY WHERE "+ URI +" LIKE ?" + ")");
		//Copy history from ZAP
		psInsert = conn.prepareStatement("INSERT INTO DIVINERHISTORY (" + HISTORYID+ ") SELECT " + ZAPHISTORYID + " FROM HISTORY WHERE " + ZAPHISTORYID + ">" +  zapHistId );
		psReadUrls = conn.prepareStatement("SELECT DISTINCT " + URI + " FROM DIVINERHISTORY JOIN HISTORY ON " + HISTORYID + "=" + ZAPHISTORYID + " WHERE " + INSCOPE + " = true ORDER BY " + URI);
	}

	//Copy zap history ID
	private void setZapHistoryId() throws HttpMalformedHeaderException, SQLException 
	{

		long zapIndex = Model.getSingleton().getDb().getTableHistory().lastIndex();

		if ( zapIndex > zapHistId) //Zap has new requests in its DB
		{
			psInsert.executeUpdate();
			zapHistId =  zapIndex;
		}
	}

	private RecordHistory read(long historyId) throws HttpMalformedHeaderException, SQLException{

		psRead.setLong(1, historyId);
		ResultSet rs = null;
		rs = psRead.executeQuery();
		return build(rs);

	}


	public ArrayList<String> getScopeUrlsAsString() throws HttpMalformedHeaderException, SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = psReadUrls.executeQuery();
		while (rs.next())
		{
			result.add(rs.getString(1));
		}
		return result;
	}

	/**
	 * Puts all history out of scope by setting scope=false for each request in the DB
	 * @throws HttpMalformedHeaderException
	 * @throws SQLException
	 */
	public void resetHistory() throws HttpMalformedHeaderException, SQLException{
		setZapHistoryId();		//Set the the last history index to match ZAP 
		psScopeReset.execute();	//Set all request to be "out of scope"
	}

	public ArrayList<RecordHistory> getScopeRecords() throws HttpMalformedHeaderException, SQLException{

		ArrayList<RecordHistory> result = new ArrayList<RecordHistory>();

		long index = 1;
		while (index <= zapHistId)
		{
			result.add(read(index));
			index++;
		}
		result.removeAll(Collections.singleton(null));	//Remove null elements (those that are not in scope are null)
		return result;

	}

	//Get the history without the temporary requests
	public ArrayList<RecordHistory> getCleanScopeRecords() throws HttpMalformedHeaderException, SQLException{

		ArrayList<RecordHistory> result = new ArrayList<RecordHistory>();

		long index = 1;
		while (index <= zapHistId)
		{
			if (read(index) != null) {
				//Make sure not temporary type and add to the list in scope
				if (read(index).getHistoryType() != HistoryReference.TYPE_TEMPORARY) {
					result.add(read(index));
					
				}
			}
			index++;
		}
		result.removeAll(Collections.singleton(null));	//Remove null elements (those that are not in scope are null)
		return result;

	}

	public void setHost(String hostname)throws HttpMalformedHeaderException, SQLException{
		psSetSelectedHost.setString(1, "http://" + hostname + "/%");
		psSetSelectedHost.setString(2, "https://" + hostname + "/%");
		psSetSelectedHost.setString(3, "http://" + hostname + ":%");	//Match domains with port
		psSetSelectedHost.setString(4, "https://" + hostname + ":%");
		psSetSelectedHost.execute();
	}

	public void setScope(String uri)  throws HttpMalformedHeaderException, SQLException{

		psSetScope.setString(1, uri);
		psSetScope.execute();
	}

	private RecordHistory build(ResultSet rs) throws HttpMalformedHeaderException, SQLException {
		RecordHistory history = null;
		try {
			if (rs.next()) {
				history = new RecordHistory(
						rs.getInt(ZAPHISTORYID),
						rs.getInt(HISTTYPE),
						rs.getLong(SESSIONID),
						rs.getLong(TIMESENTMILLIS),
						rs.getInt(TIMEELAPSEDMILLIS),
						rs.getString(REQHEADER),
						rs.getBytes(REQBODY),
						rs.getString(RESHEADER),
						rs.getBytes(RESBODY),
						rs.getString(TAG),
						rs.getString(NOTE),			// ZAP: Added note
						rs.getBoolean(RESPONSEFROMTARGETHOST)
						);
			}
		} finally {
			rs.close();
		}
		return history;

	}


}
//}
