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
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
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
import com.board.draw.util.PiUtil;
import com.board.draw.util.ScreenUtil;
import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 画布
 */
public class CircleDrawingView extends View {
    private Bitmap mBitmap;
    private Canvas mPathCanvas;
    private Canvas mCircleCanvas;
    private Canvas mLineCanvas;
    private final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mx, my;

    private Paint mPathPaint;
    //TRIANGLE
    private Paint mTrianglePaint;
    //圆画笔
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

    //椭圆
    private final RectF ovalRectF = new RectF();
    //矩形RECTANGLE
    private final RectF rectangleRectF = new RectF();

    //本地图片Bitmap
    private Bitmap localBitmap;
    private BitmapShader bitmapShader;

    private float startX;
    private float startY;

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

    public CircleDrawingView(Context context) {
        this(context, null);
    }

    public CircleDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
        init();
    }

    private void init() {
        //画布初始背景设为白色
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initPaintStyle(mPathPaint);
        initTrianglePaint();
        initPathType();
    }

    //三角形画笔
    private void initTrianglePaint() {
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setDither(true);
        mTrianglePaint.setStyle(Paint.Style.STROKE);
        mTrianglePaint.setStrokeWidth(getBrushSize());
        mTrianglePaint.setColor(getPaintColor());
    }

    private void initPathType() {
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
        mPathCanvas = new Canvas(mBitmap);
        mCircleCanvas = new Canvas(mBitmap);
        mLineCanvas = new Canvas(mBitmap);
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

    public void initPaintStyle(Paint paint) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(getBrushSize());
        paint.setColor(getPaintColor());
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
                mx = event.getX();
                my = event.getY();

                startX = event.getX();
                startY = event.getY();

                if (curPaintMode == PaintMode.PATTERN_PEN) {
                    //图案笔
                    mPath = PathUtil.drawStarPath(25);
                    pathDashPathEffect = new PathDashPathEffect(mPath, 25 * 1.5f, 0, PathDashPathEffect.Style.ROTATE);
                } else {
                    mPath = new Path();
                }

                paths.add(new DrawPath(mPath, getPaintColor(), curPaintMode, false, getBrushSize()));
                paints.add(mPathPaint);
                mPath.reset();

                mPath.moveTo(mx, my);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float abs = Math.abs(event.getX() - mx);
                float abs2 = Math.abs(event.getY() - my);
                switch (curDrawMode) {
                    case DRAW_PATH:
                    default:
                        if (abs >= getBrushSize() || abs2 >= getBrushSize()) {
                            float f3 = 2f;
                            mPath.quadTo(mx, my, (mx + x) / f3, (my + y) / f3);
                            mx = x;
                            my = y;
                        }
                        break;
                    case DRAW_CIRCLE:
                    case DRAW_OVAL:
                    case DRAW_STRAIGHT_LINE:
                    case DRAW_TRIANGLE:
                    case DRAW_RECTANGLE:
                    case DRAW_POLYGON:
                        mx = event.getX();
                        my = event.getY();
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                switch (curDrawMode) {
                    case DRAW_PATH:
                        //路径
                        mPath.lineTo(mx, my);
                        break;
                    case DRAW_CIRCLE:
                        //圆形
                        mPath.addCircle(Math.abs(mx - (mx - startX) / 2f), Math.abs(my - (my - startY) / 2f), Math.abs((mx - startX) / 2f), Path.Direction.CW);
                        break;
                    case DRAW_OVAL:
                        //椭圆
                        ovalRectF.set(startX, startY, mx, my);
                        mPath.addOval(ovalRectF, Path.Direction.CW);
                        break;
                    case DRAW_STRAIGHT_LINE:
                        //直线
                        mPath.lineTo(mx, my);
                        break;
                    case DRAW_TRIANGLE:
                        //三角形
                        mPath.moveTo(startX, startY);
                        mPath.lineTo(startX, my);
                        mPath.lineTo(mx, my);
                        mPath.close();
                        break;
                    case DRAW_POLYGON:
                        //多边形
                        drawPolygonPath(mPath, 6, ScreenUtil.dip2px(getContext(), 30));
                        break;
                    case DRAW_RECTANGLE:
                        //矩形
                        rectangleRectF.set(startX, startY, mx, my);
                        mPath.addRect(rectangleRectF, Path.Direction.CW);
                        break;
                }
                break;
        }
        performClick();
        invalidate();
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

    //绘制多边形
    private void drawPolygonPath(Path path, int count, int radius) {
        //计算单元角度
        float unitAngle = (float) (Math.PI * 2 / count);
        //初始角度
        float angle = 0;
        float xLength, yLength;
//        path.moveTo(startX, startY);
        //遍历计算点，并lineTo()绘制路径
        for (int i = 0; i < count; i++) {
            if (angle <= unitAngle) {
                xLength = (float) (radius * Math.cos(unitAngle));
                yLength = (float) (radius * Math.sin(unitAngle));
            } else {
                xLength = (float) (radius * Math.cos(angle));
                yLength = (float) (radius * Math.sin(angle));
            }
            //绘制路径
            path.lineTo(startX + xLength, startY - yLength);
            angle += unitAngle;
        }
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }
        mPathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mCircleCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mLineCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.save();
        //绘制带有效果的路径
        if (paths.size() > 0) {
            for (DrawPath drawPath : paths) {
                mPathPaint.setStrokeWidth(drawPath.getBrushSize());
                mPathPaint.setColor(drawPath.getColor());
                //画笔类型
                switch (drawPath.getDrawingMode()) {
                    case ERASER:
                        //橡皮擦
                        mPathPaint.setXfermode(porterDuffXfermode);

                        mPathPaint.setMaskFilter(null);
                        mPathPaint.setPathEffect(mPathEffect);
                        break;
                    case PENCIL:
                        //铅笔
                        mPathPaint.setPathEffect(mPathEffect);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case HIGHLIGHTER:
                        //荧光笔
                        mPathPaint.setMaskFilter(blurMaskFilter);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setPathEffect(mPathEffect);
                        break;
                    case PATTERN_PEN:
                        //图案笔
                        mPathPaint.setPathEffect(pathDashPathEffect);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case EQUAL_DASHED_LINE:
                        //虚线笔1
                        mPathPaint.setPathEffect(dashPathEffect1);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case UNEQUAL_DASHED_LINE:
                        //虚线笔2
                        mPathPaint.setPathEffect(dashPathEffect2);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case CIRCLE_LINE_DASHED_LINE:
                        //虚线笔3
                        mPathPaint.setPathEffect(dashPathEffect3);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case ALL_CIRCLE_DASHED_LINE:
                        //虚线4
                        mPathPaint.setPathEffect(dashPathEffect4);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case FIVE_DASHED_LINE:
                        //虚线5
                        mPathPaint.setPathEffect(dashPathEffect5);

                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                    case FLOWER_PEN:

                        mPathPaint.setPathEffect(null);
                        mPathPaint.setXfermode(null);
                        mPathPaint.setMaskFilter(null);
                        break;
                }

                if (curDrawMode == DrawMode.DRAW_RECTANGLE) {
                    mPathCanvas.drawRect(rectangleRectF, mTrianglePaint);
                } else if (curDrawMode == DrawMode.DRAW_TRIANGLE ||
                        curDrawMode == DrawMode.DRAW_POLYGON) {
                    mPathCanvas.drawPath(drawPath.getPath(), this.mTrianglePaint);
                } else {
                    mPathCanvas.drawPath(drawPath.getPath(), this.mPathPaint);
                }
            }

            switch (curDrawMode) {
                case DRAW_CIRCLE:
                    //圆
                    mPathCanvas.drawCircle(Math.abs(mx - (mx - startX) / 2f), Math.abs(my - (my - startY) / 2f), Math.abs((mx - startX) / 2f), mPathPaint);
                    break;
                case DRAW_OVAL:
                    ovalRectF.set(startX, startY, mx, my);
                    mPathCanvas.drawOval(ovalRectF, mPathPaint);
                    break;
                case DRAW_TRIANGLE:
                    //三角形
//                    mPath.moveTo(startX, startY);
//                    mPath.lineTo(startX, my);
//                    mPath.lineTo(mx, my);
//                    mPath.close();
                    mPathCanvas.drawPath(mPath, mTrianglePaint);
                    break;
                case DRAW_RECTANGLE:
                    //矩形
                    rectangleRectF.set(startX, startY, mx, my);
                    mPathCanvas.drawRect(rectangleRectF, mTrianglePaint);
                    break;
                case DRAW_STRAIGHT_LINE:
                    //直线
                    mPathCanvas.drawLine(startX, startY, mx, my, mPathPaint);
                    break;
                case DRAW_POLYGON:
                    //多边形
                    drawPolygonPath(mPath, 6, ScreenUtil.dip2px(getContext(), 30));
                    mPathCanvas.drawPath(mPath, mTrianglePaint);
                    break;
            }

        } else {
            //画布类型
            switch (curDrawMode) {
                case DRAW_CIRCLE:
                    //圆
                    mCircleCanvas.drawCircle(Math.abs(mx - (mx - startX) / 2f), Math.abs(my - (my - startY) / 2f), Math.abs((mx - startX) / 2f), mPathPaint);
                    break;
                case DRAW_OVAL:
                    ovalRectF.set(startX, startY, mx, my);
                    mPathCanvas.drawOval(ovalRectF, mTrianglePaint);
                    break;
                case DRAW_TRIANGLE:
                    //三角形
                    mPathCanvas.drawPath(mPath, mTrianglePaint);
                    break;
                case DRAW_RECTANGLE:
                    //矩形
                    rectangleRectF.set(startX, startY, mx, my);
                    mPathCanvas.drawRect(rectangleRectF, mTrianglePaint);
                    break;
                case DRAW_STRAIGHT_LINE:
                    //直线
                    mLineCanvas.drawLine(startX, startY, mx, my, mPathPaint);
                    break;
                case DRAW_POLYGON:
                    //多边形
                    drawPolygonPath(mPath, 6, ScreenUtil.dip2px(getContext(), 30));
                    mPathCanvas.drawPath(mPath, mTrianglePaint);
                    break;
            }
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, this.mBitmapPaint);
        canvas.restore();
    }
}
