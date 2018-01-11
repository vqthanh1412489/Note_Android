package com.hcmus.yennhi0105.usedadvance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hcmus.yennhi0105.usedadvance.R;

public class NewsActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        webView = (WebView) findViewById(R.id.webViewNew);
        Intent myIntent = getIntent();
        String link = myIntent.getStringExtra("linkNews");
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
