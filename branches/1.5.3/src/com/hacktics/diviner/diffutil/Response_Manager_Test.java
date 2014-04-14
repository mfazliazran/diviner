/**
 * 
 */
package com.hacktics.diviner.diffutil;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * @author alex.mor
 *
 */
public class Response_Manager_Test extends TestCase {

	private String new_response = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login_another.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"ADBCHDAKUADSALCE\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
	private String response1 = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"JSAFIOACOIEQUHFAQ\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
	private String response2 = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login_another.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"IEPIALHFPOIAFJFAI\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
	private Response_Manager ResponseManager;
	
	protected void setUp() {
		ArrayList<String> newArray = new ArrayList<String>();

		//add identical response
		newArray.add(this.new_response);
		
		newArray.add(this.response1);
		newArray.add(this.response2);
		
	    // Create an instance of the Response_Match object.  	
		this.ResponseManager = new Response_Manager(this.new_response,newArray);
	}
	
	public void testResponse_Match_BASE() {
		//0 differences
	    assertEquals("Response_Match: Null case (identical responses).", 0,ResponseManager.getResponses(0).getDiffs().size());
	    //100% similarity
	    assertEquals("Response_Match: 100% Similarity (identical responses).", 100,ResponseManager.getResponses(0).getSimilarity());
	    //print sanitized response (the same in this case) - without tags
	    new_response = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login_another.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"ADBCHDAKUADSALCE\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
	    assertEquals("Response_Match: Sanitized (identical responses).", new_response,ResponseManager.getResponses(0).getSanitizedResponse());
	}
	
	public void testResponse_Match_3Responses() {
		//1 difference - response 1
		assertEquals("Response_Match: 1 Difference, reponse1.", 2,ResponseManager.getResponses(1).getDiffs().size());
		assertEquals("Response_Match: 1 Difference, reponse1 - Text Between", "_another",ResponseManager.getResponses(1).getDiffs(0).originalValue);
		assertEquals("Response_Match: 1 Difference, reponse1 - Text Between", "JSAFIOACOIEQUHFAQ",ResponseManager.getResponses(1).getDiffs(1).originalValue);
	    
	    //1 difference - response 2
	    assertEquals("Response_Match: 1 Difference, reponse2.", 1,ResponseManager.getResponses(2).getDiffs().size());
	    assertEquals("Response_Match: 1 Difference, reponse2 - Text Between", "IEPIALHFPOIAFJFAI",ResponseManager.getResponses(2).getDiffs(0).originalValue);
	    	    
	}
	
	public void testResponse_Match_RemoveUnRelevantTags() {
		//Remove VIEWSTATE - new_response
		new_response = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login_another.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"ADBCHDAKUADSALCE\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		assertEquals("Response_Match: 1 Difference, base_reponse", new_response,Response_Manager.getSanitizedNew_response());
	    
		//Remove VIEWSTATE - response1
		response1 = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"JSAFIOACOIEQUHFAQ\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		assertEquals("Response_Match: 1 Difference, reponse1.", response1,ResponseManager.getResponses(1).getResponse());
	    
		//Remove VIEWSTATE - response1
		response2 = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login_another.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"IEPIALHFPOIAFJFAI\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		assertEquals("Response_Match: 1 Difference, reponse2.", response2,ResponseManager.getResponses(2).getResponse());
	    
	}
}
