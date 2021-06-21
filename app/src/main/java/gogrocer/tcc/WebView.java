package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.nio.file.WatchEvent;

import Fragment.Terms_and_Condition_fragment;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class WebView extends AppCompatActivity {
android.webkit.WebView webView;
SharedPreferences sharedPreferences;
String language="";
ProgressBar progressBar;
String url = "https://tawk.to/chat/605c2160f7ce18270933af93/1f1ju4pbd";
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
        progressBar = (ProgressBar) findViewById(R.id.progress);

        webView.setWebChromeClient( new WebView.MyWebChromeClient());
        webView.setWebViewClient( new WebView.webClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean  shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

}