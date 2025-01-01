package com.usetsai.remotesurfaceservice;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleWebView extends LinearLayout {

    public SimpleWebView(Context context) {
        this(context, null);
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(Color.WHITE);

        TextView textView = new TextView(context);
        textView.setText("WebView");
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView);

        WebView webView = new WebView(context);
        LinearLayout.LayoutParams wvLp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        wvLp1.weight = 1;
        addView(webView, wvLp1);
        setDefaultWebSettings(webView);
        webView.loadUrl("https://www.baidu.com");
    }

    private static void setDefaultWebSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }

    public static View createView(Context context) {
        SimpleWebView layout = new SimpleWebView(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }
}