package gogrocer.tcc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Adapter.SelectCityListViewAdapter;
import Config.BaseURL;
import Config.SharedPref;
import Fragment.Terms_and_Condition_fragment;
import Model.Support_info_model;
import gogrocer.tcc.networkconnectivity.NetworkConnection;
import util.ConnectivityReceiver;

public class Agreed_Activtiy extends AppCompatActivity {
    TextView tv_info;
    String title;
    android.webkit.WebView webView;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_terms_condition);

        tv_info = (TextView) findViewById(R.id.tv_info);
        webView = findViewById(R.id.webView_id);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        String geturl = BaseURL.termsandcondition;
        title = getResources().getString(R.string.nav_terms);

        webView.setWebChromeClient( new Agreed_Activtiy.MyWebChromeClient());
        webView.setWebViewClient( new Agreed_Activtiy.webClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(geturl);

//        if (ConnectivityReceiver.isConnected()) {
//            webView.loadUrl(geturl);
//        } else {
//            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
//        }
//
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, WebResourceRequest request) {
//
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//        });
//
//        webView.setWebChromeClient(new WebChromeClient()
//        {
//            @Override
//            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//
//            }
//        });

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
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

//    private void makeGetInfoRequest(String url) {
//        String tag_json_obj = "json_info_req";
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    boolean status = response.getBoolean("responce");
//                    if (status) {
//
//                        List<Support_info_model> support_info_modelList = new ArrayList<>();
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Support_info_model>>() {
//                        }.getType();
//                        support_info_modelList = gson.fromJson(response.getString("data"), listType);
//
//                        try {
//                            String desc = support_info_modelList.get(0).getPg_descri();
//                            //String title = support_info_modelList.get(0).getPg_title();
//                            tv_info.setText(Html.fromHtml(desc));
//                        }catch (Exception e){
//                            tv_info.setText(getResources().getString(R.string.no_record));
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Activity activity=getActivity();
//                    if(activity !=null)
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }

}
