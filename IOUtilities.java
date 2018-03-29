package com.ooshin.jpmorgan.utils;

import com.ooshin.jpmorgan.properties.Constants;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * The class contains some useful I/O utility methods. <br>
 * 
 * @author Thomas Oshin
 * @version 1.1
 * @since 1.0
 */
public class IOUtilities {

	public IOUtilities() {
		super();
	}

	public static float getTradeAmount(float price, int units, float agreedFx) {
		float tradeAmountUSD = price * units * agreedFx;
		return tradeAmountUSD;
	}

	/**
	 * Expands the two dimensional array
	 * 
	 * @param origArray
	 *            array to expand
	 * @return newly expanded array
	 */
	public static Object[][] expand(Object origArray[][]) {
		if (origArray == null) {
			return new Object[1][2];
		}
		int arrayLength = origArray.length;
		Object newArray[][] = new Object[arrayLength + 1][2];
		for (int i = 0; i < arrayLength; i++) {
			newArray[i][0] = origArray[i][0];
			newArray[i][1] = origArray[i][1];
		}
		return newArray;
	}

	/**
	 * Sorts a 2 dimensional array in descending order.
	 * 
	 * @param items
	 *            2 dimensional array to sort
	 * @param column
	 *            Index of the array to sort by
	 * @return The sorted array
	 * @throws ClassCastException
	 *             error occurs while attempting to cast an object
	 */
	public static Object[][] sort2DimensionalInDescendingOrder(Object items[][], int column) throws ClassCastException {
		Comparator<Object> comparator = Collections.reverseOrder(new ColumnComparator());
		ColumnComparator.index = column;
		Arrays.sort(items, comparator);
		return items;
	}

	/**
	 * Reads the input csv file and parses
	 * 
	 * @param filename
	 *            the comma seperated file to process
	 * @param seperator
	 *            The file separator
	 * @param numberOfColumns
	 *            the number of columns in the file
	 * @throws IOException
	 *             thrown if exception occurs
	 */
	public static void readAndParseInputFile(String filename, String seperator, int numberOfColumns) throws IOException {
		FileReader file = new FileReader(filename);
		BufferedReader in = new BufferedReader(file);
		String s = "";
		int lineNumber = 0;
		boolean columnNameOnce = false;
		boolean columnValueOnce = false;

		while ((s = in.readLine()) != null) {
			lineNumber++;
			s = s.trim();
			if (s.length() > 0) {
				String[] eachLine = s.split(seperator);
				int eachLineLength = eachLine.length;
				if (eachLineLength != numberOfColumns) {
					System.err.println("Error: Invalid number of columns on line " + lineNumber);
					System.exit(1);
				}

				if (columnValueOnce) {
					for (int i = 0; i < eachLineLength; i++) {
						Constants.globalColumnValues.addElement(eachLine[i]);
					}
					Constants.globalColumnValues.addElement(Constants.globalNewLine);
				}
				if (!columnNameOnce) {
					for (int i = 0; i < eachLineLength; i++) {
						Constants.globalColumnNames.addElement(eachLine[i]);
					}
					columnNameOnce = true;
					columnValueOnce = true;
				}
			}
		}
		in.close();
	}

	/**
	 * Get the week day given the date
	 * 
	 * @param date
	 *            the date to process
	 * @return the day of the week
	 */
	public static String getSettlementDate(String currency, String dateStr) {
		java.text.DateFormat format = new java.text.SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
		Date date;
		String settlementDate = dateStr;
		boolean alternateWeekend = false;
		try {
			date = format.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (currency.equalsIgnoreCase(Constants.CURRENCY_AED) || currency.equalsIgnoreCase(Constants.CURRENCY_SGP)){
				alternateWeekend = true;
			}
			Date formattedDate = null;
			if (dayOfWeek == 1 && alternateWeekend == false) {
				cal.add(Calendar.DATE, 1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				formattedDate = cal.getTime();
				settlementDate = dateFormat.format(formattedDate);
			} else if (dayOfWeek == 7 && alternateWeekend == false) {
				cal.add(Calendar.DATE, 2);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				formattedDate = cal.getTime();
				settlementDate = dateFormat.format(formattedDate);
			} else if (dayOfWeek == 6 && alternateWeekend == true) {
				cal.add(Calendar.DATE, 2);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				formattedDate = cal.getTime();
				settlementDate = dateFormat.format(formattedDate);
			} else if (dayOfWeek == 7 && alternateWeekend == true) {
				cal.add(Calendar.DATE, 1);				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				formattedDate = cal.getTime();
				settlementDate = dateFormat.format(formattedDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return settlementDate;
	}

}

/**
 * Custom comparator for 2 dimensional array sorting.
 */
class ColumnComparator implements Comparator<Object> {
	protected static int index = -1;

	public int compare(Object obj1, Object obj2) {
		Object[] str1 = (Object[]) obj1;
		Object[] str2 = (Object[]) obj2;
		float strF1 = Float.valueOf(str1[index].toString()).floatValue();
		float strF2 = Float.valueOf(str2[index].toString()).floatValue();
		if (strF1 < strF2) {
			return -1;
		} else if (strF1 == strF2) {
			return 0;
		} else {
			return 1;
		}
	}
}
