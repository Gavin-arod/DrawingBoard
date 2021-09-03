package com.board.draw.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.board.draw.R;
import com.board.draw.ui.activity.PrivacyPolicyActivity;

/**
 * @describe 给文本中的一段文字设置单独的字体颜色等属性
 */
public class SpannableStringParser {

    //隐私政策/服务条款
    public static SpannableStringBuilder parseAgreement(Context context, String text1, String text2, String text3, String text4) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text1).append(text2).append(text3).append(text4);

        //隐私政策
        int endLength = text1.length() + text2.length();
        setAgreementStyle(builder, text1.length(), endLength, "1", context);

        //服务协议
        int length = text1.length() + text2.length() + text3.length();
        int endLength1 = length + text4.length();
        setAgreementStyle(builder, length, endLength1, "2", context);

        return builder;
    }

    //设置文本
    private static void setAgreementStyle(SpannableStringBuilder builder, int startLength, int endLength, String linkType, Context context) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_main));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        builder.setSpan(colorSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(styleSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new UnderlineSpan(), startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {//服务条款
                Intent intent = new Intent(context, PrivacyPolicyActivity.class);
                intent.putExtra("linkType", linkType);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
        builder.setSpan(clickableSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
