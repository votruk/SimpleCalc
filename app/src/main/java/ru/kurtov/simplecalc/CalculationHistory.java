package ru.kurtov.simplecalc;

import android.content.Context;

import java.math.BigDecimal;
import java.util.ArrayList;

import ru.kurtov.simplecalc.Enums.operationType;
import ru.kurtov.simplecalc.Enums.specSymbol;
import ru.kurtov.simplecalc.Enums.memoryOperation;


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



	public void addLine(BigDecimal firstOperand, operationType type, BigDecimal secondOperand, BigDecimal result) {
		String newLine = String.format("%s %s %s = %s",
				mMyFormatter.formatBigDecimal(firstOperand),
				mMyFormatter.getSymbolToString(type),
				mMyFormatter.formatBigDecimal(secondOperand),
				mMyFormatter.formatBigDecimal(result));

		if (isLastLineIncomplete) {
			int lastLineCount = mCalculations.size() - 1;
			mCalculations.remove(lastLineCount);
		}
		mCalculations.add(newLine);
		isLastLineIncomplete = false;
	}

	public void addLine(BigDecimal firstOperand, operationType type) {
		String newLine = String.format("%s %s",
				mMyFormatter.formatBigDecimal(firstOperand),
				mMyFormatter.getSymbolToString(type));
		if (isLastLineIncomplete) {
			int lastLineCount = mCalculations.size() - 1;
			mCalculations.remove(lastLineCount);
		}
		mCalculations.add(newLine);
		isLastLineIncomplete = true;
	}

	public void addLine(BigDecimal firstOperand, operationType type, BigDecimal secondOperand, BigDecimal result, specSymbol symbol) {
		String newLine = "";
		if (symbol == specSymbol.PERCENT) {
			if (type == operationType.PLUS || type == operationType.MINUS) {
				newLine = String.format("%s %s %s%% = %s",
						mMyFormatter.formatBigDecimal(firstOperand),
						mMyFormatter.getSymbolToString(type),
						mMyFormatter.formatBigDecimal(secondOperand),
						mMyFormatter.formatBigDecimal(result));
			} else if (type == operationType.MULTIPLY || type == operationType.DIVISION) {
				newLine = String.format("%s%% from %s = %s",
						mMyFormatter.formatBigDecimal(firstOperand),
						mMyFormatter.formatBigDecimal(secondOperand),
						mMyFormatter.formatBigDecimal(result));
			} else if (type == operationType.NOTHING) {
				newLine = String.format("%s%% = %s",
						mMyFormatter.formatBigDecimal(secondOperand),
						mMyFormatter.formatBigDecimal(result));
			}
		}

		if (isLastLineIncomplete) {
			int lastLineCount = mCalculations.size() - 1;
			mCalculations.remove(lastLineCount);
		}
		mCalculations.add(newLine);
		isLastLineIncomplete = false;
	}

	public void addLine(BigDecimal onlyOneOperand) {
		String newLine = String.format("%s = %s",
				mMyFormatter.formatBigDecimal(onlyOneOperand),
				mMyFormatter.formatBigDecimal(onlyOneOperand));

		mCalculations.add(newLine);
		isLastLineIncomplete = false;
	}

	public void addLine(BigDecimal targetDouble, memoryOperation operation) {
		String newLine = String.format("MEMORY %s %s",
				mMyFormatter.getSymbolToString(operation),
				mMyFormatter.formatBigDecimal(targetDouble));

		if (isLastLineIncomplete) {
			int lastLineCount = mCalculations.size() - 1;
			mCalculations.remove(lastLineCount);
		}
		mCalculations.add(newLine);
		isLastLineIncomplete = false;
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
