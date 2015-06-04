package ru.kurtov.simplecalc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by KURT on 01.06.2015.
 */
public class MainFragment extends Fragment implements DigitClickable {

	private double mCurrentNumber;
	private double mPreviousNumber;
	private int mPowerCount;
	private int mPreviousPowerCount;
	private float mBuffer;
	private boolean mIsNew;
	private boolean mIsDot;

	private double mFirstOperand;
	private double mSecondOperand;
	private double mResult;

	public enum operationType {DIVISION, MULTIPLY, MINUS, PLUS, NOTHING }
	private operationType mCurrentOperation;

	public enum formatType { NORMAL, TWO_DECIMALS, ONE_DECIMAL }
	public enum specSymbol { SQUARE, PERCENT, PLUS_MINUS, BACKSPACE, EQUALS }
	private formatType mFormatType;

	private Button mSquareButton;
	private Button mPercentButton;
	private Button mChangeButton;
	private Button mBackspaceButton;

	private Button mMemoryRecallClearButton;
	private Button mMemoryAddButton;
	private Button mMemorySubtractButton;
	private Button mClearButton;

	private Button mDivideButton;
	private Button mMultiplyButton;
	private Button mMinusButton;
	private Button mPlusButton;

	private Button mActiveButton;

	private Button mEqualsButton;
	private Button mDotButton;

	private TextView mNowTypingTextView;
	private TextView mHistoryTextView;

	private CalculationHistory mCalculationHistory;
	private MyFormatter mMyFormatter;



	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		mFormatType = formatType.NORMAL;
		mMyFormatter = MyFormatter.get();
		mMyFormatter.setFormatType(mFormatType);

		mCalculationHistory = CalculationHistory.get(getActivity());

		clearAll();

		mSquareButton = (Button) v.findViewById(R.id.squareButton);
		mSquareButton.setText(mMyFormatter.getSymbolToString(specSymbol.SQUARE));
		mSquareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentNumber = Math.sqrt(mCurrentNumber);
				mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));
			}
		});


		mPercentButton = (Button) v.findViewById(R.id.percentButton);
		mPercentButton.setText(mMyFormatter.getSymbolToString(specSymbol.PERCENT));


		mChangeButton = (Button) v.findViewById(R.id.changeButton);
		mChangeButton.setText(mMyFormatter.getSymbolToString(specSymbol.PLUS_MINUS));
		mChangeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentNumber = mCurrentNumber * (-1);
				MyFormatter mf = MyFormatter.get();

				mNowTypingTextView.setText(mf.formatDouble(mCurrentNumber, mPowerCount));
			}
		});


		mBackspaceButton = (Button) v.findViewById(R.id.backspaceButton);
		mBackspaceButton.setText(mMyFormatter.getSymbolToString(specSymbol.BACKSPACE));


		mMemoryRecallClearButton = (Button) v.findViewById(R.id.memoryRecallClearButton);
		mMemoryAddButton = (Button) v.findViewById(R.id.memoryAddButton);
		mMemorySubtractButton = (Button) v.findViewById(R.id.memorySubtractButton);

		mClearButton = (Button) v.findViewById(R.id.clearButton);
		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAll();
			}
		});

		mDivideButton = (Button) v.findViewById(R.id.divideButton);
		mDivideButton.setOnClickListener(operationAction(operationType.DIVISION));
		mDivideButton.setText(mMyFormatter.getSymbolToString(operationType.DIVISION));

		mMultiplyButton = (Button) v.findViewById(R.id.multiplyButton);
		mMultiplyButton.setOnClickListener(operationAction(operationType.MULTIPLY));
		mMultiplyButton.setText(mMyFormatter.getSymbolToString(operationType.MULTIPLY));

		mMinusButton = (Button) v.findViewById(R.id.minusButton);
		mMinusButton.setOnClickListener(operationAction(operationType.MINUS));
		mMinusButton.setText(mMyFormatter.getSymbolToString(operationType.MINUS));

		mPlusButton = (Button) v.findViewById(R.id.plusButton);
		mPlusButton.setOnClickListener(operationAction(operationType.PLUS));
		mPlusButton.setText(mMyFormatter.getSymbolToString(operationType.PLUS));

		mEqualsButton = (Button) v.findViewById(R.id.equalsButton);
		mEqualsButton.setText(mMyFormatter.getSymbolToString(specSymbol.EQUALS));


		mDotButton = (Button) v.findViewById(R.id.dotButton);
		mDotButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsDot = true;
			}
		});

		mNowTypingTextView = (TextView) v.findViewById(R.id.nowTypingTextView);
		mHistoryTextView = (TextView) v.findViewById(R.id.historyTextView);
		mHistoryTextView.setMovementMethod(new ScrollingMovementMethod());
//		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Koenigtype-reg.ttf");
//		mHistoryTextView.setTypeface(tf);

		return v;
	}

	public void digitClick(View view) {

		Button currentDigit = (Button) view;
		int digit = Integer.parseInt(currentDigit.getText().toString());

		if (mIsDot) {
			mPowerCount++;
			mCurrentNumber = mCurrentNumber + digit / (Math.pow(10, mPowerCount));
		} else {
			if (mIsNew) {
				mCurrentNumber = digit;
			} else {
				mCurrentNumber = mCurrentNumber * 10 + digit;
			}
		}

		mIsNew = false;

		MyFormatter mf = MyFormatter.get();
		String currentNumberString = "";
		if (mPowerCount > 0) {
			currentNumberString = mf.formatDouble(mCurrentNumber, mPowerCount);
		} else {
			currentNumberString = mf.formatDouble(mCurrentNumber);
		}

		mNowTypingTextView.setText(currentNumberString);

	}

	private void clearAll() {
		mIsNew = true;
		mIsDot = false;
		mPowerCount = 0;
		mPreviousNumber = 0;
		mCurrentNumber = 0;


		mCurrentOperation = operationType.NOTHING;
	}


	private View.OnClickListener operationAction(final operationType type) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyFormatter mf = MyFormatter.get();
				if (mCurrentOperation == operationType.NOTHING) {
					mPreviousNumber = mCurrentNumber;
				} else {
					if (mCurrentOperation == operationType.PLUS) {
						mResult = mPreviousNumber + mCurrentNumber;
					} else if (mCurrentOperation == operationType.MINUS) {
						mResult = mPreviousNumber - mCurrentNumber;
					} else if (mCurrentOperation == operationType.MULTIPLY) {
						mResult = mPreviousNumber * mCurrentNumber;
					} else if (mCurrentOperation == operationType.DIVISION) {
						mResult = mPreviousNumber / mCurrentNumber;
					}

					CalculationHistory.get(getActivity()).addLine(mPreviousNumber,
							mCurrentOperation, mCurrentNumber, mPowerCount, mResult);

					mPreviousNumber = mResult;
				}
				mCurrentOperation = type;
				mPreviousPowerCount = mPowerCount;

				mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation);

				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());

				mCurrentNumber = 0;
				mIsDot = false;
				mIsNew = true;
				mPowerCount = 0;

				((Button) v).setTextColor(getResources().getColor(R.color.activeButtonTextColor));
				if (mActiveButton != null && mActiveButton !=  v) {
					mActiveButton.setTextColor(getResources().getColor(R.color.defaultButtonTextColor));
				}
				mActiveButton = (Button) v;
			}
		};
	}
}
