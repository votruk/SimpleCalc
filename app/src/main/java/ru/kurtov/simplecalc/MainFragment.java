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

import java.math.BigDecimal;
import java.util.ArrayList;

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
	private ArrayList<Double> mStack;

	private double mResult;

	private operationType mCurrentOperation;

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
	private TextView mMemoryTextView;


	private CalculationHistory mCalculationHistory;
	private MyFormatter mMyFormatter;



	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		mFormatType = Enums.formatType.NORMAL;
		mMyFormatter = MyFormatter.get();
		mMyFormatter.setFormatType(mFormatType);
		mStack = new ArrayList<>();

		mCalculationHistory = CalculationHistory.get(getActivity());

		mNowTypingTextView = (TextView) v.findViewById(R.id.nowTypingTextView);
		mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));

		mHistoryTextView = (TextView) v.findViewById(R.id.historyTextView);
		mHistoryTextView.setMovementMethod(new ScrollingMovementMethod());

		mMemoryTextView = (TextView) v.findViewById(R.id.memoryTextView);
		mMemoryTextView.setText("0");

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
		mBackspaceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mStack.isEmpty()) {
					mCurrentNumber = mStack.get(mStack.size() - 1);
					mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));
					mStack.remove(mStack.size() - 1);
				} else {
					mNowTypingTextView.setText("0");
					mCurrentNumber = 0;
					mPowerCount = 0;
					mIsNew = true;
				}
			}
		});


		mMemoryRecallClearButton = (Button) v.findViewById(R.id.memoryRecallClearButton);
		mMemoryRecallClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mActiveButton == v) {
					mResult = 0;
					mMemory = 0;
					mCalculationHistory.addLine(mResult, memoryOperation.CLEAR);
					String memoryString = String.format("%s", mMyFormatter.formatDouble(mMemory));
					mMemoryTextView.setText(memoryString);
					mMemoryRecallClearButton.setText("MR");
				} else {
					mCurrentNumber = mMemory;
					mIsNew = true;
					mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.RECALL);
					mResult = mMemory;
					mMemoryRecallClearButton.setText("MC");
				}
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mActiveButton = (Button) v;
				mCurrentOperation = operationType.NOTHING;

			}
		});

		mMemoryAddButton = (Button) v.findViewById(R.id.memoryAddButton);
		mMemoryAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsNew) {
					mMemory = mMemory + mResult;
					mCalculationHistory.addLine(mResult, memoryOperation.ADD);
				} else {
					mMemory = mMemory + mCurrentNumber;
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.ADD);
					mResult = mCurrentNumber;

				}
				String memoryString = String.format("%s", mMyFormatter.formatDouble(mMemory));
				mMemoryTextView.setText(memoryString);
				mIsNew = true;
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mActiveButton = (Button) v;
				mMemoryRecallClearButton.setText("MR");


			}
		});

		mMemorySubtractButton = (Button) v.findViewById(R.id.memorySubtractButton);
		mMemorySubtractButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsNew) {
					mMemory = mMemory - mResult;
					mCalculationHistory.addLine(mResult, memoryOperation.SUBTRACT);
				} else {
					mMemory = mMemory - mCurrentNumber;
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.SUBTRACT);
					mResult = mCurrentNumber;
				}

				String memoryString = String.format("%s", mMyFormatter.formatDouble(mMemory));
				mMemoryTextView.setText(memoryString);

				mIsNew = true;
//				mCalculationHistory.addLine(mCurrentNumber, memoryOperation.SUBTRACT);
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mActiveButton = (Button) v;
				mMemoryRecallClearButton.setText("MR");


			}
		});

		mClearButton = (Button) v.findViewById(R.id.clearButton);
		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAll();
//				mNowTypingTextView.setText("");
				mActiveButton = (Button) v;
				mMemoryRecallClearButton.setText("MR");

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
				if (mActiveButton != null) {
					mActiveButton.setTextColor(getResources().getColor(R.color.defaultButtonTextColor));
					mActiveButton = null;
				}
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
				mActiveButton = (Button) v;
				mMemoryRecallClearButton.setText("MR");

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
				mMemoryRecallClearButton.setText("MR");

			}
		});


//		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Koenigtype-reg.ttf");
//		mHistoryTextView.setTypeface(tf);

		return v;
	}

	public void digitClick(View view) {
		mMemoryRecallClearButton.setText("MR");

		Button currentDigit = (Button) view;
		int digit = Integer.parseInt(currentDigit.getText().toString());

		BigDecimal bgDigit = new BigDecimal(currentDigit.getText().toString());
		if (mCurrentNumber >= 1000000000) {
			return;
		}
		if (mIsDot) {
			mPowerCount++;
			mStack.add(mCurrentNumber);
			mCurrentNumber = mCurrentNumber + digit / (Math.pow(10, mPowerCount));
		} else {
			if (mIsNew) {
				mCurrentNumber = digit;
				mStack.clear();
				if (mActiveButton != null) {
					if (mActiveButton == mEqualsButton) {
						mCurrentOperation = operationType.NOTHING;
					}
					mActiveButton.setTextColor(getResources().getColor(R.color.defaultButtonTextColor));
					mActiveButton = null;
				}
			} else {
				mStack.add(mCurrentNumber);
				mCurrentNumber = mCurrentNumber * 10 + digit;
			}
		}

		mIsNew = false;

		MyFormatter mf = MyFormatter.get();
		String currentNumberString;
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
		mResult = 0;
		mNowTypingTextView.setText(mMyFormatter.formatDouble(mCurrentNumber));


		mCurrentOperation = operationType.NOTHING;
	}


	private View.OnClickListener operationAction(final operationType type) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMemoryRecallClearButton.setText("MR");

				if (!mIsNew || mActiveButton == mMemoryRecallClearButton) {
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
