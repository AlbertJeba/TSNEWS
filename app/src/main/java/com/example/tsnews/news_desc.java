package com.example.tsnews;

import static org.jsoup.Jsoup.connect;
import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.protobuf.StringValue;
import com.stfalcon.multiimageview.MultiImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class news_desc extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_desc);
        WebView webView;
        ImageView imageView;

        String u_r_l, source;

        webView = findViewById(R.id.web_view);
        //custom notification shade color  start
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        //end



        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Tech Snicks");
        pd.setMessage("Loading...!");
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


       /* webView.setWebViewClient(new WebViewClient() {
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
        );*/

        String linkdata = getIntent().getStringExtra("linkvalue");
        //webView.setBackgroundColor(R.color.fui_transparent);
        // webView.loadUrl(linkdata);


        // TextView tx = findViewById(R.id.string_text);
        //header image
        /* String img = getIntent().getStringExtra("thumbnail");
        imageView = findViewById(R.id.news_image);
        Glide.with(getApplicationContext()).load(img).into(imageView); */


        //custom font
        // TextView para_ = findViewById(R.id.source);
        // Typeface typeface = ResourcesCompat.getFont(this, R.font.proxima_nova_light);
        //para_.setTypeface(typeface);
        description_web_scrape dw = new description_web_scrape();
        dw.execute();

    }

    //Jsoup parse text from link
    public class description_web_scrape extends AsyncTask<Void, Void, Void> {
        Elements elements, el;
        Element e;
        Document document;
        URI myURI = null;
        String source;
        String link = getIntent().getStringExtra("linkvalue");
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        pro_gress pro = new pro_gress(getApplicationContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String linkdata = getIntent().getStringExtra("linkvalue");

            if (linkdata != null) {
                try {

                    document = connect(linkdata).get();
                    source = linkdata;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView title_ = findViewById(R.id.news_title);


            System.out.println(source);
            //adonhead aligncenter
            //storyContent

            // elements = document.getElementsByClass("td-post-content","td-pb-span8 td-main-content");

            elements = document.getElementsByClass("td-post-content");
            elements.select("div.td-post-content");
            elements.select("div.td-pb-span8 td-main-content");
            // elements = document.getElementsByClass("storyContent");
            // el = document.getElementsByClass("td-post-content");
            elements.select("ins.adsbygoogle").remove();


            String Title = document.title();

            try {
                myURI = new URI(link);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            //end of displaying images
            title_.setText(Title);
            //para.setText(f);
            String html = elements.toString() + "Source : " + myURI.getHost();
            String mime = "text/html";
            String encoding = "utf-8";
            // webView.loadData(html, mime, encoding);
            // content is the content of the HTML or XML.
            String stringToAdd = "width=\"100%\" ";

            // Create a StringBuilder to insert string in the middle of content.
            StringBuilder sb = new StringBuilder(html);

            int i = 0;
            int cont = 0;

            // Check for the "src" substring, if it exists, take the index where
            // it appears and insert the stringToAdd there, then increment a counter
            // because the string gets altered and you should sum the length of the inserted substring
            while (i != -1) {
                i = html.indexOf("src", i + 1);
                if (i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd);
                ++cont;
            }

            // Set the webView with the StringBuilder: sb.toString()
            WebView detailWebView = (WebView) findViewById(R.id.web_view);
            WebSettings webSettings = detailWebView.getSettings();
            webSettings.setDefaultFontSize(20);
            //detailWebView.getSettings().setLoadsImagesAutomatically(true);
            detailWebView.getSettings().setDomStorageEnabled(true);
            detailWebView.getSettings().setJavaScriptEnabled(true);

            //System.out.println(el);
            if (appLinkData != null) {
                detailWebView.loadUrl(String.valueOf(appLinkData));
            }

            detailWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);


        }


    }


}
