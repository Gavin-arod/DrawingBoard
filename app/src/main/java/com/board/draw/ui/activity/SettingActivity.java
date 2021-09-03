package com.board.draw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.ToastUtil;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlLogin;
    private RelativeLayout rlRate;
    private RelativeLayout rlAuthority;
    private RelativeLayout rlUserAgreement;
    private RelativeLayout rlPrivacy;
    private RelativeLayout rlCooperation;
    private RelativeLayout rlFeedback;
    private RelativeLayout rlAboutUs;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvLogin;
    private AppCompatTextView tvRate;
    private AppCompatTextView tvAuthority;
    private AppCompatTextView tvUserAgreement;
    private AppCompatTextView tvPrivacy;
    private AppCompatTextView tvCooperation;
    private AppCompatTextView tvFeedback;
    private AppCompatTextView tvAboutUs;
    private AppCompatTextView btnExitLogin;

    private LinearLayout llExitApp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.btn_setting_back).setOnClickListener(v -> finish());
        initView();

        rlLogin.setOnClickListener(this);
        rlRate.setOnClickListener(this);
        rlAuthority.setOnClickListener(this);
        rlUserAgreement.setOnClickListener(this);
        rlPrivacy.setOnClickListener(this);
        rlCooperation.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
        llExitApp.setOnClickListener(this);
    }


    private void initView() {
        rlLogin = findViewById(R.id.rl_login_register);
        rlRate = findViewById(R.id.rl_iv_go_to_rate);
        rlAuthority = findViewById(R.id.rl_privacy_authority);
        rlUserAgreement = findViewById(R.id.rl_user_agreement);
        rlPrivacy = findViewById(R.id.rl_privacy_policy);
        rlCooperation = findViewById(R.id.rl_business_cooperation);
        rlFeedback = findViewById(R.id.rl_feedback);
        rlAboutUs = findViewById(R.id.rl_about_us);

        tvTitle = findViewById(R.id.tv_setting_title);
        tvLogin = findViewById(R.id.tv_login_register);
        tvRate = findViewById(R.id.tv_iv_go_to_rate);
        tvAuthority = findViewById(R.id.tv_privacy_authority);
        tvUserAgreement = findViewById(R.id.tv_user_agreement);
        tvPrivacy = findViewById(R.id.tv_privacy_policy);
        tvCooperation = findViewById(R.id.tv_business_cooperation);
        tvFeedback = findViewById(R.id.tv_feedback);
        tvAboutUs = findViewById(R.id.tv_about_us);
        btnExitLogin = findViewById(R.id.tv_exit_app);

        llExitApp = findViewById(R.id.ll_exit_login);

        setTypeface(tvTitle);
        setTypeface(tvLogin);
        setTypeface(tvRate);
        setTypeface(tvAuthority);
        setTypeface(tvUserAgreement);
        setTypeface(tvPrivacy);
        setTypeface(tvCooperation);
        setTypeface(tvFeedback);
        setTypeface(tvAboutUs);
        setTypeface(btnExitLogin);
    }

    private void setTypeface(AppCompatTextView view) {
        view.setTypeface(AssetsUtil.getAssetsFont(SettingActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_login_register:
                ToastUtil.showMessage(SettingActivity.this, "登录", 2000);
                break;
            case R.id.rl_iv_go_to_rate:
                ToastUtil.showMessage(SettingActivity.this, "去评分", 2000);
                break;
            case R.id.rl_privacy_authority:
                ToastUtil.showMessage(SettingActivity.this, "权限", 2000);
                break;
            case R.id.rl_user_agreement:
                ToastUtil.showMessage(SettingActivity.this, "用户协议", 2000);
                break;
            case R.id.rl_privacy_policy:
                ToastUtil.showMessage(SettingActivity.this, "隐私政策", 2000);
                break;
            case R.id.rl_business_cooperation:
                ToastUtil.showMessage(SettingActivity.this, "商务合作", 2000);
                break;
            case R.id.rl_feedback:
                ToastUtil.showMessage(SettingActivity.this, "意见反馈", 2000);
                break;
            case R.id.rl_about_us:
                Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_exit_login:
                ToastUtil.showMessage(SettingActivity.this, "退出登录", 2000);
                break;
        }
    }
}
