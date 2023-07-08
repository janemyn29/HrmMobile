package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class ViewPdfActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        Intent intent = getIntent();
        String fileUrl= intent.getStringExtra("fileUrl");
        webView = findViewById(R.id.view_pdf_wv);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File hợp đồng");
        pd.setMessage("Đang mở...!!!");

        webView.setWebViewClient(new WebViewClient(){
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
        });

        String url="";
        try {
            url = URLEncoder.encode(fileUrl,"UTF-8");
        }catch (Exception ex){

        }
        webView.getSettings().setJavaScriptEnabled(true); // Bật JavaScript (cần thiết cho PDF.js)
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);

    }
}