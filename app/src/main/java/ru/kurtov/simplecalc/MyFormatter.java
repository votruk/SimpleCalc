package ru.kurtov.simplecalc;


import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.HashMap;

import ru.kurtov.simplecalc.Enums.formatType;
import ru.kurtov.simplecalc.Enums.operationType;
import ru.kurtov.simplecalc.Enums.specSymbol;
import ru.kurtov.simplecalc.Enums.memoryOperation;

public class MyFormatter {
	private formatType mFormatType;
	private static HashMap<operationType, String> mOperationTypeHashMap;
	private static HashMap<specSymbol, String> mSpecSymbolsHashMap;
	private static HashMap<memoryOperation, String> mMemorySymbolsHashMap;

	private static MyFormatter sMyFormatter;


	private MyFormatter() {
		mOperationTypeHashMap = new HashMap<operationType, String>();
		mOperationTypeHashMap.put(operationType.DIVISION, Character.toString((char) 247));
		mOperationTypeHashMap.put(operationType.MULTIPLY, Character.toString((char) 215));
		mOperationTypeHashMap.put(operationType.PLUS, Character.toString((char) 43));
		mOperationTypeHashMap.put(operationType.MINUS, Character.toString((char) 8722));


		mSpecSymbolsHashMap = new HashMap<specSymbol, String>();
		mSpecSymbolsHashMap.put(specSymbol.BACKSPACE, Character.toString((char) 8592));
		mSpecSymbolsHashMap.put(specSymbol.PERCENT, Character.toString((char) 37));
		mSpecSymbolsHashMap.put(specSymbol.PLUS_MINUS, Character.toString((char) 177));
		mSpecSymbolsHashMap.put(specSymbol.SQUARE, Character.toString((char) 8730));
		mSpecSymbolsHashMap.put(specSymbol.EQUALS, Character.toString((char) 61));

		mMemorySymbolsHashMap = new HashMap<memoryOperation, String>();
		mMemorySymbolsHashMap.put(memoryOperation.ADD, Character.toString((char) 43));
		mMemorySymbolsHashMap.put(memoryOperation.SUBTRACT, Character.toString((char) 8722));
		mMemorySymbolsHashMap.put(memoryOperation.RECALL, Character.toString((char) 61));
		mMemorySymbolsHashMap.put(memoryOperation.CLEAR, Character.toString((char) 61));
	}

	public static MyFormatter get() {
		if (sMyFormatter == null) {
			sMyFormatter = new MyFormatter();
		}
		return sMyFormatter;
	}

	public String formatBigDecimal(double d, int power) {

		if (power == 0) {
			return String.format("%s", (long) d);
		} else {
			double newDouble = Math.round(d * Math.pow(10, power)) / (Math.pow(10, power) * 1.0);
			return String.format("%s", newDouble);
		}

	}

	public String formatBigDecimal(BigDecimal d, int power) {

		if (power == 0) {
			return String.format("%s", d.toPlainString());
		} else {
//			double newDouble = Math.round(d * Math.pow(10, power)) / (Math.pow(10, power) * 1.0);
//			return String.format("%s", newDouble);
			return String.format("%s", d.toPlainString());
		}

	}


//	public String formatBigDecimal(double d) {
//		String returnStatement = "";
//		if (mFormatType == formatType.NORMAL) {
//			if (d == (long) d) {
//				returnStatement = String.format("%s", (long) d);
//			} else {
////				String newDouble = BigDecimal.valueOf(d).toString();
////				returnStatement = String.format("%s", newDouble);
//				DecimalFormat df = new DecimalFormat("#.#####");
//				returnStatement = df.format(d);
//			}
//		} else if (mFormatType == formatType.ONE_DECIMAL) {
//			DecimalFormat df = new DecimalFormat("#.#");
//			returnStatement = df.format(d);
//
//		} else if (mFormatType == formatType.TWO_DECIMALS) {
//			DecimalFormat df = new DecimalFormat("#.##");
//			returnStatement = df.format(d);
//		}
//		return returnStatement;
//
//	}

	public String formatBigDecimal(BigDecimal d) {
		String returnStatement = "";
		if (mFormatType == formatType.NORMAL) {
			returnStatement = String.format("%s", d.toPlainString());
//			if (d == (long) d) {
//				returnStatement = String.format("%s", (long) d);
//			} else {
////				String newDouble = BigDecimal.valueOf(d).toString();
////				returnStatement = String.format("%s", newDouble);
//				DecimalFormat df = new DecimalFormat("#.#####");
//				returnStatement = df.format(d);
//			}
		} else if (mFormatType == formatType.ONE_DECIMAL) {
			DecimalFormat df = new DecimalFormat("#.#");
			returnStatement = df.format(d.doubleValue());

		} else if (mFormatType == formatType.TWO_DECIMALS) {
			DecimalFormat df = new DecimalFormat("#.##");
			returnStatement = df.format(d.doubleValue());
		}
		return returnStatement;

	}

	public String getSymbolToString(operationType type) {
		return mOperationTypeHashMap.get(type);
	}

	public String getSymbolToString(memoryOperation operation) {
		return mMemorySymbolsHashMap.get(operation);
	}

	public String getSymbolToString(specSymbol symbol) {
		return mSpecSymbolsHashMap.get(symbol);
	}

	public void setFormatType(formatType type) {
		mFormatType = type;
	}

//	private String addCommas(BigDecimal bigDecimal) {
//		char[] chars = bigDecimal.toString().toCharArray();
//		int buffer = 0;
//		char[] newChars;
//		int lengthOfNewChar = 0;
//		if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
//			lengthOfNewChar = bigDecimal.toString().length();
//		}
//		int numberOfCommas = bigDecimal.divide(new BigDecimal("3"));
//		for (int i = chars.length - 1; i >= 0; i--) {
//			buffer++;
//			if (buffer == 3 && i != 0) {
//
//			}
//		}
//		return "aaa";
//	}

	public static boolean isNumber(String s) {
		if (s.length() == 0) return false;

		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if ((i != 0 && c == '-') || (!Character.isDigit(c) && c != '-')) {
				return false;
			}
		}
		return true;
	}
}
