package com.hacktics.diviner.payloads;

public class AuthorContainer {

	private int authorId;
	private String authorName;
	private String authorLinkedIn;
	private String authorTwitter;

	public AuthorContainer(int authorId, String authorName,
			String authorLinkedIn, String authorTwitter) {
		super();
		this.authorId = authorId;
		this.authorName = authorName;
		this.authorLinkedIn = authorLinkedIn;
		this.authorTwitter = authorTwitter;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorLinkedIn() {
		return authorLinkedIn;
	}

	public void setAuthorLinkedIn(String authorLinkedIn) {
		this.authorLinkedIn = authorLinkedIn;
	}

	public String getAuthorTwitter() {
		return authorTwitter;
	}

	public void setAuthorTwitter(String authorTwitter) {
		this.authorTwitter = authorTwitter;
	}
	
	
}
