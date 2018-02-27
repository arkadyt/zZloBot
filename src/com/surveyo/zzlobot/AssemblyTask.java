package com.surveyo.zzlobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

class AssemblyTask extends AsyncTask<Void, String, String> {

	private MainActivity mActivity;

	public AssemblyTask(Context context) {
		mActivity = (MainActivity) context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mActivity.getAssemblyProgressDialog().setMessage(
				"Идет сборка бота...\nЭлементы: 0/4560");
		mActivity.getAssemblyProgressDialog().show();
		mActivity.mInputMethodManager.hideSoftInputFromWindow(mActivity
				.getCurrentFocus().getWindowToken(), 0);
		if (mActivity.getViewAnimator().getDisplayedChild() != Layer.MODIFY_BOT) {
			mActivity.readUserInput();
		}
		mActivity.mAppLabelStatus.setText("Сборка бота...");
	}

	@Override
	protected String doInBackground(Void... params) {
		int[] st_lines = new int[mHitsSequence.length()];
		int[] bt_lines = new int[mShieldTurnsSequence.toString().length()];
		int[] pt_lines = new int[mPotionTurnsSequence.toString().length()];

		String[] picked_st_ = mHitsSequence.split("");
		String[] picked_bt_ = mShieldTurnsSequence.split("");
		String[] picked_pt_ = mPotionTurnsSequence.split("");

		picked_st_ = Arrays.copyOfRange(picked_st_, 1, picked_st_.length);
		picked_bt_ = Arrays.copyOfRange(picked_bt_, 1, picked_bt_.length);
		picked_pt_ = Arrays.copyOfRange(picked_pt_, 1, picked_pt_.length);

		String[] st_replacements = new String[mHitsSequence.length()];
		String[] bt_replacements = new String[mShieldTurnsSequence.length()];
		String[] pt_replacements = new String[mPotionTurnsSequence.length()];

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

		Log.e("", "local_must_run_battles = " + mRunsAmount);

		String[] rem_replacements = {
				"var #must_run_battles " + mRunsAmount,
				"var #skip_ed_galochka " + mSkipTickDialog,
				"var #skip_ok_dialog " + mSkipOkDialog,
				"var #bot_type " + mBotGoal,
				"var #sleep_time " + mBotSleepTimeNormal,
				"var #sleep_time_awaiting " + mBotSleepTimeTournament,
				"var #must_check_life " + (mLifeRegenOption),
				"var #after_fight_delay "
						+ (mLifeRegenOption == 0 ? mLifeRegenWait : 0),
				"var #must_win_only " + mCountWinsOnly,
				"var #chosen_mob_x "
						+ (mMonsterNumber == 1 ? 475 : mMonsterNumber == 3
								|| mMonsterNumber == 5 ? 340
								: mMonsterNumber == 2 || mMonsterNumber == 4
										|| mMonsterNumber == 6 ? 950 : 0),
				"var #chosen_mob_y "
						+ (mMonsterNumber == 1 || mMonsterNumber == 2 ? 520
								: mMonsterNumber == 3 || mMonsterNumber == 4 ? 340
										: mMonsterNumber == 5
												|| mMonsterNumber == 6 ? 180
												: 0) };

		if (mViewAnimator.getDisplayedChild() == Layer.MODIFY_BOT) {
			new File(Constants.PATH_TO_THE_HIROMACRO + mPrevBotName + ".hmd")
					.delete();
		}

		try {
			Utils.writeToFile(
					new File(Constants.PATH_TO_THE_HIROMACRO + mBotName
							+ ".hmd"),
					replace_lines(SourceArray.SOURCE_ARRAY, Utils.joinArrays(
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
		if (values[0].equals("4560/4560")) {
			mAssemblyProgressDialog
					.setMessage("Идет сборка бота...\nПост-обработка...");
		} else
			mAssemblyProgressDialog
					.setMessage("Идет сборка бота...\nЭлементы: " + values[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mAssemblyProgressDialog.dismiss();

		if (mViewAnimator.getDisplayedChild() != Layer.MODIFY_BOT) {
			switchLayer(R.anim.out__slide_to_left, R.anim.in__slide_from_right,
					Layer.MODIFY_BOT);
			showMyToast("Cборка закончена, бот сохранен.");
		} else {
			showMyToast("Пересборка закончена, бот сохранен.");
		}

		mBotModifyWindowHeader.setText(mBotName + ".hmd");
		mBotModifyWindowBotNameTextView.setText(mBotName);

		ssu_afa.setText(mLifeRegenOption == 1 ? "Ждать полного восстановления ХП"
				: "Ждать " + mLifeRegenWait + " миллисекунд");
		ssu_bp.setText(mBotGoal == 0 ? "[Охота] Моб: " + mMonsterNumber
				+ "\nКоличество: " + mRunsAmount
				: mBotGoal == 1 ? "[Дуэли] Количество: " + mRunsAmount
						: mBotGoal == 2 ? "[ВЖВ] Количество: " + mRunsAmount
								: mBotGoal == 3 ? "[Стенка] Количество: "
										+ mRunsAmount : "[Охота] Моб: "
										+ mMonsterNumber + "\nКоличество: 30");
		mBotModifyWindowShieldTurnsTextView.setText(mShieldTurnsSequence
				.equals("") ? "Щит не используется" : mShieldTurnsSequence);
		ssu_diag.setText((mSkipTickDialog == 0 ? "Большой диалог: Остановиться"
				: "Большой диалог: Подтверждать")
				+ "\n"
				+ (mSkipOkDialog == 0 ? "Маленький диалог: Остановиться"
						: "Маленький диалог: Подтверждать"));
		mBotModifyWindowPotionTurnsTextView.setText(mPotionTurnsSequence
				.equals("") ? "Никаких банок" : mPotionTurnsSequence);
		mBotModifyWindowHitsTextView
				.setText(mHitsSequence.equals("") ? "Произвольные ходы"
						: mHitsSequence);
		mBotModifyWindowIsWinOnlyTextView.setText(mCountWinsOnly == 1 ? "Да"
				: "Нет");

		mAppLabelStatus.setText("Готов");
	}
}