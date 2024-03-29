/**
 * ProgramInfo.java
 * @Description: Contains all static information of the program
 */
package com.desrosiersrobby.veloxal.utilities;

public class ProgramInfo {
    public static String NAME = "Veloxal Screenshot Tool";
    public static String VERSION = "1.0";
    public static String TITLE = NAME + " " + VERSION;
    public static String SERVER_IP = "http://api.veloxal.io";

    public static String getServerIp(String url) {
        return SERVER_IP + url;
    }
}
