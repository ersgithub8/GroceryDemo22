package Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Config.BaseURL;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import gogrocer.tcc.SplashActivity;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.Session_management;

/**
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();
    FusedLocationProviderClient fusedLocationClient;

    private EditText et_phone, et_name, et_pin, et_house, et_address;
    private RelativeLayout btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity, btn_socity, gps, tv_address;
    private String getsocity = "";
    String lat, longi;
    Double longitude,latitude;
    private Session_management sessionManagement;

    private boolean isEdit = false;

    private String getlocation_id;

    public Add_delivery_address_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add));

        sessionManagement = new Session_management(getActivity());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            location.reset();

                        }

                    }

                });


        et_address = (EditText) view.findViewById(R.id.et_add_address);
        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        et_pin = (EditText) view.findViewById(R.id.et_add_adres_pin);
        et_house = (EditText) view.findViewById(R.id.et_add_adres_home);
        tv_house = (TextView) view.findViewById(R.id.tv_add_adres_home);
        tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (RelativeLayout) view.findViewById(R.id.btn_add_adres_edit);
        btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);
        gps = (TextView) view.findViewById(R.id.get_gps);
        tv_address = (TextView) view.findViewById(R.id.tv_address);

        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        Bundle args = getArguments();

        if (args != null) {
            getlocation_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pine = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("socity_name");
            String get_house = getArguments().getString("house");

            if (TextUtils.isEmpty(get_name) && get_name == null) {
                isEdit = false;
            } else {
                isEdit = true;

                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pine);
                et_house.setText(get_house);
                btn_socity.setText(get_socity_name);

                sessionManagement.updateSocity(get_socity_name, get_socity_id);
            }
        }

        if (!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }

        btn_update.setOnClickListener(this);
        btn_socity.setOnClickListener(this);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        lat=sharedPreferences.getString("lat",null);
        longi=sharedPreferences.getString("long",null);

        //Toast.makeText(getActivity(), lat+longi, Toast.LENGTH_SHORT).show();

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                double laty = Double.parseDouble(lat);
//                double longy = Double.parseDouble(longi);
                //Toast.makeText(getActivity(), city, Toast.LENGTH_SHORT).show();

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//         addresses = null;
                try {

                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String final_address=null;
                    //Toast.makeText(getActivity(), String.valueOf(addresses), Toast.LENGTH_SHORT).show();

                    if(addresses.size()>0) {
                        //                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
                        final_address = addresses.get(0).getAddressLine(0);
                    }

                    et_address.setText(final_address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        } else if (id == R.id.btn_add_adres_socity) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

                Bundle args = new Bundle();
                Fragment fm = new Socity_fragment();
                //args.putString("pincode", getpincode);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpin = et_pin.getText().toString();
        String gethouse = et_house.getText().toString();
        String getAdress = et_address.getText().toString();

        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }

        if (TextUtils.isEmpty(gethouse)) {
            tv_house.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_house;
            cancel = true;
        }
        if (TextUtils.isEmpty(getAdress)) {
            tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_address;
            cancel = true;
        }

        if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    if (isEdit) {
                        makeEditAddressRequest(getlocation_id, getpin, getsocity, getAdress, getname, getphone);
                    } else {
                        makeAddAddressRequest(user_id, getpin, getsocity, getAdress, getname, getphone);
                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode, String socity_id,
                                       String house_no, String receiver_name, String receiver_mobile) {

        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Activity activity=getActivity();
                    if(activity !=null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode, String socity_id,
                                        String house_no, String receiver_name, String receiver_mobile) {

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", location_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDIT_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Activity activity=getActivity();
                    if(activity !=null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
