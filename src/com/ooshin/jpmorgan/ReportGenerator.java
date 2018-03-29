package com.ooshin.jpmorgan;

import java.io.IOException;
import com.ooshin.jpmorgan.utils.IOUtilities;
import com.ooshin.jpmorgan.properties.Constants;

/**
 * Main class to generate the daily report <br>
 * 
 * @author Thomas Oshin
 * @version 1.1
 * @since 1.0
 */
public class ReportGenerator {

	public static void main(String[] args) {
		ReportGenerator rg = new ReportGenerator();
		try {
			IOUtilities.readAndParseInputFile(Constants.globalInputFile, Constants.globalSeperator, Constants.globalInputFileNumberOfColumns);
			rg.printInputData();
			rg.incomingReport();
			rg.outgoingReport();
			rg.rankIncomingEntities();			
			rg.rankOutgoingEntities();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads and outputs the input data
	 */
	public void printInputData() {
		System.out.println("**** Input data ****");
		int globalColumnNamesSize = Constants.globalColumnNames.size();
		for (int i = 0; i < globalColumnNamesSize; i++) {
			String eachColumnName = Constants.globalColumnNames.get(i);
			System.out.print(eachColumnName + Constants.globalTab);
		}
		System.out.println();
		int globalColumnValuesSize = Constants.globalColumnValues.size();
		for (int i = 0; i < globalColumnValuesSize; i++) {
			String eachColumn = Constants.globalColumnValues.get(i);
			if (eachColumn.equals(Constants.globalNewLine)) {
				System.out.print(eachColumn);
			} else {
				System.out.print(eachColumn + Constants.globalTab);
			}
		}
	}

	/**
	 * Generates and outputs the incoming report
	 */
	public void incomingReport() {
		StringBuffer incomingHeader = new StringBuffer(Constants.globalNewLine + "**** Amount in USD settled incoming everyday ****");
		incomingHeader.append(Constants.globalNewLine).append("Settlement date").append(Constants.globalTab).append("Amount (USD)");
		System.out.println(incomingHeader);
		int globalColumnValuesSize = Constants.globalColumnValues.size();
		for (int i = 1; i < globalColumnValuesSize; i++) {
			if (Constants.globalColumnValues.get(i).equals("S")) {
				Constants.globalIncomingEntityAmounts = IOUtilities.expand(Constants.globalIncomingEntityAmounts);
				float price = Float.valueOf(Constants.globalColumnValues.get(i + 6));
				int units = Integer.valueOf(Constants.globalColumnValues.get(i + 5)).intValue();
				float agreedFx = Float.valueOf(Constants.globalColumnValues.get(i + 1));				
				String settlementDate = IOUtilities.getSettlementDate(Constants.globalColumnValues.get(i + 2), Constants.globalColumnValues.get(i + 4));
				String entity = Constants.globalColumnValues.get(i - 1);
				float tradeAmount = IOUtilities.getTradeAmount(price, units, agreedFx);
				System.out.println(settlementDate + Constants.globalTab + tradeAmount);
				int index = Constants.globalIncomingAmountTracker;
				Constants.globalIncomingEntityAmounts[Constants.globalIncomingEntityTracker][index] = entity;
				Constants.globalIncomingEntityAmounts[Constants.globalIncomingEntityTracker][index + 1] = tradeAmount;
				Constants.globalIncomingEntityTracker++;
			}
			i = i + Constants.globalInputFileNumberOfColumns;
		}
	}

	/**
	 * Generates and outputs the outgoing report
	 */
	public void outgoingReport() {
		StringBuffer outgoingHeader = new StringBuffer(Constants.globalNewLine + "**** Amount in USD settled outgoing everyday ****");
		outgoingHeader.append(Constants.globalNewLine).append("Settlement date").append(Constants.globalTab).append("Amount (USD)");
		System.out.println(outgoingHeader);
		int globalColumnValuesSize = Constants.globalColumnValues.size();
		for (int i = 1; i < globalColumnValuesSize; i++) {
			if (Constants.globalColumnValues.get(i).equals("B")) {
				Constants.globalOutgoingEntityAmounts = IOUtilities.expand(Constants.globalOutgoingEntityAmounts);
				float price = Float.valueOf(Constants.globalColumnValues.get(i + 6));
				int units = Integer.valueOf(Constants.globalColumnValues.get(i + 5)).intValue();
				float agreedFx = Float.valueOf(Constants.globalColumnValues.get(i + 1));
				String settlementDate = IOUtilities.getSettlementDate(Constants.globalColumnValues.get(i + 2), Constants.globalColumnValues.get(i + 4));
				String entity = Constants.globalColumnValues.get(i - 1);
				float tradeAmount = IOUtilities.getTradeAmount(price, units, agreedFx);
				System.out.println(settlementDate + Constants.globalTab + tradeAmount);
				int index = Constants.globalOutgoingAmountTracker;
				Constants.globalOutgoingEntityAmounts[Constants.globalOutgoingEntityTracker][index] = entity;
				Constants.globalOutgoingEntityAmounts[Constants.globalOutgoingEntityTracker][index + 1] = tradeAmount;
				Constants.globalOutgoingEntityTracker++;
			}
			i = i + Constants.globalInputFileNumberOfColumns;
		}
	}

	/**
	 * Ranks the outgoing entities
	 */
	public void rankOutgoingEntities() {
		StringBuffer outgoingRank = new StringBuffer(Constants.globalNewLine + "**** Outgoing rank in USD ****");
		outgoingRank.append(Constants.globalNewLine).append("Entity").append(Constants.globalTab).append("Rank").append(Constants.globalTab)
				.append("SettlementDate").append("Amount (USD)");
		System.out.println(outgoingRank);
		Object results[][] = IOUtilities.sort2DimensionalInDescendingOrder(Constants.globalOutgoingEntityAmounts, 1);
		int len = results.length;
		for (int i = 0; i < len; i++) {
			System.out.println(results[i][0] + Constants.globalTab + (i + 1) + Constants.globalTab + results[i][1]);
		}
	}

	/**
	 * Ranks the incoming entities
	 */
	public void rankIncomingEntities() {
		StringBuffer incomingRank = new StringBuffer(Constants.globalNewLine + "**** Incoming rank in USD ****");
		incomingRank.append(Constants.globalNewLine).append("Entity").append(Constants.globalTab).append("Rank").append(Constants.globalTab)
				.append("SettlementDate").append("Amount (USD)");
		System.out.println(incomingRank);
		Object results[][] = IOUtilities.sort2DimensionalInDescendingOrder(Constants.globalIncomingEntityAmounts, 1);
		int len = results.length;
		for (int i = 0; i < len; i++) {
			System.out.println(results[i][0] + Constants.globalTab + (i + 1) + Constants.globalTab + results[i][1]);
		}
	}
}
