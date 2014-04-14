/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hacktics.diviner.diffutil;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author michalg
 */
public class UnRelevantInfo {
    
    public static String CropHeaders(String s1) {
        int i = s1.indexOf("\r\n\r\n");
        if (i == -1) {
            i = 0;
        }
        return s1.substring(i);
    }
    /*
     * @alex.mor : overloaded static method to support multiple responses to be processed at once
     */
    public static List<String> removeUnRelevantData(List<String> originalResponses, List<UnRelevantTag> unRelevantTags,List<String> removed_tags) {
    	List<String> newResponses = new ArrayList<String>();
    	Iterator<String> itr = originalResponses.iterator();
    	
    	while (itr.hasNext()) {
    		String newResponse = removeUnRelevantData(itr.next(),unRelevantTags,removed_tags);
    		newResponses.add(newResponse);
    	}
    	return newResponses;
    }
    
    // Removes: Response Headers, Response Body UnRelavent Tags, Line Breakes
    public static String removeUnRelevantData(String originalResponseStr, List<UnRelevantTag> unRelevantTags,List<String> removed_tags) {   
             
        //originalResponseStr = "HTTP/1.1 200 OK\nDate: Wed, 28 Mar 2012 07:50:14 GMT\nServer: Microsoft-IIS/6.0\nX-Powered-By: ASP.NET\r\n\r\n<body>\n<form name=\"aspnetForm\" method=\"post\" action=\"login.aspx?ReturnUrl=www.ey.com%2f\" onsubmit=\"javascript:return WebForm_OnSubmit();\" id=\"aspnetForm\">\n<div>\n<input type=\"hidden\" name=\"__EVENTTARGET\" id=\"__EVENTTARGET\" value=\"\" />\n<input type=\"hidden\" name=\"__EVENTARGUMENT\" id=\"__EVENTARGUMENT\" value=\"\" />\n<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"/wEPDwUKMTEwMjIwNTk3NQ9kFgJmD2QWAgIDD2QWAgIHD2QWAgIFDw8WAh4EVGV4dGVkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUsY3RsMDAkY3BoTWFpbkNvbnRlbnQkTG9naW4xJExvZ2luSW1hZ2VCdXR0b27puccQztvge5u2R5jGz0IQl04xcg==\" />\n</div>\n</body>";
        originalResponseStr = CropHeaders(originalResponseStr);
        
        //@alex.mor : commented these out because they may show difference in the responses
        //originalResponseStr = originalResponseStr.replaceAll("\r", "");
        //originalResponseStr = originalResponseStr.replaceAll("\n", " ");
            
        for (UnRelevantTag unRlvTag : unRelevantTags)
        {
            String startStr = unRlvTag.startTagStr;
            String endStr = unRlvTag.endTagStr;
            if (endStr=="" || endStr==null) 
            	endStr = "\'?\"?&?>"; //this is yet tested
            String searchStr = unRlvTag.middleTagStr;
            
            startStr = Pattern.quote(startStr);
            endStr = Pattern.quote(endStr);
            searchStr = Pattern.quote(searchStr);
            
            String paternGreater = "(" + startStr + ")(.*?)(" + endStr +")";
            Pattern myPatternGreater = Pattern.compile(paternGreater, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher myMatcherGreater = myPatternGreater.matcher(originalResponseStr);

            String paternInner = "(.*)(" + searchStr + ")(.*)";
            Pattern myPatternInner = Pattern.compile(paternInner, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher myMatcherInner = null;

            String currentGreaterStr = "";
            String s1 = "";
            String s2 = "";
            
            while (myMatcherGreater.find())
            {
                currentGreaterStr = originalResponseStr.substring(myMatcherGreater.start(), myMatcherGreater.end());

                myMatcherInner = myPatternInner.matcher(currentGreaterStr);

                if (myMatcherInner.find())
                // It's a match, need to delete this greater part
                {   
                	//save removed tag content
                	if (removed_tags!=null)
                	removed_tags.add(originalResponseStr.substring(myMatcherGreater.start(),myMatcherGreater.end()));
                	
                	//remove the tag
                	s1 = originalResponseStr.substring(0, myMatcherGreater.start());
                    s2 = originalResponseStr.substring(myMatcherGreater.end(), originalResponseStr.length());
                    originalResponseStr = s1 + s2;
                    myMatcherGreater = myPatternGreater.matcher(originalResponseStr);
                }       
            }
        }
              
        return originalResponseStr;
    } 
}