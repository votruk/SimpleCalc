package ru.kurtov.simplecalc;


import android.content.Context;

import java.util.HashMap;

import ru.kurtov.simplecalc.MainFragment.operationType;
import ru.kurtov.simplecalc.MainFragment.specSymbol;

/**
 * Created by KURT on 03.06.2015.
 */
public class SymbolsLib {
	private static SymbolsLib sSymbolsLib;

	private static HashMap<operationType, String> mOperationTypeHashMap;
	private static HashMap<specSymbol, String> mSpecSymbolsHashMap;



	private SymbolsLib() {
		mOperationTypeHashMap = new HashMap<operationType, String>();

		mOperationTypeHashMap.put(operationType.DIVISION, Character.toString((char) 247));
		mOperationTypeHashMap.put(operationType.MULTIPLY, Character.toString((char) 215));
		mOperationTypeHashMap.put(operationType.PLUS, Character.toString((char) 43));
		mOperationTypeHashMap.put(operationType.MINUS, Character.toString((char) 8722));


		mSpecSymbolsHashMap = new HashMap<specSymbol, String>();
		mSpecSymbolsHashMap.put(specSymbol.BACKSPACE, Character.toString((char) 2190));
		mSpecSymbolsHashMap.put(specSymbol.PERCENT, Character.toString((char) 37));
		mSpecSymbolsHashMap.put(specSymbol.PLUS_MINUS, Character.toString((char) 177));
		mSpecSymbolsHashMap.put(specSymbol.SQUARE, Character.toString((char) 8730));
	}

	public static SymbolsLib get(Context c) {
		if (sSymbolsLib == null) {
			sSymbolsLib = new SymbolsLib();
		}
		return sSymbolsLib;
	}
}
