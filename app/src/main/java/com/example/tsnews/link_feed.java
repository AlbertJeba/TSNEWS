package com.example.tsnews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class link_feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_feed);
        WebView webView=findViewById(R.id.web_view_link);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);

      //  String link_feed= getIntent().getStringExtra("linkvalues");
        String link_feed="https://beebom.com/twitter-rolls-out-chirp-font-high-contrast-colors/";
        webView.loadUrl(link_feed);
        System.out.println(link_feed);
    }
}