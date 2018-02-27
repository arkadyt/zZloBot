package com.surveyo.zzlobot;

import java.util.Random;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;


public class MyLimitativeTextWatcher implements TextWatcher {

	private final EditText mEditText;

	private final TextView mTextView;

	private final Context context;
	private final Random random = new Random();

	/**
	 * @param editText
	 *            Owner of this {@link MyLimitativeTextWatcher} instance.
	 * */
	public MyLimitativeTextWatcher(Context context, EditText editText) {
		mEditText = editText;
		this.context = context;
		mTextView = null;
	}

	/**
	 * @param editText
	 *            Owner of this {@link MyLimitativeTextWatcher} instance.
	 * 
	 * @param charsLeftTextView
	 *            Corresponding TextView required to display changes in EditText
	 *            characters amount. <br>
	 *            (100 minus EditText character count.)
	 * */
	public MyLimitativeTextWatcher(Context context, EditText editText,
			TextView charsLeftTextView) {
		mEditText = editText;
		this.context = context;
		mTextView = charsLeftTextView;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		switch (mEditText.getId()) {
		case R.id.slide_1_ed_superhits:
		case R.id.slide_2_ed_shield_turns:
		case R.id.slide_3_ed_potion_turns:
			mTextView.setText(count + " / 100");
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		switch (mEditText.getId()) {
		case R.id.slide_0_ed_botname:
			if (s.toString().equals("")) {
				mEditText.setText(context.getResources().getString(
						R.string.bot_name_prefix)
						+ (10000000 + random.nextInt(10000000)));
			}
			break;
		case R.id.slide_5_ed_wait_delay:
			setToValueIfLess(s, R.string.life_regen_wait_minimum);
			break;
		case R.id.slide_6_ed_purpose_a_wall_against_the_wall_how_many:
		case R.id.slide_6_ed_purpose_duels_how_many:
		case R.id.slide_6_ed_purpose_haunt_how_many_times:
		case R.id.slide_6_ed_purpose_survival_how_many:
			setToValueIfLess(s, R.string.runs_amount_minimum);
			break;
		case R.id.abmenu_freq_edit_ed_normal:
			setToValueIfLess(s, R.string.sleep_time_normal_minimum);
			break;
		case R.id.abmenu_freq_edit_ed_awaiting:
			setToValueIfLess(s, R.string.sleep_time_tournament_minimum);
			break;
		}
	}

	/**
	 * Sets EditText value to the given value if it's numeric value
	 * is less.
	 * 
	 * @param valueResId
	 *            Link to the String resource containing the <i>!numeric!</i>
	 *            value to check against.
	 * @param editable
	 *            Editable passed to the TextWatcher methods.
	 **/
	private void setToValueIfLess(Editable editable, int valueResId) {
		int input = editable.toString().equals("") ? 0 : Integer
				.parseInt(editable.toString());
		int minimumAllowed = Integer.parseInt(context.getResources().getString(
				valueResId));
		if (input < minimumAllowed) {
			mEditText.setText(minimumAllowed);
		}
	}

}
