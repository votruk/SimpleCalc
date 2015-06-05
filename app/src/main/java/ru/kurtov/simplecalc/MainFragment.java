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

import ru.kurtov.simplecalc.Enums.formatType;
import ru.kurtov.simplecalc.Enums.operationType;
import ru.kurtov.simplecalc.Enums.specSymbol;
import ru.kurtov.simplecalc.Enums.memoryOperation;


public class MainFragment extends Fragment implements DigitClickable {

	private double mCurrentNumber;
	private double mPreviousNumber;
	private int mPowerCount;
	private double mMemory;
	private boolean mIsNew;
	private boolean mIsDot;

	private double mResult;

	private operationType mCurrentOperation;
	private operationType mPreviousOperation;

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
		mFormatType = Enums.formatType.NORMAL;
		mMyFormatter = MyFormatter.get();
		mMyFormatter.setFormatType(mFormatType);

		mCalculationHistory = CalculationHistory.get(getActivity());

		mNowTypingTextView = (TextView) v.findViewById(R.id.nowTypingTextView);
		mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));

		mHistoryTextView = (TextView) v.findViewById(R.id.historyTextView);
		mHistoryTextView.setMovementMethod(new ScrollingMovementMethod());

		clearAll();

		mSquareButton = (Button) v.findViewById(R.id.squareButton);
		mSquareButton.setText(mMyFormatter.getSymbolToString(specSymbol.SQUARE));
		mSquareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsNew) {
					mCurrentNumber = Math.sqrt(mResult);
				} else {
					mCurrentNumber = Math.sqrt(mCurrentNumber);
				}
				mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));
				mPreviousNumber = mCurrentNumber;

				mActiveButton = (Button) v;
			}
		});


		mPercentButton = (Button) v.findViewById(R.id.percentButton);
		mPercentButton.setText(mMyFormatter.getSymbolToString(specSymbol.PERCENT));
		mPercentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentOperation == operationType.PLUS) {
					mResult = mPreviousNumber * (1 + mCurrentNumber / 100);
				} else if (mCurrentOperation == operationType.MINUS) {
					mResult = mPreviousNumber * (1 - mCurrentNumber / 100);
				} else if (mCurrentOperation == operationType.MULTIPLY || mCurrentOperation == operationType.DIVISION) {
					mResult = (mPreviousNumber / 100) * mCurrentNumber;
				} else if (mCurrentOperation == operationType.NOTHING) {
					mResult = mCurrentNumber / 100;
				}
				mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation, mCurrentNumber, mResult, specSymbol.PERCENT);
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mPreviousNumber = mResult;
				mCurrentOperation = operationType.NOTHING;
				mCurrentNumber = 0;
				mIsDot = false;
				mIsNew = true;
				mPowerCount = 0;

				mActiveButton = (Button) v;

			}
		});


		mChangeButton = (Button) v.findViewById(R.id.changeButton);
		mChangeButton.setText(mMyFormatter.getSymbolToString(specSymbol.PLUS_MINUS));
		mChangeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentNumber = mCurrentNumber * (-1);
				MyFormatter mf = MyFormatter.get();

				mNowTypingTextView.setText(mf.formatDouble(mCurrentNumber, mPowerCount));

				mActiveButton = (Button) v;

			}
		});


		mBackspaceButton = (Button) v.findViewById(R.id.backspaceButton);
		mBackspaceButton.setText(mMyFormatter.getSymbolToString(specSymbol.BACKSPACE));


		mMemoryRecallClearButton = (Button) v.findViewById(R.id.memoryRecallClearButton);
		mMemoryRecallClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mActiveButton == v) {
					mResult = 0;
					mCalculationHistory.addLine(mResult, memoryOperation.CLEAR);
				} else {
					mCurrentNumber = mMemory;
					mIsNew = true;
					mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.RECALL);
				}
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mActiveButton = (Button) v;

			}
		});

		mMemoryAddButton = (Button) v.findViewById(R.id.memoryAddButton);
		mMemoryAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsNew) {
					mMemory = mMemory - mResult;
					mCalculationHistory.addLine(mResult, memoryOperation.ADD);
				} else {
					mMemory = mMemory - mCurrentNumber;
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.ADD);
				}
				mIsNew = true;
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mActiveButton = (Button) v;
			}
		});

		mMemorySubtractButton = (Button) v.findViewById(R.id.memorySubtractButton);
		mMemorySubtractButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsNew) {
					mMemory = mMemory - mResult;
					mCalculationHistory.addLine(mResult, memoryOperation.ADD);
				} else {
					mMemory = mMemory - mCurrentNumber;
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.ADD);
				}
				mIsNew = true;
				mCalculationHistory.addLine(mCurrentNumber, memoryOperation.ADD);
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mActiveButton = (Button) v;
			}
		});

		mClearButton = (Button) v.findViewById(R.id.clearButton);
		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAll();
				mNowTypingTextView.setText("");
				mActiveButton = (Button) v;
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
		mEqualsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActiveButton.setTextColor(getResources().getColor(R.color.defaultButtonTextColor));
				if (mCurrentOperation == operationType.NOTHING) {
					if (mCurrentNumber != 0 && mResult == 0) {
						mResult = mCurrentNumber;
					}
					mCalculationHistory.addLine(mResult);

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

					mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation, mCurrentNumber, mResult);
					mPreviousNumber = mResult;
					mPowerCount = 0;
				}

				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mNowTypingTextView.setText(mMyFormatter.formatDouble(mResult));
				mIsNew = true;
				mIsDot = false;

			}
		});


		mDotButton = (Button) v.findViewById(R.id.dotButton);
		mDotButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsNew) {
					mNowTypingTextView.setText("0.");
				}
				mIsDot = true;
				mIsNew = false;
			}
		});


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
		mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));


		mCurrentOperation = operationType.NOTHING;
	}


	private View.OnClickListener operationAction(final operationType type) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsNew) {
					if (mCurrentOperation == operationType.NOTHING) {
						if (mCurrentNumber == 0 && mResult != 0) {
							mPreviousNumber = mResult;
						} else {
							mPreviousNumber = mCurrentNumber;
						}
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
						mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation, mCurrentNumber, mResult);
						mNowTypingTextView.setText(mMyFormatter.formatDouble(mResult));

						mPreviousNumber = mResult;
					}

				}

				mCurrentOperation = type;

				mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation);

				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());

				mIsDot = false;
				mIsNew = true;
				mPowerCount = 0;
				mResult = 0;

				((Button) v).setTextColor(getResources().getColor(R.color.activeButtonTextColor));
				if (mActiveButton != null && mActiveButton != v) {
					mActiveButton.setTextColor(getResources().getColor(R.color.defaultButtonTextColor));
				}
				mActiveButton = (Button) v;
			}
		};
	}
}
