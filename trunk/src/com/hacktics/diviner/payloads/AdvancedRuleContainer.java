/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;

/**
 * This class stores a single advanced rule in the memory.
 * XML Entity Mapping:
 * &lt;SourceRegEx&gt;SourceRegEx&lt;/SourceRegEx&gt;
 * &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *
 * @author Shay Chen
 * @since 1.0
 */
public class AdvancedRuleContainer implements Serializable  {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = 3440890611806266105L;
    /**
     * A variable for storing the source character - the character to use.
     */
    private String sourceRegEx = null;
    /**
     * A variable for storing the source character - the character to replace.
     */
    private String targetCharacter = null;


    //****************
    //* CONSTRUCTORS *
    //****************
    /**
     * Advanced Rule Class Constructor.
     * @param newSourceRegEx The regular expression to use.
     * @param newTargetCharacter The character to replace.
     * @since 1.0
     */
    public AdvancedRuleContainer(final String newSourceRegEx,
            final String newTargetCharacter) {
        this.sourceRegEx = newSourceRegEx;
        this.targetCharacter = newTargetCharacter;
    } //end of default constructor


    //***********
    //* METHODS *
    //***********
	/**
	 * @return the sourceCharacter
	 */
	public String getSourceRegEx() {
		return sourceRegEx;
	}


	/**
	 * @param sourceCharacter the sourceCharacter to set
	 */
	public void setSourceRegEx(String sourceRegEx) {
		this.sourceRegEx = sourceRegEx;
	}


	/**
	 * @return the targetCharacter
	 */
	public String getTargetCharacter() {
		return targetCharacter;
	}


	/**
	 * @param targetCharacter the targetCharacter to set
	 */
	public void setTargetCharacter(String targetCharacter) {
		this.targetCharacter = targetCharacter;
	}

} //end of class
