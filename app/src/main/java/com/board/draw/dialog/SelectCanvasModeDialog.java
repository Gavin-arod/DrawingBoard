package com.board.draw.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.board.draw.adapter.CanvasTypeAdapter;
import com.board.draw.constants.CanvasType;
import com.board.draw.dialog.base.BaseDialogView;
import com.board.draw.impl.CanvasTypeClickListener;
import com.board.draw.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * 选择画布类型
 */
public class SelectCanvasModeDialog extends BaseDialogView implements CanvasTypeClickListener {
    private final CanvasTypeClickListener canvasTypeClickListener;
    private final List<CanvasType> drawModeList = new ArrayList<>();

    public SelectCanvasModeDialog(Context context, List<CanvasType> list, CanvasTypeClickListener listener) {
        super(context, ScreenUtil.getWidth(context), ScreenUtil.getHeight(context) / 5);
        setPopupGravity(Gravity.BOTTOM);
        setBackgroundColor(R.color.transparent);
        setOutSideDismiss(true);
        setOutSideTouchable(false);
        setContentView(R.layout.dialog_select_canvas_mode);
        this.canvasTypeClickListener = listener;
        drawModeList.addAll(list);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        RecyclerView rvCanvasMode = contentView.findViewById(R.id.rv_canvas_mode);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCanvasMode.setLayoutManager(manager);
        CanvasTypeAdapter adapter = new CanvasTypeAdapter(getContext(), this);
        rvCanvasMode.setAdapter(adapter);

        adapter.setData(drawModeList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow();
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
    }

    @Override
    public void clickCanvasItemType(CanvasType canvasType) {
        canvasTypeClickListener.clickCanvasItemType(canvasType);
        dismiss();
    }
}
