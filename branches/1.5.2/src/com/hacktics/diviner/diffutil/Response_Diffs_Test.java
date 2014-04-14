package com.hacktics.diviner.diffutil;

import java.util.ArrayList;

import com.hacktics.diviner.diffutil.Response_Diffs.BetweenType;
import com.hacktics.diviner.diffutil.Response_Diffs.Diff;



import junit.framework.TestCase;

public class Response_Diffs_Test extends TestCase {
		//testing response strings
		private String new_response = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
        
		protected void setUp() {
		    // Create an instance of the Response_Diffs object.
			Response_Manager.setNew_response(this.new_response);
			
	        
		}
		
		public void testBuildResponseDiffsMain_BASE() {
			//add identical response
			Response_Diffs responseDiffs = new Response_Diffs(0,new_response,null);
		    assertEquals("build_response_diffs: Null case (identical responses).", 0,responseDiffs.getDiffs().size());
		     
		}
	 
		public void testBuildResponseDiffsMain_EQUAL_INSERT_EQUAL() {
			String response1 = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
			        
			//add a response with only one difference
			Response_Diffs responseDiffs = new Response_Diffs(1,response1,null);
		    assertEquals("build_response_diffs: One difference.", 1,responseDiffs.getDiffs().size());
		    
		    //check internal content of ResponseDiff
		    Diff responseDiff = new Diff();
		    responseDiff.start = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__";
		    responseDiff.originalValue = "CHANGE";
		    responseDiff.betweenType = BetweenType.INSERTED;
		    responseDiff.end = "TARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";

		    assertEquals("build_response_diffs: One difference.", responseDiff.toString(),responseDiffs.getDiffs(0).toString());
		   
		}
		
		public void testBuildResponseDiffsMain_EQUAL_INSERT_EQUAL_INSERT_EQUAL() {
			String response2 = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET24121\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
	        
		    //add a response with two differences
			Response_Diffs responseDiffs = new Response_Diffs(2,response2,null);
		    assertEquals("build_response_diffs: One difference.", 2,responseDiffs.getDiffs().size());
		    
		    //check internal content of ResponseDiff
		    Diff responseDiff = new Diff();
		    responseDiff.start = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__";
		    responseDiff.originalValue = "CHANGE";
		    responseDiff.betweenType = BetweenType.INSERTED;
		    responseDiff.end = "TARGET";

		    assertEquals("build_response_diffs: First difference.", responseDiff.toString(),responseDiffs.getDiffs(0).toString());

		    responseDiff = new Diff();
		    responseDiff.start = "TARGET";
		    responseDiff.originalValue = "24121";
		    responseDiff.betweenType = BetweenType.INSERTED;
		    responseDiff.end = "\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";;

		    assertEquals("build_response_diffs: Second difference.", responseDiff.toString(),responseDiffs.getDiffs(1).toString());
		    
		}
		
		public void testBuildResponseDiffsMain_NULL_INSERT_EQUAL() {
			String response3 = "TESTMEHTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
			
			//add a response with only one difference
			Response_Diffs responseDiffs = new Response_Diffs(3,response3,null);
		    assertEquals("build_response_diffs: One difference.", 1,responseDiffs.getDiffs().size());
		    
		    //check internal content of ResponseDiff
		    Diff responseDiff = new Diff();
		    responseDiff.start = null;
		    responseDiff.originalValue = "TESTME";
		    responseDiff.betweenType = BetweenType.INSERTED;
		    responseDiff.end = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";

		    assertEquals("build_response_diffs: NULL-INSERT-EQUAL: One difference.", responseDiff.toString(),responseDiffs.getDiffs(0).toString());
		    
		}
		
		public void testBuildResponseDiffsMain_NULL_DELETE_EQUAL() {
			String response4 = "200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
			
			//add a response with only one difference
			Response_Diffs responseDiffs = new Response_Diffs(4,response4,null);
		    assertEquals("build_response_diffs: One difference.", 1,responseDiffs.getDiffs().size());
		    
		    //check internal content of ResponseDiff
		    Diff responseDiff = new Diff();
		    responseDiff.start = null;
		    responseDiff.originalValue = "HTTP/1.1 ";
		    responseDiff.betweenType = BetweenType.DELETED;
		    responseDiff.end = "200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";

		    assertEquals("build_response_diffs: NULL-DELETE-EQUAL: One difference.", responseDiff.toString(),responseDiffs.getDiffs(0).toString());
		    
		}
		
		public void testgetDiffList() {
			String response5 = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"GET\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"123456\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
			//add a response with 3 differences
			Response_Diffs responseDiffs = new Response_Diffs(5,response5,null);
			
			//check for 3
		    assertEquals("Diff List: 3 differences", 3,responseDiffs.getAllDiffs().size());
		    //return an array with 3
		    ArrayList<String> dl = new ArrayList<String>();
		    dl.add("GET");
		    dl.add("CHANGE");
		    dl.add("123456");
		    
		    assertEquals("Diff List: return 3 differences", dl ,responseDiffs.getAllDiffs());
		    
		}
		public void testgetPrintSelectedDiffsResponse() {
			String response5 = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"GET\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"123456\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
			//add a response with 3 differences
			Response_Diffs responseDiffs = new Response_Diffs(5,response5,null);
			
			String SelectedRespose = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"GET\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__TARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"123456\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
			//print a response with differences 0 and 2
		    assertEquals("Print Selected Diffs Response (0,2): ", SelectedRespose,responseDiffs.getSelectedDiffsResponse(new int[]{0,2}));
		    
		    SelectedRespose = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"GET\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		    //print a response with differences 0 and 1
		    assertEquals("Print Selected Diffs Response (0,1): ", SelectedRespose,responseDiffs.getSelectedDiffsResponse(new int[]{0,1}));
		    
		    SelectedRespose = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"123456\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		    //print a response with differences 1 and 2
		    assertEquals("Print Selected Diffs Response (1,2): ", SelectedRespose,responseDiffs.getSelectedDiffsResponse(new int[]{1,2}));
		    
		    SelectedRespose = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"GET\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__CHANGETARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"123456\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		    //print a response with all differences
		    assertEquals("Print Selected Diffs Response (0,1,2): ", SelectedRespose,responseDiffs.getSelectedDiffsResponse(new int[]{0,1,2}));
		    
		    SelectedRespose = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__TARGET11\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
		    //print a response with no differences
		    assertEquals("Print Selected Diffs Response (): ", SelectedRespose,responseDiffs.getSelectedDiffsResponse(new int[]{}));
		    
		}

}
