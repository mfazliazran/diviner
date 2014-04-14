package com.hacktics.diviner.diffutil;

import java.util.LinkedList;

import com.hacktics.diviner.diffutil.diff_match_patch.Diff;

import junit.framework.TestCase;

public class diff_match_patch_Test extends TestCase {
	  private diff_match_patch dmp;
	  private diff_match_patch.Operation DELETE = diff_match_patch.Operation.DELETE;
	  private diff_match_patch.Operation EQUAL = diff_match_patch.Operation.EQUAL;
	  private diff_match_patch.Operation INSERT = diff_match_patch.Operation.INSERT;

	  protected void setUp() {
	    // Create an instance of the diff_match_patch object.
	    dmp = new diff_match_patch();
	  }
	  
	  public void testdmp_main_1_EQUAL_DELETE_INSERT_EQUAL() {
        //Perform diff operations
        String response1 = "aaad asdx zxvxzsd sfa";
        String response2 = "aaad asdx zxfsaxv sfa";
        
        LinkedList<Diff> diffs = dmp.diff_main(response1, response2,false);
        dmp.diff_cleanupSemantic(diffs);
        
        assertEquals("diff_responses", diffList(new Diff(EQUAL, "aaad asdx zx"),new Diff(DELETE,"vxzsd"),new Diff(INSERT,"fsaxv"), new Diff(EQUAL, " sfa")),diffs);
        //System.out.print(diffs+"\r\n");
	  }
	  
	  // Private function for quickly building lists of diffs.
	  private static LinkedList<Diff> diffList(Diff... diffs) {
	    LinkedList<Diff> myDiffList = new LinkedList<Diff>();
	    for (Diff myDiff : diffs) {
	      myDiffList.add(myDiff);
	    }
	    return myDiffList;
	  }
	  
}
