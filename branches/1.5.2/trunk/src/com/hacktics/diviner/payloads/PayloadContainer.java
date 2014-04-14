/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;

/**
 * This class stores a single detection payload in the memory.
 * XML Entity Mapping:
 * &lt;Payload&gt;
 *   &lt;ID&gt;1&lt;/ID&gt;
 *   &lt;Title&gt;Title&lt;/Title&gt;
 *   &lt;TargetType&gt;String&lt;/TargetType&gt;
 *   &lt;Context&gt;WHERE&lt;/Context&gt;
 *   &lt;PayloadValue&gt;PayloadValue&lt;/PayloadValue&gt;
 *   &lt;Description&gt;PayloadDescription&lt;/Description&gt;
 *   &lt;Credit&gt;AuthorId&lt;/Credit&gt;
 *   &lt;Platforms&gt;CommaSeparatedPlatformsList&lt;/Platforms&gt;
 * &lt;/Payload&gt;
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class PayloadContainer implements Serializable {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = 3537558587484783312L;
    /**
     * A variable for storing the payload Identifier.
     */
    private String payloadId = null;
    /**
     * A variable for storing the payload title.
     */
    private String title = null;
    /**
     * A variable for storing the payload target type.
     */
    private String targetType = null;
    /**
     * A variable for storing the payload target context.
     */
    private String context = null;
    /**
     * A variable for storing the payload value.
     */
    private String payloadValue = null;
    /**
     * A variable for storing the payload description.
     */
    private String payloadDescription = null;
    /**
     * A variable for storing the payload's author/s identifier.
     */
    private String author = null;
    /**
     * A variable for storing the payload's platform association.
     */
    private String platforms = null;


	//****************
    //* CONSTRUCTORS *
    //****************
	/**
	 * @param payloadId
	 * @param title
	 * @param targetType
	 * @param context
	 * @param payloadValue
	 * @param payloadDescription
	 * @param author
	 * @param platforms
	 */
	public PayloadContainer(String payloadId, String title, String targetType,
			String context, String payloadValue, String payloadDescription,
			String author, String platforms) {
		super();
		this.payloadId = payloadId;
		this.title = title;
		this.targetType = targetType;
		this.context = context;
		this.payloadValue = payloadValue;
		this.payloadDescription = payloadDescription;
		this.author = author;
		this.platforms = platforms;
	}


    //***********
    //* METHODS *
    //***********

	/**
	 * @return the payloadId
	 */
	public String getPayloadId() {
		return payloadId;
	}


	/**
	 * @param payloadId the payloadId to set
	 */
	public void setPayloadId(String payloadId) {
		this.payloadId = payloadId;
	}


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
	 * @return the payloadValue
	 */
	public String getPayloadValue() {
		return payloadValue;
	}


	/**
	 * @param payloadValue the payloadValue to set
	 */
	public void setPayloadValue(String payloadValue) {
		this.payloadValue = payloadValue;
	}


	/**
	 * @return the payloadDescription
	 */
	public String getPayloadDescription() {
		return payloadDescription;
	}


	/**
	 * @param payloadDescription the payloadDescription to set
	 */
	public void setPayloadDescription(String payloadDescription) {
		this.payloadDescription = payloadDescription;
	}


	/**
	 * @return the author/s
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
