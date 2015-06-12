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
import java.math.MathContext;
import java.util.ArrayList;

import ru.kurtov.simplecalc.Enums.formatType;
import ru.kurtov.simplecalc.Enums.operationType;
import ru.kurtov.simplecalc.Enums.specSymbol;
import ru.kurtov.simplecalc.Enums.memoryOperation;


public class MainFragment extends Fragment implements DigitClickable {

	private BigDecimal mCurrentNumber;
	private BigDecimal mPreviousNumber;
	private int mPowerCount;
	private BigDecimal mMemory;
	private boolean mIsNew;
	private boolean mIsDot;
	private ArrayList<BigDecimal> mStack;

	private BigDecimal mResult;

	private operationType mCurrentOperation;

	final static BigDecimal oneHundred = new BigDecimal("100");
	final static BigDecimal maxNumber = new BigDecimal("999999999");


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
		mCurrentNumber = BigDecimal.ZERO;
		mPreviousNumber = BigDecimal.ZERO;
		mResult = BigDecimal.ZERO;
		mMemory = BigDecimal.ZERO;

		mCalculationHistory = CalculationHistory.get(getActivity());

		mNowTypingTextView = (TextView) v.findViewById(R.id.nowTypingTextView);
//		mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mCurrentNumber));
		updateNowTypingTextView(mCurrentNumber);

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
//					mCurrentNumber = Math.sqrt(mResult);
					mCurrentNumber = sqrt(mResult);
				} else {
//					mCurrentNumber = Math.sqrt(mCurrentNumber);
					mCurrentNumber = sqrt(mCurrentNumber);
				}
//				mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mCurrentNumber));
				updateNowTypingTextView(mCurrentNumber);
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
					mResult = mPreviousNumber.multiply((BigDecimal.ONE.add(mCurrentNumber.divide(oneHundred))));
//					mResult = mPreviousNumber * (1 + mCurrentNumber / 100);
				} else if (mCurrentOperation == operationType.MINUS) {
					mResult = mPreviousNumber.multiply((BigDecimal.ONE.subtract(mCurrentNumber.divide(oneHundred))));
//					mResult = mPreviousNumber * (1 - mCurrentNumber / 100);
				} else if (mCurrentOperation == operationType.MULTIPLY || mCurrentOperation == operationType.DIVISION) {
					mResult = mCurrentNumber.multiply(mPreviousNumber.divide(oneHundred));
//					mResult = (mPreviousNumber / 100) * mCurrentNumber;
				} else if (mCurrentOperation == operationType.NOTHING) {
					mResult = mCurrentNumber.divide(oneHundred);
//					mResult = mCurrentNumber / 100;
				}
				mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation, mCurrentNumber, mResult, specSymbol.PERCENT);
				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mPreviousNumber = mResult;
				mCurrentOperation = operationType.NOTHING;
//				mCurrentNumber = 0;
				mCurrentNumber = BigDecimal.ZERO;
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
//				mCurrentNumber = mCurrentNumber * (-1);
				mCurrentNumber = mCurrentNumber.negate();
//				MyFormatter mf = MyFormatter.get();
//				mNowTypingTextView.setText(mf.formatBigDecimal(mCurrentNumber, mPowerCount));
				updateNowTypingTextView(mCurrentNumber);

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
					mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mCurrentNumber));
					mStack.remove(mStack.size() - 1);
				} else {
					mNowTypingTextView.setText("0");
//					mCurrentNumber = 0;
					mCurrentNumber = BigDecimal.ZERO;
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
//					mResult = 0;
					mResult = BigDecimal.ZERO;
//					mMemory = 0;
					mMemory = BigDecimal.ZERO;
					mCalculationHistory.addLine(mResult, memoryOperation.CLEAR);
					String memoryString = String.format("%s", mMyFormatter.formatBigDecimal(mMemory));
					mMemoryTextView.setText(memoryString);
					mMemoryRecallClearButton.setText("MR");
				} else {
					mCurrentNumber = mMemory;
					mIsNew = true;
					mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mCurrentNumber));
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
//					mMemory = mMemory + mResult;
					mMemory = mMemory.add(mResult);
					mCalculationHistory.addLine(mResult, memoryOperation.ADD);
				} else {
//					mMemory = mMemory + mCurrentNumber;
					mMemory = mMemory.add(mCurrentNumber);
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.ADD);
					mResult = mCurrentNumber;

				}
				String memoryString = String.format("%s", mMyFormatter.formatBigDecimal(mMemory));
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
//					mMemory = mMemory - mResult;
					mMemory = mMemory.subtract(mResult);
					mCalculationHistory.addLine(mResult, memoryOperation.SUBTRACT);
				} else {
//					mMemory = mMemory - mCurrentNumber;
					mMemory = mMemory.subtract(mCurrentNumber);
					mCalculationHistory.addLine(mCurrentNumber, memoryOperation.SUBTRACT);
					mResult = mCurrentNumber;
				}

				String memoryString = String.format("%s", mMyFormatter.formatBigDecimal(mMemory));
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
				mNowTypingTextView.setText("");
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
					if (!mCurrentNumber.equals(BigDecimal.ZERO) && mResult.equals(BigDecimal.ZERO)) {
						mResult = mCurrentNumber;
					}
//					if (mCurrentNumber != 0 && mResult == 0) {
//						mResult = mCurrentNumber;
//					}
					mCalculationHistory.addLine(mResult);

				} else {

					if (mCurrentOperation == operationType.PLUS) {
//						mResult = mPreviousNumber + mCurrentNumber;
						mResult = mPreviousNumber.add(mCurrentNumber);
					} else if (mCurrentOperation == operationType.MINUS) {
//						mResult = mPreviousNumber - mCurrentNumber;
						mResult = mPreviousNumber.subtract(mCurrentNumber);
					} else if (mCurrentOperation == operationType.MULTIPLY) {
//						mResult = mPreviousNumber * mCurrentNumber;
						mResult = mPreviousNumber.multiply(mCurrentNumber);
					} else if (mCurrentOperation == operationType.DIVISION) {
//						mResult = mPreviousNumber / mCurrentNumber;
						mResult = mPreviousNumber.divide(mCurrentNumber, MathContext.DECIMAL64);
					}

					mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation, mCurrentNumber, mResult);
					mPreviousNumber = mResult;
					mPowerCount = 0;
				}

				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());
				mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mResult));
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
				String oldText = mNowTypingTextView.getText().toString();
				if (mIsNew) {
					mNowTypingTextView.setText("0.");
				} else {
					mNowTypingTextView.setText(oldText + ".");
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
		String oldText = mNowTypingTextView.getText().toString();
		String newDigit = currentDigit.getText().toString();
		String newText = "";

		if (mCurrentNumber.compareTo(maxNumber) == 1) {
			return;
		}

		if (mIsNew) {
			newText = newDigit;
		} else {
			newText = oldText + newDigit;
		}

		mNowTypingTextView.setText(newText);
		mCurrentNumber = new BigDecimal(newText);
		mIsNew = false;
	}

	private void clearAll() {
		mIsNew = true;
		mIsDot = false;
		mPowerCount = 0;
//		mPreviousNumber = 0;
		mPreviousNumber = BigDecimal.ZERO;
//		mCurrentNumber = 0;
		mPreviousNumber = BigDecimal.ZERO;
//		mResult = 0;
		mPreviousNumber = BigDecimal.ZERO;
//		mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mCurrentNumber));
		updateNowTypingTextView(mCurrentNumber);

		mCurrentOperation = operationType.NOTHING;
	}


	private View.OnClickListener operationAction(final operationType type) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMemoryRecallClearButton.setText("MR");

				if (!mIsNew || mActiveButton == mMemoryRecallClearButton) {
					if (mCurrentOperation == operationType.NOTHING) {

						if (mCurrentNumber.equals(BigDecimal.ZERO) && !mResult.equals(BigDecimal.ZERO)) {
//						if (mCurrentNumber == 0 && mResult != 0) {
							mPreviousNumber = mResult;
						} else {
							mPreviousNumber = mCurrentNumber;
						}
					} else {
						if (mCurrentOperation == operationType.PLUS) {
//							mResult = mPreviousNumber + mCurrentNumber;
							mResult = mPreviousNumber.add(mCurrentNumber);
						} else if (mCurrentOperation == operationType.MINUS) {
//							mResult = mPreviousNumber - mCurrentNumber;
							mResult = mPreviousNumber.subtract(mCurrentNumber);
						} else if (mCurrentOperation == operationType.MULTIPLY) {
//							mResult = mPreviousNumber * mCurrentNumber;
							mResult = mPreviousNumber.multiply(mCurrentNumber);
						} else if (mCurrentOperation == operationType.DIVISION) {
//							mResult = mPreviousNumber / mCurrentNumber;
							mResult = mPreviousNumber.divide(mCurrentNumber);
						}
						mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation, mCurrentNumber, mResult);
//						mNowTypingTextView.setText(mMyFormatter.formatBigDecimal(mResult));
						updateNowTypingTextView(mResult);

						mPreviousNumber = mResult;
					}

				}

				mCurrentOperation = type;

				mCalculationHistory.addLine(mPreviousNumber, mCurrentOperation);

				mHistoryTextView.setText(mCalculationHistory.getCalculationHistory());

				mIsDot = false;
				mIsNew = true;
				mPowerCount = 0;
//				mResult = 0;
				mResult = BigDecimal.ZERO;

				((Button) v).setTextColor(getResources().getColor(R.color.activeButtonTextColor));
				if (mActiveButton != null && mActiveButton != v) {
					mActiveButton.setTextColor(getResources().getColor(R.color.defaultButtonTextColor));
				}
				mActiveButton = (Button) v;
			}
		};
	}

	public static BigDecimal sqrt(BigDecimal value) {
		BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
		return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
	}

	private void updateNowTypingTextView(BigDecimal bd) {
		String s = bd.stripTrailingZeros().toPlainString();
		mNowTypingTextView.setText(s);
	}
	private void updateNowTypingTextView(BigDecimal bd, boolean cutZeroes) {
		String s = bd.toPlainString();
		mNowTypingTextView.setText(s);
	}

}
