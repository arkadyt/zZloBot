package com.surveyo.zzlobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

/* Bot = Macro TODO */
public class MainActivity extends Activity implements OnClickListener,
		OnMenuItemClickListener {

	private ViewAnimator mViewAnimator;
	private LayoutInflater mLayoutInflater;
	private Toast mToast;
	private CountDownTimer mBackKeyCountDownTimer;
	private SharedPreferences mSharedPrefs;
	private Editor mSharedPrefsEditor;
	private File mFileToDelete; // TODO wtf???
	private AlertED aed;
	private ABMenu abm;

	/* VIEWS */
	private EditText mBotnameEditText, mHitsEditText, mShieldTurnsEditText,
			mPotionsTurnsEditText, mLifeRegenWaitEditText,
			mMonsterPosInMenuEditText, mMonsterRunsEditText, mDuelRunsEditText,
			mSurvivalRunsEditText, mWallRunsEditText,
			mBotSleepTimeNormalEditText, mBotSleepTimeTournamentEditText;
	private CheckBox mWinOnlyView;
	private RadioGroup mTickDialogRadioGroup, mOkDialogRadioGroup,
			mLifeRegenRadioGroup, mBotGoalRadioGroup;
	private Button mNewBotButton, mOpenMacroButton;
	private ListView mListView;
	private TextView mCharsEnteredHitsSlideTextView,
			mCharsEnteredShieldSlideTextView,
			mCharsEnteredPotionsSlideTextView, mBotModifyWindowBotNameTextView,
			mBotModifyWindowHitsTextView, mBotModifyWindowShieldTurnsTextView,
			mBotModifyWindowPotionTurnsTextView, ssu_diag, ssu_afa, ssu_bp,
			mBotModifyWindowIsWinOnlyTextView, mBotModifyWindowHeader,
			mEmptyListViewTextView, mLifeRegenWaitDescTextView,
			mSleepTimesTxtValuesWhereFromTextView;
	TextView mAppLabelStatus;
	private WebView mWebView;
	private FrameLayout mFrameLayout;
	private Button mMobsHintButton;
	private ProgressDialog mAssemblyProgressDialog;
	private View mToastView;

	private Macro mMacro;

	// Used to delete old macro file when it's renamed. TODO
	private String mPreviousBotName;
	private int mBackKeyPressCount;
	private int mMenuAbState = MenuState.BACK_NEXT, mLastDisplayedLayer = 0;

	private MenuItem mMenuButtonLeft, mMenuButtonRight;

	public ViewAnimator getViewAnimator() {
		return mViewAnimator;
	}

	public Macro getCurrentMacro() {
		return mMacro;
	}

	public ProgressDialog getAssemblyProgressDialog() {
		return mAssemblyProgressDialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ThemeWorking);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.layout_loader);
		getActionBar().show();
		getActionBar()
				.setBackgroundDrawable(
						getResources().getDrawable(
								R.drawable.ab_transparent_dark_holo));
		getActionBar().setCustomView(
				LayoutInflater.from(this)
						.inflate(R.layout.ab_custom_view, null));
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setDisplayShowHomeEnabled(false);

		mSharedPrefs = getPreferences(MODE_PRIVATE);
		mSharedPrefsEditor = mSharedPrefs.edit();

		mViewAnimator = (ViewAnimator) findViewById(R.id.lhf_anim_layer);
		mViewAnimator.showPrevious();

		mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		mMenuAbState = MenuState.BACK_NEXT;
		invalidateOptionsMenu();

		mMobsHintButton = (Button) findViewById(R.id.slide_6_btn_bots_numbering);
		mMobsHintButton.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.files_viewing_main_list_view);

		mFrameLayout = (FrameLayout) findViewById(R.id.loader_wrapping_frame_layout);
		mFrameLayout.getForeground().setAlpha(0);

		((ImageButton) findViewById(R.id.ab_btn_menu)).setOnClickListener(this);

		((LinearLayout) findViewById(R.id.ssu_btn_after_fight_action))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_bot_purpose))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_botname))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_dialogs))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_must_win_only))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_potions))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_shieldturns))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ssu_btn_superhits))
				.setOnClickListener(this);

		ssu_afa = (TextView) findViewById(R.id.ssu_after_fight_action);
		mBotModifyWindowBotNameTextView = (TextView) findViewById(R.id.ssu_bot_name);
		ssu_bp = (TextView) findViewById(R.id.ssu_bot_purpose);
		mBotModifyWindowShieldTurnsTextView = (TextView) findViewById(R.id.ssu_shield_turns);
		ssu_diag = (TextView) findViewById(R.id.ssu_dialogs);
		mBotModifyWindowPotionTurnsTextView = (TextView) findViewById(R.id.ssu_potion_turns);
		mBotModifyWindowHitsTextView = (TextView) findViewById(R.id.ssu_super_hits);
		mBotModifyWindowIsWinOnlyTextView = (TextView) findViewById(R.id.ssu_must_win_only);
		mBotModifyWindowHeader = (TextView) findViewById(R.id.ssu_header_with_file_name);

		mAppLabelStatus = (TextView) findViewById(R.id.ab_label_status);
		mAppLabelStatus.setText("�����");

		mLifeRegenWaitDescTextView = (TextView) findViewById(R.id.slide_5_txt_above_ed_delay);
		mSleepTimesTxtValuesWhereFromTextView = (TextView) findViewById(R.id.sleeping_times_txt_values_wherefrom);
		mCharsEnteredHitsSlideTextView = (TextView) findViewById(R.id.slide_1_hmany_turns_configured_tv);
		mCharsEnteredShieldSlideTextView = (TextView) findViewById(R.id.slide_2_hmany_turns_configured_tv);
		mCharsEnteredPotionsSlideTextView = (TextView) findViewById(R.id.slide_3_hmany_turns_configured_tv);
		mOpenMacroButton = (Button) findViewById(R.id.btn_remote_lhf);
		mNewBotButton = (Button) findViewById(R.id.btn_single_lhf);
		mBotnameEditText = (EditText) findViewById(R.id.slide_0_ed_botname);
		mHitsEditText = (EditText) findViewById(R.id.slide_1_ed_superhits);
		mShieldTurnsEditText = (EditText) findViewById(R.id.slide_2_ed_shield_turns);
		mPotionsTurnsEditText = (EditText) findViewById(R.id.slide_3_ed_potion_turns);
		mTickDialogRadioGroup = (RadioGroup) findViewById(R.id.slide_4_radiogroup_edgalochka);
		mOkDialogRadioGroup = (RadioGroup) findViewById(R.id.slide_4_radiogroup_okdialog);
		mLifeRegenWaitEditText = (EditText) findViewById(R.id.slide_5_ed_wait_delay);
		mLifeRegenRadioGroup = (RadioGroup) findViewById(R.id.slide_5_rg_on_battle_finished);
		mBotGoalRadioGroup = (RadioGroup) findViewById(R.id.slide_6_rgroup_bot_purpose);
		mWinOnlyView = (CheckBox) findViewById(R.id.slide_6_cbox_must_win_only);
		mDuelRunsEditText = (EditText) findViewById(R.id.slide_6_ed_purpose_duels_how_many);
		mSurvivalRunsEditText = (EditText) findViewById(R.id.slide_6_ed_purpose_survival_how_many);
		mMonsterRunsEditText = (EditText) findViewById(R.id.slide_6_ed_purpose_haunt_how_many_times);
		mWallRunsEditText = (EditText) findViewById(R.id.slide_6_ed_purpose_a_wall_against_the_wall_how_many);
		mMonsterPosInMenuEditText = (EditText) findViewById(R.id.slide_6_ed_purpose_haunt_mob_number);
		mBotSleepTimeNormalEditText = (EditText) findViewById(R.id.abmenu_freq_edit_ed_normal);
		mBotSleepTimeTournamentEditText = (EditText) findViewById(R.id.abmenu_freq_edit_ed_awaiting);
		mEmptyListViewTextView = ((TextView) findViewById(R.id.txtEmpty));

		mOpenMacroButton.setOnClickListener(this);
		mNewBotButton.setOnClickListener(this);

		abm = new ABMenu(this);
		abm.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		mHitsEditText.addTextChangedListener(new MyLimitativeTextWatcher(
				getApplicationContext(), mHitsEditText,
				mCharsEnteredHitsSlideTextView));
		mShieldTurnsEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(), mShieldTurnsEditText,
						mCharsEnteredShieldSlideTextView));
		mPotionsTurnsEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(), mPotionsTurnsEditText,
						mCharsEnteredPotionsSlideTextView));

		mBotnameEditText.addTextChangedListener(new MyLimitativeTextWatcher(
				getApplicationContext(), mBotnameEditText));
		mBotSleepTimeNormalEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(), mBotSleepTimeNormalEditText));
		mBotSleepTimeTournamentEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(),
						mBotSleepTimeTournamentEditText));
		mLifeRegenWaitEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(), mLifeRegenWaitEditText));
		mDuelRunsEditText.addTextChangedListener(new MyLimitativeTextWatcher(
				getApplicationContext(), mDuelRunsEditText));
		mMonsterRunsEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(), mMonsterRunsEditText));
		mSurvivalRunsEditText
				.addTextChangedListener(new MyLimitativeTextWatcher(
						getApplicationContext(), mSurvivalRunsEditText));
		mWallRunsEditText.addTextChangedListener(new MyLimitativeTextWatcher(
				getApplicationContext(), mWallRunsEditText));

		mWebView = (WebView) findViewById(R.id.marquee_line);
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.loadDataWithBaseURL(null, Constants.HTML_MARQUEE_MAIN_SCREEN,
				Constants.HTML_MIME_TYPE, Constants.HTML_ENCODING, null);

		mToast = new Toast(this);
		mToast.setDuration(Toast.LENGTH_LONG);

		mToastView = mLayoutInflater.inflate(R.layout.layout_toast_welcome,
				null);
		((TextView) mToastView.findViewById(R.id.toast_tv))
				.setText("Welcome to zZlobot v1.0.13");
		((TextView) mToastView.findViewById(R.id.toast_tv))
				.setTypeface(Typeface.createFromAsset(getAssets(),
						"fonts/impact.ttf"));
		mToast.setView(mToastView);
		mToast.show();

		mBackKeyCountDownTimer = new CountDownTimer(3000, 1) {

			@Override
			public void onTick(long arg0) {
			}

			@Override
			public void onFinish() {
				mBackKeyPressCount = 0;
			}
		};
		mCharsEnteredHitsSlideTextView.setText(mHitsEditText.getText()
				.toString().length()
				+ "/100");
		mCharsEnteredShieldSlideTextView.setText(mShieldTurnsEditText.getText()
				.toString().length()
				+ "/100");
		mCharsEnteredPotionsSlideTextView.setText(mPotionsTurnsEditText
				.getText().toString().length()
				+ "/100");

		switch (mLifeRegenRadioGroup.getCheckedRadioButtonId()) {
		case R.id.slide_5_rb_wait_for_full_hp:
			mLifeRegenWaitEditText.setEnabled(false);
			mLifeRegenWaitDescTextView.setTextColor(getResources().getColor(
					R.color.grey_color));
			break;
		case R.id.slide_5_rb_wait_delay:
			mLifeRegenWaitEditText.setEnabled(true);
			mLifeRegenWaitDescTextView.setTextColor(getResources().getColor(
					R.color.digits_color));
			break;
		}

		mLifeRegenRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						switch (arg1) {
						case R.id.slide_5_rb_wait_delay:
							mLifeRegenWaitEditText.setEnabled(true);
							mLifeRegenWaitDescTextView
									.setTextColor(getResources().getColor(
											R.color.digits_color));
							break;
						case R.id.slide_5_rb_wait_for_full_hp:
							mLifeRegenWaitEditText.setEnabled(false);
							mLifeRegenWaitDescTextView
									.setTextColor(getResources().getColor(
											R.color.grey_color));
							break;
						}
					}
				});
		
		switch (mBotGoalRadioGroup.getCheckedRadioButtonId()) {
		case R.id.slide_6_rb_purpose_haunt:
			mDuelRunsEditText.setEnabled(false);
			mSurvivalRunsEditText.setEnabled(false);
			mWallRunsEditText.setEnabled(false);
			break;
		case R.id.slide_6_rb_purpose_duels:
			mMonsterRunsEditText.setEnabled(false);
			mMonsterPosInMenuEditText.setEnabled(false);
			mSurvivalRunsEditText.setEnabled(false);
			mWallRunsEditText.setEnabled(false);
			break;
		case R.id.slide_6_rb_purpose_survival:
			mDuelRunsEditText.setEnabled(false);
			mMonsterRunsEditText.setEnabled(false);
			mMonsterPosInMenuEditText.setEnabled(false);
			mWallRunsEditText.setEnabled(false);
			break;
		case R.id.slide_6_rb_purpose_a_wall_against_the_wall:
			mDuelRunsEditText.setEnabled(false);
			mMonsterRunsEditText.setEnabled(false);
			mMonsterPosInMenuEditText.setEnabled(false);
			mSurvivalRunsEditText.setEnabled(false);
			break;
		}

		mBotGoalRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						switch (arg1) {
						case R.id.slide_6_rb_purpose_haunt:
							mMonsterRunsEditText.setEnabled(true);
							mMonsterPosInMenuEditText.setEnabled(true);
							mDuelRunsEditText.setEnabled(false);
							mSurvivalRunsEditText.setEnabled(false);
							mWallRunsEditText.setEnabled(false);
							break;
						case R.id.slide_6_rb_purpose_duels:
							mDuelRunsEditText.setEnabled(true);
							mMonsterRunsEditText.setEnabled(false);
							mMonsterPosInMenuEditText.setEnabled(false);
							mSurvivalRunsEditText.setEnabled(false);
							mWallRunsEditText.setEnabled(false);
							break;
						case R.id.slide_6_rb_purpose_survival:
							mSurvivalRunsEditText.setEnabled(true);
							mDuelRunsEditText.setEnabled(false);
							mMonsterRunsEditText.setEnabled(false);
							mMonsterPosInMenuEditText.setEnabled(false);
							mWallRunsEditText.setEnabled(false);
							break;
						case R.id.slide_6_rb_purpose_a_wall_against_the_wall:
							mWallRunsEditText.setEnabled(true);
							mDuelRunsEditText.setEnabled(false);
							mMonsterRunsEditText.setEnabled(false);
							mMonsterPosInMenuEditText.setEnabled(false);
							mSurvivalRunsEditText.setEnabled(false);
							break;
						}
					}
				});
		
		setTextTypeface();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_single_lhf:
			switchLayer(R.anim.out__slide_to_left, R.anim.in__slide_from_right,
					Layer.BOTNAME);
			break;
		case R.id.btn_remote_lhf:
			abm.openMacro();
			break;
		case R.id.ab_btn_menu:
			abm.show(); 
			break;
		case R.id.slide_6_btn_bots_numbering:
			Mint mint = new Mint(this);
			mint.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
			mint.show();
			break;
		case R.id.ssu_btn_after_fight_action:
			aed = new AlertED(Constants.CLIP_TYPE_AFTER_FIGHT_ACTION);
			aed.show(getFragmentManager(), null);
			break;
		case R.id.ssu_btn_bot_purpose:
			aed = new AlertED(Constants.CLIP_TYPE_BOT_PURPOSE);
			aed.show(getFragmentManager(), null);
			break;
		case R.id.ssu_btn_botname:
			aed = new AlertED(Constants.CLIP_TYPE_BOT_NAME);
			aed.show(getFragmentManager(), null);
			break;
		case R.id.ssu_btn_dialogs:
			aed = new AlertED(Constants.CLIP_TYPE_DIALOGS);
			aed.show(getFragmentManager(), null);
			break;
		case R.id.ssu_btn_must_win_only:
			mMacro.setCountingWinsOnly(!mMacro.isCountingWinsOnly());
			mBotModifyWindowIsWinOnlyTextView.setText(mMacro
					.isCountingWinsOnly() ? "��" : "���");
			break;
		case R.id.ssu_btn_potions:
			aed = new AlertED(Constants.CLIP_TYPE_POTIONS);
			aed.show(getFragmentManager(), null);
			break;
		case R.id.ssu_btn_shieldturns:
			aed = new AlertED(Constants.CLIP_TYPE_SHIELD_TURNS);
			aed.show(getFragmentManager(), null);
			break;
		case R.id.ssu_btn_superhits:
			aed = new AlertED(Constants.CLIP_TYPE_SUPER_HITS);
			aed.show(getFragmentManager(), null);
			break;
		}
	}

	/**
	 * Switch to the layer of choice.
	 * 
	 * @param outAnimResource
	 *            Move out animation
	 * @param inAnimResource
	 *            Move in animation
	 * @param toWhichLayer
	 *            See indices in {@link Layer} class
	 * */
	private void switchLayer(int outAnimResource, int inAnimResource,
			int toWhichLayer) {
		mLastDisplayedLayer = mViewAnimator.getDisplayedChild();

		mViewAnimator.setOutAnimation(getApplicationContext(), outAnimResource);
		mViewAnimator.setInAnimation(getApplicationContext(), inAnimResource);
		mViewAnimator.setDisplayedChild(toWhichLayer);

		mMenuAbState = getRespectiveMenuState();

		invalidateOptionsMenu();
	}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem) {
		switch (mMenuAbState) {
		// state of bottom buttons. LEFT_RIGHT
		case MenuState.NONE_CLOSE:
			if (menuItem.getItemId() == R.id.menu_button_right) {
				switchLayer(R.anim.out__slide_to_bottom,
						R.anim.in__slide_from_top, Layer.HOME);
			}
			break;
		case MenuState.BACK_NEXT:
			readUserInput();
			if (menuItem.getItemId() == R.id.menu_button_left) {
				switchLayer(R.anim.out__slide_to_right,
						R.anim.in__slide_from_left,
						mViewAnimator.getDisplayedChild() - 1);
			} else if (menuItem.getItemId() == R.id.menu_button_right) {
				switchLayer(R.anim.out__slide_to_right,
						R.anim.in__slide_from_left,
						mViewAnimator.getDisplayedChild() + 1);
			}
			break;
		case MenuState.README_ABOUT:
			if (menuItem.getItemId() == R.id.menu_button_left) {
				abm.showReadMe();
			} else if (menuItem.getItemId() == R.id.menu_button_right) {
				AboutClip ac = new AboutClip(this);
				ac.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				ac.show();
			}
			break;
		case MenuState.BACK_ASSEMBLE:
			readUserInput();
			if (menuItem.getItemId() == R.id.menu_button_left) {
				switchLayer(R.anim.out__slide_to_right,
						R.anim.in__slide_from_left,
						mViewAnimator.getDisplayedChild() - 1);
			} else if (menuItem.getItemId() == R.id.menu_button_right) {
				configureMacro();
			}
			break;
		case MenuState.CLOSE_REASSEMBLE:
			if (menuItem.getItemId() == R.id.menu_button_left) {
				switchLayer(R.anim.out__slide_to_bottom,
						R.anim.in__slide_from_top, Layer.HOME);
			} else if (menuItem.getItemId() == R.id.menu_button_right) {
				readUserInput();
				configureMacro();
			}
			break;
		case MenuState.CANCEL_SAVE:
			if (menuItem.getItemId() == R.id.menu_button_left) {
				showMyToast("�������� ��������");
				switchLayer(R.anim.out__slide_to_bottom,
						R.anim.in__slide_from_top, mLastDisplayedLayer);
			} else if (menuItem.getItemId() == R.id.menu_button_right) {
				readUserInput();

				mSharedPrefsEditor.putInt(
						Constants.SPREFSKEY_BOT_SLEEP_TIME_NORMAL,
						mMacro.getSleepTimeNormal());
				mSharedPrefsEditor.putInt(
						Constants.SPREFSKEY_BOT_SLEEP_TIME_TOURNAMENT,
						mMacro.getSleepTimeTournament());
				mSharedPrefsEditor.commit();

				showMyToast("���������");
			}
			switchLayer(R.anim.out__slide_to_bottom, R.anim.in__slide_from_top,
					mLastDisplayedLayer);
			break;
		}
		invalidateOptionsMenu();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.loader, menu);

		mMenuButtonLeft = menu.findItem(R.id.menu_button_left);
		mMenuButtonRight = menu.findItem(R.id.menu_button_right);

		mMenuButtonLeft.setOnMenuItemClickListener(this);
		mMenuButtonRight.setOnMenuItemClickListener(this);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mMenuButtonLeft.setVisible(true);

		if (mMenuAbState == MenuState.BACK_NEXT) {
			mMenuButtonRight.setTitle("�����");
			mMenuButtonLeft.setTitle("�����");
		} else if (mMenuAbState == MenuState.BACK_ASSEMBLE) {
			mMenuButtonRight.setTitle("������ ������ ����!");
			mMenuButtonLeft.setTitle("�����");
		} else if (mMenuAbState == MenuState.README_ABOUT) {
			mMenuButtonRight.setTitle("� ����������");
			mMenuButtonLeft.setTitle("�������");
		} else if (mMenuAbState == MenuState.CANCEL_SAVE) {
			mMenuButtonRight.setTitle("���������");
			mMenuButtonLeft.setTitle("������");
		} else if (mMenuAbState == MenuState.NONE_CLOSE) {
			mMenuButtonRight.setTitle("�������");
			mMenuButtonLeft.setVisible(false);
		} else if (mMenuAbState == MenuState.CLOSE_REASSEMBLE) {
			mMenuButtonRight.setTitle("�����������");
			mMenuButtonLeft.setTitle("�������");
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mBackKeyPressCount == 2) {
				super.onKeyDown(keyCode, event);
			} else {
				if (mBackKeyCountDownTimer != null) {
					mBackKeyCountDownTimer.cancel();
					mBackKeyCountDownTimer.start();
				}
				mBackKeyPressCount++;
				((TextView) mToastView.findViewById(R.id.toast_tv))
						.setText("������� ��� " + (3 - mBackKeyPressCount)
								+ " ���(�) ����� �����...");
				mToast.show();
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			abm.show();
		}
		return true;
	}

	private void setTextTypeface() {
		final Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/impact.ttf");

		((TextView) findViewById(R.id._step_text_view_first_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_second_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_third_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_fourth_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_fifth_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_sixth_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view)).setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_zero_slide))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_freq_edit))
				.setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_files_list))
				.setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_header_with_file_name))
				.setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense1)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense2)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense3)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense4)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense5)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense6)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense7)).setTypeface(typeface);
		((TextView) findViewById(R.id.ssu_tv_no_sense8)).setTypeface(typeface);
		((TextView) findViewById(R.id.ref_htv_0)).setTypeface(typeface);
		((TextView) findViewById(R.id.ref_htv_1)).setTypeface(typeface);
		((TextView) findViewById(R.id.ref_htv_2)).setTypeface(typeface);
		((TextView) findViewById(R.id.ref_htv_3)).setTypeface(typeface);
		((TextView) findViewById(R.id.ref_htv_4)).setTypeface(typeface);
		((TextView) findViewById(R.id._step_text_view_reference))
				.setTypeface(typeface);
		((TextView) findViewById(R.id.warning_tv)).setTypeface(typeface);
		((TextView) findViewById(R.id.warning_tv_2)).setTypeface(typeface);

		mCharsEnteredHitsSlideTextView.setTypeface(typeface);
		mCharsEnteredShieldSlideTextView.setTypeface(typeface);
		mCharsEnteredPotionsSlideTextView.setTypeface(typeface);
		mMobsHintButton.setTypeface(typeface);
	}

	public final void showMyToast(String text) {
		((TextView) mToastView.findViewById(R.id.toast_tv)).setText(text);
		mToast.show();
	}

	private final void hideKeyboard() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public final BotAdapter getNewAdapter(ArrayList<Bot> list) {
		return new BotAdapter(list);
	}

	private final void configureMacro() {
		readUserInput();
		hideKeyboard();

		mAssemblyProgressDialog = new ProgressDialog(this);
		mAssemblyProgressDialog.setCancelable(false);
		new AssemblyTask().execute();
	}

	class OpenFileTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAssemblyProgressDialog
					.setMessage("����������, ���������...\n������ "
							+ mMacro.getName() + ".hmd...");
			mAssemblyProgressDialog.show();
			Log.i("zZloBot: Reading macro",
					"������ ������� " + mMacro.getName() + ".hmd...");
			mAppLabelStatus.setText("������ ����...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			String source[] = null;
			try {
				source = Utils.readString(
						Constants.PATH_TO_THE_HIROMACRO + mMacro.getName()
								+ ".hmd", 369).split("\n");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			mMacro.setBotGoal(Integer.parseInt(source[26 - 1].replaceAll(
					"\\D+", "")));

			mMacro.setRunsAmount(Integer.parseInt(source[20 - 1].replaceAll(
					"\\D+", "")));

			// mMacro.setSkippingTickDialog(Integer.parseInt(source[23 -
			// 1].replaceAll(
			// "\\D+", "")));
			// mMacro.setSkippingOkDialog(Integer.parseInt(source[24 -
			// 1].replaceAll("\\D+",
			// "")));

			mMacro.setSleepTimeNormal(Integer.parseInt(source[27 - 1]
					.replaceAll("\\D+", "")));
			mMacro.setSleepTimeTournament(Integer.parseInt(source[28 - 1]
					.replaceAll("\\D+", "")));

			mMacro.setLifeRegenOption(Integer.parseInt(source[29 - 1]
					.replaceAll("\\D+", "")));
			mMacro.setLifeRegenWait(Integer.parseInt(source[30 - 1].replaceAll(
					"\\D+", "")));

			// mMacro.setCountingWinsOnly(Integer.parseInt(source[32 -
			// 1].replaceAll("\\D+",
			// "")));

			int chosen_mob_x = Integer.parseInt(source[52 - 1].replaceAll(
					"\\D+", ""));
			int chosen_mob_y = Integer.parseInt(source[53 - 1].replaceAll(
					"\\D+", ""));

			if (chosen_mob_x == 475 && chosen_mob_y == 520) {
				mMacro.setMonsterNumberInMenu(1);
			} else if (chosen_mob_x == 340) {
				if (chosen_mob_y == 340) {
					mMacro.setMonsterNumberInMenu(3);
				} else if (chosen_mob_y == 180) {
					mMacro.setMonsterNumberInMenu(5);
				}
			} else if (chosen_mob_x == 950) {
				if (chosen_mob_y == 520) {
					mMacro.setMonsterNumberInMenu(2);
				} else if (chosen_mob_y == 340) {
					mMacro.setMonsterNumberInMenu(4);
				} else if (chosen_mob_y == 180) {
					mMacro.setMonsterNumberInMenu(6);
				}
			}

			String st_ = "", bt_ = "", pt_ = "";

			for (int i = 0; i <= 99; i++) {
				String line = source[68 - 1 + i];
				switch (line.length()) {
				case 11:
					line = line.substring(11 - 1);
					break;
				case 12:
					line = line.substring(12 - 1);
					break;
				case 13:
					line = line.substring(13 - 1);
					break;
				}
				st_ += line;
			}
			for (int i = 0; i <= 99; i++) {
				String line = source[169 - 1 + i];
				switch (line.length()) {
				case 11:
					line = line.substring(11 - 1);
					break;
				case 12:
					line = line.substring(12 - 1);
					break;
				case 13:
					line = line.substring(13 - 1);
					break;
				}
				bt_ += line;
			}
			for (int i = 0; i <= 99; i++) {
				String line = source[270 - 1 + i];
				switch (line.length()) {
				case 11:
					line = line.substring(11 - 1);
					break;
				case 12:
					line = line.substring(12 - 1);
					break;
				case 13:
					line = line.substring(13 - 1);
					break;
				}
				pt_ += line;
			}

			// strings clean up
			int last_index_of_configured_turns = Collections.max(Arrays.asList(
					st_.lastIndexOf("1"), st_.lastIndexOf("2"),
					st_.lastIndexOf("3"))) + 1;
			mMacro.setHitsSequence(st_.substring(0,
					last_index_of_configured_turns).equals("") ? "" : st_
					.substring(0, last_index_of_configured_turns));
			mMacro.setShieldTurnsSequence(bt_.substring(0,
					bt_.lastIndexOf("1") + 1).equals("") ? "" : bt_.substring(
					0, bt_.lastIndexOf("1") + 1));
			last_index_of_configured_turns = Collections.max(Arrays.asList(
					pt_.lastIndexOf("1"), pt_.lastIndexOf("2"),
					pt_.lastIndexOf("3"), pt_.lastIndexOf("4"),
					pt_.lastIndexOf("5"), pt_.lastIndexOf("6"),
					pt_.lastIndexOf("7"))) + 1;
			mMacro.setPotionTurnsSequence(pt_.substring(0,
					last_index_of_configured_turns).equals("") ? "" : pt_
					.substring(0, last_index_of_configured_turns));
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mAssemblyProgressDialog.dismiss();

			mViewAnimator.setOutAnimation(getApplicationContext(),
					R.anim.out__slide_to_left);
			mViewAnimator.setInAnimation(getApplicationContext(),
					R.anim.in__slide_from_right);
			mViewAnimator.setDisplayedChild(Layer.MODIFY_BOT);
			invalidateOptionsMenu();

			mBotModifyWindowHeader.setText(mMacro.getName() + ".hmd");
			ssu_afa.setText(mMacro.getLifeRegenOption() == 1 ? "����� ������� �������������� ��"
					: "����� " + mMacro.getLifeRegenWait() + " �����������");
			mBotModifyWindowBotNameTextView.setText(mMacro.getName());
			ssu_bp.setText(mMacro.getBotGoal() == 0 ? "[�����] ���: "
					+ mMacro.getMonsterNumberInMenu() + "\n����������: "
					+ mMacro.getRunsAmount()
					: mMacro.getBotGoal() == 1 ? "[�����] ����������: "
							+ mMacro.getRunsAmount()
							: mMacro.getBotGoal() == 2 ? "[���] ����������: "
									+ mMacro.getRunsAmount()
									: mMacro.getBotGoal() == 3 ? "[������] ����������: "
											+ mMacro.getRunsAmount()
											: "[�����] ���: "
													+ mMacro.getMonsterNumberInMenu()
													+ "\n����������: 30");
			mBotModifyWindowShieldTurnsTextView
					.setText(mMacro.getShieldTurnsSequence().equals("") ? "��� �� ������������"
							: mMacro.getShieldTurnsSequence());
			ssu_diag.setText((mMacro.isSkippingTickDialog() ? "������� ������: ������������"
					: "������� ������: ������������")
					+ "\n"
					+ (mMacro.isSkippingOkDialog() ? "��������� ������: ������������"
							: "��������� ������: ������������"));
			mBotModifyWindowPotionTurnsTextView.setText(mMacro
					.getPotionTurnsSequence().equals("") ? "������� �����"
					: mMacro.getPotionTurnsSequence());
			mBotModifyWindowHitsTextView.setText(mMacro.getHitsSequence()
					.equals("") ? "������������ ����" : mMacro
					.getHitsSequence());
			mBotModifyWindowIsWinOnlyTextView.setText(mMacro
					.isCountingWinsOnly() ? "��" : "���");

			mAppLabelStatus.setText("�����");
		}

	}

	class AssemblyTask extends AsyncTask<Void, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAssemblyProgressDialog
					.setMessage("���� ������ ����.\n���������� ���������...");
			mAssemblyProgressDialog.show();
			hideKeyboard();
			// In case of scenario of opening app and going straight to
			// reassembling newly opened macro. TODO
			if (mViewAnimator.getDisplayedChild() != Layer.MODIFY_BOT) {
				readUserInput();
			}
			mAppLabelStatus.setText("������ ����...");
		}

		@Override
		protected String doInBackground(Void... params) {
			int[] st_lines = new int[mMacro.getHitsSequence().length()];
			int[] bt_lines = new int[mMacro.getShieldTurnsSequence().toString()
					.length()];
			int[] pt_lines = new int[mMacro.getPotionTurnsSequence().toString()
					.length()];

			String[] picked_st_ = mMacro.getHitsSequence().split("");
			String[] picked_bt_ = mMacro.getShieldTurnsSequence().split("");
			String[] picked_pt_ = mMacro.getPotionTurnsSequence().split("");

			picked_st_ = Arrays.copyOfRange(picked_st_, 1, picked_st_.length);
			picked_bt_ = Arrays.copyOfRange(picked_bt_, 1, picked_bt_.length);
			picked_pt_ = Arrays.copyOfRange(picked_pt_, 1, picked_pt_.length);

			String[] st_replacements = new String[mMacro.getHitsSequence()
					.length()];
			String[] bt_replacements = new String[mMacro
					.getShieldTurnsSequence().length()];
			String[] pt_replacements = new String[mMacro
					.getPotionTurnsSequence().length()];

			for (int i = 0; i <= st_lines.length - 1; i++) {
				st_lines[i] = 68 + i;
				st_replacements[i] = "var #st_" + (i + 1) + " " + picked_st_[i];
			}
			for (int i = 0; i <= bt_lines.length - 1; i++) {
				bt_lines[i] = 169 + i;
				bt_replacements[i] = "var #bt_" + (i + 1) + " " + picked_bt_[i];
			}
			for (int i = 0; i <= pt_lines.length - 1; i++) {
				pt_lines[i] = 270 + i;
				pt_replacements[i] = "var #pt_" + (i + 1) + " " + picked_pt_[i];
			}

			int[] rem_lines = { 20, 23, 24, 26, 27, 28, 29, 30, 32, 52, 53 };

			String[] rem_replacements = {
					"var #must_run_battles " + mMacro.getRunsAmount(),
					"var #skip_ed_galochka " + mMacro.isSkippingTickDialog(),
					"var #skip_ok_dialog " + mMacro.isSkippingOkDialog(),
					"var #bot_type " + mMacro.getBotGoal(),
					"var #sleep_time " + mMacro.getSleepTimeNormal(),
					"var #sleep_time_awaiting "
							+ mMacro.getSleepTimeTournament(),
					"var #must_check_life " + (mMacro.getLifeRegenOption()),
					"var #after_fight_delay "
							+ (mMacro.getLifeRegenOption() == 0 ? mMacro
									.getLifeRegenWait() : 0),
					"var #must_win_only " + mMacro.isCountingWinsOnly(),
					"var #chosen_mob_x "
							+ (mMacro.getMonsterNumberInMenu() == 1 ? 475
									: mMacro.getMonsterNumberInMenu() == 3
											|| mMacro.getMonsterNumberInMenu() == 5 ? 340
											: mMacro.getMonsterNumberInMenu() == 2
													|| mMacro
															.getMonsterNumberInMenu() == 4
													|| mMacro
															.getMonsterNumberInMenu() == 6 ? 950
													: 0),
					"var #chosen_mob_y "
							+ (mMacro.getMonsterNumberInMenu() == 1
									|| mMacro.getMonsterNumberInMenu() == 2 ? 520
									: mMacro.getMonsterNumberInMenu() == 3
											|| mMacro.getMonsterNumberInMenu() == 4 ? 340
											: mMacro.getMonsterNumberInMenu() == 5
													|| mMacro
															.getMonsterNumberInMenu() == 6 ? 180
													: 0) };

			if (mViewAnimator.getDisplayedChild() == Layer.MODIFY_BOT) {
				new File(Constants.PATH_TO_THE_HIROMACRO + mPreviousBotName
						+ ".hmd").delete();
			}

			try {
				Utils.writeToFile(new File(Constants.PATH_TO_THE_HIROMACRO
						+ mMacro.getName() + ".hmd"), Utils.replaceLines(
						SourceArray.SOURCE_ARRAY, Utils.joinArrays(
								Utils.joinArrays(st_lines, bt_lines),
								Utils.joinArrays(pt_lines, rem_lines)), Utils
								.joinArrays(Utils.joinArrays(st_replacements,
										bt_replacements), Utils.joinArrays(
										pt_replacements, rem_replacements))));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mAssemblyProgressDialog.dismiss();

			if (mViewAnimator.getDisplayedChild() != Layer.MODIFY_BOT) {
				switchLayer(R.anim.out__slide_to_left,
						R.anim.in__slide_from_right, Layer.MODIFY_BOT);
				showMyToast("C����� ���������, ��� ��������.");
			} else {
				showMyToast("���������� ���������, ��� ��������.");
			}

			mBotModifyWindowHeader.setText(mMacro.getName() + ".hmd");
			mBotModifyWindowBotNameTextView.setText(mMacro.getName());

			ssu_afa.setText(mMacro.getLifeRegenOption() == 1 ? "����� ������� �������������� ��"
					: "����� " + mMacro.getLifeRegenWait() + " �����������");
			ssu_bp.setText(mMacro.getBotGoal() == 0 ? "[�����] ���: "
					+ mMacro.getMonsterNumberInMenu() + "\n����������: "
					+ mMacro.getRunsAmount()
					: mMacro.getBotGoal() == 1 ? "[�����] ����������: "
							+ mMacro.getRunsAmount()
							: mMacro.getBotGoal() == 2 ? "[���] ����������: "
									+ mMacro.getRunsAmount()
									: mMacro.getBotGoal() == 3 ? "[������] ����������: "
											+ mMacro.getRunsAmount()
											: "[�����] ���: "
													+ mMacro.getMonsterNumberInMenu()
													+ "\n����������: 30");
			mBotModifyWindowShieldTurnsTextView
					.setText(mMacro.getShieldTurnsSequence().equals("") ? "��� �� ������������"
							: mMacro.getShieldTurnsSequence());
			ssu_diag.setText((mMacro.isSkippingTickDialog() ? "������� ������: ������������"
					: "������� ������: ������������")
					+ "\n"
					+ (mMacro.isSkippingOkDialog() ? "��������� ������: ������������"
							: "��������� ������: ������������"));
			mBotModifyWindowPotionTurnsTextView.setText(mMacro
					.getPotionTurnsSequence().equals("") ? "������� �����"
					: mMacro.getPotionTurnsSequence());
			mBotModifyWindowHitsTextView.setText(mMacro.getHitsSequence()
					.equals("") ? "������������ ����" : mMacro
					.getHitsSequence());
			mBotModifyWindowIsWinOnlyTextView.setText(mMacro
					.isCountingWinsOnly() ? "��" : "���");

			mAppLabelStatus.setText("�����");
		}
	}

	/**
	 * Get user input from every view of every layer.
	 * Data validity checks are performed in assigned {@link MyLimitativeTextWatcher}`s.
	 * */
	final void readUserInput() {
		// BOTNAME, SUPER_HITS, SHIELD and POTIONS.
		mMacro.setName(mBotnameEditText.getText().toString());
		mMacro.setHitsSequence(mHitsEditText.getText().toString());
		mMacro.setShieldTurnsSequence(mShieldTurnsEditText.getText().toString());
		mMacro.setPotionTurnsSequence(mPotionsTurnsEditText.getText()
				.toString());

		// SKIPPING_DIALOGS.
		switch (mTickDialogRadioGroup.getCheckedRadioButtonId()) {
		case R.id.slide_4_rg_edgalochka_rb_confirm:
			mMacro.setSkippingTickDialog(true);
			break;
		case R.id.slide_4_rg_edgalochka_rb_stop:
			mMacro.setSkippingTickDialog(false);
			break;
		}
		switch (mOkDialogRadioGroup.getCheckedRadioButtonId()) {
		case R.id.slide_4_rg_okdialog_rb_confirm:
			mMacro.setSkippingOkDialog(true);
			break;
		case R.id.slide_4_rg_okdialog_rb_stop:
			mMacro.setSkippingOkDialog(false);
			break;
		}

		// AFTER_FIGHT_ACTION.
		if (mLifeRegenRadioGroup.getCheckedRadioButtonId() == R.id.slide_5_rb_wait_delay) {
			mMacro.setLifeRegenOption(0);
			mMacro.setLifeRegenWait(Integer.parseInt(mLifeRegenWaitEditText
					.getText().toString()));
		} else {
			mMacro.setLifeRegenOption(1);
		}

		// BOT_GOAL.
		switch (mBotGoalRadioGroup.getCheckedRadioButtonId()) {
		case R.id.slide_6_rb_purpose_haunt:
			mMacro.setBotGoal(0);
			mMacro.setMonsterNumberInMenu(Integer
					.parseInt(mMonsterPosInMenuEditText.getText().toString()));
			mMacro.setRunsAmount(Integer.parseInt(mMonsterRunsEditText
					.getText().toString()));
			break;
		case R.id.slide_6_rb_purpose_duels:
			mMacro.setBotGoal(1);
			mMacro.setRunsAmount(Integer.parseInt(mDuelRunsEditText.getText()
					.toString()));
			break;
		case R.id.slide_6_rb_purpose_survival:
			mMacro.setBotGoal(2);
			mMacro.setRunsAmount(Integer.parseInt(mSurvivalRunsEditText
					.getText().toString()));
			break;
		case R.id.slide_6_rb_purpose_a_wall_against_the_wall:
			mMacro.setBotGoal(3);
			mMacro.setRunsAmount(Integer.parseInt(mWallRunsEditText.getText()
					.toString()));
			break;
		}
		mMacro.setCountingWinsOnly(mWinOnlyView.isChecked());

		// SLEEP_TIMES.
		mMacro.setSleepTimeNormal(Integer.parseInt(mBotSleepTimeNormalEditText
				.getText().toString()));
		mMacro.setSleepTimeTournament(Integer
				.parseInt(mBotSleepTimeTournamentEditText.getText().toString()));
	}

	/**
	 * Get menu state respective to the current layer.
	 * */
	public final int getRespectiveMenuState() {
		switch (mViewAnimator.getDisplayedChild()) {
		case Layer.BOTNAME:
			return MenuState.BACK_NEXT;
		case Layer.HITS:
			return MenuState.BACK_NEXT;
		case Layer.SHIELD:
			return MenuState.BACK_NEXT;
		case Layer.POTIONS:
			return MenuState.BACK_NEXT;
		case Layer.SKIP_DIALOGS:
			return MenuState.BACK_NEXT;
		case Layer.LIFE_REGEN_OPTIONS:
			return MenuState.BACK_NEXT;
		case Layer.BOT_GOAL:
			return MenuState.BACK_ASSEMBLE;
		case Layer.MODIFY_BOT:
			return MenuState.CLOSE_REASSEMBLE;
		case Layer.HOME:
			return MenuState.README_ABOUT;
		case Layer.BOT_FILES_LIST:
			return MenuState.NONE_CLOSE;
		case Layer.REFERENCE:
			return MenuState.NONE_CLOSE;
		case Layer.SLEEP_TIMES:
			return MenuState.CANCEL_SAVE;
		}
		return -1;
	}

	public final void openMacroFile(String path) {
		mMacro.setName(path.substring(0, path.indexOf(".hmd")));

		mAssemblyProgressDialog = new ProgressDialog(this);
		mAssemblyProgressDialog.setCancelable(false);

		new OpenFileTask().execute();
	}

	public final MainActivity getContext() {
		return this;
	}
	

	public class BotAdapter extends BaseAdapter implements OnClickListener {
		class DeletingAsyncTask extends AsyncTask<Void, Void, Void> {

			int attempts = 0;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mAssemblyProgressDialog
						.setMessage("����������, ���������...\n�������� "
								+ mMacro.getName() + ".hmd...");
				mAssemblyProgressDialog.show();
				mAppLabelStatus.setText("�������� ����...");
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				if (mFileToDelete != null)
					while (mFileToDelete.exists()) {
						attempts++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mFileToDelete.delete();
					}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				mAssemblyProgressDialog.dismiss();
				remove_item_with(mMacro.getName() + ".hmd");
				showMyToast("���� ������");
				mDeletingAsyncTask = null;
				mAppLabelStatus.setText("�����");
			}

		}

		LayoutInflater mLayoutInflater;
		ArrayList<Bot> mBotList;
		ViewHolder mViewHolder;
		View current_view;

		DeletingAsyncTask mDeletingAsyncTask;

		public BotAdapter(ArrayList<Bot> bot_list) {
			this.mBotList = bot_list;
			this.mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return mBotList.size();
		}

		@Override
		public Object getItem(int position) {
			return mBotList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void remove_item_with(String name) {
			for (int i = mListView.getFirstVisiblePosition(); i <= mListView
					.getLastVisiblePosition(); i++) {
				if (((Bot) getItem(i)).getName().equals(name)) {
					mBotList.remove(i);
					if (mBotList.size() <= 0)
						mEmptyListViewTextView.setText("���� ������ ���.");
					this.notifyDataSetChanged();
					// this.notifyDataSetInvalidated();
					break;
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			current_view = convertView;
			if (current_view == null) {
				current_view = mLayoutInflater.inflate(
						R.layout.layout_fileslist_custom_item, parent, false);
				mViewHolder = new ViewHolder();

				mViewHolder.bot_name = (TextView) current_view
						.findViewById(R.id.m_listitem_tv_name);
				mViewHolder.file_date = (TextView) current_view
						.findViewById(R.id.m_listitem_dateandtime_tv);
				mViewHolder.file_size = (TextView) current_view
						.findViewById(R.id.m_listitem_filesize_tv);

				mViewHolder.btn_delete = (ImageButton) current_view
						.findViewById(R.id.m_listitem_delete);
				mViewHolder.btn_edit = (ImageButton) current_view
						.findViewById(R.id.m_listitem_edit);

				current_view.setTag(mViewHolder);

				mViewHolder.btn_delete.setTag(mViewHolder);
				mViewHolder.btn_edit.setTag(mViewHolder);

				mViewHolder.pos = position;
			} else {
				mViewHolder = (ViewHolder) current_view.getTag();
			}

			Bot mBotObject = (Bot) getItem(position);

			mViewHolder.bot_name.setText(mBotObject.getName());
			mViewHolder.file_size.setText(mBotObject.getSize());
			mViewHolder.file_date.setText(mBotObject.getDate());

			mViewHolder.bot_name.setTypeface(Typeface.createFromAsset(
					getAssets(), "fonts/impact.ttf"));

			mViewHolder.btn_delete.setOnClickListener(this);
			mViewHolder.btn_edit.setOnClickListener(this);

			return current_view;
		}

		@Override
		public void onClick(View clicked_button) {
			switch (clicked_button.getId()) {
			case R.id.m_listitem_delete:
				if (mDeletingAsyncTask == null) {
					String tmp = ((ViewHolder) clicked_button.getTag()).bot_name
							.getText().toString();
					mMacro.setName(tmp.substring(0, tmp.indexOf(".hmd")));
					mFileToDelete = new File(Constants.PATH_TO_THE_HIROMACRO
							+ mMacro.getName() + ".hmd");
					mAssemblyProgressDialog = new ProgressDialog(getContext());
					mAssemblyProgressDialog.setCancelable(false);

					mDeletingAsyncTask = new DeletingAsyncTask();
					mDeletingAsyncTask.execute();
				}
				break;
			case R.id.m_listitem_edit:
				mMacro.setName(((ViewHolder) clicked_button.getTag()).bot_name
						.getText().toString());
				mPreviousBotName = mMacro.getName();
				openMacroFile(mMacro.getName());
				break;
			}
		}
	}

	public class ViewHolder { 
		TextView bot_name, file_size, file_date;
		ImageButton btn_delete, btn_edit;
		int pos;
	}

	class Mint extends Dialog {

		public Mint(Context context) {
			super(context);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layout_mint);
		}
	}

	public class ABMenu extends Dialog implements
			android.view.View.OnClickListener {

		private Button btnAboutAuthor, btnOpenMacro, btnFrequency;

		ArrayList<Bot> mBotsList = new ArrayList<Bot>();

		public ABMenu(Activity a) {
			super(a);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layout_abmenu);

			btnFrequency = (Button) findViewById(R.id.ab_menu_btn_analysing_freq);
			btnOpenMacro = (Button) findViewById(R.id.ab_menu_btn_read_macro);
			btnAboutAuthor = (Button) findViewById(R.id.ab_menu_btn_reference);

			btnFrequency.setTypeface(Typeface.createFromAsset(getContext()
					.getAssets(), "fonts/impact.ttf"));
			btnOpenMacro.setTypeface(Typeface.createFromAsset(getContext()
					.getAssets(), "fonts/impact.ttf"));
			btnAboutAuthor.setTypeface(Typeface.createFromAsset(getContext()
					.getAssets(), "fonts/impact.ttf"));

			btnOpenMacro.setOnClickListener(this);
			btnFrequency.setOnClickListener(this);
			btnAboutAuthor.setOnClickListener(this);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				dismiss();
			}
			return super.onKeyDown(keyCode, event);
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.ab_menu_btn_analysing_freq:
				if (mViewAnimator.getDisplayedChild() != Layer.SLEEP_TIMES) {

					if (mViewAnimator.getDisplayedChild() != Layer.BOT_FILES_LIST
							&& mViewAnimator.getDisplayedChild() != Layer.REFERENCE)
						mLastDisplayedLayer = mViewAnimator.getDisplayedChild();

					if (mViewAnimator.getDisplayedChild() == Layer.MODIFY_BOT) {
						mBotSleepTimeNormalEditText.setText(""
								+ mMacro.getSleepTimeNormal());
						mBotSleepTimeTournamentEditText.setText(""
								+ mMacro.getSleepTimeTournament());

						mSleepTimesTxtValuesWhereFromTextView
								.setText("\n������ �� ������������ �������� � �������\n"
										+ mMacro.getName() + ".hmd\n");
					} else {
						mBotSleepTimeNormalEditText
								.setText(mSharedPrefs
										.getInt(Constants.SPREFSKEY_BOT_SLEEP_TIME_NORMAL,
												1000)
										+ "");
						mBotSleepTimeTournamentEditText
								.setText(mSharedPrefs
										.getInt(Constants.SPREFSKEY_BOT_SLEEP_TIME_TOURNAMENT,
												5000)
										+ "");

						mMacro.setSleepTimeNormal( mSharedPrefs
								.getInt(Constants.SPREFSKEY_BOT_SLEEP_TIME_NORMAL,
										1000));
						mMacro.setSleepTimeTournament( mSharedPrefs.getInt(
								Constants.SPREFSKEY_BOT_SLEEP_TIME_TOURNAMENT,
								5000));

						mSleepTimesTxtValuesWhereFromTextView
								.setText("\n������ �������� ����������� ��� ���� ����������� ��������!\n");
					}

					mViewAnimator.setOutAnimation(getApplicationContext(),
							R.anim.out__slide_to_top);
					mViewAnimator.setInAnimation(getApplicationContext(),
							R.anim.in__slide_from_bottom);
					mViewAnimator.setDisplayedChild(Layer.SLEEP_TIMES);
					invalidateOptionsMenu();

					dismiss();
				} else {
					showMyToast("���� ��� �������!");
				}
				break;
			case R.id.ab_menu_btn_read_macro:
				openMacro();
				break;
			case R.id.ab_menu_btn_reference:
				showReadMe();
				break;
			}
		}

		private void showReadMe() {
			if (!(mViewAnimator.getDisplayedChild() == Layer.REFERENCE)) {

				if (mViewAnimator.getDisplayedChild() != Layer.SLEEP_TIMES
						&& mViewAnimator.getDisplayedChild() != Layer.BOT_FILES_LIST)
					mLastDisplayedLayer = mViewAnimator.getDisplayedChild();

				mViewAnimator.setOutAnimation(getApplicationContext(),
						R.anim.out__slide_to_top);
				mViewAnimator.setInAnimation(getApplicationContext(),
						R.anim.in__slide_from_bottom);
				mViewAnimator.setDisplayedChild(Layer.REFERENCE);
				invalidateOptionsMenu();

				dismiss();
			} else {
				showMyToast("���� ��� �������!");
			}
		}

		private void openMacro() {
			if (mBotsList != null)
				mBotsList.clear();

			if (!(mViewAnimator.getDisplayedChild() == Layer.BOT_FILES_LIST)) {

				if (mViewAnimator.getDisplayedChild() != Layer.SLEEP_TIMES
						&& mViewAnimator.getDisplayedChild() != Layer.REFERENCE)
					mLastDisplayedLayer = mViewAnimator.getDisplayedChild();
				else
					showMyToast("�������� ��������");

				File files_in_folder[] = (new File(
						Constants.PATH_TO_THE_HIROMACRO).listFiles());
				for (File list_item : files_in_folder) {
					if (list_item.isFile()
							&& list_item.getName().contains(".hmd")) {

						String date = new Date(list_item.lastModified())
								.toString();

						mBotsList
								.add(new Bot(list_item.getName(),
										humanReadableByteCount(
												list_item.length(), true), date
												.substring(0,
														date.indexOf("GMT"))));
					}
				}

				if (mBotsList.size() <= 0) {
					mEmptyListViewTextView.setText("���� ������ ���.");
				} else if (mBotsList.size() > 0) {
					mEmptyListViewTextView.setText("");
				}

				mListView.setAdapter(getNewAdapter(mBotsList));

				mViewAnimator.setOutAnimation(getApplicationContext(),
						R.anim.out__slide_to_top);
				mViewAnimator.setInAnimation(getApplicationContext(),
						R.anim.in__slide_from_bottom);
				mViewAnimator.setDisplayedChild(Layer.BOT_FILES_LIST);
				invalidateOptionsMenu();

				dismiss();
			} else {
				showMyToast("���� ��� �������!");
			}
		}

		public String humanReadableByteCount(long bytes, boolean si) {
			int unit = si ? 1000 : 1024;
			if (bytes < unit)
				return bytes + " B";
			int exp = (int) (Math.log(bytes) / Math.log(unit));
			String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
					+ (si ? "" : "i");
			return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
		}
	}

	public class AboutClip extends Dialog {

		public AboutClip(Context context) {
			super(context);
		}

		@Override
		public void onAttachedToWindow() {
			super.onAttachedToWindow();
			mFrameLayout.getForeground().setAlpha(150); // dim
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layout_about);

			((TextView) findViewById(R.id.about_clip_tv_0))
					.setTypeface(Typeface.createFromAsset(getAssets(),
							"fonts/impact.ttf"));
			((TextView) findViewById(R.id.about_clip_tv_1))
					.setTypeface(Typeface.createFromAsset(getAssets(),
							"fonts/impact.ttf"));
		}

		@Override
		public void onDetachedFromWindow() {
			super.onDetachedFromWindow();
			mFrameLayout.getForeground().setAlpha(0);
		}

	}

	@SuppressLint("ValidFragment")
	public class AlertED extends DialogFragment {

		private View v;
		private LayoutInflater inflater;
		private AlertDialog adb;
		private boolean data_is_correct = false, data_is_korrekt;

		private int type, last_index_of_configured_turns;

		private String pt_, bt_, st_;

		private EditText ed_bot_name, ed_bot_purpose_haunt_what_mob,
				ed_bot_purpose_haunt_hmany, ed_bot_pupose_duels_hmany,
				ed_bot_purpose_survivals_hmany, ed_bot_purpose_walls_hmany,
				ed_delay, ed_potions, ed_shield, ed_superhits;

		private RadioGroup rg_bot_purpose, rg_afa, rg_edgalochka, rg_okdialog;
		private Button bot_numbering;

		public AlertED(int type) {
			this.type = type;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			inflater = LayoutInflater.from(getActivity());
			switch (type) {
			case Constants.CLIP_TYPE_AFTER_FIGHT_ACTION:
				v = inflater.inflate(R.layout.clip_end_of_battle, null);
				ed_delay = (EditText) v.findViewById(R.id.clip_afa_ed_delay);

				rg_afa = (RadioGroup) v.findViewById(R.id.clip_afa_rg_afa);
				rg_afa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int id) {
						switch (id) {
						case R.id.clip_afa_rg_afa_rb_delay:
							ed_delay.setEnabled(true);
							break;
						case R.id.clip_afa_rg_afa_rb_hp:
							ed_delay.setEnabled(false);
							break;
						}
					}
				});
				switch (mMacro.getLifeRegenOption()) {
				case 0: // delay
					rg_afa.check(R.id.clip_afa_rg_afa_rb_delay);
					ed_delay.setText("" + mMacro.getLifeRegenWait());
					ed_delay.setSelection((mMacro.getLifeRegenWait() + "")
							.length());
					break;
				case 1: // hitpoints
					rg_afa.check(R.id.clip_afa_rg_afa_rb_hp);
					ed_delay.setText("" + mMacro.getLifeRegenWait());
					ed_delay.setSelection((mMacro.getLifeRegenWait() + "")
							.length());
					ed_delay.setEnabled(false);
					break;
				}
				break;
			case Constants.CLIP_TYPE_BOT_NAME:
				v = inflater.inflate(R.layout.clip_botname, null);
				ed_bot_name = (EditText) v
						.findViewById(R.id.clip_botname_ed_name);
				ed_bot_name.setText(mMacro.getName());
				ed_bot_name.setSelection(mMacro.getName().length());
				break;
			case Constants.CLIP_TYPE_BOT_PURPOSE:
				v = inflater.inflate(R.layout.clip_bot_purpose, null);
				ed_bot_pupose_duels_hmany = (EditText) v
						.findViewById(R.id.clip_bot_purpose_ed_purpose_duels_how_many);
				ed_bot_purpose_haunt_hmany = (EditText) v
						.findViewById(R.id.clip_bot_purpose_ed_purpose_haunt_how_many_times);
				ed_bot_purpose_haunt_what_mob = (EditText) v
						.findViewById(R.id.clip_bot_purpose_ed_purpose_haunt_mob_number);
				ed_bot_purpose_survivals_hmany = (EditText) v
						.findViewById(R.id.clip_bot_purpose_ed_purpose_survival_how_many);
				ed_bot_purpose_walls_hmany = (EditText) v
						.findViewById(R.id.clip_bot_purpose_ed_purpose_a_wall_against_the_wall_how_many);

				bot_numbering = (Button) v
						.findViewById(R.id.clip_bot_purpose_btn_bots_numbering);
				bot_numbering.setTypeface(Typeface.createFromAsset(getAssets(),
						"fonts/impact.ttf"));
				bot_numbering.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Mint mint = new Mint(getContext());
						mint.getWindow().setBackgroundDrawable(
								new ColorDrawable(Color.TRANSPARENT));
						mint.show();
					}
				});

				rg_bot_purpose = (RadioGroup) v
						.findViewById(R.id.clip_bot_purpose_rg_main);
				rg_bot_purpose
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								switch (checkedId) {
								case R.id.clip_bot_purpose__rb_purpose_haunt:
									ed_bot_purpose_haunt_hmany.setEnabled(true);
									ed_bot_purpose_haunt_what_mob
											.setEnabled(true);
									ed_bot_pupose_duels_hmany.setEnabled(false);
									ed_bot_purpose_survivals_hmany
											.setEnabled(false);
									ed_bot_purpose_walls_hmany
											.setEnabled(false);
									break;
								case R.id.clip_bot_purpose_rb_purpose_a_wall_against_the_wall:
									ed_bot_purpose_walls_hmany.setEnabled(true);
									ed_bot_pupose_duels_hmany.setEnabled(false);
									ed_bot_purpose_haunt_hmany
											.setEnabled(false);
									ed_bot_purpose_haunt_what_mob
											.setEnabled(false);
									ed_bot_purpose_survivals_hmany
											.setEnabled(false);
									break;
								case R.id.clip_bot_purpose_rb_purpose_duels:
									ed_bot_pupose_duels_hmany.setEnabled(true);
									ed_bot_purpose_haunt_hmany
											.setEnabled(false);
									ed_bot_purpose_haunt_what_mob
											.setEnabled(false);
									ed_bot_purpose_survivals_hmany
											.setEnabled(false);
									ed_bot_purpose_walls_hmany
											.setEnabled(false);
									break;
								case R.id.clip_bot_purpose_rb_purpose_survival:
									ed_bot_purpose_survivals_hmany
											.setEnabled(true);
									ed_bot_pupose_duels_hmany.setEnabled(false);
									ed_bot_purpose_haunt_hmany
											.setEnabled(false);
									ed_bot_purpose_haunt_what_mob
											.setEnabled(false);
									ed_bot_purpose_walls_hmany
											.setEnabled(false);
									break;
								}
							}
						});
				switch (mMacro.getBotGoal()) {
				case 0:
					rg_bot_purpose
							.check(R.id.clip_bot_purpose__rb_purpose_haunt);

					ed_bot_purpose_haunt_what_mob.setText(""
							+ mMacro.getMonsterNumberInMenu());
					ed_bot_purpose_haunt_what_mob.setSelection(("" + mMacro
							.getMonsterNumberInMenu()).length());

					ed_bot_purpose_haunt_hmany.setText(""
							+ mMacro.getRunsAmount());
					ed_bot_purpose_haunt_hmany.setSelection((mMacro
							.getRunsAmount() + "").length());

					ed_bot_pupose_duels_hmany.setEnabled(false);
					ed_bot_purpose_survivals_hmany.setEnabled(false);
					ed_bot_purpose_walls_hmany.setEnabled(false);
					break;
				case 1:
					rg_bot_purpose
							.check(R.id.clip_bot_purpose_rb_purpose_duels);
					ed_bot_pupose_duels_hmany.setText(""
							+ mMacro.getRunsAmount());
					ed_bot_pupose_duels_hmany.setSelection(("" + mMacro
							.getRunsAmount()).length());

					ed_bot_purpose_haunt_hmany.setEnabled(false);
					ed_bot_purpose_haunt_what_mob.setEnabled(false);
					ed_bot_purpose_survivals_hmany.setEnabled(false);
					ed_bot_purpose_walls_hmany.setEnabled(false);
					break;
				case 2:
					rg_bot_purpose
							.check(R.id.clip_bot_purpose_rb_purpose_survival);
					ed_bot_purpose_survivals_hmany.setText(""
							+ mMacro.getRunsAmount());
					ed_bot_purpose_survivals_hmany.setSelection(("" + mMacro
							.getRunsAmount()).length());

					ed_bot_pupose_duels_hmany.setEnabled(false);
					ed_bot_purpose_haunt_hmany.setEnabled(false);
					ed_bot_purpose_haunt_what_mob.setEnabled(false);
					ed_bot_purpose_walls_hmany.setEnabled(false);
					break;
				case 3:
					rg_bot_purpose
							.check(R.id.clip_bot_purpose_rb_purpose_a_wall_against_the_wall);
					ed_bot_purpose_walls_hmany.setText(""
							+ mMacro.getRunsAmount());
					ed_bot_purpose_walls_hmany.setSelection(("" + mMacro
							.getRunsAmount()).length());

					ed_bot_pupose_duels_hmany.setEnabled(false);
					ed_bot_purpose_haunt_hmany.setEnabled(false);
					ed_bot_purpose_haunt_what_mob.setEnabled(false);
					ed_bot_purpose_survivals_hmany.setEnabled(false);
					break;
				}
				break;
			case Constants.CLIP_TYPE_DIALOGS:
				v = inflater.inflate(R.layout.clip_dialogs, null);

				rg_edgalochka = (RadioGroup) v
						.findViewById(R.id.clip_dialogs_rg_big_diag);
				rg_okdialog = (RadioGroup) v
						.findViewById(R.id.clip_dialogs_rg_small_diag);

				if (mMacro.isSkippingTickDialog()) {
					rg_edgalochka.check(R.id.clip_dialogs_rg_big_diag_rb_go);
				} else {
					rg_edgalochka.check(R.id.clip_dialogs_rg_big_diag_rb_stop);
				}

				if (mMacro.isSkippingOkDialog()) {
					rg_okdialog.check(R.id.clip_dialogs_rg_small_diag_rb_go);

				} else {
					rg_okdialog.check(R.id.clip_dialogs_rg_small_diag_rb_stop);

				}

				break;
			case Constants.CLIP_TYPE_POTIONS:
				v = inflater.inflate(R.layout.clip_potions, null);

				ed_potions = (EditText) v.findViewById(R.id.alert_ed_3);
				ed_potions.setText("" + mMacro.getPotionTurnsSequence());
				ed_potions.setSelection(mMacro.getPotionTurnsSequence()
						.length());
				break;
			case Constants.CLIP_TYPE_SHIELD_TURNS:
				v = inflater.inflate(R.layout.clip_shield, null);

				ed_shield = (EditText) v.findViewById(R.id.alert_ed_2);
				ed_shield.setText("" + mMacro.getShieldTurnsSequence());
				ed_shield
						.setSelection(mMacro.getShieldTurnsSequence().length());
				break;
			case Constants.CLIP_TYPE_SUPER_HITS:
				v = inflater.inflate(R.layout.clip_superhits, null);

				ed_superhits = (EditText) v.findViewById(R.id.alert_ed_1);
				ed_superhits.setText("" + mMacro.getHitsSequence());
				ed_superhits.setSelection(mMacro.getHitsSequence().length());
				break;
			}

			adb = new AlertDialog.Builder(getActivity(), 2)

					.setView(v)
					.setNeutralButton("OK", null)
					.setNegativeButton("������",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface di, int arg1) {
									di.dismiss();
								}
							}).create();
			adb.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface arg0) {
					adb.getButton(AlertDialog.BUTTON_NEUTRAL)
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									switch (type) {
									case Constants.CLIP_TYPE_AFTER_FIGHT_ACTION:
										switch (rg_afa
												.getCheckedRadioButtonId()) {
										case R.id.clip_afa_rg_afa_rb_delay:
											if (!ed_delay.getText().toString()
													.equals("")) {
												mMacro.setLifeRegenOption(0);
												mMacro.setLifeRegenWait(Integer
														.parseInt(ed_delay
																.getText()
																.toString()));
												ssu_afa.setText("����� "
														+ mMacro.getLifeRegenWait()
														+ " �����������");
												dismiss();
											} else {
												showMyToast("������� �������� ������!");
											}
											break;
										case R.id.clip_afa_rg_afa_rb_hp:
											mMacro.setLifeRegenOption(1);
											ssu_afa.setText("����� ������� �������������� ��");
											dismiss();
											break;
										}
										break;
									case Constants.CLIP_TYPE_BOT_NAME:
										if (!ed_bot_name.getText().toString()
												.equals("")) {
											mPreviousBotName = mMacro.getName();
											mMacro.setName(ed_bot_name
													.getText().toString());
											mBotModifyWindowBotNameTextView
													.setText(mMacro.getName());
											dismiss();
										} else {
											showMyToast("������� �������� ������!");
										}
										break;
									case Constants.CLIP_TYPE_BOT_PURPOSE:
										switch (rg_bot_purpose
												.getCheckedRadioButtonId()) {
										case R.id.clip_bot_purpose__rb_purpose_haunt:
											if (!ed_bot_purpose_haunt_what_mob
													.getText().toString()
													.equals("")
													&& !ed_bot_purpose_haunt_hmany
															.getText()
															.toString()
															.equals("")) {
												mMacro.setBotGoal(0);
												mMacro.setMonsterNumberInMenu(Integer
														.parseInt(ed_bot_purpose_haunt_what_mob
																.getText()
																.toString()));
												mMacro.setRunsAmount(Integer
														.parseInt(ed_bot_purpose_haunt_hmany
																.getText()
																.toString()));
												dismiss();
												data_is_correct = true;
											} else {
												data_is_correct = false;
												showMyToast("������� �������� ������!");
											}
											break;
										case R.id.clip_bot_purpose_rb_purpose_duels:
											if (!ed_bot_pupose_duels_hmany
													.getText().toString()
													.equals("")) {
												mMacro.setBotGoal(1);
												mMacro.setRunsAmount(Integer
														.parseInt(ed_bot_pupose_duels_hmany
																.getText()
																.toString()));
												dismiss();
												data_is_correct = true;
											} else {
												data_is_correct = false;
												showMyToast("������� �������� ������!");
											}
											break;
										case R.id.clip_bot_purpose_rb_purpose_survival:
											if (!ed_bot_purpose_survivals_hmany
													.getText().toString()
													.equals("")) {
												mMacro.setBotGoal(2);
												mMacro.setRunsAmount(Integer
														.parseInt(ed_bot_purpose_survivals_hmany
																.getText()
																.toString()));
												dismiss();
												data_is_correct = true;
											} else {
												data_is_correct = false;
												showMyToast("������� �������� ������!");
											}
											break;
										case R.id.clip_bot_purpose_rb_purpose_a_wall_against_the_wall:
											if (!ed_bot_purpose_walls_hmany
													.getText().toString()
													.equals("")) {
												mMacro.setBotGoal(3);
												mMacro.setRunsAmount(Integer
														.parseInt(ed_bot_purpose_walls_hmany
																.getText()
																.toString()));
												dismiss();
												data_is_correct = true;
											} else {
												data_is_correct = false;
												showMyToast("������� �������� ������!");
											}
											break;
										}
										if (data_is_correct) {
											data_is_correct = false;
											ssu_bp.setText(mMacro.getBotGoal() == 0 ? "[�����] ���: "
													+ mMacro.getMonsterNumberInMenu()
													+ "\n����������: "
													+ mMacro.getRunsAmount()
													: mMacro.getBotGoal() == 1 ? "[�����] ����������: "
															+ mMacro.getRunsAmount()
															: mMacro.getBotGoal() == 2 ? "[���] ����������: "
																	+ mMacro.getRunsAmount()
																	: mMacro.getBotGoal() == 3 ? "[������] ����������: "
																			+ mMacro.getRunsAmount()
																			: "[�����] ���: "
																					+ mMacro.getMonsterNumberInMenu()
																					+ "\n����������: 30");
										}
										break;
									case Constants.CLIP_TYPE_DIALOGS:
										switch (rg_edgalochka
												.getCheckedRadioButtonId()) {
										case R.id.clip_dialogs_rg_big_diag_rb_stop:
											mMacro.setSkippingTickDialog(false);
											data_is_correct = true;
											dismiss();
											break;
										case R.id.clip_dialogs_rg_big_diag_rb_go:
											mMacro.setSkippingTickDialog(true);
											data_is_correct = true;
											dismiss();
											break;
										default:
											data_is_correct = false;
											showMyToast("������� �������� ������!");
											break;
										}

										switch (rg_okdialog
												.getCheckedRadioButtonId()) {
										case R.id.clip_dialogs_rg_small_diag_rb_stop:
											mMacro.setSkippingOkDialog(false);
											data_is_korrekt = true;
											dismiss();
											break;
										case R.id.clip_dialogs_rg_small_diag_rb_go:
											mMacro.setSkippingOkDialog(true);
											data_is_korrekt = true;
											dismiss();
											break;
										default:
											data_is_korrekt = false;
											showMyToast("������� �������� ������!");
											break;
										}

										if (data_is_correct && data_is_korrekt) {
											data_is_correct = false;
											data_is_korrekt = false;
											ssu_diag.setText((mMacro
													.isSkippingTickDialog() ? "������� ������: ������������"
													: "������� ������: ������������")
													+ "\n"
													+ (mMacro
															.isSkippingOkDialog() ? "��������� ������: ������������"
															: "��������� ������: ������������"));
										}
										break;
									case Constants.CLIP_TYPE_POTIONS:
										pt_ = ed_potions.getText().toString();

										last_index_of_configured_turns = Collections
												.max(Arrays.asList(
														pt_.lastIndexOf("1"),
														pt_.lastIndexOf("2"),
														pt_.lastIndexOf("3"),
														pt_.lastIndexOf("4"),
														pt_.lastIndexOf("5"),
														pt_.lastIndexOf("6"),
														pt_.lastIndexOf("7"))) + 1;
										mMacro.setPotionTurnsSequence(pt_
												.substring(0,
														last_index_of_configured_turns)
												.equals("") ? ""
												: pt_.substring(0,
														last_index_of_configured_turns));

										mBotModifyWindowPotionTurnsTextView
												.setText(mMacro
														.getPotionTurnsSequence()
														.equals("") ? "������� �����"
														: mMacro.getPotionTurnsSequence());
										dismiss();
										break;
									case Constants.CLIP_TYPE_SHIELD_TURNS:
										bt_ = ed_shield.getText().toString();

										mMacro.setShieldTurnsSequence(bt_
												.substring(
														0,
														bt_.lastIndexOf("1") + 1)
												.equals("") ? ""
												: bt_.substring(
														0,
														bt_.lastIndexOf("1") + 1));

										mBotModifyWindowShieldTurnsTextView
												.setText(mMacro
														.getShieldTurnsSequence()
														.equals("") ? "��� �� ������������"
														: mMacro.getShieldTurnsSequence());
										dismiss();
										break;
									case Constants.CLIP_TYPE_SUPER_HITS:
										st_ = ed_superhits.getText().toString();

										last_index_of_configured_turns = Collections
												.max(Arrays.asList(
														st_.lastIndexOf("1"),
														st_.lastIndexOf("2"),
														st_.lastIndexOf("3"))) + 1;
										mMacro.setHitsSequence(st_.substring(0,
												last_index_of_configured_turns)
												.equals("") ? ""
												: st_.substring(0,
														last_index_of_configured_turns));

										mBotModifyWindowHitsTextView
												.setText(mMacro
														.getHitsSequence()
														.equals("") ? "������������ ����"
														: mMacro.getHitsSequence());
										dismiss();
										break;
									}
								}
							});
				}
			});
			return adb;
		}

		@Override
		public void onResume() {
			super.onResume();
			try {
				adb.getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(
						getActivity().getResources().getDrawable(
								R.drawable.btn_adj_clr_clip_edition));
				adb.getButton(DialogInterface.BUTTON_NEUTRAL).setBackground(
						getActivity().getResources().getDrawable(
								R.drawable.btn_adj_clr_clip_edition));
			} catch (NullPointerException e) {
			}
		}
	}
}
