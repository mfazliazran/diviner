package com.hacktics.diviner.analyze;

/**
 * 
 * @author Eran Tamari
 *
 */

public class Clairvoyance {
	
	

//	//Behaviour of Clairvoyance
//	public static final int BEHAVIOUR_SERVER_PARAMAETER_REFLECTION = 0;
//	public static final int BEHAVIOUR_SESSION_PARAMETER_REFLECTION = 1;
//	public static final int BEHAVIOUR_DATABASE_PARAMETER_REFLECTION = 2;
//	public static final int BEHAVIOUR_COOKIE_UPDATED = 3;
//	public static final int BEHAVIOUR_REPLAYABLE_PARAMETER_UPDATED = 4;
	
	
	
	//JAVA source Code
	public static final String JAVA_INPUT_SERVER_VARIABLE = "String input" + AnalyzerUtils.ANCHOR1 + " = request.getParameter(\"" + AnalyzerUtils.ANCHOR2 + "\");";
	public static final String JAVA_OUTPUT_SERVER_VARIABLE = "out.println( input" + AnalyzerUtils.ANCHOR1 + " );";

	public static final String JAVA_UPDATE_SESSION_ATTRIBUTE = "request.getSession().setAttribute(" + AnalyzerUtils.ANCHOR3 + ", input" + AnalyzerUtils.ANCHOR1 +");";
	public static final String JAVA_PULL_REFLECTED_SESSION_ATTRIBUTE = "String output" + AnalyzerUtils.ANCHOR1 + " = request.getSession().getAttribute( " + AnalyzerUtils.ANCHOR3 + ");";
	public static final String JAVA_OUTPUT_REFLECTED_SESSION_ATTRIBUTE = "out.println(output" + AnalyzerUtils.ANCHOR1 + ");";

	public static final String JAVA_CONNECTION_MANAGER = "connection conn = DriveManager.getConnection(\"[connection-string]\");";
	public static final String JAVA_INSERT_DATABASE_FIELD = "PreparedStatement Sqlstatement" + AnalyzerUtils.ANCHOR1 + " = conn.PreparedStatement(\"UPDATE tabel" + AnalyzerUtils.ANCHOR1 + " SET target_field" + AnalyzerUtils.ANCHOR1  +" = ? WHERE [conditions]\");";
	public static final String JAVA_SET_DATABASE_QUERY = "Sqlstatement!!!1.setString(1, input" + AnalyzerUtils.ANCHOR1 + ");";
	public static final String JAVA_EXECTURE_DATABASE_UPDATE_QUERY = "Sqlstatement" + AnalyzerUtils.ANCHOR1  +".executeUpdate();";

	public static final String JAVA_SELECT_DATABASE_QUERY = "PreparedStatement stmt" + AnalyzerUtils.ANCHOR1 + " = conn.PreparedStatement(\"SELECT sourceField" + AnalyzerUtils.ANCHOR1 + ", [FieldCollection] FROM tabel" + AnalyzerUtils.ANCHOR1 + "[Table Collection] WHERE [criteria]\");";
	public static final String JAVA_EXECTURE_DATABASE_SELECT_QUERY = "ResultSet rs" + AnalyzerUtils.ANCHOR1 + " = stmt" + AnalyzerUtils.ANCHOR1  + ".executeQuery();";
	public static final String JAVA_OUTPUT_REFLECTED_DATABASE_FIELD = "if (rs" + AnalyzerUtils.ANCHOR1 + ".next()) <br/> { out.println (rs.getString(1)); }";

	public static final String JAVA_COOKIE_UPDATED = "";
	public static final String JAVA_REPLAYABLE_PARAMETER_UPDATED = "";


	//C# source code
	public static final String cSharp_INPUT_SERVER_VARIABLE = "String input" + AnalyzerUtils.ANCHOR1 + " = request.getParameter(\"" + AnalyzerUtils.ANCHOR2 + "\");";
	public static final String cSharp_OUTPUT_SERVER_VARIABLE = "out.println( input" + AnalyzerUtils.ANCHOR1 + " );";

	public static final String cSharp_UPDATE_SESSION_ATTRIBUTE = "request.getSession().setAttribute(" + AnalyzerUtils.ANCHOR3 + ", input" + AnalyzerUtils.ANCHOR1 +");";
	public static final String cSharp_PULL_REFLECTED_SESSION_ATTRIBUTE = "String output" + AnalyzerUtils.ANCHOR1 + " = request.getSession().getAttribute( " + AnalyzerUtils.ANCHOR3;
	public static final String cSharp_OUTPUT_REFLECTED_SESSION_ATTRIBUTE = "out.println(output" + AnalyzerUtils.ANCHOR1 + ");";

	public static final String cSharp_CONNECTION_MANAGER = "connection conn = DriveManager.getConnection(\"[connection-string]\");";
	public static final String cSharp_INSERT_DATABASE_FIELD = "PreparedStatement Sqlstatement" + AnalyzerUtils.ANCHOR1 + " = conn.PreparedStatement(\"UPDATE tabel" + AnalyzerUtils.ANCHOR1 + " SET target_field" + AnalyzerUtils.ANCHOR1  +" = ? WHERE [conditions]\");";
	public static final String cSharp_SET_DATABASE_QUERY = "Sqlstatement!!!1.setString(1, input" + AnalyzerUtils.ANCHOR1 + ");";
	public static final String cSharp_EXECTURE_DATABASE_UPDATE_QUERY = "Sqlstatement" + AnalyzerUtils.ANCHOR1  +".executeUpdate();";

	public static final String cSharp_SELECT_DATABASE_QUERY = "PreparedStatement stmt" + AnalyzerUtils.ANCHOR1 + " = conn.PreparedStatement(\"SELECT sourceField" + AnalyzerUtils.ANCHOR1 + ", [FieldCollection] FROM tabel" + AnalyzerUtils.ANCHOR1 + "[Table Collection] WHERE [criteria]\");";
	public static final String cSharp_EXECTURE_DATABASE_SELECT_QUERY = "ResultSet rs = stmt" + AnalyzerUtils.ANCHOR1  + ".executeQuery();";
	public static final String cSharp_OUTPUT_REFLECTED_DATABASE_FIELD = "if (rs.next()) <br/> { out.println (rs.getString(1)); }";

	public static final String cSharp_COOKIE_UPDATED = "";
	public static final String cSharp_REPLAYABLE_PARAMETER_UPDATED = "";


	//Probabilities of Clairvoyance
	public static final int PROBABILITY_INPUT_SERVER_VARIABLE = 80;
	public static final int PROBABILITY_OUTPUT_SERVER_VARIABLE = 90;

	public static final int PROBABILITY_INPUT_SESSION_ATTRIBUTE = 80;
	public static final int PROBABILITY_UPDATE_SESSION_ATTRIBUTE = 80;
	public static final int PROBABILITY_PULL_REFLECTED_SESSION_ATTRIBUTE = 90;
	public static final int PROBABILITY_OUTPUT_REFLECTED_SESSION_ATTRIBUTE = 90;

	public static final int PROBABILITY_CONNECTION_MANAGER = 70;
	public static final int PROBABILITY_INSERT_DATABASE_FIELD = 70;
	public static final int PROBABILITY_SET_DATABASE_QUERY = 70;
	public static final int PROBABILITY_EXECTURE_DATABASE_UPDATE_QUERY = 70;

	public static final int PROBABILITY_SELECT_DATABASE_QUERY = 70;
	public static final int PROBABILITY_EXECTURE_DATABASE_SELECT_QUERY = 70;
	public static final int PROBABILITY_OUTPUT_REFLECTED_DATABASE_FIELD = 70;

	public static final int PROBABILITY_COOKIE_UPDATED = -1;
	public static final int PROBABILITY_REPLAYABLE_PARAMETER_UPDATED = -1;


	//Order of appearance
	public static final int ORDER_INPUT_SERVER_VARIABLE = 1000;
	public static final int ORDER_OUTPUT_SERVER_VARIABLE = 5000;

	public static final int ORDER_INPUT_SESSION_ATTRIBUTE = 2000;
	public static final int ORDER_UPDATE_SESSION_ATTRIBUTE = 3000;
	public static final int ORDER_PULL_REFLECTED_SESSION_ATTRIBUTE = 6000;
	public static final int ORDER_OUTPUT_REFLECTED_SESSION_ATTRIBUTE = 7000;

	public static final int ORDER_CONNECTION_MANAGER = 4000;
	public static final int ORDER_INSERT_DATABASE_FIELD = 4001;
	public static final int ORDER_SET_DATABASE_QUERY = 4002;
	public static final int ORDER_EXECTURE_DATABASE_UPDATE_QUERY = 4003;

	public static final int ORDER_SELECT_DATABASE_QUERY = 8000;
	public static final int ORDER_EXECTURE_DATABASE_SELECT_QUERY = 8001;
	public static final int ORDER_OUTPUT_REFLECTED_DATABASE_FIELD = 8002;

	public static final int ORDER_COOKIE_UPDATED = 10000;
	public static final int ORDER_REPLAYABLE_PARAMETER_UPDATED = 11000;

	
	
	//Category ID of line
	
	public static final int CATEGORY_INPUT_SERVER_VARIABLE = 1;
	public static final int CATEGORY_OUTPUT_SERVER_VARIABLE = 2;

	public static final int CATEGORY_UPDATE_SESSION_ATTRIBUTE = 4;
	public static final int CATEGORY_PULL_REFLECTED_SESSION_ATTRIBUTE = 5;
	public static final int CATEGORY_OUTPUT_REFLECTED_SESSION_ATTRIBUTE = 6;

	public static final int CATEGORY_CONNECTION_MANAGER = 7;
	public static final int CATEGORY_INSERT_DATABASE_FIELD = 8;
	public static final int CATEGORY_SET_DATABASE_QUERY = 9;
	public static final int CATEGORY_EXECTURE_DATABASE_UPDATE_QUERY = 10;

	public static final int CATEGORY_SELECT_DATABASE_QUERY = 11;
	public static final int CATEGORY_EXECTURE_DATABASE_SELECT_QUERY = 12;
	public static final int CATEGORY_OUTPUT_REFLECTED_DATABASE_FIELD = 13;

	public static final int CATEGORY_COOKIE_UPDATED = 14;
	public static final int CATEGORY_SESSION_UPDATED = 14;
	
	public static final int CATEGORY_REPLAYABLE_PARAMETER_UPDATED = 15;

}
