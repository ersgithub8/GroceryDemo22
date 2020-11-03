package Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Delivery_get_address_adapter;
import Adapter.View_time_adapter;
import Config.BaseURL;
import Config.SharedPref;
import Model.Delivery_address_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 27/6/2017.
 */

public class Delivery_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Delivery_fragment.class.getSimpleName();

    private TextView tv_afternoon, tv_morning, tv_total, tv_item, tv_socity;
    private TextView tv_date, tv_time,delivery_string;
    private EditText et_address;
    private RelativeLayout btn_checkout, tv_add_adress;
    private RecyclerView rv_address;

    private Delivery_get_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();

    private DatabaseHandler db_cart;
    SharedPreferences preferences;
    private Session_management sessionManagement;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String gettime = "";
    private String getdate = "";

    private String deli_charges,ischarge;
    String store_id,B_time;
String language;
    public Delivery_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_delivery_time, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery));

        store_id = SharedPref.getString(getActivity(), BaseURL.STORE_ID);
        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

        ischarge=getArguments().getString("ischarge");

        delivery_string = view.findViewById(R.id.show_delivery_charges_string);
        tv_date = (TextView) view.findViewById(R.id.tv_deli_date);
        tv_time = (TextView) view.findViewById(R.id.tv_deli_fromtime);
        tv_add_adress = (RelativeLayout) view.findViewById(R.id.tv_deli_add_address);
        tv_total = (TextView) view.findViewById(R.id.tv_deli_total);
        tv_item = (TextView) view.findViewById(R.id.tv_deli_item);
        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_deli_checkout);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        //tv_socity = (TextView) view.findViewById(R.id.tv_deli_socity);
        //et_address = (EditText) view.findViewById(R.id.et_deli_address);

        db_cart = new DatabaseHandler(getActivity());
        //Toast.makeText(getActivity(), db_cart.getTotalAmount(), Toast.LENGTH_SHORT).show();
        tv_total.setText(db_cart.getTotalAmount());
        tv_item.setText("" + db_cart.getCartCount());

        // get session user data
        sessionManagement = new Session_management(getActivity());
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        String getaddress = sessionManagement.getUserDetails().get(BaseURL.KEY_HOUSE);

        //First Time Date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date dateobj = new Date();
        String stro = df.format(dateobj);
        makeGetTimeRequest(stro);


        //tv_socity.setText("Socity Name: " + getsocity);
        //et_address.setText(getaddress);

        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_add_adress.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        String date = sessionManagement.getdatetime().get(BaseURL.KEY_DATE);
        String time = sessionManagement.getdatetime().get(BaseURL.KEY_TIME);



        if (date != null && time != null) {

            getdate = date;
            gettime = time;

            try {
                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd-MM-yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = inputFormat.parse(getdate);
                String str = outputFormat.format(date1);

                tv_date.setText(getResources().getString(R.string.delivery_date) + str);

            } catch (ParseException e) {
                e.printStackTrace();

                tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
            }
            language=preferences.getString("language","");
            if (language.contains("spanish")) {
                String timeset=time;
                 timeset=timeset.replace("PM","م");
                 timeset=timeset.replace("AM","ص");
                tv_time.setText(timeset);

            }
            else {

                tv_time.setText(time);
            }
        }


        if (ConnectivityReceiver.isConnected()) {
            String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            makeGetAddressRequest(user_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_deli_checkout) {
            attemptOrder();
        } else if (id == R.id.tv_deli_date) {
            getdate();
        } else if (id == R.id.tv_deli_fromtime) {

            if (TextUtils.isEmpty(getdate)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
            } else {
                Bundle args = new Bundle();
                Fragment fm = new View_time_fragment();
                args.putString("date", getdate);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null)
                        .commit();
            }
        } else if (id == R.id.tv_deli_add_address) {

            sessionManagement.updateSocity("", "");

            Fragment fm = new Add_delivery_address_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    //.addToBackStack(null)
            .commit();

        }

    }

    private void getdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            tv_date.setText(getResources().getString(R.string.delivery_date) + str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void attemptOrder() {

        //String getaddress = et_address.getText().toString();

        String location_id = "";
        String address = "";

        boolean cancel = false;

//        if (TextUtils.isEmpty(getdate)) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
//            cancel = true;
//        } else if (TextUtils.isEmpty(gettime)) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
//            cancel = true;
//        }

        if (!delivery_address_modelList.isEmpty()) {
            if (adapter.ischeckd()) {
                location_id = adapter.getlocation_id();
                address = adapter.getaddress();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_add_address), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getdate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

            sessionManagement.cleardatetime();

            Bundle args = new Bundle();
            Fragment fm = new Delivery_payment_detail_fragment();
            if (getdate.isEmpty()) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date dateobj = new Date();
                String newDateStr = df.format(dateobj);
                args.putString("getdate", newDateStr);
            }
            else {
                args.putString("getdate", getdate);
            }

            if (gettime.isEmpty()){
                args.putString("time", B_time);

            }
            else {
                args.putString("time", gettime);
            }

            args.putString("location_id", location_id);
            args.putString("address", address);
            if (ischarge.equals("1")){
                args.putString("deli_charges", "0");
            }else {
                args.putString("deli_charges", deli_charges);
            }

            args.putString("store_id", store_id);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();

        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetAddressRequest(String user_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        delivery_address_modelList.clear();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();

                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);

                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                        adapter = new Delivery_get_address_adapter(delivery_address_modelList);
                        ((Delivery_get_address_adapter) adapter).setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (delivery_address_modelList.isEmpty()) {
                            if (getActivity() != null) {
                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_delivery_charge"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            deli_charges =getArguments().getString("delivery_charges");

            if (type.contentEquals("update")) {
                //updateData();
                //deli_charges = intent.getStringExtra("charge");
//                Toast.makeText(getActivity(), getArguments().getString("delivery_charges"), Toast.LENGTH_SHORT).show();

                if (ischarge.equals("1")){
                    Double total = Double.parseDouble(db_cart.getTotalAmount());
                    delivery_string.setText(getActivity().getResources().getString(R.string.nocharges));

                    tv_total.setText("" + db_cart.getTotalAmount() + " + " + "0" + " = "  + total+ getActivity().getResources().getString(R.string.currency));
                }else {
                    Double total = Double.parseDouble(db_cart.getTotalAmount()) + Integer.parseInt(deli_charges);
                    delivery_string.setText(getActivity().getResources().getString(R.string.delivery_charge) + deli_charges +" "+ getActivity().getResources().getString(R.string.currency));

                    tv_total.setText("" + db_cart.getTotalAmount() + " + " + deli_charges + " = " + total + getActivity().getResources().getString(R.string.currency));
                }
            }
        }
    };


    //For First Date and Time
    private void makeGetTimeRequest(String date) {

        // Tag used to cancel the request
        String tag_json_obj = "json_time_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date",date);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TIME_SLOT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                            B_time = ""+response.getJSONArray("times").get(0);

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
