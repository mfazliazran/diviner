/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class stores an attack vector configuration in the memory.
 * It contains a collection of detection payloads,
 * exploitation payloads, delimiter swap rules and evasion rules,
 * each associated to specific platforms, and credited to a specific
 * author.
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class AttackVectorContainer implements Serializable {

	// **********************
	// * FIELD DECLERATIONS *
	// **********************
	/**
	 * Unique class identifier.
	 */
	private static final long serialVersionUID = 8044438376016908541L;
	/**
	 * A variable for storing the attack vector name.
	 */
	private String attackName = null;
	/**
	 * A variable for storing the attack vector title (short name).
	 */
	private String attackTitle = null;
	/**
	 * A variable for storing the attack vector template.
	 */
	private String template = null;
	/**
	 * A variable for storing the attack vector description.
	 */
	private String description = null;
	/**
	 * A collection of attack specific platforms.
	 */
	HashMap<String, PlatformContainer> platforms = null;
	/**
	 * A collection of detection payloads.
	 */
	private HashMap<String, PayloadContainer> detectionPayloads = null;
	/**
	 * A collection of exploitation payloads.
	 */
	private HashMap<String, ExploitContainer> exploitationPayloads = null;
	/**
	 * A collection of delimiter swap rules.
	 */
	private HashMap<String, DelimiterSwapRuleContainer> delimiterSwapRules = null;
	/**
	 * A collection of delimiter evasion rules.
	 */
	private HashMap<String, EvasionRuleContainer> evasionRules = null;

	// ****************
	// * CONSTRUCTORS *
	// ****************
	/**
	 * @param attackTitle
	 * @param template
	 * @param description
	 * @param detectionPayloads
	 * @param exploitationPayloads
	 * @param delimiterSwapRules
	 * @param evasionRules
	 */
	public AttackVectorContainer(String attackName, String attackTitle,
			String description, String template,
			HashMap<String, PlatformContainer> platforms,
			HashMap<String, PayloadContainer> detectionPayloads,
			HashMap<String, ExploitContainer> exploitationPayloads,
			HashMap<String, DelimiterSwapRuleContainer> delimiterSwapRules,
			HashMap<String, EvasionRuleContainer> evasionRules) {
		super();
		this.attackName = attackName;
		this.attackTitle = attackTitle;
		this.description = description;
		this.template = template;
		
		if (platforms == null)
			this.platforms = new HashMap<String, PlatformContainer>();
		else {
			this.platforms =  platforms;
		}
		
		if (detectionPayloads == null)
			this.detectionPayloads = new HashMap<String, PayloadContainer>();
		else {
			this.detectionPayloads =  detectionPayloads;
		}
		
		if (exploitationPayloads == null)
			this.exploitationPayloads = new HashMap<String, ExploitContainer>();
		else {
			this.exploitationPayloads =  exploitationPayloads;
		}
		
		if (delimiterSwapRules == null)
			this.delimiterSwapRules = new HashMap<String, DelimiterSwapRuleContainer>();
		else {
			this.delimiterSwapRules =  delimiterSwapRules;
		}
		
		if (evasionRules == null)
			this.evasionRules = new HashMap<String, EvasionRuleContainer>();
		else {
			this.evasionRules =  evasionRules;
		}
	}

	// ***********
	// * METHODS *
	// ***********
	/**
	 * @return the attackName
	 */
	public String getAttackName() {
		return attackName;
	}

	/**
	 * @param attackName the attackName to set
	 */
	public void setAttackName(String attackName) {
		this.attackName = attackName;
	}

	/**
	 * @return the attackTitle
	 */
	public String getAttackTitle() {
		return attackTitle;
	}

	/**
	 * @param attackTitle
	 *            the attackTitle to set
	 */
	public void setAttackTitle(String attackTitle) {
		this.attackTitle = attackTitle;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the platforms
	 */
	public HashMap<String, PlatformContainer> getPlatforms() {
		return platforms;
	}

	/**
	 * @param platforms the platforms to set
	 */
	public void setPlatforms(
			HashMap<String, PlatformContainer> platforms) {
		this.platforms = platforms;
	}

	/**
	 * @param platform the platform to add
	 * @param id the platform unique identifier
	 */
	public void addPlatform(PlatformContainer platform, String id) {
		platforms.put(id, platform);
	}


	//Get the max id of platforms (for adding new platforms)
	public int getMaxPlatformId() {
		int max = 0;
		int convertedId = 0;
		for (String id : platforms.keySet()) {
			try {
				convertedId = Integer.parseInt(id);
				if (max < convertedId) {
					max = convertedId;
				}
			}
			catch (Exception e) {
				//Some platforms will cannot be parsed to int (General/ ALL...)
			}
		}
		return max;
	}
	
	public int getMaxDetectionId() {
		int max = 0;
		int convertedId = 0;
		for (String id : detectionPayloads.keySet()) {
			try {
				convertedId = Integer.parseInt(id);
				if (max < convertedId) {
					max = convertedId;
				}
			}
			catch (Exception e) {
			}
		}
		return max;
	}
	
	public int getMaxExploitId() {
		int max = 0;
		int convertedId = 0;
		for (String id : exploitationPayloads.keySet()) {
			try {
				convertedId = Integer.parseInt(id);
				if (max < convertedId) {
					max = convertedId;
				}
			}
			catch (Exception e) {
			}
		}
		return max;
	}
	
	public int getMaxEvasionId() {
		int max = 0;
		int convertedId = 0;
		for (String id : evasionRules.keySet()) {
			try {
				convertedId = Integer.parseInt(id);
				if (max < convertedId) {
					max = convertedId;
				}
			}
			catch (Exception e) {
			}
		}
		return max;
	}
	
	public int getMaxDelimiterId() {
		int max = 0;
		int convertedId = 0;
		for (String id : delimiterSwapRules.keySet()) {
			try {
				convertedId = Integer.parseInt(id);
				if (max < convertedId) {
					max = convertedId;
				}
			}
			catch (Exception e) {
			}
		}
		return max;
	}
	
	/**
	 * @return the platform
	 */
	public PlatformContainer getPlatform(String id) {
		return platforms.get(id);
	}


	/**
	 * @return the detectionPayloads
	 */
	public HashMap<String, PayloadContainer> getDetectionPayloads() {
		return detectionPayloads;
	}

	/**
	 * @param detectionPayloads
	 *            the detectionPayloads to set
	 */
	public void setDetectionPayloads(
			HashMap<String, PayloadContainer> detectionPayloads) {
		this.detectionPayloads = detectionPayloads;
	}

	/**
	 * @param payload
	 *            the detection payload to add
	 * @param id
	 *            the detection payload unique identifier
	 */
	public void addDetectionPayload(PayloadContainer payload, String id) {
		detectionPayloads.put(id, payload);
	}

	/**
	 * @return the detection payload
	 */
	public PayloadContainer getDetectionPayload(String id) {
		return detectionPayloads.get(id);
	}

	/**
	 * @return the exploitationPayloads
	 */
	public HashMap<String, ExploitContainer> getExploitationPayloads() {
		return exploitationPayloads;
	}

	/**
	 * @param exploitationPayloads
	 *            the exploitationPayloads to set
	 */
	public void setExploitationPayloads(
			HashMap<String, ExploitContainer> exploitationPayloads) {
		this.exploitationPayloads = exploitationPayloads;
	}

	/**
	 * @param payload
	 *            the exploitation payload to add
	 * @param id
	 *            the exploitation payload unique identifier
	 */
	public void addExploitationPayload(ExploitContainer payload, String id) {
		exploitationPayloads.put(id, payload);
	}

	/**
	 * @return the exploitation payload
	 */
	public ExploitContainer getExploitationPayload(String id) {
		return exploitationPayloads.get(id);
	}

	/**
	 * @return the delimiterSwapRules
	 */
	public HashMap<String, DelimiterSwapRuleContainer> getDelimiterSwapRules() {
		return delimiterSwapRules;
	}

	/**
	 * @param delimiterSwapRules
	 *            the delimiterSwapRules to set
	 */
	public void setDelimiterSwapRules(
			HashMap<String, DelimiterSwapRuleContainer> delimiterSwapRules) {
		this.delimiterSwapRules = delimiterSwapRules;
	}

	/**
	 * @param rule the delimiter swap rule to add
	 * @param id the delimiter swap rule unique identifier
	 */
	public void addDelimiterSwapRule(DelimiterSwapRuleContainer rule, String id) {
		delimiterSwapRules.put(id, rule);
	}

	/**
	 * @return the delimiter swap rule
	 */
	public DelimiterSwapRuleContainer getDelimiterSwapRule(String id) {
		return delimiterSwapRules.get(id);
	}

	/**
	 * @return the evasionRules
	 */
	public HashMap<String, EvasionRuleContainer> getEvasionRules() {
		return evasionRules;
	}

	/**
	 * @param evasionRules the evasionRules to set
	 */
	public void setEvasionRules(
			HashMap<String, EvasionRuleContainer> evasionRules) {
		this.evasionRules = evasionRules;
	}

	/**
	 * @param rule the evasion rule to add
	 * @param id the evasion rule unique identifier
	 */
	public void addEvasionRule(EvasionRuleContainer rule, String id) {
		evasionRules.put(id, rule);
	}

	/**
	 * @return the evasion rule
	 */
	public EvasionRuleContainer getEvasionRule(String id) {
		return evasionRules.get(id);
	}

} // end of class
