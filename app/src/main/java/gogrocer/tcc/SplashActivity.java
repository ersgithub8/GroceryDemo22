package gogrocer.tcc;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapter.Store_Adapter;
import Config.BaseURL;
import Model.Store_Model;
import util.CustomVolleyJsonRequest;
import util.Session_management;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SplashActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String language="";
    String city;
    ArrayList<String> store_ids;
    private AlertDialog dialog;

    private Session_management sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store_ids = new ArrayList<>();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);
        language=sharedPreferences.getString("language","");
        if (language.equals("spanish")){
            setTheme(R.style.AppTheme1);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_splash);
        sessionManagement = new Session_management(SplashActivity.this);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 500);

                    // After 5 seconds redirect to another intent
                    checkAppPermissions();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // start thread
        background.start();
    }

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE )&&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest
                    .permission.ACCESS_COARSE_LOCATION)

            ) {

                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            go_next();
        }
    }

    private void getLanguange() {

        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final String current_lan = sharedPreferences.getString("language",null);

        if (current_lan == null){
            LocaleHelper.setLocale(getApplicationContext(), "en");
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            editor.putString("language", "english");
            editor.apply();
        }
        else if (current_lan.equals("english")){
            LocaleHelper.setLocale(getApplicationContext(), "en");
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            editor.putString("language", "english");
            editor.apply();
        }
        else if (current_lan.equals("spanish")){
            LocaleHelper.setLocale(getApplicationContext(), "ar");
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            editor.putString("language", "spanish");
            editor.apply();
        }
        else {
            LocaleHelper.setLocale(getApplicationContext(), "en");
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            editor.putString("language", "english");
            editor.apply();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
            ) {
                go_next();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("App required some permission please enable it")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openPermissionScreen();
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                dialog = builder.show();
            }
            return;
        }
    }

    public void go_next() {
        getLanguange();
        getLocation();



        if(sessionManagement.isLoggedIn()) {
        Intent startmain = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(startmain);
            finish();
        }else{
            Intent startmain = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(startmain);
            finish();
        }

        finish();


    }

    private void getStore_ids(String citoo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", citoo);

        CustomVolleyJsonRequest jsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getStores, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response !=null && response.length()>0){

                        Boolean status=response.getBoolean("response");
                        if(status)
                        {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                store_ids.add(object.getString("user_id"));
                            }

                            String ids = String.valueOf(store_ids).substring(1, String.valueOf(store_ids).length()-1);

//                            Toast.makeText(SplashActivity.this, ids, Toast.LENGTH_SHORT).show();

                            sharedPreferences= getSharedPreferences("store_ids", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("store_ids",ids);
                            editor.apply();

                        }
                        else {

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonRequest, "json_stores_req");


    }

    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", SplashActivity.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }




    public void getLocation(){



        final SharedPreferences locationss=getSharedPreferences("location",MODE_PRIVATE);
        final SharedPreferences.Editor leditor=locationss.edit();

        FusedLocationProviderClient fusedLocationClient;
        LocationManager locationManager;


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(SplashActivity.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
        ){

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
            },2);
        }else {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener( new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {

                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                Double latitude=location.getLatitude();
                                Double longitude=location.getLongitude();


                                Geocoder geocoder = new Geocoder(SplashActivity.this, Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                                    if(addresses.size()>0) {
                                        city = addresses.get(0).getSubAdminArea();
                                        getStore_ids(city);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                leditor.putString("lat",latitude.toString());
                                leditor.putString("long",longitude.toString());
                                leditor.apply();

//                                double laty = Double.parseDouble(lati);
//                                double longy = Double.parseDouble(longi);
//                                Geocoder geocoder = new Geocoder(SplashActivity.this, Locale.getDefault());
//                                List<Address> addresses = new ArrayList<>();
//                                try {
//                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                String cityName = addresses.get(0).getLocality();
//
//                                leditor.putString("city",cityName);
//                                leditor.apply();

                                location.reset();

                            }else{
//                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(intent);
                            }

                        }

                    });

        }
    }
}
