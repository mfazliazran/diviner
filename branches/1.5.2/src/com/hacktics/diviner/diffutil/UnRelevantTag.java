/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hacktics.diviner.diffutil;

/**
 *
 * @author michalg
 */
public class UnRelevantTag {
    public String startTagStr;
    public String middleTagStr;
    public String endTagStr;
    
    public UnRelevantTag(String startTag, String middleTag, String endTag)
    {
        this.startTagStr = startTag;
        this.middleTagStr = middleTag;
        this.endTagStr = endTag;
    }
}
