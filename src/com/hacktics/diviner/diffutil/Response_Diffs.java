/**
 * 
 */
package com.hacktics.diviner.diffutil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
//import org.apache.commons.codec.binary.Base64;


/**
 * @author alex.mor
 * The class represents all differences for a single response.
 */

public class Response_Diffs {
	/*
	 * A list of all differences between the base request and the current
	 */
	private ArrayList<Diff> diffs;
	/*
	 * The response number associated with this response.
	 */
	private int responseNumber;
	/*
	 * Current response
	 */
	private String response;
	/*
	 * Current original response (don't remove tags)
	 */
	private String original_response;
	
	public String getOriginal_response() {
		return original_response;
	}

	/*
	 * Resemblance between accounts
	 */
	private int similarity;
	/*
	 * List to hold irrelevant tags  
	 */
	private List<String> removed_tags;
	
	public List<String> getRemoved_tags() {
		return removed_tags;
	}

	public int getSimilarity() {
		return similarity;
	}

	public void setSimilarity(int similarity) {
		this.similarity = similarity;
	}

	public List<Diff> getDiffs() {
		return diffs;
	}
	
	public Diff getDiffs(int index) {
		return diffs.get(index);
	}

	public int getResponseNumber() {
		return responseNumber;
	}

	public void setResponseNumber(int responseNumber) {
		this.responseNumber = responseNumber;
	}

	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	/*
	 * diff_match_patch operations representing the difference
	 */
	private diff_match_patch.Operation DELETE = diff_match_patch.Operation.DELETE;
	private diff_match_patch.Operation EQUAL = diff_match_patch.Operation.EQUAL;
	private diff_match_patch.Operation INSERT = diff_match_patch.Operation.INSERT;
	
	public enum BetweenType {
		    DELETED, INSERTED, EQUAL
	}
		
	public Response_Diffs(int responseNumber,String response,List<UnRelevantTag> tagList){
		this.removed_tags = new ArrayList<String>();
		
		this.responseNumber = responseNumber;
		this.original_response = response;
		this.response = response;
		
		//remove tags
		if (tagList!=null)
		this.response = UnRelevantInfo.removeUnRelevantData(response, tagList,this.removed_tags);

		//initialize the list of differences
		this.diffs = new ArrayList<Diff>();
		
		//call main method
		this.build_response_diffs();
	}
	
	
	private void build_response_diffs() {
		// Create an instance of the diff_match_patch object.
        diff_match_patch dmp = new diff_match_patch();
        
        //Perform diff operations
        LinkedList<diff_match_patch.Diff> dmp_diffs = dmp.diff_main(Response_Manager.getSanitizedNew_response(), this.response,false);
        
        //very important - combines deletes and inserts together
        dmp.diff_cleanupSemantic(dmp_diffs);
       
        //DEBUG - Print differences from diff_match_patch class
        //System.out.print(dmp_diffs+"\r\n");
        
        //check if there are any differences at all
        if (dmp_diffs.size()>1) {
        	
        	//start main diff generation
        	try {
	        	//prevent access to diffs object (for future multi-therading)
	        	synchronized(this.diffs) {       	
		        	ListIterator<diff_match_patch.Diff> itr = dmp_diffs.listIterator();
		        	
		        	//main diff manager, many cases to consider
		        	while (itr.hasNext()) {
		        		Diff newResponseDiff = new Diff();
		        		diff_match_patch.Diff currDiff = itr.next();
		        		
		        		/*
		        		 * Diff always starts with DELETE or INSERT or EQUAL and then comes one after another, never two in a row
		        		 * DELETE will never come after INSERT
		        		 */
		        		if (currDiff.operation == EQUAL) { //chain is EQUAL
		        			newResponseDiff.start = currDiff.text;
		        			
		        			//this should always be true (when only one EQUAL diff exists, the diff array is null) 
		        			if (itr.hasNext()) {
		        				currDiff = itr.next();
		        				if (currDiff.operation == DELETE) { //chain is EQUAL-DELETE
		        					if (itr.hasNext()) {
		        						diff_match_patch.Diff nextDiff = itr.next();
		        						
			        					if (nextDiff.operation == EQUAL) { //chain is EQUAL-DELETE-EQUAL
			        						newResponseDiff.between = currDiff.text;
			        						newResponseDiff.betweenType = BetweenType.DELETED;
			        						newResponseDiff.end = nextDiff.text;
			        						this.diffs.add(newResponseDiff);
			        						
			        						//move back one - for the next diff, remember the previous equal
			        						if (itr.hasNext())
			        							nextDiff = itr.previous();
				        				}
			        					else if (nextDiff.operation == INSERT) { //chain is EQUAL-DELETE-INSERT-EQUAL (there already is a place for this)
			        						newResponseDiff.originalValue = currDiff.text;
			        						currDiff = nextDiff;
			        						
			        						
			        					}
		        					}
		        					else { //chain is EQUAL-DELETE-null
		        						newResponseDiff.between = currDiff.text;
		        						newResponseDiff.betweenType = BetweenType.DELETED;
		        						newResponseDiff.end = null;
		        						this.diffs.add(newResponseDiff);
		        					}
		        					
		        				}
		        				if (currDiff.operation == INSERT) { //chain is EQUAL-DELETE/INSERT
		        					if (itr.hasNext()) {
		        						
		        						diff_match_patch.Diff nextDiff = itr.next();
			        					if (nextDiff.operation == EQUAL) { //chain is EQUAL-INSERT/DELETE-EQUAL
			        						newResponseDiff.between = currDiff.text;
			        						newResponseDiff.betweenType = BetweenType.INSERTED;
			        						newResponseDiff.end = nextDiff.text;
			        						this.diffs.add(newResponseDiff);
			        						
			        						//move back one - for the next diff, remember the previous equal
			        						if (itr.hasNext())
			        						nextDiff = itr.previous();
				        				}
		        					}
		        					else { //chain is EQUAL-INSERT-null
		        						newResponseDiff.between = currDiff.text;
		        						newResponseDiff.betweenType = BetweenType.INSERTED;
		        						newResponseDiff.end = null;
		        						this.diffs.add(newResponseDiff);
		        					}
		        				}
		        			}	
		        		}
		        		else if (currDiff.operation == INSERT) { //chain is null-INSERT
		        			newResponseDiff.start = null;
		        			if (itr.hasNext()) {
		        				diff_match_patch.Diff nextDiff = itr.next();
		    					if (nextDiff.operation == EQUAL) { //chain is null-INSERT-EQUAL
		    						newResponseDiff.between = currDiff.text;
		    						newResponseDiff.betweenType = BetweenType.INSERTED;
		    						newResponseDiff.end = nextDiff.text;
		    						this.diffs.add(newResponseDiff);
		    						
		    						//move back one - for the next diff, remember the previous equal
		    						if (itr.hasNext())
		    						nextDiff = itr.previous();
		        				}
		        			}
		        		}
		        		else if (currDiff.operation == DELETE) { //chain is null-DELETE
		        			newResponseDiff.start = null;
		        			if (itr.hasNext()) {
		        				diff_match_patch.Diff nextDiff = itr.next();
	        					if (nextDiff.operation == EQUAL) { //chain is null-DELETE-EQUAL
	        						newResponseDiff.between = currDiff.text;
	        						newResponseDiff.betweenType = BetweenType.INSERTED;
	        						newResponseDiff.end = nextDiff.text;
	        						this.diffs.add(newResponseDiff);
	        						
	        						//move back one - for the next diff, remember the previous equal
	        						if (itr.hasNext())
	        						nextDiff = itr.previous();
		        				}
	    					}
		        		}
		        	}
	        	}
	        }
        	catch (Exception e) {
        		System.out.print(e);
        	}
        }
        // Get the responses similarity
        this.similarity = GetResponsesSimilarity.getResponsesSimilarity(Response_Manager.getSanitizedNew_response(), this.response);
	}
	
	/*
	 * Return the response after all differences have been removed
	 */
	public String getSanitizedResponse() {
		if (this.similarity<100) {
			StringBuffer sb = new StringBuffer();
			for (Iterator<Diff> iterator = diffs.iterator(); iterator.hasNext();) {
				Diff diff = (Diff) iterator.next();
				sb.append(diff.start);
				if (!iterator.hasNext()) 
					sb.append(diff.end);
			}
			
			return sb.toString();
		}
		else
			return this.response;
	}
	
	/*
	 * 
	 */
	public String getSelectedDiffsResponse(int[] selected) {
		StringBuffer sb = new StringBuffer();
		if (selected.length>0) {
			//print the original response - all differences selected
			if (selected.length==this.diffs.size()) {
				return this.response;
			}
			//print only selected
			else {
				int j=0;
				for (int i = 0; i < this.diffs.size(); i++) {
					sb.append(this.diffs.get(i).start);
					if (i==selected[j]) {
						sb.append(this.diffs.get(selected[j]).between);
						if (j<(selected.length-1))
							j++;
						
						
					}
					//do not append equal start and end of differences
					if (i<(this.diffs.size()-1)) {
						if (this.diffs.get(i).end != this.diffs.get(i+1).start)
							sb.append(this.diffs.get(i).end);
					}
				}
				sb.append(this.diffs.get(this.diffs.size()-1).end);
				return sb.toString();
			}
			
		}
		else
			return getSanitizedResponse();
		
	}
	/*
	 * An array of all differences as strings
	 */
	public List<String> getAllDiffs() {
		List<String> dl = new ArrayList<String>();
		Iterator<Diff> itr = this.diffs.iterator();
		while (itr.hasNext()) {
			dl.add(itr.next().between);
		}
		return dl;
	}


	public static class Diff {
		public String start = "";
		public String between = "";
		public BetweenType betweenType;
		public String originalValue = ""; //if there was a delete before an insert
		public String end = "";
		private ObjectType objectType = ObjectType.OTHER;
		public ObjectType getObjectType() {
			
			if (this.objectType == ObjectType.OTHER) {
				//check if this is a number
				try {
					Integer.parseInt(this.between);
					return ObjectType.INTEGER;
				}
				catch (NumberFormatException nfe){
					//this was not a number, maybe this is base64
					try {
						//TODO: decode base64
						//Base64.decodeBase64(this.between);
						//return ObjectType.BASE64;
						return ObjectType.STRING;
					}
					catch (Exception e){
						//that was not base64... just leave it a string
						return ObjectType.STRING;
					}
				}
			}
			return objectType;
		}

		public String objectRange= "";
		public enum ObjectType {
			INTEGER,STRING,BASE64,OTHER;
		}
		/**
	     * Display a human-readable version of this Diff.
	     * @return text version.
	     */
	    public String toString() {
	      return this.start+this.between+this.end;
	    }
		
	}
	
	
}
