package com.hacktics.diviner.payloads;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The PayloadDatabaseLoader class is used to load the various entries from the
 * payload database XML file into a PayloadDatabaseContainer object.
 * The class loads the entries from an XML file stored in a path provided as
 * input to the various class methods.

 * The class uses the external package JDOM (jdom.jar) for parsing XML.
 *
 * The following sample describes the structure of the configuration file:
 *
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;root&gt;
 *     &lt;Vendor&gt;
 *         &lt;VendorName&gt;VendorName&lt;/VendorName&gt;
 *         &lt;Logo&gt;[/path/imagefile.extension]&lt;/Logo&gt;
 *         &lt;Description&gt;VendorDescription&lt;/Description&gt;
 *         &lt;WebSite&gt;WebSiteAddress&lt;/WebSite&gt;
 *     &lt;/Vendor&gt;
 *     &lt;Authors&gt;
 *         &lt;Author1&gt;
 *             &lt;AuthorId&gt;AuthorId&lt;/AuthorId&gt;
 *             &lt;FullName&gt;AuthorName&lt;/FullName&gt;
 *             &lt;Twitter&gt;AuthorTwitter&lt;/Twitter&gt;
 *             &lt;Linkedin&gt;AuthorLinkedin&lt;/Linkedin&gt;
 *         &lt;/Author1&gt;
 *         &lt;Author2&gt;
 *             &lt;AuthorId&gt;AuthorId&lt;/AuthorId&gt;
 *             &lt;FullName&gt;AuthorName&lt;/FullName&gt;
 *             &lt;Twitter&gt;AuthorTwitter&lt;/Twitter&gt;
 *             &lt;Linkedin&gt;AuthorLinkedin&lt;/Linkedin&gt;
 *         &lt;/Author2&gt;
 *     &lt;/Authors&gt;
 *     &lt;AttackVectors&gt;
 *         &lt;AttackVectorName&gt;
 *             &lt;Title&gt;Title&lt;/Title&gt;
 *             &lt;Description&gt;AttackVectorDescription&lt;/Description&gt;
 *             &lt;Template&gt;AttackVectorType&lt;/Template&gt;
 *             &lt;Platforms&gt;
 *                 &lt;Platform&gt;
 *                    &lt;ID&gt;1&lt;/ID&gt;
 *                    &lt;PlatformName&gt;Name&lt;/PlatformName&gt;
 *                    &lt;PlatformVersion&gt;Version&lt;/PlatformVersion&gt;
 *                 &lt;/Platform&gt;
 *                 &lt;Platform&gt;
 *                    &lt;ID&gt;2&lt;/ID&gt;
 *                    &lt;PlatformName&gt;Name&lt;/PlatformName&gt;
 *                    &lt;PlatformVersion&gt;Version&lt;/PlatformVersion&gt;
 *                 &lt;/Platform&gt;
 *             &lt;/Platforms&gt;
 *             &lt;DetectionPayloads&gt;
 *                 &lt;Payload1&gt;
 *                     &lt;ID&gt;1&lt;/ID&gt;
 *                     &lt;Title&gt;Title&lt;/Title&gt;
 *                     &lt;TargetType&gt;String&lt;/TargetType&gt;
 *                     &lt;Context&gt;WHERE&lt;/Context&gt;
 *                     &lt;PayloadValue&gt;PayloadValue&lt;/PayloadValue&gt;
 *                     &lt;Description&gt;PayloadDescription&lt;/Description&gt;
 *                     &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *                     &lt;Platforms&gt;CommaSeparatedPlatforms&lt;/Platforms&gt;
 *                 &lt;/Payload1&gt;
 *                 &lt;Payload2&gt;
 *                     &lt;ID&gt;2&lt;/ID&gt;
 *                     &lt;Title&gt;Title&lt;/Title&gt;
 *                     &lt;TargetType&gt;String&lt;/TargetType&gt;
 *                     &lt;Context&gt;WHERE&lt;/Context&gt;
 *                     &lt;PayloadValue&gt;PayloadValue&lt;/PayloadValue&gt;
 *                     &lt;Description&gt;PayloadDescription&lt;/Description&gt;
 *                     &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *                     &lt;Platforms&gt;CommaSeparatedPlatforms&lt;/Platforms&gt;
 *                 &lt;/Payload2&gt;
 *             &lt;/DetectionPayloads&gt;
 *             &lt;ExploitationPayloads&gt;
 *                 &lt;Exploit1&gt;
 *                     &lt;ID&gt;1&lt;/ID&gt;
 *                     &lt;Title&gt;Title&lt;/Title&gt;
 *                     &lt;TargetType&gt;String&lt;/TargetType&gt;
 *                     &lt;Context&gt;WHERE&lt;/Context&gt;
 *                     &lt;ExploitValue&gt;ExploitValue&lt;/ExploitValue&gt;
 *                     &lt;Description&gt;ExploitDescription&lt;/Description&gt;
 *                     &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *                     &lt;Platforms&gt;CommaSeparatedPlatforms&lt;/Platforms&gt;
 *                 &lt;/Exploit1&gt;
 *                 &lt;Exploit2&gt;
 *                     &lt;ID&gt;2&lt;/ID&gt;
 *                     &lt;Title&gt;Title&lt;/Title&gt;
 *                     &lt;TargetType&gt;String&lt;/TargetType&gt;
 *                     &lt;Context&gt;WHERE&lt;/Context&gt;
 *                     &lt;ExploitValue&gt;ExploitValue&lt;/ExploitValue&gt;
 *                     &lt;Description&gt;ExploitDescription&lt;/Description&gt;
 *                     &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *                     &lt;Platforms&gt;CommaSeparatedPlatforms&lt;/Platforms&gt;
 *                 &lt;/Exploit2&gt;
 *             &lt;/ExploitationPayloads&gt;
 *             &lt;DelimiterSwapRules&gt;
 *                 &lt;Technique1&gt;
 *                     &lt;ID&gt;1&lt;/ID&gt;
 *                     &lt;Title&gt;Title&lt;/Title&gt;
 *                     &lt;TargetType&gt;String&lt;/TargetType&gt;
 *                     &lt;Context&gt;WHERE&lt;/Context&gt;
 *                     &lt;Rules&gt;
 *                         &lt;Rule1&gt;
 *                             &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 *                             &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *                         &lt;/Rule1&gt;
 *                         &lt;Rule2&gt;
 *                             &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 *                             &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *                         &lt;/Rule2&gt;
 *                     &lt;/Rules&gt;
 *                     &lt;Description&gt;TechniqueValue&lt;/Description&gt;
 *                     &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *                     &lt;Platforms&gt;CommaSeparatedPlatforms&lt;/Platforms&gt;
 *                 &lt;/Technique1&gt;
 *             &lt;/DelimiterSwapRules&gt;
 *             &lt;EvasionTechniques&gt;
 *                 &lt;Technique1&gt;
 *                     &lt;ID&gt;1&lt;/ID&gt;
 *                     &lt;Title&gt;Title&lt;/Title&gt;
 *                     &lt;TargetType&gt;String&lt;/TargetType&gt;
 *                     &lt;Context&gt;WHERE&lt;/Context&gt;
 *                     &lt;Rules&gt;
 *                         &lt;Rule1&gt;
 *                             &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 *                             &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *                         &lt;/Rule1&gt;
 *                         &lt;Rule2&gt;
 *                             &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 *                             &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *                         &lt;/Rule2&gt;
 *                     &lt;/Rules&gt;
 *                     &lt;AdvancedRules&gt;
 *                         &lt;AdvancedRule1&gt;
 *                             &lt;SourceRegEx&gt;RegEx&lt;/SourceRegEx&gt;
 *                             &lt;TargetValue&gt;Value&lt;/TargetValue&gt;
 *                         &lt;/AdvancedRule1&gt;
 *                         &lt;AdvancedRule2&gt;
 *                             &lt;SourceRegEx&gt;RegEx&lt;/SourceRegEx&gt;
 *                             &lt;TargetValue&gt;Value&lt;/TargetValue&gt;
 *                         &lt;/AdvancedRule2&gt;
 *                     &lt;/AdvancedRules&gt;
 *                     &lt;Description&gt;TechniqueValue&lt;/Description&gt;
 *                     &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *                     &lt;Platforms&gt;CommaSeparatedPlatforms&lt;/Platforms&gt;
 *                 &lt;/Technique1&gt;
 *             &lt;/EvasionTechniques&gt;
 *         &lt;/AttackVectorName&gt;
 *     &lt;/AttackVectors&gt;
 * &lt;/root&gt;
 *
 * <B>The description of fields in the payload database file:</B>
 * Vendor - The name of the payload database vendor.
 * Author - The name of an author of a specific payload/rule.
 * AttackVectorName - The name of the attack vector.
 * Template - The type of the attack vector (affects the GUI).
 * DetectionPayloads - Ready-to-use payloads for vulnerability detection.
 * ExploitationPayloads - Ready-to-use payloads for vulnerability exploitation.
 * DelimiterSwapRules - Rules to *simultaneously* swap payload delimiters.
 * EvasionTechniques - Rules to employ evasion techniques on a payload.
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class PayloadDatabaseLoader implements Serializable {
	//****************
	//* CONSTRUCTORS *
	//****************

	/**
	 * Unique class identifier.
	 */
	private static final long serialVersionUID = -8400314859759466074L;
	/**
	 * A private constructor designed to prevent instance creation.
	 */
	private static ArrayList<AttackVectorContainer> attackVectorsList;
	private static ArrayList<AuthorContainer> authorsLists = new ArrayList<AuthorContainer>();
	private PayloadDatabaseLoader() {
		//Do Nothing
	} //end of default constructor


	//***********
	//* METHODS *
	//***********
	/**
	 * Loads the payloads & rules from the xml payload database and stores
	 * them in a payload database container.
	 *
	 * @param payloadFilePath The hashing configuration file path.
	 * @return PayloadDatabaseContainer A payload database container object.
	 * @throws Exception Invalid path, file format or element name.
	 * @since 1.0
	 */


	public static void getPayloadDatabase(String pathToPayloadDb) throws Exception {
		//		System.out.println(Resources.PAYLOAD_DATABASE_BASIC);
		//		System.out.println(pathToPayloadDb);
		String f = readFileAsString(pathToPayloadDb);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource ins = new InputSource(new StringReader(f));

		Document doc = builder.parse(ins);

		parseDivinerXML(doc.getDocumentElement());

	} //end of method


	public static ArrayList<AttackVectorContainer> getAttackVectorList() {
		return attackVectorsList;
	}
	
	public static void addNewAttackVector(AttackVectorContainer attack) {
		attackVectorsList.add(attack);
	}

	public static ArrayList<AuthorContainer> getAuthors() {
		return authorsLists;
	}

	public static void parseDivinerXML(Node node) {

		// do something with the current node instead of System.out
		if (node.getNodeName().equals("Vendor")) {

			VendorContainer vendor = getVendor(node);
		}

		//Parse authors from XML
		if (node.getNodeName().equals("Authors")) {
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentNode = nodeList.item(i);
				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					AuthorContainer author = getAuthor(currentNode);
					authorsLists.add(author);
				}
			}
		}

		//Parse attacks from XML (recursive)
		if (node.getNodeName().equals("AttackVectors")) {
			attackVectorsList = getAttackVectorList(node);
		}
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				//calls this method for all the children which is Element
				parseDivinerXML(currentNode);
			}
		}
	}


	private static VendorContainer getVendor(Node node) {
		String logo ="";
		String name = "";
		String desc = "";
		String website = "";
		NodeList nodeList = node.getChildNodes();


		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch (currentNode.getNodeName()) {
				case "Logo":
					logo = currentNode.getTextContent();
					break;
				case "VendorName":
					name = currentNode.getTextContent();
					break;
				case "Description":
					desc = currentNode.getTextContent();
					break;
				case "Website":
					website = currentNode.getTextContent();
					break;
				}
			}
		}
		return new VendorContainer(name, logo, desc, website);
	}

	private static AuthorContainer getAuthor(Node node) {
		int id = 0;
		String name = "";
		String twitter = "";
		String linkedin = "";

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch (currentNode.getNodeName()) {
				case "AuthorId":
					id = Integer.parseInt(currentNode.getTextContent());
					break;
				case "FullName":
					name = currentNode.getTextContent();
					break;
				case "Twitter":
					twitter = currentNode.getTextContent();
					break;
				case "Linkedin":
					linkedin = currentNode.getTextContent();
					break;
				}
			}
		}
		return new AuthorContainer(id, name, linkedin, twitter);
	}

	private static ArrayList<AttackVectorContainer> getAttackVectorList(Node AttackVectorList) {
		ArrayList<AttackVectorContainer> result = new ArrayList<AttackVectorContainer>();
		NodeList nodeList = AttackVectorList.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				result.add(getAttackVector(currentNode));

			}
		}
		return result;
	}

	private static AttackVectorContainer getAttackVector(Node attackVector) {
		String attackName = attackVector.getNodeName();
		String attackTitle = "";
		String description = "";
		String template = "";
		HashMap<String, PlatformContainer> platforms = new HashMap<String, PlatformContainer>();
		HashMap<String , PayloadContainer> detectionPayloads = null;
		HashMap<String , ExploitContainer> exploitationPayloads = new HashMap<String , ExploitContainer>();
		HashMap<String , DelimiterSwapRuleContainer> delimiterSwapRules = new HashMap<String , DelimiterSwapRuleContainer>();
		HashMap<String , EvasionRuleContainer> evasionRules = new HashMap<String , EvasionRuleContainer>();

		NodeList nodeList = attackVector.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

				switch(currentNode.getNodeName()) {
				case "Description":
					description = currentNode.getTextContent();
					break;
				case "Template":
					template = currentNode.getTextContent();
					break;
				case "Platforms":
					platforms = getPlatforms(currentNode);
					break;
				case "DetectionPayloads":
					detectionPayloads = getDetectionPayloadList(currentNode);
					break;
				case "DelimiterSwapRules":
					delimiterSwapRules = getDelimiterList(currentNode);
					break;
				case "EvasionTechniques":
					evasionRules = getEvasionRules(currentNode);
					break;
				case "ExploitationPayloads":
					exploitationPayloads = getExploitationList(currentNode);
					break;
				}

			}
		}

		return new AttackVectorContainer(attackName, attackTitle, description, template, platforms, detectionPayloads, exploitationPayloads, delimiterSwapRules, evasionRules);
	}

	private static HashMap<String, DelimiterSwapRuleContainer> getDelimiterList(Node delimiters) {
		HashMap<String, DelimiterSwapRuleContainer> result = new HashMap<String, DelimiterSwapRuleContainer>();		
		NodeList nodeList = delimiters.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				DelimiterSwapRuleContainer delimiter = getDelimiter(currentNode);
				result.put(delimiter.getRuleId(), delimiter);
			}	
		}
		return result;
	}

	private static HashMap<String, PlatformContainer> getPlatforms(Node platforms) {
		HashMap<String, PlatformContainer> result = new HashMap<String, PlatformContainer>();		
		NodeList nodeList = platforms.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				PlatformContainer platform = getPlatform(currentNode);
				result.put(platform.getPlatformId(), platform);
			}	
		}
		return result;
	}

	private static PlatformContainer getPlatform(Node platform) {

		String platformId = "";
		String platformName = "";
		String platformVersion = "";
		String platformAlias = "";

		NodeList nodeList = platform.getChildNodes();

		platformAlias = platform.getNodeName();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch(currentNode.getNodeName()) {
				case "ID":
					platformId = currentNode.getTextContent();
					break;
				case "PlatformName":
					platformName = currentNode.getTextContent();
					break;
				case "PlatformVersion":
					platformVersion = currentNode.getTextContent();
					break;


				}

			}
		}
		return new PlatformContainer(platformAlias, platformId, platformName, platformVersion);
	}
	private static HashMap<String , EvasionRuleContainer> getEvasionRules(Node evasionRule) {
		HashMap<String , EvasionRuleContainer> result = new HashMap<String , EvasionRuleContainer>();
		NodeList nodeList = evasionRule.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				EvasionRuleContainer evasion = getEvasion(currentNode);
				result.put(evasion.getRuleId(), evasion);
			}	
		}
		return result;

	}

	private static HashMap<String, ExploitContainer> getExploitationList(Node exploitations) {
		HashMap<String , ExploitContainer> result = new HashMap<String , ExploitContainer>();
		NodeList nodeList = exploitations.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				ExploitContainer exploit = getExploit(currentNode);
				result.put(exploit.getPayloadId(), exploit);
			}	
		}
		return result;
	}

	private static ExploitContainer getExploit(Node exploit) {
		String payloadId = "";
		String title = "";
		String targetType = "";
		String context = "";
		String exploitValue = "";
		String exploitDescription = "";
		String author = "";
		String platforms = "";

		NodeList nodeList = exploit.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch(currentNode.getNodeName()) {
				case "ID":
					payloadId = currentNode.getTextContent();
					break;
				case "Title":
					title = currentNode.getTextContent();
					break;
				case "TargetType":
					targetType = currentNode.getTextContent();
					break;
				case "Context":
					context = currentNode.getTextContent();
					break;
				case "ExploitValue":
					exploitValue = currentNode.getTextContent();
					break;
				case "Description":
					exploitDescription = currentNode.getTextContent();
					break;
				case "Credit":
					author = currentNode.getTextContent();
					break;
				case "Platforms":
					platforms = currentNode.getTextContent();
					break;
				}
			}
		}
		return new ExploitContainer(payloadId, title, targetType, context, exploitValue, exploitDescription, author, platforms);
	}

	private static EvasionRuleContainer getEvasion(Node rule) {
		String ruleId = "";
		String title = "";
		String targetType = "";
		String context = "";
		ArrayList<SwapRuleContainer> swapRuleCollection = null;
		ArrayList<AdvancedRuleContainer> advancedRuleCollection = null;
		String description = "";
		String author = "";
		String platforms = "";

		NodeList nodeList = rule.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch(currentNode.getNodeName()) {
				case "ID":
					ruleId = currentNode.getTextContent();
					break;
				case "Title":
					title = currentNode.getTextContent();
					break;
				case "TargetType":
					targetType = currentNode.getTextContent();
					break;
				case "Context":
					context = currentNode.getTextContent();
					break;
				case "Rules":
					swapRuleCollection = getRulesList(currentNode);
					break;
				case "AdvancedRule":
					advancedRuleCollection = getAdvancedRulesList(currentNode);
					break;
				case "Description":
					description = currentNode.getTextContent();
					break;
				case "Credit":
					author = currentNode.getTextContent();
					break;
				case "Platforms":
					platforms = currentNode.getTextContent();
					break;
				}
			}
		}

		return new EvasionRuleContainer(ruleId, title, targetType, context, swapRuleCollection, advancedRuleCollection, description, author, platforms);
	}

	private static DelimiterSwapRuleContainer getDelimiter(Node delimiter) {
		String ruleId = "";
		String title = "";
		String targetType = "";
		String context = "";
		ArrayList<SwapRuleContainer> swapRuleCollection = null;
		String description = "";
		String author = "";
		String platforms = "";

		NodeList nodeList = delimiter.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch(currentNode.getNodeName()) {
				case "ID":
					ruleId = currentNode.getTextContent();
					break;
				case "Title":
					title = currentNode.getTextContent();
					break;
				case "TargetType":
					targetType = currentNode.getTextContent();
					break;
				case "Context":
					context = currentNode.getTextContent();
					break;
				case "Rules":
					swapRuleCollection = getRulesList(currentNode);
					break;
				case "Description":
					description = currentNode.getTextContent();
					break;
				case "Credit":
					author = currentNode.getTextContent();
					break;
				case "Platforms":
					platforms = currentNode.getTextContent();
					break;
				}
			}
		}
		return new DelimiterSwapRuleContainer(ruleId, title, targetType, context, swapRuleCollection, description, author, platforms);
	}

	private static ArrayList<SwapRuleContainer> getRulesList(Node rules) {
		ArrayList<SwapRuleContainer> result =  new ArrayList<SwapRuleContainer>();
		NodeList nodeList = rules.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				result.add(getSwapRule(currentNode));
			}
		}
		return result;
	}

	private static ArrayList<AdvancedRuleContainer> getAdvancedRulesList(Node rules) {
		ArrayList<AdvancedRuleContainer> result =  new ArrayList<AdvancedRuleContainer>();
		NodeList nodeList = rules.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				result.add(getAdvancedRule(currentNode));
			}
		}
		return result;
	}

	private static AdvancedRuleContainer getAdvancedRule(Node rule) {
		String sourceChar = "";
		String targetChar = "";
		NodeList nodeList = rule.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch (currentNode.getNodeName()) {
				case "SourceRegEx":
					sourceChar = currentNode.getTextContent();
					break;
				case "TargetValue":
					targetChar = currentNode.getTextContent();
					break;

				}
			}
		}
		return new AdvancedRuleContainer(sourceChar, targetChar);
	}

	private static SwapRuleContainer getSwapRule(Node rule) {
		String sourceChar = "";
		String targetChar = "";
		NodeList nodeList = rule.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				switch (currentNode.getNodeName()) {
				case "SourceChar":
					sourceChar = currentNode.getTextContent();
					break;
				case "TargetChar":
					targetChar = currentNode.getTextContent();
					break;

				}
			}
		}
		return new SwapRuleContainer(sourceChar, targetChar);
	}

	private static HashMap<String, PayloadContainer> getDetectionPayloadList(Node payloads) {

		HashMap<String, PayloadContainer> result = new HashMap<String, PayloadContainer>();		
		NodeList nodeList = payloads.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				PayloadContainer payload = getDetectionPayload(currentNode);
				result.put(payload.getPayloadId(), payload);
			}	
		}
		return result;
	}


	private static PayloadContainer getDetectionPayload(Node payload) {

		String payloadId = "";
		String title = "";
		String targetType = "";
		String context = "";
		String payloadValue = "";
		String payloadDescription = "";
		String author = "";
		String platforms = "";

		NodeList nodeList = payload.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

				switch(currentNode.getNodeName()) {
				case "ID":
					payloadId = currentNode.getTextContent();
					break;
				case "Title":
					title = currentNode.getTextContent();
					break;
				case "TargetType":
					targetType = currentNode.getTextContent();
					break;
				case "Context":
					context = currentNode.getTextContent();
					break;
				case "PayloadValue":
					payloadValue = currentNode.getTextContent();
					break;
				case "Description":
					payloadDescription = currentNode.getTextContent();
					break;
				case "Credit":
					author = currentNode.getTextContent();
					break;
				case "Platforms":
					platforms = currentNode.getTextContent();
					break;
				}
			}	
		}
		return  new PayloadContainer(payloadId, title, targetType, context, payloadValue, payloadDescription, author, platforms);
	}
	private static String readFileAsString(String filePath)
			throws java.io.IOException{
		StringBuffer fileData = new StringBuffer(2000);
		BufferedReader reader = new BufferedReader(
				new FileReader(filePath));
		char[] buf = new char[2048];
		int numRead=0;
		while((numRead=reader.read(buf)) != -1){
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		return fileData.toString();
	}
} //end of class
