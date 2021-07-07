package com.board.draw.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.board.draw.R;
import com.board.draw.constants.DrawPath;
import com.board.draw.util.BitmapUtil;
import com.board.draw.util.PaintMode;
import com.board.draw.util.PathUtil;
import com.board.draw.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 画笔
 */
public class DrawingView extends View {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mx, my;

    private Paint mPaint;
    private PathEffect mPathEffect;
    //画笔类型
    private PaintMode curPaintMode = PaintMode.PENCIL;

    private PorterDuffXfermode porterDuffXfermode;
    //图案笔
    private PathDashPathEffect pathDashPathEffect;
    //虚线笔
    private DashPathEffect dashPathEffect1;
    private DashPathEffect dashPathEffect2;

    //荧光效果
    private BlurMaskFilter blurMaskFilter;
    private Path mPath;
    //笔刷粗细
    private float brushSize = 5f;
    //线条圆角
    private int roundedCorner = 300;
    //画笔颜色
    private int paintColor = Color.RED;
    //路径集合
    private final List<DrawPath> paths = new ArrayList<>();
    //撤回的路径集合
    private final List<DrawPath> revokePaths = new ArrayList<>();
    //撤回的画笔集合
    private final List<Paint> revokePaints = new ArrayList<>();
    //画笔集合
    private final List<Paint> paints = new ArrayList<>();
    //虚线效果1://画10、空10
    private final float[] intervals = new float[]{10, 10};
    //虚线效果2：//画10、空15、画20、空15
    private final float[] intervals2 = new float[]{10, 15, 20, 15};

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
        init();
    }

    private void init() {
        //画布初始背景设为白色
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        setPaintStyle();
        initPaint();
    }

    private void initPaint() {
        //荧光效果
        blurMaskFilter = new BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER);
        mPathEffect = new CornerPathEffect(getRoundedCorner());
        //橡皮擦
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        //虚线笔1
        dashPathEffect1 = new DashPathEffect(intervals, 0);
        //虚线笔2
        dashPathEffect2 = new DashPathEffect(intervals2, 0);
    }

    private void initBitmap() {
        mBitmap = Bitmap.createBitmap(ScreenUtil.getWidth(getContext()), ScreenUtil.getHeight(getContext()), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void setCurPaintMode(PaintMode curPaintMode) {
        this.curPaintMode = curPaintMode;
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

    public float getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(float brushSize) {
        this.brushSize = brushSize;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public int getRoundedCorner() {
        return roundedCorner;
    }

    public void setRoundedCorner(int roundedCorner) {
        this.roundedCorner = roundedCorner;
    }

    public void setPaintStyle() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(getBrushSize());
        mPaint.setColor(getPaintColor());
    }


    /**
     * 设置画笔颜色
     */
    public void selectPaintColor(int which) {
        String color = this.getResources().getStringArray(R.array.paintColor)[which];
        int colorResId;
        switch (color) {
            case "BLUE":
                colorResId = Color.BLUE;
                break;
            case "BLACK":
                colorResId = Color.BLACK;
                break;
            case "GREEN":
                colorResId = Color.GREEN;
                break;
            case "YELLOW":
                colorResId = Color.YELLOW;
                break;
            case "CYAN":
                colorResId = Color.CYAN;
                break;
            case "LTGRAY":
                colorResId = Color.LTGRAY;
                break;
            case "RED":
            default:
                colorResId = Color.RED;
                break;
        }
        setPaintColor(colorResId);
    }

    /**
     * 撤回到上一步
     */
    public void revokePath() {
        if (paths == null) {
            return;
        }

        if (paths.size() == 0) {
            Toast.makeText(getContext(), "画布已空", Toast.LENGTH_SHORT).show();
            return;
        }

        DrawPath endPath = paths.get(paths.size() - 1);
        paths.remove(endPath);
        revokePaths.add(endPath);

        Paint endPaint = paints.get(paints.size() - 1);
        paints.remove(endPaint);
        revokePaints.add(endPaint);

        postInvalidate();
    }

    /**
     * 恢复Path
     */
    public void restorePath() {
        if (revokePaths == null) {
            return;
        }

        if (revokePaths.size() == 0) {
            Toast.makeText(getContext(), "已完全恢复", Toast.LENGTH_SHORT).show();
            return;
        }

        DrawPath endPath = revokePaths.get(revokePaths.size() - 1);
        if (paths.size() == 0) {
            paths.add(endPath);
        } else {
            paths.add(paths.size(), endPath);
        }
        revokePaths.remove(endPath);

        Paint endPaint = revokePaints.get(revokePaints.size() - 1);
        if (paints.size() == 0) {
            paints.add(paints.size(), endPaint);
        } else {
            paints.add(endPaint);
        }
        revokePaints.remove(endPaint);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (curPaintMode == PaintMode.PATTERN_PEN) {
                    //图案笔
                    mPath = PathUtil.buildStarPath();
                    pathDashPathEffect = new PathDashPathEffect(mPath, 50 * 1.5f, 0, PathDashPathEffect.Style.TRANSLATE);
                } else {
                    mPath = new Path();
                }

                paths.add(new DrawPath(mPath, getPaintColor(), curPaintMode, false, getBrushSize()));
                paints.add(mPaint);

                mPath.moveTo(event.getX(), event.getY());
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float abs = Math.abs(event.getX() - mx);
                float abs2 = Math.abs(event.getY() - my);
                if (abs >= getBrushSize() || abs2 >= getBrushSize()) {
                    float f3 = 2f;
                    mPath.quadTo(mx, my, (mx + x) / f3, (my + y) / f3);
                    mx = x;
                    my = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                mPath.lineTo(mx, my);
                break;
        }
        performClick();
        postInvalidate();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 保存画布
     */
    public void saveToBitmap(Context context) {
        BitmapUtil.saveBitmapToLocal(context, this, "画图-".concat(System.currentTimeMillis() + ""));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //绘制带有效果的路径
        canvas.save();
        if (paths.size() > 0) {
            for (DrawPath drawPath : paths) {
                mPaint.setStrokeWidth(drawPath.getBrushSize());
                mPaint.setColor(drawPath.getColor());

                switch (drawPath.getDrawingMode()) {
                    case ERASER:
                        //橡皮擦
                        mPaint.setXfermode(porterDuffXfermode);

                        mPaint.setMaskFilter(null);
                        mPaint.setPathEffect(mPathEffect);
                        break;
                    case PENCIL:
                        //铅笔
                        mPaint.setPathEffect(mPathEffect);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                    case HIGHLIGHTER:
                        //荧光笔
                        mPaint.setMaskFilter(blurMaskFilter);

                        mPaint.setXfermode(null);
                        mPaint.setPathEffect(mPathEffect);
                        break;
                    case PATTERN_PEN:
                        //图案笔
                        mPaint.setPathEffect(pathDashPathEffect);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                    case EQUAL_DASHED_LINE:
                        //虚线笔1
                        mPaint.setPathEffect(dashPathEffect1);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                    case UNEQUAL_DASHED_LINE:
                        //虚线笔2
                        mPaint.setPathEffect(dashPathEffect2);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                }
                mCanvas.drawPath(drawPath.getPath(), this.mPaint);
            }
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, this.mBitmapPaint);
        canvas.restore();
    }
}
