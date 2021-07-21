package com.board.draw.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
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
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.board.draw.R;
import com.board.draw.constants.DrawPath;
import com.board.draw.util.BitmapUtil;
import com.board.draw.util.DateUtil;
import com.board.draw.util.DrawMode;
import com.board.draw.util.PaintMode;
import com.board.draw.util.PathUtil;
import com.board.draw.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 画布
 */
public class PathDrawingView extends View {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mx, my;

    private Paint mPaint;
    private PathEffect mPathEffect;
    //画笔类型
    private PaintMode curPaintMode = PaintMode.PENCIL;
    //画布类型
    private DrawMode curDrawMode = DrawMode.DRAW_PATH;

    private PorterDuffXfermode porterDuffXfermode;
    //图案笔
    private PathDashPathEffect pathDashPathEffect;
    //虚线笔
    private DashPathEffect dashPathEffect1;
    private DashPathEffect dashPathEffect2;
    private DashPathEffect dashPathEffect3;
    private DashPathEffect dashPathEffect4;
    private DashPathEffect dashPathEffect5;

    //本地图片Bitmap
    private Bitmap localBitmap;
    private BitmapShader bitmapShader;

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
    //虚线效果3：画10、空10、画1、空10、画1、空10、画1、空10
    private final float[] interval3 = new float[]{10, 10, 1, 10, 1, 10, 1, 10};
    //虚线4效果:画10、空10、画1、空10
    private final float[] interval4 = new float[]{10, 10, 1, 10};
    //虚线5效果：画10、空10、画1、空10、画1、空10
    private final float[] interval5 = new float[]{10, 10, 1, 10, 1, 10};

    public PathDrawingView(Context context) {
        this(context, null);
    }

    public PathDrawingView(Context context, @Nullable AttributeSet attrs) {
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
        //虚线笔3
        dashPathEffect3 = new DashPathEffect(interval3, 0);
        //虚线4
        dashPathEffect4 = new DashPathEffect(interval4, 0);
        //虚线5
        dashPathEffect5 = new DashPathEffect(interval5, 0);
        //图片笔
        bitmapShader = getLocalBitmap(R.mipmap.ic_flower);
    }

    private void initBitmap() {
        mBitmap = Bitmap.createBitmap(ScreenUtil.getWidth(getContext()), ScreenUtil.getHeight(getContext()), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    /**
     * 得到本地图片的bitmapShader对象
     */
    public BitmapShader getLocalBitmap(int imageResId) {
        localBitmap = BitmapFactory.decodeResource(getResources(), imageResId);
        return new BitmapShader(localBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    /**
     * 设置画笔类型
     */
    public void setCurPaintMode(PaintMode curPaintMode) {
        this.curPaintMode = curPaintMode;
    }

    /**
     * 设置画布类型
     */
    public void setCurDrawMode(DrawMode curDrawMode) {
        this.curDrawMode = curDrawMode;
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
    public void selectPaintColor(int selectedColor) {
        setPaintColor(selectedColor);
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

    /**
     * 清屏操作
     */
    public void clearScreen() {
        paths.clear();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (curPaintMode == PaintMode.PATTERN_PEN) {
                    //图案笔
                    mPath = PathUtil.drawStarPath(25);
                    pathDashPathEffect = new PathDashPathEffect(mPath, 25 * 1.5f, 0, PathDashPathEffect.Style.ROTATE);
                } else {
                    mPath = new Path();
                }

                paths.add(new DrawPath(mPath, getPaintColor(), curPaintMode, false, getBrushSize()));
                paints.add(mPaint);
                mPath.reset();

                mx = event.getX();
                my = event.getY();
                mPath.moveTo(mx, my);
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
    public void saveToBitmap(Context context, String fileName) {
        BitmapUtil.saveBitmapToLocal(context, this, fileName.concat("-").concat(DateUtil.timeToDate()));
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
                //画笔类型
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
                    case CIRCLE_LINE_DASHED_LINE:
                        //虚线笔3
                        mPaint.setPathEffect(dashPathEffect3);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                    case ALL_CIRCLE_DASHED_LINE:
                        //虚线4
                        mPaint.setPathEffect(dashPathEffect4);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                    case FIVE_DASHED_LINE:
                        //虚线5
                        mPaint.setPathEffect(dashPathEffect5);

                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                    case FLOWER_PEN:

                        mPaint.setPathEffect(null);
                        mPaint.setXfermode(null);
                        mPaint.setMaskFilter(null);
                        break;
                }

                //画布类型
                switch (curDrawMode) {
                    case DRAW_PATH:
                    default:
                        //路径
                        mCanvas.drawPath(drawPath.getPath(), this.mPaint);
                        break;
                    case DRAW_CIRCLE:
                        //圆
                        mCanvas.drawCircle(mx, my, 300f, mPaint);
                        break;
                    case DRAW_TRIANGLE:
                        //三角形

                        break;
                    case DRAW_RECTANGLE:
                        //矩形

                        break;
                    case DRAW_STRAIGHT_LINE:
                        //直线

                        break;
                    case DRAW_POLYGON:
                        //多边形

                        break;
                }
            }
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, this.mBitmapPaint);
        canvas.restore();
    }
}