package com.hacktics.diviner.analyze;
/**
 *  @author Eran Tamari
 */
import java.sql.SQLException;
import java.util.ArrayList;
import com.hacktics.diviner.database.CodeLine;
import com.hacktics.diviner.database.DivinerTableFlow;
import com.hacktics.diviner.zapDB.ZapHistoryDB;


public enum CodeBehaviour {

	//SERVER variable
	//input
	INPUT_SERVER_VARIABLE (Clairvoyance.JAVA_INPUT_SERVER_VARIABLE, Clairvoyance.cSharp_INPUT_SERVER_VARIABLE, Clairvoyance.ORDER_INPUT_SERVER_VARIABLE, Clairvoyance.PROBABILITY_INPUT_SERVER_VARIABLE, Clairvoyance.CATEGORY_INPUT_SERVER_VARIABLE),
	//output
	OUTPUT_SERVER_VARIABLE(Clairvoyance.JAVA_OUTPUT_SERVER_VARIABLE, Clairvoyance.cSharp_OUTPUT_SERVER_VARIABLE, Clairvoyance.ORDER_OUTPUT_SERVER_VARIABLE, Clairvoyance.PROBABILITY_OUTPUT_SERVER_VARIABLE, Clairvoyance.CATEGORY_OUTPUT_SERVER_VARIABLE),

	//SESSION variable
	//input
	UPDATE_SESSION_ATTRIBUTE(Clairvoyance.JAVA_UPDATE_SESSION_ATTRIBUTE, Clairvoyance.cSharp_UPDATE_SESSION_ATTRIBUTE, Clairvoyance.ORDER_UPDATE_SESSION_ATTRIBUTE, Clairvoyance.PROBABILITY_UPDATE_SESSION_ATTRIBUTE, Clairvoyance.CATEGORY_UPDATE_SESSION_ATTRIBUTE),
	//output
	PULL_REFLECTED_SESSION_ATTRIBUTE(Clairvoyance.JAVA_PULL_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.cSharp_PULL_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.ORDER_PULL_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.PROBABILITY_PULL_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.CATEGORY_PULL_REFLECTED_SESSION_ATTRIBUTE),
	OUTPUT_REFLECTED_SESSION_ATTRIBUTE(Clairvoyance.JAVA_OUTPUT_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.cSharp_OUTPUT_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.ORDER_OUTPUT_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.PROBABILITY_OUTPUT_REFLECTED_SESSION_ATTRIBUTE, Clairvoyance.CATEGORY_OUTPUT_REFLECTED_SESSION_ATTRIBUTE),

	//DATABASE variable
	//input
	CONNECTION_MANAGER(Clairvoyance.JAVA_CONNECTION_MANAGER , Clairvoyance.cSharp_CONNECTION_MANAGER, Clairvoyance.ORDER_CONNECTION_MANAGER, Clairvoyance.PROBABILITY_CONNECTION_MANAGER, Clairvoyance.CATEGORY_CONNECTION_MANAGER),
	INSERT_DATABASE_FIELD(Clairvoyance.JAVA_INSERT_DATABASE_FIELD, Clairvoyance.cSharp_INSERT_DATABASE_FIELD, Clairvoyance.ORDER_INSERT_DATABASE_FIELD, Clairvoyance.PROBABILITY_INSERT_DATABASE_FIELD, Clairvoyance.CATEGORY_INSERT_DATABASE_FIELD),
	SET_DATABASE_QUERY(Clairvoyance.JAVA_SET_DATABASE_QUERY, Clairvoyance.cSharp_SET_DATABASE_QUERY, Clairvoyance.ORDER_SET_DATABASE_QUERY, Clairvoyance.PROBABILITY_SET_DATABASE_QUERY, Clairvoyance.CATEGORY_SET_DATABASE_QUERY),
	EXECTURE_DATABASE_UPDATE_QUERY(Clairvoyance.JAVA_EXECTURE_DATABASE_UPDATE_QUERY, Clairvoyance.cSharp_EXECTURE_DATABASE_UPDATE_QUERY, Clairvoyance.ORDER_EXECTURE_DATABASE_UPDATE_QUERY, Clairvoyance.PROBABILITY_EXECTURE_DATABASE_UPDATE_QUERY, Clairvoyance.CATEGORY_EXECTURE_DATABASE_UPDATE_QUERY),
	//output
	SELECT_DATABASE_QUERY(Clairvoyance.JAVA_SELECT_DATABASE_QUERY, Clairvoyance.cSharp_SELECT_DATABASE_QUERY, Clairvoyance.ORDER_SELECT_DATABASE_QUERY, Clairvoyance.PROBABILITY_SELECT_DATABASE_QUERY, Clairvoyance.CATEGORY_SELECT_DATABASE_QUERY), 
	EXECTURE_DATABASE_SELECT_QUERY(Clairvoyance.JAVA_EXECTURE_DATABASE_SELECT_QUERY, Clairvoyance.cSharp_EXECTURE_DATABASE_SELECT_QUERY, Clairvoyance.ORDER_EXECTURE_DATABASE_SELECT_QUERY, Clairvoyance.PROBABILITY_EXECTURE_DATABASE_SELECT_QUERY, Clairvoyance.CATEGORY_EXECTURE_DATABASE_SELECT_QUERY),
	OUTPUT_REFLECTED_DATABASE_FIELD(Clairvoyance.JAVA_OUTPUT_REFLECTED_DATABASE_FIELD, Clairvoyance.cSharp_OUTPUT_REFLECTED_DATABASE_FIELD, Clairvoyance.ORDER_OUTPUT_REFLECTED_DATABASE_FIELD, Clairvoyance.PROBABILITY_OUTPUT_REFLECTED_DATABASE_FIELD, Clairvoyance.CATEGORY_OUTPUT_REFLECTED_DATABASE_FIELD),

	//OTHERS
	COOKIE_UPDATED(Clairvoyance.JAVA_COOKIE_UPDATED, Clairvoyance.cSharp_COOKIE_UPDATED, Clairvoyance.ORDER_COOKIE_UPDATED, Clairvoyance.PROBABILITY_COOKIE_UPDATED, Clairvoyance.CATEGORY_COOKIE_UPDATED),
	REPLAYABLE_PARAMETER_UPDATED(Clairvoyance.JAVA_REPLAYABLE_PARAMETER_UPDATED , Clairvoyance.cSharp_REPLAYABLE_PARAMETER_UPDATED, Clairvoyance.ORDER_REPLAYABLE_PARAMETER_UPDATED, Clairvoyance.PROBABILITY_REPLAYABLE_PARAMETER_UPDATED, Clairvoyance.CATEGORY_REPLAYABLE_PARAMETER_UPDATED);

	private static int codeId = 0;
	private String javaCode = "";
	private String cSharpCode = "";
	private int lineOrder = 0;
	private int defaultProbability = 0;
	private int categoryId = 0;
	
	private CodeBehaviour(String javaCode, String cSharpCode, int lineOrder, int defaultProbability, int categoryId) {
		this.javaCode = javaCode;
		this.lineOrder = lineOrder;
		this.categoryId = categoryId;
		this.cSharpCode = cSharpCode;
		this.defaultProbability = defaultProbability;
	}
	public String getJavaSourceCode() {
		return javaCode;
	}

	public int getBehaviourId(){
		return this.ordinal();
	}

	public static int getCodeId() {
		return codeId;
	}

	public int getOrder() {
		return lineOrder;
	}
	
	
	public String getcSharpCode() {
		return cSharpCode;
	}
	public int getDefaultProbability() {
		return defaultProbability;
	}
	public int getCategoryId() {
		return categoryId;
	}
	//Should be called after the code id was used in order to avoid duplicate ID's
	public static void incrementCodeId(){ 
		codeId++;		
	}

	public static void serverReflection(String page, String paramName, int codeId, FlowDocumentation flow, int lineOrder, int sourceRequestID, long resultId) {
		try{
			
			ZapHistoryDB.flowTable.insert(CodeBehaviour.INPUT_SERVER_VARIABLE.ordinal(), page, paramName, CodeBehaviour.getCodeId(), null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_INPUT_SERVER_VARIABLE);
			ZapHistoryDB.flowTable.insert(CodeBehaviour.OUTPUT_SERVER_VARIABLE.ordinal(), page, paramName, CodeBehaviour.getCodeId(), null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_OUTPUT_SERVER_VARIABLE);
		
			CodeBehaviour.incrementCodeId();

		}
		catch(SQLException e) { e.printStackTrace(); }

	}

	public static void sessionReflection(String sourcePage, String targetPage, String paramName, int codeId, FlowDocumentation flow, int lineOrder, int sourceRequestID, long resultId) {
		try {
			int newCodeId = ZapHistoryDB.flowTable.getByParam(sourcePage, paramName, codeId);
			if (newCodeId == codeId) {
				ZapHistoryDB.flowTable.insert(CodeBehaviour.INPUT_SERVER_VARIABLE.ordinal(), sourcePage, paramName, newCodeId, flow, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_INPUT_SESSION_ATTRIBUTE);
			}
			//Input page code
			ZapHistoryDB.flowTable.insert(CodeBehaviour.UPDATE_SESSION_ATTRIBUTE.ordinal(), sourcePage, paramName, newCodeId, flow, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_UPDATE_SESSION_ATTRIBUTE);
		
			//Output page code
			ZapHistoryDB.flowTable.insert(CodeBehaviour.PULL_REFLECTED_SESSION_ATTRIBUTE.ordinal(), targetPage, paramName, newCodeId, flow, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_PULL_REFLECTED_SESSION_ATTRIBUTE);
			ZapHistoryDB.flowTable.insert(CodeBehaviour.OUTPUT_REFLECTED_SESSION_ATTRIBUTE.ordinal(), targetPage, paramName, newCodeId, flow, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_OUTPUT_REFLECTED_SESSION_ATTRIBUTE);
		
			CodeBehaviour.incrementCodeId();
		}
		catch(SQLException e) { e.printStackTrace(); }

	}

	public static void databaseReflection(String sourcePage, String targetPage, String paramName, int codeId, FlowDocumentation flow, int lineOrder, int sourceRequestID, long resultId) {
		try {
			
			//Follow the input id (input#) from previous
			int newCodeId = ZapHistoryDB.flowTable.getByParam(sourcePage, paramName, codeId);
			if (newCodeId == codeId) {
				ZapHistoryDB.flowTable.insert(CodeBehaviour.INPUT_SERVER_VARIABLE.ordinal(), sourcePage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_INPUT_SERVER_VARIABLE);
			}
			
			//Don't insert connection manager more than once
			if (! ZapHistoryDB.flowTable.findBehabiour(sourcePage, CodeBehaviour.CONNECTION_MANAGER.ordinal())) {
				ZapHistoryDB.flowTable.insert(CodeBehaviour.CONNECTION_MANAGER.ordinal(),sourcePage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_CONNECTION_MANAGER);
			}
			
			ZapHistoryDB.flowTable.insert(CodeBehaviour.INSERT_DATABASE_FIELD.ordinal(), sourcePage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_INSERT_DATABASE_FIELD);
			ZapHistoryDB.flowTable.insert(CodeBehaviour.SET_DATABASE_QUERY.ordinal(),sourcePage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_SET_DATABASE_QUERY);
			ZapHistoryDB.flowTable.insert(CodeBehaviour.EXECTURE_DATABASE_UPDATE_QUERY.ordinal(), sourcePage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_EXECTURE_DATABASE_UPDATE_QUERY);
			
			ZapHistoryDB.flowTable.insert(CodeBehaviour.SELECT_DATABASE_QUERY.ordinal(), targetPage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_SELECT_DATABASE_QUERY);
			ZapHistoryDB.flowTable.insert(CodeBehaviour.EXECTURE_DATABASE_SELECT_QUERY.ordinal(),targetPage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_EXECTURE_DATABASE_SELECT_QUERY);
			ZapHistoryDB.flowTable.insert(CodeBehaviour.OUTPUT_REFLECTED_DATABASE_FIELD.ordinal(), targetPage, paramName, newCodeId, null, DivinerTableFlow.DEFAULT_ORDER, sourceRequestID, resultId, Clairvoyance.PROBABILITY_OUTPUT_REFLECTED_DATABASE_FIELD);
			
			CodeBehaviour.incrementCodeId();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}

	public static ArrayList<CodeLine> getSourceCode(String pageName) {
		ArrayList<CodeLine> AllCodeLines = null;
		ArrayList<CodeLine> result = new ArrayList<CodeLine>();
		
		//Get code lines while removing lines with same category with lower probability
		try {
		AllCodeLines = ZapHistoryDB.flowTable.readSourceCode(pageName);
		
		for (CodeLine line : AllCodeLines) {
			int currentCategoty = CodeBehaviour.values()[line.getBehaviour()].getCategoryId();
			int currentProbability = line.getProbability();
			boolean duplicateCategoty = false; 
			
			for (CodeLine comparedLine : AllCodeLines) {
				if (currentCategoty == CodeBehaviour.values()[comparedLine.getBehaviour()].getCategoryId()) {
					if (currentProbability < comparedLine.getProbability()) {
						duplicateCategoty = true; //Found a line of the same category with higher probability
					}
					else {
						duplicateCategoty = false;
					}
					
				}
			}
			
			if (! duplicateCategoty) {
				result.add(line);
			}
			
		}
		}
		catch (SQLException e) { e.printStackTrace(); }
		
		return result;
	}


}
