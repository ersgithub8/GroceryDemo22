package Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Model.Support_info_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;

public class Terms_and_Condition_fragment extends Fragment {
    android.webkit.WebView webView;

    private static String TAG = Terms_and_Condition_fragment.class.getSimpleName();
    private TextView tv_info;
    String title;

    ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_condition, container, false);

        tv_info = (TextView) view.findViewById(R.id.tv_info);
        webView = view.findViewById(R.id.webView_id);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        String geturl = getArguments().getString("url");
        title = getArguments().getString("title");
        ((MainActivity) getActivity()).setTitle(title);

        webView.setWebChromeClient( new MyWebChromeClient());
        webView.setWebViewClient( new webClient());
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

        return view;
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean  shouldOverrideUrlLoading(WebView view, String url) {
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
