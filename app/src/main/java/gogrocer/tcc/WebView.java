package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.nio.file.WatchEvent;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WebView extends AppCompatActivity {
android.webkit.WebView webView;
ProgressBar mProgress;
SharedPreferences sharedPreferences;
String language="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);
        language=sharedPreferences.getString("language","");
        if (language.equals("spanish")){
            setTheme(R.style.AppTheme1);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView_id);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://tawk.to/chat/5ea1a9d669e9320caac6a09a/default");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }
        });

    }

}