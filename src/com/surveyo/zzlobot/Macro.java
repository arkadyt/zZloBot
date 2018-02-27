package com.surveyo.zzlobot;

public class Macro {
	private String mName, mHitsSequence, mShieldTurnsSequence,
			mPotionTurnsSequence;
	private boolean mCountingWinsOnly, mSkippingTickDialog, mSkippingOkDialog;
	private int mLifeRegenWait, mMonsterNumberInMenu, mRunsAmount, mBotGoal,
			mLifeRegenOption, mSleepTimeNormal = 1000,
			mSleepTimeTournament = 5000;

	public Macro() {
	}

	public Macro(String name) {
		setName(name);
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getHitsSequence() {
		return mHitsSequence;
	}

	public void setHitsSequence(String hitsSequence) {
		mHitsSequence = hitsSequence;
	}

	public String getShieldTurnsSequence() {
		return mShieldTurnsSequence;
	}

	public void setShieldTurnsSequence(String shieldTurnsSequence) {
		mShieldTurnsSequence = shieldTurnsSequence;
	}

	public String getPotionTurnsSequence() {
		return mPotionTurnsSequence;
	}

	public void setPotionTurnsSequence(String potionTurnsSequence) {
		mPotionTurnsSequence = potionTurnsSequence;
	}

	public boolean isCountingWinsOnly() {
		return mCountingWinsOnly;
	}

	public void setCountingWinsOnly(boolean countingWinsOnly) {
		mCountingWinsOnly = countingWinsOnly;
	}

	public boolean isSkippingTickDialog() {
		return mSkippingTickDialog;
	}

	public void setSkippingTickDialog(boolean skippingTickDialog) {
		mSkippingTickDialog = skippingTickDialog;
	}

	public boolean isSkippingOkDialog() {
		return mSkippingOkDialog;
	}

	public void setSkippingOkDialog(boolean skippingOkDialog) {
		mSkippingOkDialog = skippingOkDialog;
	}

	public int getLifeRegenWait() {
		return mLifeRegenWait;
	}

	public void setLifeRegenWait(int lifeRegenWait) {
		mLifeRegenWait = lifeRegenWait;
	}

	public int getMonsterNumberInMenu() {
		return mMonsterNumberInMenu;
	}

	public void setMonsterNumberInMenu(int monsterNumberInMenu) {
		mMonsterNumberInMenu = monsterNumberInMenu;
	}

	public int getRunsAmount() {
		return mRunsAmount;
	}

	public void setRunsAmount(int runsAmount) {
		mRunsAmount = runsAmount;
	}

	public int getBotGoal() {
		return mBotGoal;
	}

	public void setBotGoal(int botGoal) {
		mBotGoal = botGoal;
	}

	public int getLifeRegenOption() {
		return mLifeRegenOption;
	}

	public void setLifeRegenOption(int lifeRegenOption) {
		mLifeRegenOption = lifeRegenOption;
	}

	public int getSleepTimeNormal() {
		return mSleepTimeNormal;
	}

	public void setSleepTimeNormal(int sleepTimeNormal) {
		mSleepTimeNormal = sleepTimeNormal;
	}

	public int getSleepTimeTournament() {
		return mSleepTimeTournament;
	}

	public void setSleepTimeTournament(int sleepTimeTournament) {
		mSleepTimeTournament = sleepTimeTournament;
	}
}
