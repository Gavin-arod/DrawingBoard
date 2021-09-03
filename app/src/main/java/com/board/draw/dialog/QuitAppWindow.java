package com.board.draw.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.util.AssetsUtil;

import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * @describe 退出APP弹窗
 */
public class QuitAppWindow extends BasePopupWindow {

    public QuitAppWindow(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        setBackgroundColor(R.color.transparent);
        setOutSideDismiss(true);
        setOutSideTouchable(false);
        setContentView(R.layout.dialog_quit_app);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);

        AppCompatTextView tvTitle = contentView.findViewById(R.id.tv_quit_app_title);
        AppCompatTextView tvEndContent = contentView.findViewById(R.id.tv_quit_app_content);
        AppCompatTextView tvDisagree = contentView.findViewById(R.id.btn_disagree);

        AppCompatButton btnConfirm = contentView.findViewById(R.id.btn_confirm_quit_app);
        AppCompatButton btnSeeAgain = contentView.findViewById(R.id.btn_see_again);

        tvTitle.setTypeface(AssetsUtil.getAssetsFont(getContext()));
        tvEndContent.setTypeface(AssetsUtil.getAssetsFont(getContext()));
        tvDisagree.setTypeface(AssetsUtil.getAssetsFont(getContext()));
        btnConfirm.setTypeface(AssetsUtil.getAssetsFont(getContext()));
        btnSeeAgain.setTypeface(AssetsUtil.getAssetsFont(getContext()));
    }

    //显示动画
    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
    }

    //退出动画
    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
    }
}
