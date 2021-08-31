package com.board.draw.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.board.draw.R;

/**
 * 画布背景View
 */
public class BackgroundView extends View {
    private final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //本地图片Bitmap
    private Bitmap localBitmap;
    //绘制背景图
    private boolean startDrawBackImage;

    public BackgroundView(Context context) {
        super(context, null);
    }

    public BackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //画布初始背景设为白色
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    /**
     * 得到本地图片bitmap
     */
    public void getLocalBitmap(Bitmap bitmap) {
        localBitmap = Bitmap.createBitmap(bitmap);
        startDrawBackImage = true;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //本地图片
        canvas.save();
        if (localBitmap != null && startDrawBackImage) {
            canvas.drawBitmap(localBitmap, 0, 0, mBitmapPaint);
        }
        canvas.restore();
    }
}
