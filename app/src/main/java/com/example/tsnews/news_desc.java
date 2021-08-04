package com.example.tsnews;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class news_desc extends AppCompatActivity {
    WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_desc);

        webView = findViewById(R.id.webview);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        Uri data = getIntent().getData();

        if (data != null) {
            String path = data.toString();
            //Toast.makeText(this, "Path =" + path, Toast.LENGTH_SHORT).show();

            WebView webView = findViewById(R.id.webview);
            webView.loadUrl(path);
        }

        pro_gress pro = new pro_gress(this);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Tech Snicks");
        pd.setMessage("Loading...!");

        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         //pd.show();
                                         pro.show();

                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         // pd.dismiss();
                                         pro.dismiss();
                                     }
                                 }
        );

        String linkdata = getIntent().getStringExtra("linkvalue");
        webView.loadUrl(linkdata);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
}