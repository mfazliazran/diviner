/**
 *
 */
package com.hacktics.diviner.payloads;

import java.io.Serializable;

/**
 * This class stores the details of a payload author.
 * XML Entity Mapping:
 * &lt;AuthorId&gt;AuthorId&lt;/AuthorId&gt;
 * &lt;FullName&gt;FullName&lt;/FullName&gt;
 * &lt;Twitter&gt;Twitter&lt;/Twitter&gt;
 * &lt;Linkedin&gt;Linkedin&lt;/Linkedin&gt;
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class PayloadAuthorContainer implements Serializable {

    //**********************
    //* FIELD DECLERATIONS *
    //**********************
    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = -2998763402085689428L;
    /**
     * A variable for storing the paylod author identifier.
     */
    private int authorId = 0;
    /**
     * A variable for storing the author name.
     */
    private String authorName = null;
    /**
     * A variable for storing the author's twitter account.
     */
    private String twitter = null;
    /**
     * A variable for storing the author's linkedin account.
     */
    private String linkedin = null;


    //****************
    //* CONSTRUCTORS *
    //****************
    /**
     * Payload Author Class Constructor.
     * @param authorId The author identifier.
     * @param authorName The author's name.
     * @param twitter The author's twitter account.
     * @param linkedin The author's linkedin account.
     * @since 1.0
     */
    public PayloadAuthorContainer(int authorId, String authorName,
            String twitter, String linkedin) {
        super();
        this.authorId = authorId;
        this.authorName = authorName;
        this.twitter = twitter;
        this.linkedin = linkedin;
    } //end of constructor


    //***********
    //* METHODS *
    //***********
	/**
	 * @return the authorId
	 */
	public int getAuthorId() {
		return authorId;
	}


	/**
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}


	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}


	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}


	/**
	 * @return the twitter
	 */
	public String getTwitter() {
		return twitter;
	}


	/**
	 * @param twitter the twitter to set
	 */
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}


	/**
	 * @return the linkedin
	 */
	public String getLinkedin() {
		return linkedin;
	}


	/**
	 * @param linkedin the linkedin to set
	 */
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

} //end of class
