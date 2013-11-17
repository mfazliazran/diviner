/**
 * 
 */
package com.hacktics.diviner.diffutil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
/**
 * @author alex.mor
 *
 */
public class Response_Manager {
	/*
	 * Base response to compare with
	 */
	private static String new_response;
	public static String getSanitizedNew_response() {
		return new_response;
	}
	public static void setNew_response(String new_response) {
		Response_Manager.new_response = new_response;
	}
	
	private List<String> removed_tag_list = null;

	public List<String> getRemoved_tag_list() {
		return removed_tag_list;
	}

	/*
	 * The list of responses to process
	 */
	private List<Response_Diffs> responses;
	/*
	 * getter and setter for responses
	 */
	public List<Response_Diffs> getResponses() {
		return responses;
	}
	public Response_Diffs getResponses(int index) {
		return responses.get(index);
	}

	
	
	public Response_Manager(String base_response,ArrayList<String> responses,List<UnRelevantTag> tagList) {
		
		//initialize Responses list
		this.responses = new ArrayList<Response_Diffs>();
		this.removed_tag_list = new ArrayList<String>();
		
		//set base response, after removing irrelevant tags
		Response_Manager.new_response = UnRelevantInfo.removeUnRelevantData(base_response, tagList,this.removed_tag_list);	
        
		try {
			//check if there are any responses at all
	        if (responses.size()>0) {
	        		//future thread-safe
	        		synchronized (responses) {
	        			ListIterator<String> itr = responses.listIterator();
	    	        	int i = 0;	
	    	        	while (itr.hasNext()) {
	    	        		//add current response to the list, remove tag list and find differences
	    	        		this.responses.add(new Response_Diffs(i++,(String)itr.next(),tagList));     
					}
	        	}
	        }
		}
		catch (Exception e) {
			System.out.print(e);
		}
		
	}
	
	/*
	 * Constructor with removable tags
	 */
	public Response_Manager(String base_response,ArrayList<String> responses) {	

		//call main constructor, with empty tag list
		this(base_response,responses,new ArrayList<UnRelevantTag>());
	}
	
}


