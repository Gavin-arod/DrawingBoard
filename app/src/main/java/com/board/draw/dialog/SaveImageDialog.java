package com.board.draw.dialog;

import android.content.Context;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.board.draw.R;
import com.board.draw.dialog.base.BaseDialogView;
import com.board.draw.impl.SaveImageLocalListener;
import com.board.draw.util.ScreenUtil;

import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * 保存图片到本地
 */
public class SaveImageDialog extends BaseDialogView implements View.OnClickListener {
    private AppCompatEditText etInputFileName;
    private AppCompatButton btnConfirmSave;

    private SaveImageLocalListener saveImageLocalListener;

    public void setSaveImageLocalListener(SaveImageLocalListener listener) {
        this.saveImageLocalListener = listener;
    }

    public AppCompatEditText getInputFileNameView() {
        return etInputFileName;
    }

    public void setInputFileNameView(AppCompatEditText etInputFileName) {
        this.etInputFileName = etInputFileName;
    }

    public AppCompatButton getConfirmSaveView() {
        return btnConfirmSave;
    }

    public void setConfirmSaveView(AppCompatButton btnConfirmSave) {
        this.btnConfirmSave = btnConfirmSave;
    }

    public SaveImageDialog(Context context) {
        super(context, (int) (ScreenUtil.getHeight(context) * 3 / 4f), (int) (ScreenUtil.getHeight(context) / 2f));
        setPopupGravity(Gravity.CENTER);
        setBackgroundColor(R.color.transparent);
        setOutSideDismiss(true);
        setOutSideTouchable(false);
        setContentView(R.layout.dialog_save_image);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);

        setInputFileNameView(contentView.findViewById(R.id.et_input_file_name));
        setConfirmSaveView(contentView.findViewById(R.id.btn_confirm_save));

        getConfirmSaveView().setOnClickListener(this);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        getPopupWindow().setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm_save) {
            Editable editable = etInputFileName.getText();
            if (editable != null && !"".equals(editable.toString())) {
                saveImageLocalListener.saveLocal(editable.toString());
                dismiss();
            } else {
                Toast.makeText(getContext(), "输入内容不能为空", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
