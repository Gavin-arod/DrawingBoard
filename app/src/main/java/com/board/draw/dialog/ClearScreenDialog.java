package com.board.draw.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.dialog.base.BaseDialogView;
import com.board.draw.impl.OnClearScreenListener;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.ScreenUtil;

/**
 * 清屏dialog
 */
public class ClearScreenDialog extends BaseDialogView implements View.OnClickListener {
    private OnClearScreenListener clearScreenListener;

    public void setClearScreenListener(OnClearScreenListener clearScreenListener) {
        this.clearScreenListener = clearScreenListener;
    }

    public ClearScreenDialog(Context context) {
        super(context, (int) (ScreenUtil.getWidth(context) / 3f), (int) (ScreenUtil.getWidth(context) * 2f / 3f / 3f));
        setPopupGravity(Gravity.CENTER);
        setBackgroundColor(R.color.transparent);
        setOutSideDismiss(true);
        setOutSideTouchable(false);
        setContentView(R.layout.dialog_clear_screen);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        AppCompatTextView tvClearTip = contentView.findViewById(R.id.tv_dialog_title);
        AppCompatButton btnCancel = contentView.findViewById(R.id.btn_cancel);
        AppCompatButton btnConfirm = contentView.findViewById(R.id.btn_confirm);

        tvClearTip.setTypeface(AssetsUtil.getAssetsFont(getContext()));
        btnCancel.setTypeface(AssetsUtil.getAssetsFont(getContext()));
        btnConfirm.setTypeface(AssetsUtil.getAssetsFont(getContext()));

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_cancel) {
            dismiss();
        } else if (viewId == R.id.btn_confirm) {
            clearScreenListener.clearScreen();
            dismiss();
        }
    }
}
