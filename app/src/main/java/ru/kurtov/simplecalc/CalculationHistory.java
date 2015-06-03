package ru.kurtov.simplecalc;

import android.content.Context;

import java.util.ArrayList;

import ru.kurtov.simplecalc.MainFragment.operationType;


/**
 * Created by KURT on 03.06.2015.
 */
public class CalculationHistory {

	private static CalculationHistory sCalculationHistory;
	private Context mAppContext;

	private MyFormatter mMyFormatter;

	private ArrayList<String> mCalculations;

	private static final int MAX_LINES = 50;

	private CalculationHistory(Context appContext) {
		mAppContext = appContext;
		mMyFormatter = MyFormatter.get();
	}

	public static CalculationHistory get(Context c) {
		if (sCalculationHistory == null) {
			sCalculationHistory = new CalculationHistory(c.getApplicationContext());
		}
		return sCalculationHistory;
	}



	public void addLine(double firstOperand, operationType type, double secondOperand, double result) {
		String newLine = String.format("%s %s %s = %s",
				mMyFormatter.formatDouble(firstOperand),
				mMyFormatter.getOperationToString(type),
				mMyFormatter.formatDouble(secondOperand),
				mMyFormatter.formatDouble(result));

		mCalculations.add(newLine);
	}

	public void addLine(double firstOperand, operationType type) {
		String newLine = String.format("%s %s",
				mMyFormatter.formatDouble(firstOperand),
				mMyFormatter.getOperationToString(type));

		mCalculations.add(newLine);
	}

	public String getCalculationHistory() {
		String allLines = "";
		if (mCalculations.size() > 0) {
			allLines = mCalculations.get(0);
			for (int i = 1; i < mCalculations.size(); i++) {
				allLines = String.format("\n%s", mCalculations.get(i));
			}
		}

		return allLines;
	}
}
