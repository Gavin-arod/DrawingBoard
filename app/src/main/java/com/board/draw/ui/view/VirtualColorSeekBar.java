package com.board.draw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 自定义竖直的SeekBar
 */
public class VirtualColorSeekBar extends View {
    private int startColor = Color.RED;
    private int middleColor = Color.YELLOW;
    private int endColor = Color.GREEN;
    private int thumbColor = Color.DKGRAY;
    private int thumbBorderColor = Color.TRANSPARENT;
    private final int[] colorArray = {startColor, middleColor, endColor};
    private float x, y;
    private float mRadius;
    private float progress;
    private float maxCount = 50f;
    private float sLeft, sTop, sRight, sBottom;
    private float sWidth, sHeight;
    private final Paint paint = new Paint();
    protected OnStateChangeListener onStateChangeListener;

    public VirtualColorSeekBar(Context context) {
        super(context);
    }

    public VirtualColorSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VirtualColorSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setColor(int startColor, int middleColor, int endColor, int thumbColor, int thumbBorderColor) {
        this.startColor = startColor;
        this.middleColor = middleColor;
        this.endColor = endColor;
        this.thumbColor = thumbColor;
        this.thumbBorderColor = thumbBorderColor;
        colorArray[0] = startColor;
        colorArray[1] = middleColor;
        colorArray[2] = endColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = getMeasuredHeight();
        int w = getMeasuredWidth();
        mRadius = (float) w / 2;
        // 背景左边缘坐标
        sLeft = w * 0.25f;
        // 背景右边缘坐标
        sRight = w * 0.75f;
        sTop = 0;
        sBottom = h;
        // 背景宽度
        sWidth = sRight - sLeft;
        // 背景高度
        sHeight = sBottom - sTop;
        //圆心的x坐标
        x = (float) w / 2;
        //圆心y坐标
        y = (float) (1 - 0.01 * progress) * sHeight;
        drawBackground(canvas);
        drawCircle(canvas);
        paint.reset();
    }

    private void drawBackground(Canvas canvas) {
        RectF rectBlackBg = new RectF(sLeft, sTop, sRight, sBottom);
        LinearGradient linearGradient = new LinearGradient(sLeft, sTop, sWidth, sHeight, colorArray, null, Shader.TileMode.MIRROR);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //设置渲染器
        paint.setShader(linearGradient);
        canvas.drawRoundRect(rectBlackBg, sWidth / 2, sWidth / 2, paint);
    }

    private void drawCircle(Canvas canvas) {
        Paint thumbPaint = new Paint();
        //判断thumb边界
        y = Math.max(y, mRadius);
        y = Math.min(y, sHeight - mRadius);
        thumbPaint.setAntiAlias(true);
        thumbPaint.setDither(true);
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setColor(thumbColor);
        canvas.drawCircle(x, y, mRadius, thumbPaint);
        thumbPaint.setStyle(Paint.Style.STROKE);
        thumbPaint.setColor(thumbBorderColor);
        thumbPaint.setStrokeWidth(2);
        canvas.drawCircle(x, y, mRadius, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.y = event.getY();
        progress = (sHeight - y) / sHeight * 100;
        Log.e("进度：", progress + "");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStopTrackingTouch(this, progress);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStateChange(this, progress);
                }
                setProgress(progress);
                this.invalidate();
                break;
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public interface OnStateChangeListener {
        void onStateChange(View view, float progress);

        void onStopTrackingTouch(View view, float progress);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}