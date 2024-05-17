package com.ladas.linesofaction;

import java.util.Vector;

public class Log {

    private static final Vector<String> gamelog = new Vector<String>();

    /**
     * Appends a new game activity message to the view log
     * @param argMessage a String, the argument passed through which contains a message
     */
    public static void AddMessage(String argMessage) {
       gamelog.add( " • " + argMessage + "\n");
    }

    /**
     * Gets rid of any elements inside the log currently
     */
    public static void ClearLog() {
        gamelog.clear();
    }

    /**
     * Allows the log to be set as a single String for Text in the GUI
     * @return a value which represents the whole log vector as only one combined string
     */
    public static String FormatLog() {
        StringBuilder formattedLog = new StringBuilder();
        for (String message : gamelog) {
            formattedLog.append(message);
        }
        return formattedLog.toString();
    }

}
