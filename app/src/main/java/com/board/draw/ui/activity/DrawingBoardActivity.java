package com.board.draw.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
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
import com.board.draw.impl.SelectImageListener;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.ui.view.BackgroundView;
import com.board.draw.ui.view.CircleDrawingView;
import com.board.draw.ui.view.VirtualColorSeekBar;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.DrawMode;
import com.board.draw.util.PaintMode;
import com.board.draw.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * drawing page
 */
public class DrawingBoardActivity extends BaseActivity implements View.OnClickListener, ItemClickListener,
        VirtualColorSeekBar.OnStateChangeListener, SaveImageLocalListener, CanvasTypeClickListener,
        OnClearScreenListener, ColorChooserDialog.ColorCallback, SelectImageListener {
    private CircleDrawingView cornerPathEffectView;
    private BackgroundView backgroundView;
    private List<Bitmap> imagesList;
    //??????????????????
    private final List<CanvasType> canvasTypeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_path);

        backgroundView = findViewById(R.id.back_canvas_view);

        cornerPathEffectView = findViewById(R.id.corner_path_view);
        cornerPathEffectView.setRoundedCorner(300);

        setSelectImageListener(this);

        //????????????????????????
        if (imagesList == null) {
            imagesList = AssetsUtil.getAssetsFiles(DrawingBoardActivity.this);
        }

        //brush size SeekBar view
        VirtualColorSeekBar seekBar = findViewById(R.id.brush_size_seek_bar);
        seekBar.setProgress(2f);
        seekBar.setOnStateChangeListener(this);

        AppCompatButton btnShowBackImage = findViewById(R.id.btn_show_back_image);
        AppCompatButton btnEraser = findViewById(R.id.btn_eraser);
        AppCompatButton btnClearScreen = findViewById(R.id.btn_clear_screen);
        AppCompatButton btnShowBrush = findViewById(R.id.btn_show_brush);
        AppCompatButton btnShowGraphics = findViewById(R.id.btn_show_graphics);
        AppCompatButton btnSave = findViewById(R.id.btn_save);
        AppCompatButton btnChangeColor = findViewById(R.id.btn_change_paint_color);
        AppCompatButton btnRevoke = findViewById(R.id.btn_revoke);
        AppCompatButton btnRestore = findViewById(R.id.btn_restore);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        //???????????????
        btnShowBackImage.setOnClickListener(this);
        //?????????
        btnEraser.setOnClickListener(this);
        //??????
        btnClearScreen.setOnClickListener(this);
        //??????
        btnSave.setOnClickListener(this);
        //??????????????????
        btnChangeColor.setOnClickListener(this);
        //??????????????????
        btnRevoke.setOnClickListener(this);
        //??????
        btnRestore.setOnClickListener(this);
        //brush button
        btnShowBrush.setOnClickListener(this);
        btnShowGraphics.setOnClickListener(this);

        //????????????
        btnShowBackImage.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnEraser.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnClearScreen.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnSave.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnChangeColor.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnRevoke.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnRestore.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnShowBrush.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));
        btnShowGraphics.setTypeface(AssetsUtil.getAssetsFont(DrawingBoardActivity.this));

        canvasTypeList.addAll(initData());
    }

    private List<CanvasType> initData() {
        List<CanvasType> list = new ArrayList<>();

        list.add(buildCanvasType(1, R.mipmap.ic_shape_path, "??????", true));
        list.add(buildCanvasType(2, R.mipmap.ic_shape_circular, "??????", false));
        list.add(buildCanvasType(3, R.mipmap.ic_shape_straight, "??????", false));
        list.add(buildCanvasType(4, R.mipmap.ic_shape_triangle, "?????????", false));
//        list.add(buildCanvasType(5, R.mipmap.ic_shape_hexagon, "?????????", false));
        list.add(buildCanvasType(6, R.mipmap.ic_shape_rectangle, "??????", false));
        list.add(buildCanvasType(7, R.mipmap.ic_shape_oval, "??????", false));
//        list.add(buildCanvasType(8, R.mipmap.ic_little_yellow_chicken_1, "?????????", false));
        return list;
    }

    private CanvasType buildCanvasType(int type, int resId, String name, boolean isSelected) {
        CanvasType canvasType = new CanvasType();
        canvasType.setType(type);
        canvasType.setGraphicsId(resId);
        canvasType.setName(name);
        canvasType.setSelected(isSelected);
        return canvasType;
    }

    /**
     * ??????????????????
     */
    private void showSelectPaintColorDialog() {
        new ColorChooserDialog.Builder(this, R.string.choose_brush_color)
                .backButton(R.string.back)
                .cancelButton(R.string.cancel)
                .customButton(R.string.custom)
                .doneButton(R.string.confirm)
                .presetsButton(R.string.back)
                .typeface("huakangdollbody.ttf", "huakangdollbody.ttf")
                .allowUserColorInputAlpha(false)
                .show(this);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (!dialog.isAccentMode()) {
            if (selectedColor != 0 && Color.alpha(selectedColor) != 255) {
                selectedColor = R.color.red;
            }
            cornerPathEffectView.selectPaintColor(selectedColor);
        }
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

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
                //??????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_PATH);
                break;
            case 2:
                //??????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_CIRCLE);
                break;
            case 3:
                //??????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_STRAIGHT_LINE);
                break;
            case 4:
                //?????????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_TRIANGLE);
                break;
            case 5:
                //?????????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_POLYGON);
                break;
            case 6:
                //??????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_RECTANGLE);
                break;
            case 7:
                //??????
                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_OVAL);
                break;
//            case 8:
//                //????????????
//                cornerPathEffectView.setCurDrawMode(DrawMode.DRAW_BITMAP);
//                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectBrushEvent(BrushType brushType) {
        int type = brushType.getType();
        if (type == 200) {
            //??????
            cornerPathEffectView.setCurPaintMode(PaintMode.PENCIL);
        } else if (type == 201) {
            //?????????
            cornerPathEffectView.setCurPaintMode(PaintMode.HIGHLIGHTER);
        } else if (type == 202) {
            //?????????1
            cornerPathEffectView.setCurPaintMode(PaintMode.EQUAL_DASHED_LINE);
        } else if (type == 203) {
            //?????????2
            cornerPathEffectView.setCurPaintMode(PaintMode.UNEQUAL_DASHED_LINE);
        } else if (type == 204) {
            //?????????3
            cornerPathEffectView.setCurPaintMode(PaintMode.CIRCLE_LINE_DASHED_LINE);
        } else if (type == 205) {
            //?????????4
            cornerPathEffectView.setCurPaintMode(PaintMode.ALL_CIRCLE_DASHED_LINE);
        } else if (type == 206) {
            //?????????
            cornerPathEffectView.setCurPaintMode(PaintMode.PATTERN_PEN);
        }
//        else if (type == 207) {
//            //?????????
//            cornerPathEffectView.setCurPaintMode(PaintMode.FLOWER_PEN);
//        }
        else if (type == 208) {
            //??????5
            cornerPathEffectView.setCurPaintMode(PaintMode.FIVE_DASHED_LINE);
        } else if (type == 209) {
            //??????
            cornerPathEffectView.setCurPaintMode(PaintMode.POLY_LINE);
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
            if (cornerPathEffectView.getCurPaintMode() == PaintMode.ERASER) {
                cornerPathEffectView.setCurPaintMode(PaintMode.PENCIL);
            } else {
                cornerPathEffectView.setCurPaintMode(PaintMode.ERASER);
            }
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
     * ????????????
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

    //??????????????????
    @Override
    public void openLocalAlbum() {
        launcher.launch("open");
    }

    @Override
    public void selectLocalResult(Uri pathUri) {
        //???????????????uri
        if (pathUri == null) {
            Toast.makeText(DrawingBoardActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pathUri);
            if (null != backgroundView) {
                backgroundView.getLocalBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????
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
        cornerPathEffectView.setBrushSize(progress / 4f);
    }

    @Override
    public void onStopTrackingTouch(View view, float progress) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        //????????????????????????????????????
        SPUtil.putInt("selectedBrushType", -1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //????????????????????????????????????
        SPUtil.putInt("selectedBrushType", -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //????????????????????????????????????
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
