/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;

/**
 * This class stores a single swap rule in the memory.
 * XML Entity Mapping:
 * &lt;SourceChar&gt;SourceChar&lt;/SourceChar&gt;
 * &lt;TargetChar&gt;TargetChar&lt;/TargetChar&gt;
 *
 * @author Shay Chen
 * @since 1.0
 */
public class SwapRuleContainer implements Serializable  {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = 2212291333091044098L;
    /**
     * A variable for storing the source character - the character to use.
     */
    private String sourceCharacter = null;
    /**
     * A variable for storing the source character - the character to replace.
     */
    private String targetCharacter = null;


    //****************
    //* CONSTRUCTORS *
    //****************
    /**
     * SwapRule Class Constructor.
     * @param newSourceCharacter The character to use.
     * @param newTargetCharacter The character to replace.
     * @since 1.0
     */
    public SwapRuleContainer(final String newSourceCharacter,
            final String newTargetCharacter) {
        this.sourceCharacter = newSourceCharacter;
        this.targetCharacter = newTargetCharacter;
    } //end of default constructor


    //***********
    //* METHODS *
    //***********
	/**
	 * @return the sourceCharacter
	 */
	public String getSourceCharacter() {
		return sourceCharacter;
	}


	/**
	 * @param sourceCharacter the sourceCharacter to set
	 */
	public void setSourceCharacter(String sourceCharacter) {
		this.sourceCharacter = sourceCharacter;
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
