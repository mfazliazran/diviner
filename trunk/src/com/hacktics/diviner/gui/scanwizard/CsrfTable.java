package com.hacktics.diviner.gui.scanwizard;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTable;

import com.hacktics.diviner.csrf.CsrfToken;
import com.hacktics.diviner.csrf.CsrfToken.CSRF_TOKEN_TYPE;

/**
 * This class creates and stores a default list of parameters that should be resent to the server
 * when they are updated in the response - either in each page, or in each session.
 *
 * @authors Eran Tamari, Shay Chen
 * @since 1.0
 *
 */

public class CsrfTable extends JTable {

	private static final long serialVersionUID = -5715036679415272790L;
	private static final String [] COLOUMNS = {"Token name", "Per page", "Active"  };
	private static ArrayList<Object[]> data;
	private static WizardTable model;
	
	public CsrfTable() {
		data = new ArrayList<Object[]>();
		model = new WizardTable(data, COLOUMNS, true);
		setModel(model);
		
		setGridColor(Color.GRAY);
		setShowGrid(true);
		getColumnModel().getColumn(0).setPreferredWidth(100);
		getColumnModel().getColumn(1).setPreferredWidth(30);
		getColumnModel().getColumn(2).setPreferredWidth(30);
		this.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 13));
		
		//Adding default CSRF tokens 
		
		//.net parameters that are required (and thus, diviner needs to re-send their updated values)
		Object[] defaultRow1 = {"__VIEWSTATE",new Boolean(true), new Boolean(false)};
		Object[] defaultRow2 = {"__VIEWSTATEENCRYPTED",new Boolean(true), new Boolean(false)};
		Object[] defaultRow3 = {"__EVENTTARGET",new Boolean(true), new Boolean(false)};
		Object[] defaultRow4 = {"__EVENTARGUMENT",new Boolean(true), new Boolean(false)};
		Object[] defaultRow5 = {"__EVENTVALIDATION",new Boolean(true), new Boolean(false)};
		Object[] defaultRow6 = {"__LASTFOCUS",new Boolean(true), new Boolean(false)};	
		Object[] defaultRow7 = {"__ASYNCPOST",new Boolean(true), new Boolean(false)};
		Object[] defaultRow8 = {"__VSTATE",new Boolean(true), new Boolean(false)};
		Object[] defaultRow9 = {"__PREVIOUSPAGE",new Boolean(true), new Boolean(false)};
		Object[] defaultRow10 = {"__REQUESTDIGEST",new Boolean(true), new Boolean(false)};
		Object[] defaultRow11 = {"__RequestVerificationToken", new Boolean(true), new Boolean(false)}; //ASP.Net MVC AntiCSRF
		
		//java based frameworks and coldfusion 
		Object[] defaultRow12 = {"org.apache.struts.taglib.html.TOKEN", new Boolean(true), new Boolean(false)};
		Object[] defaultRow13 = {"_javax.faces.ViewState", new Boolean(true), new Boolean(false)};
		Object[] defaultRow14 = {"CFTOKEN", new Boolean(true), new Boolean(false)};
		
		//general
		Object[] defaultRow15 = {"anticsrf", new Boolean(false), new Boolean(false)};
		Object[] defaultRow16 = {"CSRFToken",new Boolean(false), new Boolean(false)};
		//rare
		Object[] defaultRow17 = {"ViewStateUserKey", new Boolean(false), new Boolean(false)};
		
		//Session Identifiers - should only be checked if there is no cookie
		Object[] defaultRow18 = {"jsessionid", new Boolean(false), new Boolean(false)}; //jsp/java session id
		Object[] defaultRow19 = {"sessionid", new Boolean(false), new Boolean(false)};
		Object[] defaultRow20 = {"PHPSESSID", new Boolean(false), new Boolean(false)}; //php session id
		Object[] defaultRow21 = {"PHPSESSIONID", new Boolean(false), new Boolean(false)}; //php session id
		Object[] defaultRow22 = {"ASP.NET_SessionId", new Boolean(false), new Boolean(false)}; //asp.net session id
		Object[] defaultRow23 = {"ASPSESSIONID", new Boolean(false), new Boolean(false)}; //ASP 3.0 session id
		Object[] defaultRow24 = {"cfid", new Boolean(false), new Boolean(false)}; //coldfusion session id
		Object[] defaultRow25 = {"FORMCRED", new Boolean(true), new Boolean(false)};
		Object[] defaultRow26 = {"SMSESSION", new Boolean(false), new Boolean(false)};
		Object[] defaultRow27 = {"BV_EngineID", new Boolean(false), new Boolean(false)};
		Object[] defaultRow28 = {"BV_SESSIONID", new Boolean(false), new Boolean(false)};
		
		model.addNewRow(defaultRow1);
		model.addNewRow(defaultRow2);
		model.addNewRow(defaultRow3);
		model.addNewRow(defaultRow4);
		model.addNewRow(defaultRow5);
		model.addNewRow(defaultRow6);
		model.addNewRow(defaultRow7);
		model.addNewRow(defaultRow8);
		model.addNewRow(defaultRow9);
		model.addNewRow(defaultRow10);
		model.addNewRow(defaultRow11);
		model.addNewRow(defaultRow12);
		model.addNewRow(defaultRow13);
		model.addNewRow(defaultRow14);
		model.addNewRow(defaultRow15);
		model.addNewRow(defaultRow16);
		model.addNewRow(defaultRow17);
		model.addNewRow(defaultRow18);
		model.addNewRow(defaultRow19);
		model.addNewRow(defaultRow20);
		model.addNewRow(defaultRow21);
		model.addNewRow(defaultRow22);
		model.addNewRow(defaultRow23);
		model.addNewRow(defaultRow24);
		model.addNewRow(defaultRow25);
		model.addNewRow(defaultRow26);
		model.addNewRow(defaultRow27);
		model.addNewRow(defaultRow28);
		
	}
	
	public void addRow() {
		Object [] emptyRow = {"", new Boolean(false), new Boolean(false)};
		model.addNewRow(emptyRow);
		repaint();
	}
	
	public ArrayList<CsrfToken> getTokens() {
		ArrayList<CsrfToken> result = new ArrayList<CsrfToken>();
		for (Object[] row : model.getIsLastColActiveRows()) {
			String tokenName = (String)row[0];
			boolean isSessionToken = !((Boolean)row[1]);
			if (isSessionToken) { // Per Session Tokens
				result.add(new CsrfToken(tokenName, "", null, CSRF_TOKEN_TYPE.PER_SESSION));
			}
			else {	//Per Page Tokens
				result.add(new CsrfToken(tokenName, "", null, CSRF_TOKEN_TYPE.PER_PAGE));
			}
		}
		return result;
	}
	
	
} //end of class
