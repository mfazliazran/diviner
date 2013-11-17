/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class stores the payload database in the memory.
 * It contains the vendor information, as well as a collection of authors
 * and attack vector configurations.
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class PayloadDatabaseContainer implements Serializable {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = -5387028307491412916L;
    /**
     * An object storing the details of the payload database vendor.
     */
    private VendorContainer vendor = null;
    /**
     * A collection of payload authors.
     */
    private HashMap<String,PayloadAuthorContainer> authors = null;
    /**
     * A collection of detection payloads.
     */
    private HashMap<String,AttackVectorContainer> attackVectors = null;


    //****************
    //* CONSTRUCTORS *
    //****************
	/**
	 * @param vendor
	 * @param logo
	 * @param description
	 * @param website
	 * @param authors
	 * @param attackVectors
	 */
	public PayloadDatabaseContainer(VendorContainer vendor,
			HashMap<String, PayloadAuthorContainer> authors,
			HashMap<String, AttackVectorContainer> attackVectors) {
		super();
		this.vendor = vendor;
		this.authors = authors;
		this.attackVectors = attackVectors;
	}


	//***********
    //* METHODS *
    //***********

	/**
	 * @return the vendor
	 */
	public VendorContainer getVendor() {
		return vendor;
	}


	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(VendorContainer vendor) {
		this.vendor = vendor;
	}


	/**
	 * @return the authors
	 */
	public HashMap<String, PayloadAuthorContainer> getAuthors() {
		return authors;
	}


	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(HashMap<String, PayloadAuthorContainer> authors) {
		this.authors = authors;
	}


	/**
	 * @param author the author to add
	 * @param id the author unique identifier
	 */
	public void addAuthor(
			PayloadAuthorContainer author, String id) {
		authors.put(id,author);
	}


	/**
	 * @return the author object
	 */
	public PayloadAuthorContainer getAuthor(String id) {
		return authors.get(id);
	}


	/**
	 * @return the attackVectors
	 */
	public HashMap<String, AttackVectorContainer> getAttackVectors() {
		return attackVectors;
	}

	
	/**
	 * @param attackVectors the attackVectors to set
	 */
	public void setAttackVectors(
			HashMap<String, AttackVectorContainer> attackVectors) {
		this.attackVectors = attackVectors;
	}

	/**
	 * @param av the attack vector to add
	 * @param id the attack vector unique identifier
	 */
	public void addAttackVector(
			AttackVectorContainer av, String id) {
		attackVectors.put(id,av);
	}


	/**
	 * @return the attack vector
	 */
	public AttackVectorContainer getAttackVector(String id) {
		return attackVectors.get(id);
	}
	
	


} //end of class
