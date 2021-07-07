package com.board.draw.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.board.draw.adapter.BackImageAdapter;
import com.board.draw.dialog.base.BaseDialogView;
import com.board.draw.impl.ItemClickListener;
import com.board.draw.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * 背景图选择dialog
 */
public class SelectCanvasBackgroundDialog extends BaseDialogView implements ItemClickListener {
    private final ItemClickListener itemClickListener;
    private final List<Bitmap> imagesList = new ArrayList<>();

    public SelectCanvasBackgroundDialog(Context context, List<Bitmap> list, ItemClickListener listener) {
        super(context, ScreenUtil.getWidth(context), ScreenUtil.getHeight(context) / 5);
        setPopupGravity(Gravity.BOTTOM);
        setBackgroundColor(R.color.transparent);
        setOutSideDismiss(true);
        setOutSideTouchable(false);
        setContentView(R.layout.dialog_select_background);
        imagesList.addAll(list);
        this.itemClickListener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        RecyclerView rvList = contentView.findViewById(R.id.rv_background_pics);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        BackImageAdapter adapter = new BackImageAdapter(getContext(), this);
        rvList.setLayoutManager(manager);
        rvList.setAdapter(adapter);

        adapter.setData(imagesList);
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
    public void itemClick(Bitmap bitmap) {
        itemClickListener.itemClick(bitmap);
        dismiss();
    }
}
