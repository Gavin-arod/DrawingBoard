package com.board.draw.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.board.draw.R;
import com.board.draw.constants.BrushType;
import com.board.draw.constants.CanvasType;
import com.board.draw.dialog.ClearScreenDialog;
import com.board.draw.dialog.SaveImageDialog;
import com.board.draw.dialog.SelectBrushDialog;
import com.board.draw.dialog.SelectCanvasBackgroundDialog;
import com.board.draw.dialog.SelectCanvasModeDialog;
import com.board.draw.impl.CanvasTypeClickListener;
import com.board.draw.impl.ItemClickListener;
import com.board.draw.impl.OnClearScreenListener;
import com.board.draw.impl.SaveImageLocalListener;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.ui.view.DrawingView;
import com.board.draw.ui.view.VirtualColorSeekBar;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.DrawMode;
import com.board.draw.util.PaintMode;
import com.board.draw.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * drawing page
 */
public class DrawingBoardActivity extends BaseActivity implements View.OnClickListener, ItemClickListener,
        VirtualColorSeekBar.OnStateChangeListener, SaveImageLocalListener, CanvasTypeClickListener,
        OnClearScreenListener {
    private DrawingView cornerPathEffectView;
    private int paintColorIndex = 0;
    private List<Bitmap> imagesList;
    //画布类型集合
    private final List<CanvasType> canvasTypeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_path);

        cornerPathEffectView = findViewById(R.id.corner_path_view);
        cornerPathEffectView.setRoundedCorner(300);

        //获取本地图片列表
        if (imagesList == null) {
            imagesList = AssetsUtil.getAssetsFiles(DrawingBoardActivity.this);
        }

        //brush size SeekBar view
        VirtualColorSeekBar seekBar = findViewById(R.id.brush_size_seek_bar);
        seekBar.setProgress(5f);
        seekBar.setOnStateChangeListener(this);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        //显示背景图
        findViewById(R.id.btn_show_back_image).setOnClickListener(this);
        //橡皮擦
        findViewById(R.id.btn_eraser).setOnClickListener(this);
        //清屏
        findViewById(R.id.btn_clear_screen).setOnClickListener(this);
        //保存
        findViewById(R.id.btn_save).setOnClickListener(this);
        //改变笔刷颜色
        findViewById(R.id.btn_change_paint_color).setOnClickListener(this);
        //撤回到上一步
        findViewById(R.id.btn_revoke).setOnClickListener(this);
        //恢复
        findViewById(R.id.btn_restore).setOnClickListener(this);
        //brush button
        findViewById(R.id.btn_show_brush).setOnClickListener(this);
        findViewById(R.id.btn_show_graphics).setOnClickListener(this);

        canvasTypeList.addAll(initData());
    }

    private List<CanvasType> initData() {
        List<CanvasType> list = new ArrayList<>();

        list.add(buildCanvasType(1, R.mipmap.ic_shape_path, "路径"));
        list.add(buildCanvasType(2, R.mipmap.ic_shape_circular, "圆形"));
        list.add(buildCanvasType(3, R.mipmap.ic_shape_straight, "直线"));
        list.add(buildCanvasType(4, R.mipmap.ic_shape_triangle, "三角形"));
        list.add(buildCanvasType(5, R.mipmap.ic_shape_hexagon, "多边形"));
        list.add(buildCanvasType(6, R.mipmap.ic_shape_rectangle, "矩形"));
        return list;
    }

    private CanvasType buildCanvasType(int type, int resId, String name) {
        CanvasType canvasType = new CanvasType();
        canvasType.setType(type);
        canvasType.setGraphicsId(resId);
        canvasType.setName(name);
        return canvasType;
    }

    private void showSelectPaintColorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.custom_alert_dialog);
        builder.setTitle("选择画笔颜色：");

        builder.setSingleChoiceItems(R.array.paintColor, paintColorIndex, (dialog, which) -> {
            paintColorIndex = which;
            cornerPathEffectView.selectPaintColor(which);
            dialog.dismiss();
        });
        builder.show();
    }

    /**
     * select canvas background image
     */
    private void showSelectBackImageDialog() {
        SelectCanvasBackgroundDialog dialog = new SelectCanvasBackgroundDialog(DrawingBoardActivity.this, imagesList, this);
        dialog.setBackgroundColor(R.color.transparent);
        dialog.showPopupWindow();
    }

    /**
     * select brush
     */
    private void showSelectBrushTypeDialog() {
        SelectBrushDialog selectBrushDialog = new SelectBrushDialog(DrawingBoardActivity.this);
        selectBrushDialog.showPopupWindow();
    }

    /**
     * select draw mode
     */
    private void showSelectDrawModeDialog() {
        SelectCanvasModeDialog dialog = new SelectCanvasModeDialog(DrawingBoardActivity.this, canvasTypeList, this);
        dialog.showPopupWindow();
    }

    @Override
    public void clickCanvasItemType(CanvasType canvasType) {
        for (int i = 0; i < canvasTypeList.size(); i++) {
            CanvasType curCanvasType = canvasTypeList.get(i);
            canvasTypeList.get(i).setSelected(canvasType.getType() == curCanvasType.getType());
        }

        switch (canvasType.getType()) {
            case 1:
            default:
                //路径
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_PATH);
                break;
            case 2:
                //圆形
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_CIRCLE);
                break;
            case 3:
                //直线
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_STRAIGHT_LINE);
                break;
            case 4:
                //三角形
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_TRIANGLE);
                break;
            case 5:
                //多边形
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_POLYGON);
                break;
            case 6:
                //矩形
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_RECTANGLE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectBrushEvent(BrushType brushType) {
        int type = brushType.getType();
        if (type == 200) {
            //圆笔
            cornerPathEffectView.setCurPaintMode(PaintMode.PENCIL);
        } else if (type == 201) {
            //荧光笔
            cornerPathEffectView.setCurPaintMode(PaintMode.HIGHLIGHTER);
        } else if (type == 202) {
            //虚线笔1
            cornerPathEffectView.setCurPaintMode(PaintMode.EQUAL_DASHED_LINE);
        } else if (type == 203) {
            //虚线笔2
            cornerPathEffectView.setCurPaintMode(PaintMode.UNEQUAL_DASHED_LINE);
        } else if (type == 204) {
            //虚线笔3
            cornerPathEffectView.setCurPaintMode(PaintMode.CIRCLE_LINE_DASHED_LINE);
        } else if (type == 205) {
            //虚线笔4
            cornerPathEffectView.setCurPaintMode(PaintMode.ALL_CIRCLE_DASHED_LINE);
        } else if (type == 206) {
            //图案笔
            cornerPathEffectView.setCurPaintMode(PaintMode.PATTERN_PEN);
        } else if (type == 207) {
            //图片笔
            cornerPathEffectView.setCurPaintMode(PaintMode.FLOWER_PEN);
        } else if (type == 208) {
            //虚线5
            cornerPathEffectView.setCurPaintMode(PaintMode.FIVE_DASHED_LINE);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_save) {
            //save
            showSaveFileDialog();
        } else if (viewId == R.id.btn_change_paint_color) {
            //brush color
            showSelectPaintColorDialog();
        } else if (viewId == R.id.btn_show_back_image) {
            //select canvas background
            showSelectBackImageDialog();
        } else if (viewId == R.id.btn_show_brush) {
            //show brush selection
            showSelectBrushTypeDialog();
        } else if (viewId == R.id.btn_revoke) {
            //revoke
            cornerPathEffectView.revokePath();
        } else if (viewId == R.id.btn_restore) {
            //restore
            cornerPathEffectView.restorePath();
        } else if (viewId == R.id.btn_eraser) {
            //eraser
            cornerPathEffectView.setCurPaintMode(PaintMode.ERASER);
        } else if (viewId == R.id.btn_clear_screen) {
            //clear screen
            ClearScreenDialog clearScreenDialog = new ClearScreenDialog(DrawingBoardActivity.this);
            clearScreenDialog.setClearScreenListener(this);
            clearScreenDialog.showPopupWindow();
        } else if (viewId == R.id.btn_show_graphics) {
            //select draw graphics
            showSelectDrawModeDialog();
        }
    }

    /**
     * 清屏回调
     */
    @Override
    public void clearScreen() {
        cornerPathEffectView.clearScreen();
    }

    @Override
    public void itemClick(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        cornerPathEffectView.setBackground(drawable);
    }

    /**
     * 显示保存弹框
     */
    private void showSaveFileDialog() {
        SaveImageDialog saveImageDialog = new SaveImageDialog(DrawingBoardActivity.this);
        saveImageDialog.setSaveImageLocalListener(this);
        saveImageDialog.showPopupWindow();
    }

    @Override
    public void saveLocal(String fileName) {
        cornerPathEffectView.saveToBitmap(DrawingBoardActivity.this, fileName);
    }

    //change brush size
    @Override
    public void onStateChange(View view, float progress) {
        cornerPathEffectView.setBrushSize(progress / 2f);
    }

    @Override
    public void onStopTrackingTouch(View view, float progress) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        //画布销毁时，重置画笔类型
        SPUtil.putInt("selectedBrushType", -1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //画布销毁时，重置画笔类型
        SPUtil.putInt("selectedBrushType", -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //画布销毁时，重置画笔类型
        SPUtil.putInt("selectedBrushType", -1);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }
}
