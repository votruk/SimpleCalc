package ru.kurtov.simplecalc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

import ru.kurtov.simplecalc.MainFragment.operationType;

/**
 * Created by KURT on 02.06.2015.
 */
public class MyFormatter {
//	private enum operationType {DIVIDE, MULTIPLY, MINUS, PLUS, NOTHING};

	private HashMap<operationType, String> operationToString;


	public MyFormatter() {
		operationToString = new HashMap<operationType, String>();
		operationToString.put(operationType.PLUS, "+");
		operationToString.put(operationType.MINUS, "-");
		operationToString.put(operationType.DIVIDE, "/");
		operationToString.put(operationType.MULTIPLY, "*");
	}

	public String getFullDouble(double d, int power) {

		if (power == 0) {
			return String.format("%s", (long) d);
		} else {
			double newDouble = Math.round(d * Math.pow(10, power)) / (Math.pow(10, power) * 1.0);
			return String.format("%s", newDouble);
		}

	}

	public String getFullDouble(double d) {

		if (d == (long) d) {
			return String.format("%s", (long) d);
		} else {
			String newDouble = BigDecimal.valueOf(d).toString();
			return String.format("%s", newDouble);
		}

	}

	public String getTwoDecimals(double d) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(d);
	}

	public String getOneDecimal(double d) {
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(d);
	}

	public String getOperationToString(operationType type) {
		return operationToString.get(type);
	}
}
