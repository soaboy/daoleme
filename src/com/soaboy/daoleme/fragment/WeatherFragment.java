package com.soaboy.daoleme.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.soaboy.daoleme.R;

/**
 * Created by liuyun on 2015/4/25.
 */
public class WeatherFragment extends Fragment {
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View weatherLayout = inflater.inflate(R.layout.fragment_weather, container, false);
        webView=(WebView)weatherLayout.findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);	//设置JavaScript可用
        webView.setWebChromeClient(new WebChromeClient());	//处理JavaScript对话框
        webView.setWebViewClient(new WebViewClient());	//处理各种通知和请求事件，如果不使用该句代码，将使用内置浏览器访问网页
        webView.loadUrl("http://m.weather.com.cn/m/pn12/weather.htm?id=101180101T");	//设置默认显示的天气预报信息
        webView.setInitialScale(57*4);	//放网页内容放大4倍
        return weatherLayout;
    }
}