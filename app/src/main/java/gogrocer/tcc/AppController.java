package gogrocer.tcc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.ConnectivityReceiver;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        List<Locale> locales = new ArrayList<>();
        locales.add(Locale.ENGLISH);
        locales.add(new Locale("ar","ARABIC"));
        LocaleHelper.setLocale(getApplicationContext(),"en");

        SharedPreferences sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);

        final String current_lan = sharedPreferences.getString("language","english");

        if(current_lan.equals("spanish")){
        setTheme(R.style.AppTheme1);
            Toast.makeText(mInstance, "abc", Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(mInstance, "jkadgsd", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme);
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
