/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;

/**
 * This class stores information on a single platform in the memory.
 * XML Entity Mapping:
 * &lt;PlatformTitle&gt;
 *   &lt;ID&gt;PlatformID&lt;/ID&gt;
 *   &lt;PlatformName&gt;PlatformName&lt;/PlatformName&gt;
 *   &lt;PlatformVersion&gt;PlatformVersion&lt;/PlatformVersion&gt;
 * &lt;/PlatformTitle&gt;
 *
 * @author Shay Chen
 * @since 1.0
 */

public class PlatformContainer implements Serializable  {

	//**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
	private static final long serialVersionUID = -4991058027658501791L;
	/**
     * A variable for storing the platform relative identifier.
     */
	private String platformAlias = null;
    /**
     * A variable for storing the platform Alias.
     */
    private String platformId = null;
	/**
     * A variable for storing the platform name.
     */
    private String platformName = null;
    /**
     * A variable for storing the platform version.
     */
    private String platformVersion = null;


    //****************
    //* CONSTRUCTORS *
    //****************
    /**
     * @param platformId
	 * @param platformName
	 * @param platformVersion
	 */
	public PlatformContainer(String platformAlias,String platformId, String platformName,
			String platformVersion) {
		super();
		this.platformAlias = platformAlias;
		this.platformId = platformId;
		this.platformName = platformName;
		this.platformVersion = platformVersion;
	}


    //***********
    //* METHODS *
    //***********
	/**
	 * @return the platformAlias
	 */
	public String getPlatformAlias() {
		return platformAlias;
	}
	
	/**
	 * @param platformAlias the platformAlias to set
	 */
	public void setPlatformAlias(String platformAlias) {
		this.platformAlias = platformAlias;
	}
	
	/**
	 * @return the platformId
	 */
	public String getPlatformId() {
		return platformId;
	}


	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}


	/**
	 * @return the platformName
	 */
	public String getPlatformName() {
		return platformName;
	}


	/**
	 * @param platformName the platformName to set
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}


	/**
	 * @return the platformVersion
	 */
	public String getPlatformVersion() {
		return platformVersion;
	}


	/**
	 * @param platformVersion the platformVersion to set
	 */
	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

} //end of class
