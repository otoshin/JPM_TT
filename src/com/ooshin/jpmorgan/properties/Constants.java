package com.ooshin.jpmorgan.properties;

import java.util.Vector;

/**
 * The class simply defines the constants. <br>
 * 
 * @author Thomas Oshin
 * @version 1.1
 * @since 1.0
 */
public class Constants {
	public static String globalInputFile = "input.csv";
	public static Vector<String> globalColumnNames = new Vector<String>();
	public static Vector<String> globalColumnValues = new Vector<String>();
	public static String globalSeperator = ",";
	public static String globalNewLine = "\n";
	public static String globalTab = "\t";
	public static int globalInputFileNumberOfColumns = 8;
	public static Object globalIncomingEntityAmounts[][] = null;
	public static int globalIncomingEntityTracker = 0;
	public static int globalIncomingAmountTracker = 0;
	public static Object globalOutgoingEntityAmounts[][] = null;
	public static int globalOutgoingEntityTracker = 0;
	public static int globalOutgoingAmountTracker = 0;
	public static String CURRENCY_AED = "AED";
	public static String CURRENCY_SGP = "AED";
}
