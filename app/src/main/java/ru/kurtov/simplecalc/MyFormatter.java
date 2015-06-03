package ru.kurtov.simplecalc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

import ru.kurtov.simplecalc.MainFragment.operationType;
import ru.kurtov.simplecalc.MainFragment.formatType;
/**
 * Created by KURT on 02.06.2015.
 */
public class MyFormatter {
//	private enum operationType {DIVIDE, MULTIPLY, MINUS, PLUS, NOTHING};

	private formatType mFormatType;
	private HashMap<operationType, String> operationToString;
	private static MyFormatter sMyFormatter;


	private MyFormatter() {
		operationToString = new HashMap<operationType, String>();
		operationToString.put(operationType.PLUS, "+");
		operationToString.put(operationType.MINUS, "-");
		operationToString.put(operationType.DIVIDE, "/");
		operationToString.put(operationType.MULTIPLY, "*");
	}

	public static MyFormatter get() {
		if (sMyFormatter == null) {
			sMyFormatter = new MyFormatter();
		}
		return sMyFormatter;
	}

	public String formatDouble(double d, int power) {

		if (power == 0) {
			return String.format("%s", (long) d);
		} else {
			double newDouble = Math.round(d * Math.pow(10, power)) / (Math.pow(10, power) * 1.0);
			return String.format("%s", newDouble);
		}

	}

	public String formatDouble(double d) {
		String returnStatement = "";
		if (mFormatType == formatType.NORMAL) {
			if (d == (long) d) {
				returnStatement = String.format("%s", (long) d);
			} else {
//				String newDouble = BigDecimal.valueOf(d).toString();
//				returnStatement = String.format("%s", newDouble);
				DecimalFormat df = new DecimalFormat("#.#####");
				returnStatement = df.format(d);
			}
		} else if (mFormatType == formatType.ONE_DECIMAL) {
			DecimalFormat df = new DecimalFormat("#.#");
			returnStatement = df.format(d);

		} else if (mFormatType == formatType.TWO_DECIMALS) {
			DecimalFormat df = new DecimalFormat("#.##");
			returnStatement = df.format(d);
		}
		return returnStatement;

	}

//	public String getTwoDecimals(double d) {
//		DecimalFormat df = new DecimalFormat("#.##");
//		return df.format(d);
//	}
//
//	public String getOneDecimal(double d) {
//		DecimalFormat df = new DecimalFormat("#.#");
//		return df.format(d);
//	}

	public String getOperationToString(operationType type) {
		return operationToString.get(type);
	}

	public void setFormatType(formatType type) {
		mFormatType = type;
	}
}
