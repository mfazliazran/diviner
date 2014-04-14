/**
 * 
 */
package com.hacktics.diviner.constants;

/**
 * This class contains GUI text constants, which are used throughout the extension.
 * @author Shay Chen
 * @since 1.0
 */
public class DivinerGuiConstants {
	public static final String WELCOME_TEXT = 
			"<html><font size='5'><b>Welcome to Diviner,</b></font><br>"
					+ "<font size='4'>An active information gathering platform aimed"
					+ "to enhance the tester's decision making process.<br><br>"
					+ "<font size='5'><b>Quickstart</b></font><br>"
					+ "<font size='4'>Prior to using Diviner <b>'Analysis'</b> feature (upper menu), define ZAP as the browser proxy, and <b>manually crawl</b> the application,<br>"
					+ "while activating the various features and operations with <b>valid input</b>. The better the coverage, the better the result. <br>"
					+ "It is currently recommended to use diviner only on small to medium sized applications (or on a limited amount of URLs). <br><br>"
					+ "<html><font size='5'><b>How Does it Work?</b></font><br>"
					+ "Diviner analyzes and reuses the requests found in ZAP's history at at the moment of its activation,<br>"
					+ "activates the application entry points in many extreme scenarios, generates and isolates specific application behaviors, <br>"
					+ "and uses the information obtained to <b>predict</b> the structure of the <b>server side memory, source code, and processes.</b><br>"
					+ "These aspects are then presented in the form of a <b>visual map</b>, which includes <b>leads, tasks and payload recommendations</b>.<br><br>"
					+ "Diviner also attempts to analyze this information in order to locate potential leads for vulnerabilities, <br>"
					+ "both simple and complex, and provides recommendations for detecting and exploiting them.<br><br>"
					+ "The following features are currently supported:</font><br><br>"
					+ "<table border='0' title='features'>"
					+ "<tr><td><font size='4'><b><u>Analysis Features</b></u></font></td><td><font size='4'><b><u>Coverage Features</b></u></font></td></tr>"
					+ "<tr>"
					+ "<td valign='top'><font size='4'>"
					+ "<UL TYPE='CIRCLE'>"
					+ "<LI><I>Detect Input Reflections (Potential XSS, CRLF Injection, Etc)</I>"
					+ "<LI><I>Detect Error-Generating Scenarios (Potential Injections)</I>"
					+ "<LI><I>Detect Content Differentiation Effects (Direct & Indirect Effect of Input)</I>"
					+ "</UL>"
					+ "</font></td>"
					+ "<td valign='top'><font size='4'>"
					+ "<UL TYPE='CIRCLE'>"
					+ "<LI><I>Reuse the Content in ZAP's History</I>"
					+ "<LI><I>Domain Restrictions</I>"
					+ "<LI><I>URL Exclusion</I>"
					+ "</UL>"
					+ "</font></td>"
					+ "</tr>"
					+ "<tr><td><font size='4'><b><u>Deduction Processes</b></u></font></td><td><font size='4'><b><u>Barrier Support</b></u></font></td></tr>"
					+ "<tr>"
					+ "<td valign='top'><font size='4'>"
					+ "<UL TYPE='CIRCLE'>"
					+ "<LI><I>Convert Behaviors into Pseudo-code Representation of Server-Side Code</I>"
					+ "<LI><I>Predict the Structure of the Server Side Memory (Session / DB / Etc)</I>"
					+ "<LI><I>Isolate and Present a Map of the Server-Side Processes</I>"
					+ "<LI><I>Specific Payload Recommendations</I>"
					+ "</UL>"
					+ "</font></font></td>"
					+ "<td valign='top'><font size='4'>"
					+ "<UL TYPE='CIRCLE'>"
					+ "<LI><I>Authentication Support</I>"
					+ "<LI><I>Anti-CSRF Token Support</I>"
					+ "<LI><I>Resend Updated Values of Required Parameters (VIEWSTATE, Etc)</I>"
					+ "<LI><I>Replay Relevant History Prior To Resending Requests</I>"
					+ "</UL>"
					+ "</font></td>"
					+ "</tr>"
					+ "<tr><td><font size='4'><b><u>Built-in Plugins</b></u></font></td><td><font size='4'><b><u>Integration Features</b></u></font></td></tr>"
					+ "<tr>"
					+ "<td valign='top'><font size='4'>"
					+ "<UL TYPE='CIRCLE'>"
					+ "<LI><I>A Customized Manual Penetration Test Payload Manager</I>"
					+ "</UL>"
					+ "</font></td>"
					+ "<td valign='top'><font size='4'>"
					+ "<UL TYPE='CIRCLE'>"
					+ "<LI><I>Integration With ZAP's 'Resend Request' Feature</I>"
					+ "</UL>"
					+ "</font></td>"
					+ "</tr>"
					+ "</table>"
					+ "</font>"
					+ "</html>";


}
