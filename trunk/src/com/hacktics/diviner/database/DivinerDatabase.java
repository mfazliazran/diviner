package com.hacktics.diviner.database;

import java.sql.SQLException;

import org.parosproxy.paros.model.Model;

/**
 * 
 * @author Eran Tamari
 *
 */

public class DivinerDatabase {

	private  DivinerTableHistory tableDivinerHistory = null;
	private  DivinerTableParam tableDivinerParam = null;
	private  DivinerTableResults tableDivinerResult = null;
	private DivinerTableFlow tableDivinerFlow = null;
	
	public DivinerDatabase()
	{
		tableDivinerHistory = new DivinerTableHistory();
	    tableDivinerParam = new DivinerTableParam();
	    tableDivinerResult = new DivinerTableResults();
	    tableDivinerFlow = new DivinerTableFlow();
	    
	    Model.getSingleton().getDb().addDatabaseListener(tableDivinerHistory);
	    Model.getSingleton().getDb().addDatabaseListener(tableDivinerParam);
	    Model.getSingleton().getDb().addDatabaseListener(tableDivinerResult);
	    Model.getSingleton().getDb().addDatabaseListener(tableDivinerFlow);
	    
	    try{
		    tableDivinerHistory.databaseOpen(Model.getSingleton().getDb().getDatabaseServer());
		    tableDivinerParam.databaseOpen(Model.getSingleton().getDb().getDatabaseServer());
		    tableDivinerResult.databaseOpen(Model.getSingleton().getDb().getDatabaseServer());
		    tableDivinerFlow.databaseOpen(Model.getSingleton().getDb().getDatabaseServer());
	    }
	    catch(SQLException e){e.printStackTrace();}
	}
	
	public DivinerTableResults getDivinerTableResult() {
		return tableDivinerResult;		
	}
	
	public DivinerTableHistory getDivinerTableHistory() {
		return tableDivinerHistory;		
	}
	
	public DivinerTableParam getDivinerTableParam() {
		return tableDivinerParam;		
	}
	
	public DivinerTableFlow getDivinerTableFlow() {
		return tableDivinerFlow;
	}
	
}
