package com.hacktics.diviner.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hacktics.diviner.payloads.AttackVectorContainer;
import com.hacktics.diviner.payloads.DelimiterSwapRuleContainer;
import com.hacktics.diviner.payloads.EvasionRuleContainer;
import com.hacktics.diviner.payloads.ExploitContainer;
import com.hacktics.diviner.payloads.PayloadAuthorContainer;
import com.hacktics.diviner.payloads.PayloadContainer;
import com.hacktics.diviner.payloads.PayloadDatabaseContainer;
import com.hacktics.diviner.payloads.PayloadDatabaseLoader;
import com.hacktics.diviner.payloads.PlatformContainer;
import com.hacktics.diviner.payloads.SwapRuleContainer;
import com.hacktics.diviner.payloads.VendorContainer;

public class XMLSavePayloadManger {

	public static void PayloadMangertoXML(JDialog parent) throws ParserConfigurationException {

		int iReturnedValue = 0 ;
		JFileChooser SaveFileDialog = new JFileChooser();


		//Jfilechooser
		//				
		//Save The File
		iReturnedValue = SaveFileDialog.showSaveDialog(SaveFileDialog);

		if (iReturnedValue == JFileChooser.APPROVE_OPTION) {
			SavePayloadDatabaseContainerToXml(SaveFileDialog.getSelectedFile().toString());
			JOptionPane.showMessageDialog(parent, "Payloads saved to XML");

		}

	}

	private static void SavePayloadDatabaseContainerToXml(String FilePath) throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document PayloadXMLDocument = docBuilder.newDocument();
		Element rootElement = PayloadXMLDocument.createElement("root");
		
		Element AttacksElement = PayloadXMLDocument.createElement("AttackVectors");

//		PayloadXMLDocument.createTextNode("aaaa");

		//CreateVendorsXml(PayloadXMLDocument);
		//CreateAuthorsXml(PayloadXMLDocument);
//		rootElement = CreateAttackVectorsXml(PayloadXMLDocument,rootElement);
	
		Collection<AttackVectorContainer> AttackVectors = PayloadDatabaseLoader.getAttackVectorList();

		Element AttackVectorNameElement = null;
		Element AttackVectorDescElement = null;
		Element AttackVectorTemplateElement = null;
		Element AttackVectorPlatformsElement = null;
		Element AttackVectorDetectionPayloadElement = null;
		Element AttackVectorExploitationPayloadElement = null;
		Element AttackVectorDelimiterSwapRulesElement = null;
		Element AttackVectorEvasionTechniquesElement = null;


		for (AttackVectorContainer AttackVector : AttackVectors) {

			AttackVectorNameElement = PayloadXMLDocument.createElement(AttackVector.getAttackName());
			

//			System.out.println(AttackVector.getDescription());
//			System.out.println(AttackVector.getTemplate());
//			System.out.println(CreatePlatformsElement(AttackVector.getPlatforms().values(),PayloadXMLDocument,AttackVectorPlatformsElement));
//			System.out.println(CreatePayloadsElement(AttackVector.getDetectionPayloads().values(),PayloadXMLDocument,AttackVectorDetectionPayloadElement));
//			System.out.println(CreateExploitaionElement(AttackVector.getExploitationPayloads().values(),PayloadXMLDocument,AttackVectorExploitationPayloadElement));
//			System.out.println(CreateSwapRulesElement(AttackVector.getDelimiterSwapRules().values(),PayloadXMLDocument,AttackVectorDelimiterSwapRulesElement));
//			System.out.println(CreateEvasionTechniquesElement(AttackVector.getEvasionRules().values(),PayloadXMLDocument,AttackVectorEvasionTechniquesElement));
			
			AttackVectorDescElement = PayloadXMLDocument.createElement("Description");
			AttackVectorDescElement.appendChild(PayloadXMLDocument.createTextNode(AttackVector.getDescription()));
			AttackVectorNameElement.appendChild(AttackVectorDescElement);
					
			AttackVectorTemplateElement = PayloadXMLDocument.createElement("Template");
			AttackVectorTemplateElement.appendChild(PayloadXMLDocument.createTextNode(AttackVector.getTemplate()));
			AttackVectorNameElement.appendChild(AttackVectorTemplateElement);

			AttackVectorPlatformsElement = PayloadXMLDocument.createElement("Platforms");
			AttackVectorPlatformsElement.appendChild(PayloadXMLDocument.createTextNode(CreatePlatformsElement(AttackVector.getPlatforms().values(),PayloadXMLDocument,AttackVectorPlatformsElement)));
			AttackVectorNameElement.appendChild(AttackVectorPlatformsElement);

			AttackVectorDetectionPayloadElement = PayloadXMLDocument.createElement("DetectionPayloads");
			AttackVectorDetectionPayloadElement.appendChild(PayloadXMLDocument.createTextNode(CreatePayloadsElement(AttackVector.getDetectionPayloads().values(),PayloadXMLDocument,AttackVectorDetectionPayloadElement)));
			AttackVectorNameElement.appendChild(AttackVectorDetectionPayloadElement);

			AttackVectorExploitationPayloadElement = PayloadXMLDocument.createElement("ExploitationPayloads");
			AttackVectorExploitationPayloadElement.appendChild(PayloadXMLDocument.createTextNode(CreateExploitaionElement(AttackVector.getExploitationPayloads().values(),PayloadXMLDocument,AttackVectorExploitationPayloadElement)));
			AttackVectorNameElement.appendChild(AttackVectorExploitationPayloadElement);

			AttackVectorDelimiterSwapRulesElement = PayloadXMLDocument.createElement("DelimiterSwapRules");
			AttackVectorDelimiterSwapRulesElement.appendChild(PayloadXMLDocument.createTextNode(CreateSwapRulesElement(AttackVector.getDelimiterSwapRules().values(),PayloadXMLDocument,AttackVectorDelimiterSwapRulesElement)));
			AttackVectorNameElement.appendChild(AttackVectorDelimiterSwapRulesElement);

			AttackVectorEvasionTechniquesElement = PayloadXMLDocument.createElement("EvasionTechniques");
			AttackVectorEvasionTechniquesElement.appendChild(PayloadXMLDocument.createTextNode(CreateEvasionTechniquesElement(AttackVector.getEvasionRules().values(),PayloadXMLDocument,AttackVectorEvasionTechniquesElement)));
			AttackVectorNameElement.appendChild(AttackVectorEvasionTechniquesElement);
			
			AttacksElement.appendChild(AttackVectorNameElement);

		}
			
			rootElement.appendChild(AttacksElement);
			PayloadXMLDocument.appendChild(rootElement);

		TransformerFactory transformerFactory = 
				  TransformerFactory.newInstance();
				  Transformer transformer = null;
				try {
					transformer = transformerFactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  DOMSource source = new DOMSource(PayloadXMLDocument);
				  StreamResult result =  new StreamResult(FilePath);
				  try {
					System.out.println(rootElement);
					transformer.transform(source, result);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}



	//private static void CreateVendorsXml(Document PayloadXMLDocument) {
	// TODO Auto-generated method stub

	//		Element VendorNameElement = null;
	//		Element LogoElement= null;
	//		Element DescriptionElement = null;
	//		Element WebSiteElement = null;
	//		
	//		VendorContainer Vendor = dbContainer.getVendor();
	//		
	//		Element VendorElement = PayloadXMLDocument.createElement("Vendor");
	//		PayloadXMLDocument.appendChild(VendorElement);
	//			
	//		VendorNameElement = PayloadXMLDocument.createElement("VendorName");
	//		VendorNameElement.appendChild(PayloadXMLDocument.createTextNode(Vendor.getVendor()));
	//		
	//		LogoElement = PayloadXMLDocument.createElement("Logo");
	//		LogoElement.appendChild(PayloadXMLDocument.createTextNode(Vendor.getLogo()));
	//		
	//		DescriptionElement = PayloadXMLDocument.createElement("Description");
	//		DescriptionElement.appendChild(PayloadXMLDocument.createTextNode(Vendor.getDescription()));
	//		
	//		WebSiteElement = PayloadXMLDocument.createElement("WebSite");
	//		WebSiteElement.appendChild(PayloadXMLDocument.createTextNode(Vendor.getWebsite()));


	//	}

	//private static void CreateAuthorsXml(Document PayloadXMLDocument) {
	// TODO Auto-generated method stub
	//		Element AuthorIDElement = null;
	//		Element FullNameElement= null;
	//		Element TwitterElement= null;
	//		Element LinkedinElement= null;
	//		Element SingleAuthorElement = null;
	//		
	//		Element AuthorsElement = PayloadXMLDocument.createElement("Authors");
	//		PayloadXMLDocument.appendChild(AuthorsElement);
	//		
	//		for (PayloadAuthorContainer Author : dbContainer.getAuthors().values()) {
	//			
	//			SingleAuthorElement = PayloadXMLDocument.createElement("Author");
	//			PayloadXMLDocument.appendChild(SingleAuthorElement);
	//			
	//			AuthorIDElement = PayloadXMLDocument.createElement("AuthorID");
	//			AuthorIDElement.appendChild(PayloadXMLDocument.createTextNode(Integer.toString(Author.getAuthorId())));
	//			PayloadXMLDocument.appendChild(AuthorIDElement);
	//			
	//			FullNameElement = PayloadXMLDocument.createElement("FullName");
	//			FullNameElement.appendChild(PayloadXMLDocument.createTextNode(Author.getAuthorName()));
	//			PayloadXMLDocument.appendChild(FullNameElement);
	//			
	//			TwitterElement = PayloadXMLDocument.createElement("Twitter");
	//			TwitterElement.appendChild(PayloadXMLDocument.createTextNode(Author.getTwitter()));
	//			PayloadXMLDocument.appendChild(TwitterElement);
	//			
	//			LinkedinElement = PayloadXMLDocument.createElement("Linkedin");
	//			LinkedinElement.appendChild(PayloadXMLDocument.createTextNode(Author.getLinkedin()));
	//			PayloadXMLDocument.appendChild(TwitterElement);
	//		}
	//	}

	private static Element CreateAttackVectorsXml(Document PayloadXMLDocument, Element RootElement) {
		// TODO Auto-generated method stub
		Collection<AttackVectorContainer> AttackVectors = PayloadDatabaseLoader.getAttackVectorList();
		
		Element AttackVectorNameElement = null;
		Element AttackVectorDescElement = null;
		Element AttackVectorTemplateElement = null;
		Element AttackVectorPlatformsElement = null;
		Element AttackVectorDetectionPayloadElement = null;
		Element AttackVectorExploitationPayloadElement = null;
		Element AttackVectorDelimiterSwapRulesElement = null;
		Element AttackVectorEvasionTechniquesElement = null;


		for (AttackVectorContainer AttackVector : AttackVectors) {

			AttackVectorNameElement = PayloadXMLDocument.createElement(AttackVector.getAttackName());
			
			AttackVectorDescElement = PayloadXMLDocument.createElement("Description");
			AttackVectorDescElement.appendChild(PayloadXMLDocument.createTextNode(AttackVector.getDescription()));
			AttackVectorNameElement.appendChild(AttackVectorDescElement);

			AttackVectorTemplateElement = PayloadXMLDocument.createElement("Template");
			AttackVectorTemplateElement.appendChild(PayloadXMLDocument.createTextNode(AttackVector.getTemplate()));
			AttackVectorNameElement.appendChild(AttackVectorTemplateElement);

			AttackVectorPlatformsElement = PayloadXMLDocument.createElement("Platforms");
			AttackVectorPlatformsElement.appendChild(PayloadXMLDocument.createTextNode(CreatePlatformsElement(AttackVector.getPlatforms().values(),PayloadXMLDocument,AttackVectorPlatformsElement)));
			AttackVectorNameElement.appendChild(AttackVectorPlatformsElement);

			AttackVectorDetectionPayloadElement = PayloadXMLDocument.createElement("DetectionPayloads");
			AttackVectorDetectionPayloadElement.appendChild(PayloadXMLDocument.createTextNode(CreatePayloadsElement(AttackVector.getDetectionPayloads().values(),PayloadXMLDocument,AttackVectorDetectionPayloadElement)));
			AttackVectorNameElement.appendChild(AttackVectorDetectionPayloadElement);

			AttackVectorExploitationPayloadElement = PayloadXMLDocument.createElement("ExploitationPayloads");
			AttackVectorExploitationPayloadElement.appendChild(PayloadXMLDocument.createTextNode(CreateExploitaionElement(AttackVector.getExploitationPayloads().values(),PayloadXMLDocument,AttackVectorExploitationPayloadElement)));
			AttackVectorNameElement.appendChild(AttackVectorExploitationPayloadElement);

			AttackVectorDelimiterSwapRulesElement = PayloadXMLDocument.createElement("DelimiterSwapRules");
			AttackVectorDelimiterSwapRulesElement.appendChild(PayloadXMLDocument.createTextNode(CreateSwapRulesElement(AttackVector.getDelimiterSwapRules().values(),PayloadXMLDocument,AttackVectorDelimiterSwapRulesElement)));
			AttackVectorNameElement.appendChild(AttackVectorDelimiterSwapRulesElement);

			AttackVectorEvasionTechniquesElement = PayloadXMLDocument.createElement("EvasionTechniques");
			AttackVectorEvasionTechniquesElement.appendChild(PayloadXMLDocument.createTextNode(CreateEvasionTechniquesElement(AttackVector.getEvasionRules().values(),PayloadXMLDocument,AttackVectorEvasionTechniquesElement)));
			AttackVectorNameElement.appendChild(AttackVectorEvasionTechniquesElement);
			
			RootElement.appendChild(AttackVectorNameElement);
		}
		return RootElement;


	}

	private static String CreateEvasionTechniquesElement(
			Collection<EvasionRuleContainer> EvasionTechniques, Document PayloadXMLDocument, Element RootElement) {
		// TODO Auto-generated method stub
		Element EvasionTechniquesElement = null;
		Element SinglEvasionTechniqueElement = null;
		Element EvasionTechniquesIDElement = null;
		Element EvasionTechniquesTitleElement = null;
		Element EvasionTechniquesTargetTypeElement = null;
		Element EvasionTechniquesContextElement = null;
		Element EvasionTechniquesRulesElement = null;
		Element EvasionTechniquesAdvancedRulesElement = null;
		Element EvasionTechniquesDescriptionElement = null;
		Element EvasionTechniquesCreditElement = null;
		Element EvasionTechniquesPlatformsElement = null;

//		EvasionTechniquesElement = PayloadXMLDocument.createElement("EvasionTechniques");
//		RootElement.appendChild(EvasionTechniquesElement);

		for (EvasionRuleContainer EvasionTechnique : EvasionTechniques) {

			SinglEvasionTechniqueElement = PayloadXMLDocument.createElement("Technique");
			RootElement.appendChild(SinglEvasionTechniqueElement);

			EvasionTechniquesIDElement = PayloadXMLDocument.createElement("ID");
			EvasionTechniquesIDElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getRuleId()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesIDElement);

			EvasionTechniquesTitleElement = PayloadXMLDocument.createElement("Title");
			EvasionTechniquesTitleElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getTitle()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesTitleElement);

			EvasionTechniquesTargetTypeElement = PayloadXMLDocument.createElement("TargetType");
			EvasionTechniquesTargetTypeElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getTargetType()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesTargetTypeElement);

			EvasionTechniquesContextElement = PayloadXMLDocument.createElement("Context");
			EvasionTechniquesContextElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getContext()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesContextElement);

			EvasionTechniquesRulesElement = PayloadXMLDocument.createElement("Rules");
			EvasionTechniquesRulesElement.appendChild(PayloadXMLDocument.createTextNode(CreateDelimiterSwapRuleRules(EvasionTechnique.getSwapRuleCollection(),PayloadXMLDocument,EvasionTechniquesRulesElement)));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesRulesElement);

			EvasionTechniquesAdvancedRulesElement = PayloadXMLDocument.createElement("AdvancedRules");
			EvasionTechniquesAdvancedRulesElement.appendChild(PayloadXMLDocument.createTextNode(CreateAdvancedEvasionRules(EvasionTechnique.getSwapRuleCollection(),PayloadXMLDocument,EvasionTechniquesAdvancedRulesElement)));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesAdvancedRulesElement);

			EvasionTechniquesDescriptionElement = PayloadXMLDocument.createElement("Description");
			EvasionTechniquesDescriptionElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getDescription()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesDescriptionElement);

			EvasionTechniquesCreditElement = PayloadXMLDocument.createElement("Credit");
			EvasionTechniquesCreditElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getAuthor()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesCreditElement);

			EvasionTechniquesPlatformsElement = PayloadXMLDocument.createElement("Platforms");
			EvasionTechniquesPlatformsElement.appendChild(PayloadXMLDocument.createTextNode(EvasionTechnique.getPlatforms()));
			SinglEvasionTechniqueElement.appendChild(EvasionTechniquesPlatformsElement);

		}


		return "";
	}

	private static String CreateAdvancedEvasionRules(
			ArrayList<SwapRuleContainer> swapRuleCollection,
			Document PayloadXMLDocument,Element RootElement) {
		// TODO Auto-generated method stub
		int iCounter = 1;
		Element SingleRuleElement = null;
		Element RuleIDElement = null;
		Element SourceCharElement = null;
		Element TargetCharElement = null;

//		RulesElement = PayloadXMLDocument.createElement("AdvancedRules");
//		RootElement.appendChild(RulesElement);

		for (SwapRuleContainer Rule : swapRuleCollection) {
			SingleRuleElement = PayloadXMLDocument.createElement("AdvancedRule");
			RootElement.appendChild(SingleRuleElement);

			RuleIDElement = PayloadXMLDocument.createElement("ID");
			RuleIDElement.appendChild(PayloadXMLDocument.createTextNode(Integer.toString(++iCounter)));
			SingleRuleElement.appendChild(RuleIDElement);

			SourceCharElement = PayloadXMLDocument.createElement("SourceChar");
			SourceCharElement.appendChild(PayloadXMLDocument.createTextNode(Rule.getSourceCharacter()));
			SingleRuleElement.appendChild(SourceCharElement);

			TargetCharElement = PayloadXMLDocument.createElement("TargetChar");
			TargetCharElement.appendChild(PayloadXMLDocument.createTextNode(Rule.getTargetCharacter()));
			SingleRuleElement.appendChild(TargetCharElement);
		}

		return "";
	}

	private static String CreateSwapRulesElement(
			Collection<DelimiterSwapRuleContainer> DelimiterSwapRules,
			Document PayloadXMLDocument, Element RootElement) {
		//TODO Auto-generated method stub
		Element DelimiterSwapRulesElement = null;
		Element SingleDelimiterSwapRuleElement = null;
		Element DelimiterSwapRuleIDElement = null;
		Element DelimiterSwapRuleTitleElement = null;
		Element DelimiterSwapRuleTargetTypeElement = null;
		Element DelimiterSwapRuleContextElement = null;
		Element DelimiterSwapRuleRulesElement = null;
		Element DelimiterSwapRuleDescriptionElement = null;
		Element DelimiterSwapRuleCreditElement = null;
		Element DelimiterSwapRulePlatformsElement = null;

//		DelimiterSwapRulesElement = PayloadXMLDocument.createElement("DelimiterSwapRules");
//		RootElement.appendChild(DelimiterSwapRulesElement);

		for (DelimiterSwapRuleContainer SwapRule : DelimiterSwapRules) {

			SingleDelimiterSwapRuleElement = PayloadXMLDocument.createElement("Technique");
			RootElement.appendChild(SingleDelimiterSwapRuleElement);

			DelimiterSwapRuleIDElement = PayloadXMLDocument.createElement("ID");
			DelimiterSwapRuleIDElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getRuleId()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleIDElement);

			DelimiterSwapRuleTitleElement = PayloadXMLDocument.createElement("Title");
			DelimiterSwapRuleTitleElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getTitle()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleTitleElement);

			DelimiterSwapRuleTargetTypeElement = PayloadXMLDocument.createElement("TargetType");
			DelimiterSwapRuleTargetTypeElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getTargetType()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleTargetTypeElement);

			DelimiterSwapRuleContextElement = PayloadXMLDocument.createElement("Context");
			DelimiterSwapRuleContextElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getContext()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleContextElement);

			DelimiterSwapRuleRulesElement = PayloadXMLDocument.createElement("Rules");
			DelimiterSwapRuleRulesElement.appendChild(PayloadXMLDocument.createTextNode(CreateDelimiterSwapRuleRules(SwapRule.getSwapRuleCollection(),PayloadXMLDocument,DelimiterSwapRuleRulesElement)));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleRulesElement);

			DelimiterSwapRuleDescriptionElement = PayloadXMLDocument.createElement("Description");
			DelimiterSwapRuleDescriptionElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getDescription()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleDescriptionElement);

			DelimiterSwapRuleCreditElement = PayloadXMLDocument.createElement("Credit");
			DelimiterSwapRuleCreditElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getAuthor()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRuleCreditElement);

			DelimiterSwapRulePlatformsElement = PayloadXMLDocument.createElement("Platforms");
			DelimiterSwapRulePlatformsElement.appendChild(PayloadXMLDocument.createTextNode(SwapRule.getPlatforms()));
			SingleDelimiterSwapRuleElement.appendChild(DelimiterSwapRulePlatformsElement);

		}

		// TODO Auto-generated method stub
		return "";
	}

	private static String CreateDelimiterSwapRuleRules(
			ArrayList<SwapRuleContainer> swapRuleCollection, Document PayloadXMLDocument, Element RootElement) {
		// TODO Auto-generated method stub
		int iCounter = 1;
		Element RulesElement = null;
		Element SingleRuleElement = null;
		Element RuleIDElement = null;
		Element SourceCharElement = null;
		Element TargetCharElement = null;

//		RulesElement = PayloadXMLDocument.createElement("Rules");
//		RootElement.appendChild(RulesElement);

		for (SwapRuleContainer Rule : swapRuleCollection) {
			SingleRuleElement = PayloadXMLDocument.createElement("Rule");
			RootElement.appendChild(SingleRuleElement);

			RuleIDElement = PayloadXMLDocument.createElement("ID");
			RuleIDElement.appendChild(PayloadXMLDocument.createTextNode(Integer.toString(++iCounter)));
			SingleRuleElement.appendChild(RuleIDElement);

			SourceCharElement = PayloadXMLDocument.createElement("SourceChar");
			SourceCharElement.appendChild(PayloadXMLDocument.createTextNode(Rule.getSourceCharacter()));
			SingleRuleElement.appendChild(SourceCharElement);

			TargetCharElement = PayloadXMLDocument.createElement("TargetChar");
			TargetCharElement.appendChild(PayloadXMLDocument.createTextNode(Rule.getTargetCharacter()));
			SingleRuleElement.appendChild(TargetCharElement);
		}

		return "";
	}

	private static String CreateExploitaionElement(
			Collection<ExploitContainer> Exploits, Document PayloadXMLDocument, Element RootElement) {
		// TODO Auto-generated method stub
		Element ExploitationsElement = null;
		Element SingleExploitationElement = null;
		Element ExploitationIDElement = null;
		Element ExploitationTitleElement = null;
		Element ExploitationTargetTypeElement = null;
		Element ExploitationContextElement = null;
		Element ExploitationValueElement = null;
		Element ExploitationDescriptionElement = null;
		Element ExploitationCreditElement = null;
		Element ExploitationPlatformsElement = null;

//		ExploitationsElement = PayloadXMLDocument.createElement("ExploitationPayloads");
//		RootElement.appendChild(ExploitationsElement);

		for (ExploitContainer Exploit : Exploits) {

			SingleExploitationElement =  PayloadXMLDocument.createElement("Exploit");
			RootElement.appendChild(SingleExploitationElement);

			ExploitationIDElement =  PayloadXMLDocument.createElement("ID");
			ExploitationIDElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getPayloadId()));
			SingleExploitationElement.appendChild(ExploitationIDElement);

			ExploitationTitleElement =  PayloadXMLDocument.createElement("Title");
			ExploitationTitleElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getTitle()));
			SingleExploitationElement.appendChild(ExploitationTitleElement);

			ExploitationTargetTypeElement =  PayloadXMLDocument.createElement("TargetType");
			ExploitationTargetTypeElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getTargetType()));
			SingleExploitationElement.appendChild(ExploitationTargetTypeElement);

			ExploitationContextElement =  PayloadXMLDocument.createElement("Context");
			ExploitationContextElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getContext()));
			SingleExploitationElement.appendChild(ExploitationContextElement);

			ExploitationValueElement =  PayloadXMLDocument.createElement("ExploitValue");
			ExploitationValueElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getExploitValue()));
			SingleExploitationElement.appendChild(ExploitationValueElement);

			ExploitationDescriptionElement =  PayloadXMLDocument.createElement("Description");
			ExploitationDescriptionElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getExploitDescription()));
			SingleExploitationElement.appendChild(ExploitationDescriptionElement);

			ExploitationCreditElement =  PayloadXMLDocument.createElement("Credit");
			ExploitationCreditElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getAuthor()));
			SingleExploitationElement.appendChild(ExploitationCreditElement);

			ExploitationPlatformsElement =  PayloadXMLDocument.createElement("Platforms");
			ExploitationPlatformsElement.appendChild(PayloadXMLDocument.createTextNode(Exploit.getPlatforms()));
			SingleExploitationElement.appendChild(ExploitationPlatformsElement);
		}

		return "";
	}

	private static String CreatePayloadsElement(
			Collection<PayloadContainer> Payloads,
			Document PayloadXMLDocument, Element RootElement) {
		// TODO Auto-generated method stub
		Element PayloadsElement = null;
		Element SinglePayloadElement = null;
		Element PayloadIDElement = null;
		Element PayloadTitleElement = null;
		Element PayloadTargetTypeElement = null;
		Element PayloadContextElement = null;
		Element PayloadValueElement = null;
		Element PayloadDescriptionElement = null;
		Element PayloadCreditElement = null;
		Element PayloadPlatformsElement = null;

//		PayloadsElement = PayloadXMLDocument.createElement("DetectionPayloads");
//		RootElement.appendChild(PayloadsElement);

		for (PayloadContainer Payload : Payloads) {

			SinglePayloadElement =  PayloadXMLDocument.createElement("Payload");
			RootElement.appendChild(SinglePayloadElement);

			PayloadIDElement =  PayloadXMLDocument.createElement("ID");
			PayloadIDElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getPayloadId()));
			SinglePayloadElement.appendChild(PayloadIDElement);

			PayloadTitleElement =  PayloadXMLDocument.createElement("Title");
			PayloadTitleElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getTitle()));
			SinglePayloadElement.appendChild(PayloadTitleElement);

			PayloadTargetTypeElement =  PayloadXMLDocument.createElement("TargetType");
			PayloadTargetTypeElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getTargetType()));
			SinglePayloadElement.appendChild(PayloadTargetTypeElement);

			PayloadContextElement =  PayloadXMLDocument.createElement("Context");
			PayloadContextElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getContext()));
			SinglePayloadElement.appendChild(PayloadContextElement);

			PayloadValueElement =  PayloadXMLDocument.createElement("PayloadValue");
			PayloadValueElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getPayloadValue()));
			SinglePayloadElement.appendChild(PayloadValueElement);

			PayloadDescriptionElement =  PayloadXMLDocument.createElement("Description");
			PayloadDescriptionElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getPayloadDescription()));
			SinglePayloadElement.appendChild(PayloadDescriptionElement);

			PayloadCreditElement =  PayloadXMLDocument.createElement("Credit");
			PayloadCreditElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getAuthor()));
			SinglePayloadElement.appendChild(PayloadCreditElement);

			PayloadPlatformsElement =  PayloadXMLDocument.createElement("Platforms");
			PayloadPlatformsElement.appendChild(PayloadXMLDocument.createTextNode(Payload.getPlatforms()));
			SinglePayloadElement.appendChild(PayloadPlatformsElement);
		}



		return "";
	}

	private static String CreatePlatformsElement(
			Collection<PlatformContainer> values, Document PayloadXMLDocument, Element RootElement) {
		// TODO Auto-generated method stub
		Element PlatformsElement = null;
		Element PlatformAliasElement = null;
		Element PlatformIDElement = null;
		Element PlatformNameElement = null;
		Element PlatformVersionElement = null;

//		PlatformsElement = PayloadXMLDocument.createElement("Platforms");
//		RootElement.appendChild(PlatformsElement);

		for (PlatformContainer Platform : values) {

			PlatformAliasElement = PayloadXMLDocument.createElement(Platform.getPlatformAlias());
			RootElement.appendChild(PlatformAliasElement);

			PlatformIDElement = PayloadXMLDocument.createElement("ID");
			PlatformIDElement.appendChild(PayloadXMLDocument.createTextNode(Platform.getPlatformId()));
			PlatformAliasElement.appendChild(PlatformIDElement);

			PlatformNameElement = PayloadXMLDocument.createElement("PlatformName");
			PlatformNameElement.appendChild(PayloadXMLDocument.createTextNode(Platform.getPlatformName()));
			PlatformAliasElement.appendChild(PlatformNameElement);

			PlatformVersionElement = PayloadXMLDocument.createElement("PlatformVersion");
			PlatformVersionElement.appendChild(PayloadXMLDocument.createTextNode(Platform.getPlatformVersion()));
			PlatformAliasElement.appendChild(PlatformVersionElement);
		}

		return "";
	}


}

