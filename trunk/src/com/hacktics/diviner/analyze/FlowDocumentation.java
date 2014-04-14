package com.hacktics.diviner.analyze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import org.parosproxy.paros.network.HtmlParameter;

import com.hacktics.diviner.gui.OUTPUT_TYPE;

/**
 * 
 * @author Eran Tamari
 *
 */

public class FlowDocumentation  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2012270315161L;
	private HISTORY_MODE history;
	private SCENARIO_MODE scenario;
	private String initialToken;
	private String token;
	private int sourceId;
	private int targetId;
	private boolean loginFirstSourceAccessSessionChange = false;
	private boolean loginFirstLoginAccessSessionChange = false;
	private boolean reLoginFlag = false;
	private boolean ignoreLoginFlag = false;
	private boolean loginFirst2ndSourceAccessSessionChange = false;
	private boolean loginFirst2ndLoginAccessSessionChange = false;
	private boolean loginAfterSourceAccessSessionChange = false;
	private boolean loginAfterLoginAccessSessionChange = false;
	private boolean directSourceAccessSessionChange = false;
	private boolean isFoundInOutput;
	private OUTPUT_TYPE outputType;
	
	

	public FlowDocumentation(HISTORY_MODE history, SCENARIO_MODE scenario,
			String initialToken, String token, int sourceId, int targetId,
			boolean loginFirstSourceAccessSessionChange,
			boolean loginFirstLoginAccessSessionChange, boolean reLoginFlag,
			boolean ignoreLoginFlag,
			boolean loginFirst2ndSourceAccessSessionChange,
			boolean loginFirst2ndLoginAccessSessionChange,
			boolean loginAfterSourceAccessSessionChange,
			boolean loginAfterLoginAccessSessionChange,
			boolean directSourceAccessSessionChange, boolean isFoundInOutput,
			ArrayList<ResponseDocumentation> responseQueue) {

		this.history = history;
		this.scenario = scenario;
		this.initialToken = initialToken;
		this.token = token;
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.loginFirstSourceAccessSessionChange = loginFirstSourceAccessSessionChange;
		this.loginFirstLoginAccessSessionChange = loginFirstLoginAccessSessionChange;
		this.reLoginFlag = reLoginFlag;
		this.ignoreLoginFlag = ignoreLoginFlag;
		this.loginFirst2ndSourceAccessSessionChange = loginFirst2ndSourceAccessSessionChange;
		this.loginFirst2ndLoginAccessSessionChange = loginFirst2ndLoginAccessSessionChange;
		this.loginAfterSourceAccessSessionChange = loginAfterSourceAccessSessionChange;
		this.loginAfterLoginAccessSessionChange = loginAfterLoginAccessSessionChange;
		this.directSourceAccessSessionChange = directSourceAccessSessionChange;
		this.isFoundInOutput = isFoundInOutput;
		this.responseQueue = responseQueue;
	}
	
	public FlowDocumentation() {
		
	}
	public HISTORY_MODE getHistory() {
		return history;
	}
	public void setHistory(HISTORY_MODE history) {
		this.history = history;
	}

	public SCENARIO_MODE getScenario() {
		return scenario;
	}

	public void setScenario(SCENARIO_MODE scenario) {
		this.scenario = scenario;
	}

	public String getInitialToken() {
		return initialToken;
	}

	public void setInitialToken(String initialToken) {
		this.initialToken = initialToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public boolean isLoginFirstSourceAccessSessionChange() {
		return loginFirstSourceAccessSessionChange;
	}

	public void setLoginFirstSourceAccessSessionChange(
			boolean loginFirstSourceAccessSessionChange) {
		this.loginFirstSourceAccessSessionChange = loginFirstSourceAccessSessionChange;
	}

	public boolean isLoginFirstLoginAccessSessionChange() {
		return loginFirstLoginAccessSessionChange;
	}

	public void setLoginFirstLoginAccessSessionChange(
			boolean loginFirstLoginAccessSessionChange) {
		this.loginFirstLoginAccessSessionChange = loginFirstLoginAccessSessionChange;
	}

	public boolean isReLoginFlag() {
		return reLoginFlag;
	}

	public void setReLoginFlag(boolean reLoginFlag) {
		this.reLoginFlag = reLoginFlag;
	}

	public boolean isIgnoreLoginFlag() {
		return ignoreLoginFlag;
	}

	public void setIgnoreLoginFlag(boolean ignoreLoginFlag) {
		this.ignoreLoginFlag = ignoreLoginFlag;
	}

	public boolean isLoginFirst2ndSourceAccessSessionChange() {
		return loginFirst2ndSourceAccessSessionChange;
	}

	public void setLoginFirst2ndSourceAccessSessionChange(
			boolean loginFirst2ndSourceAccessSessionChange) {
		this.loginFirst2ndSourceAccessSessionChange = loginFirst2ndSourceAccessSessionChange;
	}

	public boolean isLoginFirst2ndLoginAccessSessionChange() {
		return loginFirst2ndLoginAccessSessionChange;
	}

	public void setLoginFirst2ndLoginAccessSessionChange(
			boolean loginFirst2ndLoginAccessSessionChange) {
		this.loginFirst2ndLoginAccessSessionChange = loginFirst2ndLoginAccessSessionChange;
	}

	public boolean isLoginAfterSourceAccessSessionChange() {
		return loginAfterSourceAccessSessionChange;
	}

	public void setLoginAfterSourceAccessSessionChange(
			boolean loginAfterSourceAccessSessionChange) {
		this.loginAfterSourceAccessSessionChange = loginAfterSourceAccessSessionChange;
	}

	public boolean isLoginAfterLoginAccessSessionChange() {
		return loginAfterLoginAccessSessionChange;
	}

	public void setLoginAfterLoginAccessSessionChange(
			boolean loginAfterLoginAccessSessionChange) {
		this.loginAfterLoginAccessSessionChange = loginAfterLoginAccessSessionChange;
	}

	public boolean isDirectSourceAccessSessionChange() {
		return directSourceAccessSessionChange;
	}

	public void setDirectSourceAccessSessionChange(
			boolean directSourceAccessSessionChange) {
		this.directSourceAccessSessionChange = directSourceAccessSessionChange;
	}

	public boolean isFoundInOutput() {
		return isFoundInOutput;
	}

	public void setFoundInOutput(boolean isFoundInOutput) {
		this.isFoundInOutput = isFoundInOutput;
	}

	public ArrayList<ResponseDocumentation> getResponseQueue() {
		return responseQueue;
	}

	public void setResponseQueue(ArrayList<ResponseDocumentation> responseQueue) {
		this.responseQueue = responseQueue;
	}

	
	private ArrayList<ResponseDocumentation> responseQueue = new ArrayList<ResponseDocumentation>();

	public void addResponseDocumentation(String token, TreeSet<HtmlParameter> initialSession, TreeSet<HtmlParameter> newSession, String responseCode, String type) {
		responseQueue.add(new ResponseDocumentation(token, initialSession, newSession, responseCode, type));
	}

//	public void addResponseDocumentation(ResponseDocumentation doc) {
//		responseQueue.add (new ResponseDocumentation(doc.getToken(),doc.getInitialSession(),doc.getNewSession(), doc.getResponseCode(), doc.getType()));
//	}

	public int getResponseListSize() {
		return responseQueue.size();
	}

	public ResponseDocumentation getResponseDocumentation(int location) {
		return responseQueue.get(location);
	}

	public FlowDocumentation clone() {		
		return new FlowDocumentation(history, scenario, initialToken, token, sourceId, targetId, loginFirstSourceAccessSessionChange,
									loginFirstLoginAccessSessionChange, reLoginFlag, ignoreLoginFlag, loginFirst2ndSourceAccessSessionChange, 
									loginFirst2ndLoginAccessSessionChange, loginAfterSourceAccessSessionChange, loginAfterLoginAccessSessionChange,
									directSourceAccessSessionChange, isFoundInOutput, responseQueue);
	}

	public OUTPUT_TYPE getOutputType() {
		return outputType;
	}

	public void setOutputType(OUTPUT_TYPE outputType) {
		this.outputType = outputType;
	}
	
	public class ResponseDocumentation implements Serializable{
		
		private static final long serialVersionUID = 1L;
		private String token;
//		private TreeSet<HtmlParameter> initialSession;
//		private TreeSet<HtmlParameter> newSession;
		private String responseCode;
		private String type;

		public ResponseDocumentation(String newToken, TreeSet<HtmlParameter> newInitialSession, TreeSet<HtmlParameter> newNewSession,
																							String newResponseCode, String newType) {
			setToken(newToken);
//			setInitialSession(newInitialSession);
//			setNewSession(newNewSession);
			setResponseCode(newResponseCode);
			setType(newType);
		}

		public String getToken() {
			return token;
		}
		public void setToken(String newToken) {
			token = newToken;
		}

//		public TreeSet<HtmlParameter> getInitialSession() {
//			return initialSession;
//		}
//		public void setInitialSession(TreeSet<HtmlParameter> session) {
//			initialSession = session;
//		}
//
//		public TreeSet<HtmlParameter> getNewSession() {
//			return newSession;
//		}
//		public void setNewSession(TreeSet<HtmlParameter> session) {
//			newSession = session;
//		}

		public String getResponseCode() {
			return responseCode;
		}
		public void setResponseCode(String code) {
			responseCode = code;
		}	

		public String getType() {
			return type;
		}
		public void setType(String newType) {
			type = newType;
		}
		
		
	} //end of inner class

} //end of class
