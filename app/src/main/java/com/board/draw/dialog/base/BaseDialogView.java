package com.board.draw.dialog.base;

import android.content.Context;
import android.view.View;

import com.board.draw.util.ImmersiveBarUtil;

import razerdp.basepopup.BasePopupWindow;

/**
 * base dialog
 */
public class BaseDialogView extends BasePopupWindow {
    public BaseDialogView(Context context, int width, int height) {
        super(context, width, height);
    }

    @Override
    public void onWindowFocusChanged(View popupDecorViewProxy, boolean hasWindowFocus) {
        super.onWindowFocusChanged(popupDecorViewProxy, hasWindowFocus);
        ImmersiveBarUtil.hideBar(popupDecorViewProxy);
    }
}
