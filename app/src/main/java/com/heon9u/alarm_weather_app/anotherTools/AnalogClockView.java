package com.heon9u.alarm_weather_app.anotherTools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.heon9u.alarm_weather_app.R;

import java.util.Calendar;

public class AnalogClockView extends View {

    private int mWidth;
    private int mRadius;
    private int mHeight;
    private double mAngle;
    private int mCentreX;
    private int mCentreY;
    private int mPadding;
    private boolean mIsInit;
    private Paint mPaint;
    private int[] mNumbers;
    private int mMinimum;
    private float mHour;
    private float mMinute;
    private float mSecond;
    private int mHourHandSize;
    private int mMinuteHandSize;
    private int mHandSize;
    private Rect mRect;

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        mHeight = getHeight();
        mWidth = getWidth();
        mPadding = 50;
        mCentreX = mWidth/2;
        mCentreY = mHeight/2; // 중앙값

        mMinimum = Math.min(mHeight, mWidth);
        mRadius = mMinimum/2 - mPadding;
        mAngle = (float) ((Math.PI/30) - (Math.PI/2));
        mPaint = new Paint();
        mRect = new Rect();
        mHourHandSize = mRadius - mRadius/2;
        mMinuteHandSize = mRadius - mRadius/5;
        mHandSize = mRadius - mRadius/4;
        mNumbers = new int[] {3, 6, 9, 12};
        mIsInit = true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!mIsInit) {
            init();
        }

        drawCircle(canvas);
        drawHands(canvas);
        drawCenter(canvas);

        // 아날로그 숫자 입력은 보류, 이쁘지가 않다
        // drawNumerals(canvas);
        postInvalidateDelayed(500);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.reset();

        setPaintAttributes(Color.WHITE, Paint.Style.FILL, 0);
        canvas.drawCircle(mCentreX, mCentreY, mRadius, mPaint);

        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8);
        canvas.drawCircle(mCentreX, mCentreY, mRadius, mPaint);
    }

    private void setPaintAttributes(int color, Paint.Style stroke, int strokeWidth) {
        mPaint.reset();
        mPaint.setColor(color);
        mPaint.setStyle(stroke);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);
    }

    private void drawHands(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mHour = mHour > 12 ? mHour-12:mHour;
        mMinute = calendar.get(Calendar.MINUTE);
        mSecond = calendar.get(Calendar.SECOND);

        drawHourHand(canvas, (mHour + mMinute/60.0) * 5f);
        drawMinuteHand(canvas, mMinute);
        drawSecondsHand(canvas, mSecond);
    }

    private void drawHourHand(Canvas canvas, double location) {
        mPaint.reset();
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 12);
        mAngle = Math.PI * location/30 - Math.PI/2;
        canvas.drawLine(mCentreX, mCentreY,
                (float) (mCentreX + Math.cos(mAngle)*mHourHandSize),
                (float) (mCentreY + Math.sin(mAngle)*mHourHandSize),
                mPaint);
    }

    private void drawMinuteHand(Canvas canvas, float location) {
        mPaint.reset();
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE,10);
        mAngle = Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(mCentreX, mCentreY,
                (float) (mCentreX + Math.cos(mAngle)*mMinuteHandSize),
                (float) (mCentreY + Math.sin(mAngle)*mMinuteHandSize),
                mPaint);
    }

    private void drawSecondsHand(Canvas canvas, float location) {
        mPaint.reset();
        setPaintAttributes(Color.RED, Paint.Style.STROKE,8);
        mAngle = Math.PI * location/30 - Math.PI/2;
        canvas.drawLine(mCentreX, mCentreY,
                (float) (mCentreX + Math.cos(mAngle)*mHandSize),
                (float) (mCentreY + Math.sin(mAngle)*mHandSize),
                mPaint);
    }

    private void drawCenter(Canvas canvas) {
        setPaintAttributes(Color.BLACK, Paint.Style.FILL, 0);
        canvas.drawCircle(mCentreX, mCentreY, 10, mPaint);
    }

    private void drawNumerals(Canvas canvas) {
        setPaintAttributes(R.color.purple_200, Paint.Style.FILL, 60);
        mPaint.setTextSize(40);
        mPaint.setFakeBoldText(true);

        for (int number : mNumbers) {
            String num = String.valueOf(number);
            mPaint.getTextBounds(num, 0, num.length(), mRect);
            double angle = Math.PI/6 * (number-3);

            int x = (int) (mCentreX + Math.cos(angle) * mRadius);
            int y = (int) (mCentreY + Math.sin(angle) * mRadius);

            if(angle == 0) {
                x = (int) (x - mRect.width()*1.5);
                y = y + mRect.height()/2;
            } else if(angle == Math.PI/6 * 3) {
                x = x - mRect.width()/2;
                y = y - mRect.height()/2;
            } else if(angle == Math.PI/6 * 6) {
                x = x + mRect.width()/2;
                y = y + mRect.height()/2;
            } else {
                x = x - mRect.width()/2;
                y = (int) (y + mRect.height()*1.5);
            }

            canvas.drawText(num, x, y, mPaint);
        }
    }
}

