package com.example.tsnews;

import static org.jsoup.Jsoup.connect;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import android.widget.TextView;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class news_desc extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_desc);
        WebView webView;


        webView = findViewById(R.id.web_view);
        //custom notification shade color  start

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.black));

        //end

        webView.getSettings().setJavaScriptEnabled(true);
        pro_gress pro = new pro_gress(this);
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
        String link = getIntent().getStringExtra("linkvalue");
        webView.loadUrl(link);

    }
}

//Jsoup parse text from link


