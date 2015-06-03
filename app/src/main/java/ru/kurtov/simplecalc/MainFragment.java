package ru.kurtov.simplecalc;

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

	public enum operationType { DIVIDE, MULTIPLY, MINUS, PLUS, NOTHING }
	private operationType mCurrentOperation;

	public enum formatType { NORMAL, TWO_DECIMALS, ONE_DECIMAL }
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


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		mFormatType = formatType.NORMAL;
		MyFormatter.get().setFormatType(mFormatType);

		mCalculationHistory = CalculationHistory.get(getActivity());

		clearAll();

		mSquareButton = (Button) v.findViewById(R.id.squareButton);
		mPercentButton = (Button) v.findViewById(R.id.percentButton);
		mChangeButton = (Button) v.findViewById(R.id.changeButton);


		mChangeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentNumber = mCurrentNumber * (-1);
				MyFormatter mf = MyFormatter.get();

				mNowTypingTextView.setText(mf.formatDouble(mCurrentNumber, mPowerCount));
			}
		});


		mBackspaceButton = (Button) v.findViewById(R.id.backspaceButton);

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
		mDivideButton.setOnClickListener(operationAction(operationType.DIVIDE));
		mMultiplyButton = (Button) v.findViewById(R.id.multiplyButton);
		mMultiplyButton.setOnClickListener(operationAction(operationType.MULTIPLY));
		mMultiplyButton.setText(Character.toString((char)215));
		mMinusButton = (Button) v.findViewById(R.id.minusButton);
		mMinusButton.setOnClickListener(operationAction(operationType.MINUS));
		mPlusButton = (Button) v.findViewById(R.id.plusButton);
		mPlusButton.setOnClickListener(operationAction(operationType.PLUS));

		mEqualsButton = (Button) v.findViewById(R.id.equalsButton);


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
					} else if (mCurrentOperation == operationType.DIVIDE) {
						mResult = mPreviousNumber / mCurrentNumber;
					}

					CalculationHistory.get(getActivity()).addLine(mPreviousNumber,
							mCurrentOperation, mCurrentNumber, mPowerCount, mResult);
//					String topString = String.format("%s %s = %s",
//							mHistoryTextView.getText(),
//							mf.formatDouble(mCurrentNumber),
//							mf.formatDouble(mResult));

//					mHistoryTextView.setText(topString);
					mPreviousNumber = mResult;


				}
				mCurrentOperation = type;
				mPreviousPowerCount = mPowerCount;
//				String newHistoryText = "";
//				if (mHistoryTextView.getText().equals("")) {
//					newHistoryText = String.format("%s %s",
//							mf.formatDouble(mPreviousNumber),
//							mf.getOperationToString(mCurrentOperation));
//				} else {
//					newHistoryText = String.format("%s\n%s %s",
//							mHistoryTextView.getText(),
//							mf.formatDouble(mPreviousNumber),
//							mf.getOperationToString(mCurrentOperation));
//				}

				mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation);

//				String testHistory = mCalculationHistory.getCalculationHistory();
//				mHistoryTextView.setText(newHistoryText);
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
