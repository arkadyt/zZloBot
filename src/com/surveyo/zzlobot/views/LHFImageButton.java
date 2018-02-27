package com.surveyo.zzlobot.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.surveyo.zzlobot.R;

public class LHFImageButton extends ImageButton {

	int CLICK_FEEDBACK_COLOR;
	static final int CLICK_FEEDBACK_INTERVAL = 10;
	static final int CLICK_FEEDBACK_DURATION = 350;

	float mTextX;
	float mTextY;
	long mAnimStart;
	Paint mFeedbackPaint;

	public LHFImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {

			Resources res = getResources();

			CLICK_FEEDBACK_COLOR = res.getColor(R.color.magic_flame);
			mFeedbackPaint = new Paint();
			mFeedbackPaint.setStyle(Style.STROKE);
			mFeedbackPaint.setStrokeWidth(2);

			mAnimStart = -1;
		}
	}

	private void drawMagicFlame(int duration, Canvas canvas) {
		int alpha = 255 - 255 * duration / CLICK_FEEDBACK_DURATION;
		int color = CLICK_FEEDBACK_COLOR | (alpha << 24);

		mFeedbackPaint.setColor(color);
		canvas.drawRect(1, 1, getWidth() - 1, getHeight() - 1, mFeedbackPaint);
	}

	@Override
	public void onDraw(Canvas canvas) {

		if (mAnimStart != -1) {
			int animDuration = (int) (System.currentTimeMillis() - mAnimStart);

			if (animDuration >= CLICK_FEEDBACK_DURATION) {
				mAnimStart = -1;
			} else {
				drawMagicFlame(animDuration, canvas);
				postInvalidateDelayed(CLICK_FEEDBACK_INTERVAL);
			}
		}
		if (isPressed()) {
			drawMagicFlame(0, canvas);
		}
		super.onDraw(canvas);
	}

	public void animateClickFeedback() {
		mAnimStart = System.currentTimeMillis();
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		boolean bool = super.onTouchEvent(paramMotionEvent);
		switch (paramMotionEvent.getAction()) {
		case 2:
		default:
			return bool;
		case 1:
			if (isPressed()) {
				animateClickFeedback();
				return bool;
			}
			invalidate();
			return bool;
		case 0:
		case 3:
		}
		this.mAnimStart = -1L;
		invalidate();
		return bool;
	}

}