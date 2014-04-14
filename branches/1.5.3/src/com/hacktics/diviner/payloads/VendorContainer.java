package com.hacktics.diviner.payloads;

import java.io.Serializable;

public class VendorContainer implements Serializable{

	//**********************
    //* FIELD DECLERATIONS *
    //**********************
	/**
     * Unique class identifier.
	 */
	private static final long serialVersionUID = 8518990898494848777L;
    /**
     * A variable for storing the name of the payload database vendor.
     */
    private String vendor = null;
    /**
     * A variable for storing the name of the vendor logo.
     */
    private String logo = null;
    /**
     * A variable for storing the vendor description.
     */
    private String description = null;
    /**
     * A variable for storing the name of the vendor website.
     */
    private String website = null;


    //****************
    //* CONSTRUCTORS *
    //****************
    /**
	 * @param vendor
	 * @param logo
	 * @param description
	 * @param website
	 */
	public VendorContainer(String vendor, String logo, String description,
			String website) {
		super();
		this.vendor = vendor;
		this.logo = logo;
		this.description = description;
		this.website = website;
	}


	//***********
    //* METHODS *
    //***********
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}


	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}


	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}


	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
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
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}


	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

} //end of class
