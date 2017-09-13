package com.vk.challenge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.vk.challenge.utils.AndroidUtils;

/**
 * Created by nikita on 07.09.17.
 */

public class FontBackgroundEditText extends TFEditText {

    private static final int CORNER_RADIUS = 4;//dp
    private static final int PADDING_HORIZONTAL = 9;//dp
    private static final int PADDING_TOP = 2;//dp
    private static final int PADDING_BOT = 4;//dp

    private int mBgColor = Color.TRANSPARENT;

    private Paint mBgPaint;

    private Path mBgPath;

    private RectF mLineBounds = new RectF();

    private int mPaddingHorizontal;
    private int mPaddingTop;
    private int mPaddingBot;

    public FontBackgroundEditText(Context context) {
        super(context);
        init(context);
    }

    public FontBackgroundEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontBackgroundEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setDither(true);
        mBgPaint.setPathEffect(new CornerPathEffect(dm.density * CORNER_RADIUS));
        mBgPath = new Path();
        mPaddingHorizontal = (int) (dm.density * PADDING_HORIZONTAL);
        mPaddingTop = (int) (dm.density * PADDING_TOP);
        mPaddingBot = (int) (dm.density * PADDING_BOT);
    }

    public void setFontBackgroundColor(int color) {
        mBgColor = color;
        invalidate();
    }

    public int getFontBackgroundColor() {
        return mBgColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        Layout layout = getLayout();
        if (layout == null || getText() == null || getText().length() == 0) {
            return;
        }
        mBgPaint.setColor(mBgColor);
        mBgPath.reset();

        int lineCount = layout.getLineCount();

        RectF line0Bounds = getLineBounds(0);
        mBgPath.moveTo(line0Bounds.left, line0Bounds.top);

        for (int i = 0; i < lineCount; i++) {
            RectF lineBounds = getLineBounds(i);
            mBgPath.lineTo(lineBounds.right, lineBounds.top);
            mBgPath.lineTo(lineBounds.right, lineBounds.bottom);
        }

        for (int i = lineCount - 1; i >= 0; i--) {
            RectF lineBounds = getLineBounds(i);
            mBgPath.lineTo(lineBounds.left, lineBounds.bottom);
            mBgPath.lineTo(lineBounds.left, lineBounds.top);
        }
        mBgPath.close();
        canvas.drawPath(mBgPath, mBgPaint);
    }

    private RectF getLineBounds(int line) {
        float left = getLayout().getLineLeft(line) - mPaddingHorizontal;
        float right = getLayout().getLineRight(line) + mPaddingHorizontal;
        float top = getLayout().getLineTop(line);
        float bot = getLayout().getLineBottom(line);
        if (line == 0) {
            top -= mPaddingTop;
        }
        if (line == getLayout().getLineCount() - 1) {
            bot += mPaddingBot;
        }
        mLineBounds.set(left, top, right, bot);
        return mLineBounds;
    }
}
