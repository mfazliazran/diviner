/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores a collection of rules for swapping delimiters.
 * XML Entity Mapping:
 * &lt;ID&gt;1&lt;/ID&gt;
 * &lt;Title&gt;Title&lt;/Title&gt;
 * &lt;TargetType&gt;String&lt;/TargetType&gt;
 * &lt;Context&gt;WHERE&lt;/Context&gt;
 * &lt;Rules&gt;
 *     &lt;Rule1&gt;
 *         &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 *         &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *     &lt;/Rule1&gt;
 *     &lt;Rule2&gt;
 *         &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 *         &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *     &lt;/Rule2&gt;
 * &lt;/Rules&gt;
 * &lt;Description&gt;Description&lt;/Description&gt;
 * &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 * &lt;Platforms&gt;Platforms&lt;/Platforms&gt;
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class DelimiterSwapRuleContainer implements Serializable {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = 7597449857180690759L;
    /**
     * A variable for storing the rule Identifier.
     */
    private String ruleId = null;
    /**
     * A variable for storing the title.
     */
    private String title = null;
    /**
     * A variable for storing the rule target type.
     */
    private String targetType = null;
    /**
     * A variable for storing the rule target context.
     */
    private String context = null;
    /**
     * A variable for storing the swap rule collection.
     */
    private ArrayList<SwapRuleContainer> swapRuleCollection = null;
    /**
     * A variable for storing the description.
     */
    private String description = null;
    /**
     * A variable for storing the author identifier.
     */
    private String author = null;
    /**
     * A variable for storing the swap rule platform association.
     */
    private String platforms = null;


    //****************
    //* CONSTRUCTORS *
    //****************
	/**
	 * @param ruleId
	 * @param title
	 * @param targetType
	 * @param context
	 * @param swapRuleCollection
	 * @param description
	 * @param author
	 * @param platforms
	 */
	public DelimiterSwapRuleContainer(String ruleId, String title,
			String targetType, String context,
			ArrayList<SwapRuleContainer> swapRuleCollection,
			String description, String author, String platforms) {
		super();
		this.ruleId = ruleId;
		this.title = title;
		this.targetType = targetType;
		this.context = context;
		this.swapRuleCollection = swapRuleCollection;
		this.description = description;
		this.author = author;
		this.platforms = platforms;
	}


    //***********
    //* METHODS *
    //***********
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the swapRuleCollection
	 */
	public ArrayList<SwapRuleContainer> getSwapRuleCollection() {
		return swapRuleCollection;
	}


	/**
	 * @param swapRuleCollection the swapRuleCollection to set
	 */
	public void setSwapRuleCollection(
            ArrayList<SwapRuleContainer> swapRuleCollection) {
		this.swapRuleCollection = swapRuleCollection;
	}


	/**
	 * This method adds a swap rule to the collection.
	 * @param rule A new swap rule.
	 */
	public void addSwapRule(SwapRuleContainer rule) {
		swapRuleCollection.add(rule);
	}


	/**
	 * This method returns a single swap rule from the collection.
	 * @param index The index of the swap rule.
	 */
	public void getSwapRule(int index) {
		swapRuleCollection.get(index);
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}


	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}


	/**
	 * @return the ruleId
	 */
	public String getRuleId() {
		return ruleId;
	}


	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}


	/**
	 * @return the targetType
	 */
	public String getTargetType() {
		return targetType;
	}


	/**
	 * @param targetType the targetType to set
	 */
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}


	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}


	/**
	 * @return the platforms
	 */
	public String getPlatforms() {
		return platforms;
	}


	/**
	 * @param platforms the platforms to set
	 */
	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}

} //end of class
