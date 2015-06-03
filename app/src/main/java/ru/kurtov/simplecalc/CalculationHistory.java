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

	private boolean isLastLineIncomplete;

	private CalculationHistory(Context appContext) {
		mAppContext = appContext;
		mMyFormatter = MyFormatter.get();
		mCalculations = new ArrayList<String>();
		isLastLineIncomplete = false;
	}

	public static CalculationHistory get(Context c) {
		if (sCalculationHistory == null) {
			sCalculationHistory = new CalculationHistory(c.getApplicationContext());
		}
		return sCalculationHistory;
	}



	public void addLine(double firstOperand, operationType type, double secondOperand, int power, double result) {
		String newLine = String.format("%s %s %s = %s",
				mMyFormatter.formatDouble(firstOperand),
				mMyFormatter.getSymbolToString(type),
				mMyFormatter.formatDouble(secondOperand),
				mMyFormatter.formatDouble(result));

		if (isLastLineIncomplete) {
			int lastLineCount = mCalculations.size() - 1;
			mCalculations.remove(lastLineCount);
		}
		mCalculations.add(newLine);
		isLastLineIncomplete = false;
	}

	public void addLine(double firstOperand, operationType type) {
		String newLine = String.format("%s %s",
				mMyFormatter.formatDouble(firstOperand),
				mMyFormatter.getSymbolToString(type));

		mCalculations.add(newLine);
		isLastLineIncomplete = true;
	}

	public String getCalculationHistory() {
		String allLines = "";
		if (mCalculations.size() > 0) {
			allLines = mCalculations.get(0);
			for (int i = 1; i < mCalculations.size(); i++) {
				allLines = allLines + String.format("\n%s", mCalculations.get(i));
			}
		}

		return allLines;
	}
}
