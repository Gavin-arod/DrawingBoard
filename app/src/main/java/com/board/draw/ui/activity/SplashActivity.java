package com.board.draw.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.dialog.PrivacyPolicyWindow;
import com.board.draw.dialog.QuitAppWindow;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.Constant;
import com.board.draw.util.SpannableStringParser;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {
    private final Handler mHandler = new Handler();

    private PrivacyPolicyWindow privacyPolicyWindow;
    private QuitAppWindow quitAppWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_splash);

        //设置字体
        AppCompatTextView appName = findViewById(R.id.app_name);
        appName.setTypeface(AssetsUtil.getAssetsFont(SplashActivity.this));

        if (checkAgreement()) {
            autoToHomePage();
        } else {
            showPrivacyPopWindow();
        }
    }

    //显示隐私政策确认框
    private void showPrivacyPopWindow() {
        if (privacyPolicyWindow == null) {
            privacyPolicyWindow = new PrivacyPolicyWindow(SplashActivity.this);
        }
        privacyPolicyWindow.setPopupGravity(Gravity.CENTER);
        privacyPolicyWindow.setOutSideDismiss(false);

        AppCompatButton btAgree = privacyPolicyWindow.getContentView().findViewById(R.id.btn_agree_privacy_policy);
        AppCompatTextView btDisagree = privacyPolicyWindow.getContentView().findViewById(R.id.btn_disagree);
        btAgree.setOnClickListener(v -> agreePrivacy());
        btDisagree.setOnClickListener(v -> disagreePrivacy());

        AppCompatTextView textView = privacyPolicyWindow.getContentView().findViewById(R.id.privacy_service_text);
        textView.setText(SpannableStringParser.parseAgreement(getApplicationContext(), "在使用我们的产品和服务前，请您先阅读并了解", "《隐私政策》", "和", "《用户协议》").append("。"));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        privacyPolicyWindow.showPopupWindow();
    }

    //检查是否同意隐私政策
    private boolean checkAgreement() {
        return Constant.SP.getBoolean(Constant.SP_DRAW_PRIVACY_AGREEMENT, false);
    }

    //同意隐私政策
    private void agreePrivacy() {
        try {
            SharedPreferences.Editor editor = Constant.SP.edit();
            editor.putBoolean(Constant.SP_DRAW_PRIVACY_AGREEMENT, true);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (privacyPolicyWindow != null) {
            privacyPolicyWindow.dismiss();
            //继续后续操作
            autoToHomePage();
        }
    }

    //不同意隐私政策
    private void disagreePrivacy() {
        if (quitAppWindow == null) {
            quitAppWindow = new QuitAppWindow(SplashActivity.this);
        }
        quitAppWindow.setPopupGravity(Gravity.CENTER);
        quitAppWindow.setOutSideDismiss(false);

        AppCompatButton btConfirm = quitAppWindow.getContentView().findViewById(R.id.btn_confirm_quit_app);
        AppCompatButton btSeeAgain = quitAppWindow.getContentView().findViewById(R.id.btn_see_again);
        btConfirm.setOnClickListener(v -> {
            if (privacyPolicyWindow != null) {
                privacyPolicyWindow.dismiss();
            }
            finish();
        });
        btSeeAgain.setOnClickListener(v -> quitAppWindow.dismiss());

        quitAppWindow.showPopupWindow();
    }

    /**
     * 跳转到首页
     */
    private void autoToHomePage() {
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
        }, 1500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ESCAPE ||
                keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
