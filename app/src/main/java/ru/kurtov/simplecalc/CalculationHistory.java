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

	private ArrayList<String> mCalculations;

	private static final int MAX_LINES = 50;

	private CalculationHistory(Context appContext) {
		mAppContext = appContext;
	}

	public static CalculationHistory get(Context c) {
		if (sCalculationHistory == null) {
			sCalculationHistory = new CalculationHistory(c.getApplicationContext());
		}
		return sCalculationHistory;
	}

	public void addLine(double firstOperand, operationType type, double secondOperand) {

	}
}
