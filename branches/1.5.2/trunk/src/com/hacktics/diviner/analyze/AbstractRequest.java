package com.hacktics.diviner.analyze;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.htmlparser.jericho.Source;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;
import com.hacktics.diviner.csrf.CsrfToken;
import com.hacktics.diviner.csrf.ResponseParser;
import com.hacktics.diviner.csrf.Url;

/**
 * 
 * @author Eran Tamari
 *
 */
public abstract class AbstractRequest {
	public static final int TYPE_DIVINER = 9;
	public static final boolean ALLOW_STATE = true;
	public static final int INITIATOR = HttpSender.MANUAL_REQUEST_INITIATOR; /*	Added initiator for HttpSender when upgrading to ZAP 1.4.1 - r29,issue 29	*/
	private static HttpSender sender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(), ALLOW_STATE,INITIATOR);
	public abstract void sendMessage();
	private static final String POST = "POST";

	public static void send(HttpMessage msg) {
		String msgUriStr = msg.getRequestHeader().getURI().toString();
		try {
			//CSRF tokens are enabled - need to check if request contains CSRF tokens and set the correct token value
			if (AnalyzerUtils.getCsrfTokens() != null) {

				Set<String> params = new HashSet<String>(Arrays.asList(msg.getParamNames()));
				Url url = new Url(msgUriStr, msg.getRequestHeader().getMethod(), params);
				HashMap<String, CsrfToken> msgCsrfParams = ResponseParser.getTokens(url); 

				//Request contains CSRF tokens
				if (msgCsrfParams != null) {
					for (CsrfToken token : msgCsrfParams.values()) {
						if (url.getMethod().equalsIgnoreCase(POST) ) {
							AnalyzerUtils.setParameterPostRequest(msg, token.getName(), token.getValue());		
						}
						else { 	//Request is not using POST method, then use GET
							AnalyzerUtils.setParameterGetRequest(msg, token.getName(), token.getValue());
						}
					}
				}

			}

			removeCachingHeaders(msg);
			sender.sendAndReceive(msg);
			//Load CSRF tokens from response
			try {
				//If CSRF tokens are enabled - need to parse the response and look for CSRF tokens
				if (AnalyzerUtils.getCsrfTokens().size() > 0) {
					Source  response = new Source(msg.getResponseHeader().toString() +
							msg.getResponseBody().toString());
					ResponseParser.parseTokens(msgUriStr, response);
				}
			}
			catch (Exception e){
				e.printStackTrace();	//In case the response is null
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	private static void removeCachingHeaders(HttpMessage msg) {
		msg.getRequestHeader().setHeader(HttpHeader.IF_MODIFIED_SINCE, null);
		msg.getRequestHeader().setHeader(HttpHeader.IF_NONE_MATCH, null);
	}



}
