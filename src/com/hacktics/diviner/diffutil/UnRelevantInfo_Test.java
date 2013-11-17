package com.hacktics.diviner.diffutil;

import java.util.ArrayList;

import junit.framework.TestCase;

public class UnRelevantInfo_Test extends TestCase {
	
	private String new_response = "<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login_another.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"ADBCHDAKUADSALCE\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
	protected void setUp() throws Exception {

		super.setUp();
	}

	public void testCropHeaders() {
		fail("Not yet implemented");
	}

	public void testRemoveUnRelevantDataArrayListOfStringArrayListOfUnRelevantTag() {
		ArrayList<UnRelevantTag> tagList = new ArrayList<UnRelevantTag>();
		UnRelevantTag tag1 = new UnRelevantTag("<input", "__VIEWSTATE", "/>");
		tagList.add(tag1);
		
		UnRelevantInfo.removeUnRelevantData(this.new_response, tagList,null);
	}

	public void testRemoveUnRelevantDataStringArrayListOfUnRelevantTag() {
		fail("Not yet implemented");
	}

}
