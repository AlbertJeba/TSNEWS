package com.example.tsnews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class news_desc extends AppCompatActivity
{
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_desc);

        webView=(WebView)findViewById(R.id.webview);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        Uri data = getIntent().getData();

        if(data!=null){
            String path=data.toString();
            Toast.makeText(this, "Path ="+path, Toast.LENGTH_SHORT).show();

            WebView webView=(WebView)findViewById(R.id.webview);
            webView.loadUrl(path);
        }




        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("Tech Snicks");
        pd.setMessage("Loading...!");

        webView.setWebViewClient(new WebViewClient()
                                 {
                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         pd.show();
                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         pd.dismiss();
                                     }
                                 }
        );

        String linkdata=getIntent().getStringExtra("linkvalue");
        webView.loadUrl(linkdata);

    }
}