package ru.kurtov.simplecalc;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends SingleFragmentActivity {

	DigitClickable mNewFragment;

	@Override
	protected Fragment createFragment() {
		Fragment fragment = new MainFragment();
		mNewFragment = (DigitClickable) fragment;
		return fragment;
	}

	public void digitClick(View v) {
		mNewFragment.digitClick(v);
	}
}
