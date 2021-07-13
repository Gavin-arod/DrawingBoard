package com.board.draw.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.board.draw.R;
import com.board.draw.constants.BrushType;
import com.board.draw.dialog.SaveImageDialog;
import com.board.draw.dialog.SelectBrushDialog;
import com.board.draw.dialog.SelectCanvasBackgroundDialog;
import com.board.draw.impl.ItemClickListener;
import com.board.draw.impl.SaveImageLocalListener;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.ui.view.DrawingView;
import com.board.draw.ui.view.VirtualColorSeekBar;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.PaintMode;
import com.board.draw.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * drawing page
 */
public class CornerPathActivity extends BaseActivity implements View.OnClickListener, ItemClickListener,
        VirtualColorSeekBar.OnStateChangeListener, SaveImageLocalListener {
    private DrawingView cornerPathEffectView;
    private int paintColorIndex = 0;
    private List<Bitmap> imagesList;

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
            imagesList = AssetsUtil.getAssetsFiles(CornerPathActivity.this);
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
        SelectCanvasBackgroundDialog dialog = new SelectCanvasBackgroundDialog(CornerPathActivity.this, imagesList, this);
        dialog.setBackgroundColor(R.color.transparent);
        dialog.showPopupWindow();
    }

    /**
     * select brush
     */
    private void showSelectBrushTypeDialog() {
        SelectBrushDialog selectBrushDialog = new SelectBrushDialog(CornerPathActivity.this);
        selectBrushDialog.showPopupWindow();
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
            cornerPathEffectView.setCurPaintMode(PaintMode.CIRCLE_DASHED_LINE);
        } else if (type == 205) {
            //图案笔
            cornerPathEffectView.setCurPaintMode(PaintMode.PATTERN_PEN);
        } else if (type == 206) {
            //图片笔
            cornerPathEffectView.setCurPaintMode(PaintMode.FLOWER_PEN);
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
            cornerPathEffectView.clearScreen();
        }
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
        SaveImageDialog saveImageDialog = new SaveImageDialog(CornerPathActivity.this);
        saveImageDialog.setSaveImageLocalListener(this);
        saveImageDialog.showPopupWindow();
    }

    @Override
    public void saveLocal(String fileName) {
        cornerPathEffectView.saveToBitmap(CornerPathActivity.this, fileName);
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
