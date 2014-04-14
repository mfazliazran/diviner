/**
 *
 */
package com.hacktics.diviner.constants;

import java.io.File;

/**
 * The Resources class contains the locations of files used by other components.
 *
 * @author Shay Chen
 * @since 1.0
 */
public final class Resources {
    //****************
    //* CONSTRUCTORS *
    //****************

    /**
     * A private constructor designed to prevent instance creation.
     */
    private Resources() {
        //Do Nothing
    } //end of default constructor

    //*************************
    //* CONSTANT DECLERATIONS *
    //*************************
    /**
     * The current state of the debug flag.
     */
    public static final boolean DEBUG_FLAG = true;
    /**
     * The current OS file delimiter.
     */
    public static final String FD = "/";
        //System.getProperty("file.separator")
    /**
     * A constant that contains the base path to the resource files.
     */
    public static final String BASE_PATH =
        FD + "resources";
    /**
     * A constant that contains the path to the main logo file.
     * Currnetly the resources are located under the classess dir.
     */
    
    private static final String fileDelimiter = System.getProperty("file.separator"); //OS Independent
    private static final File f = new File ("");
    private static final String directory = f.getAbsolutePath();
    public static final String PAYLOAD_DATABASE_BASIC =
    		directory + fileDelimiter + "payload-db" + fileDelimiter + "hacktics_payload_db(encoded)-v1.1.xml";

    public static final String HACKTICS_LOGO =
        BASE_PATH + FD + "images" + FD + "hacktics_icon.jpg";
    /**
     * A constant that contains the path to the payload xml database.
     */
//    public static final String PAYLOAD_DATABASE_BASIC =
//        "com/hacktics/diviner/payloads/constants/hacktics_payload_db(encoded)-v1.1.xml";
    /**
     * A constant that contains the path to the template payload database.
     */
    public static final String PAYLOAD_DATABASE_TEMPLATE =
        BASE_PATH + FD + "payloads" + FD + "template_payload_db.xml";

} //end of class
