package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Delivery_get_address_adapter;
import Config.BaseURL;
import Config.SharedPref;
import Model.Delivery_address_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.ProductActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bilal Tahir on 25/8/2020.
 */

public class Delivery_payment_detail_fragment extends Fragment {

    private static String TAG = Delivery_payment_detail_fragment.class.getSimpleName();

    private TextView tv_timeslot, tv_address, tv_total,note,tv_dis,tv_totalamount;
    private LinearLayout btn_order;

    Double discount;
    private int checkfo= 0;
    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private String getstore_id = "";

    private int deli_charges;
    Double total;
SharedPreferences preferences;
    private DatabaseHandler db_cart;
    private Session_management sessionManagement;

    SharedPreferences sharedPreferences;
    String usrid;
    public Delivery_payment_detail_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment));

        db_cart = new DatabaseHandler(getActivity());
        sessionManagement = new Session_management(getActivity());



        tv_timeslot = (TextView) view.findViewById(R.id.textTimeSlot);
        tv_address = (TextView) view.findViewById(R.id.txtAddress);
        //tv_item = (TextView) view.findViewById(R.id.textItems);
        //tv_total = (TextView) view.findViewById(R.id.textPrice);
        tv_total = (TextView) view.findViewById(R.id.txtTotal);

        tv_totalamount = (TextView) view.findViewById(R.id.txtTotalamount);
        tv_dis = (TextView) view.findViewById(R.id.txtDiscount);
        btn_order = (LinearLayout) view.findViewById(R.id.btn_order_now);

        getdate = getArguments().getString("getdate");

        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        if (language.contains("spanish")) {
            gettime = getArguments().getString("time");

            gettime=gettime.replace("PM","م");
            gettime=gettime.replace("AM","ص");

        }else {
            gettime = getArguments().getString("time");

        }

        sharedPreferences=getActivity().getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

        usrid=sharedPreferences.getString(BaseURL.KEY_ID,"0");

        getlocation_id = getArguments().getString("location_id");
        getstore_id = getArguments().getString("store_id");
        deli_charges = Integer.parseInt(getArguments().getString("deli_charges"));
        String getaddress = getArguments().getString("address");

        tv_timeslot.setText(getdate + " " + gettime);
        tv_address.setText(getaddress);

//        total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;

        //tv_total.setText("" + db_cart.getTotalAmount());
        //tv_item.setText("" + db_cart.getWishlistCount());
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        getdiscount(user_id);

//        tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
//                getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
//                getResources().getString(R.string.delivery_charge) + deli_charges + "\n" +
//                getResources().getString(R.string.total_amount) +
//                db_cart.getTotalAmount() + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));

        tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
                getResources().getString(R.string.delivery_charge) + deli_charges);



        checkfirstorder(usrid, String.valueOf(total));

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    if (checkfo==2)
                    {Fragment fm = new Payment_fragment();
                    Bundle args = new Bundle();
                    args.putString("total", String.valueOf(total));
                    args.putString("getdate", getdate);
                    args.putString("gettime", gettime);
                    args.putString("getlocationid", getlocation_id);
                    args.putString("getstoreid", getstore_id);
                    args.putString("delivery_charges", String.valueOf(deli_charges));
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    SharedPref.putString(getActivity(),BaseURL.TOTAL_AMOUNT, String.valueOf(total));
                    }else if (checkfo==1){
                        Fragment fm = new Payment_fragment();
                        Bundle args = new Bundle();
                        args.putString("total", String.valueOf(total-discount));
                        args.putString("getdate", getdate);
                        args.putString("gettime", gettime);
                        args.putString("delivery_charges", String.valueOf(deli_charges));
                        args.putString("getlocationid", getlocation_id);
                        args.putString("getstoreid", getstore_id);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();
                        SharedPref.putString(getActivity(),BaseURL.TOTAL_AMOUNT, String.valueOf(total-discount));
                    }else{
                        SweetAlertDialog dialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE).setTitleText("Something went wrong try again later")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        getActivity().onBackPressed();
                                    }
                                });
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        return view;
    }

    private void getdiscount(String user_id) {
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_VIP_DISCOUNT, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");

                    if (status) {

                        String temp = response.getString("discount");
                        String dis = temp.replace("%","");
                        float f1 = Float.parseFloat(dis)/100;
                        float amount= Float.parseFloat(db_cart.getTotalAmount());
                        float famount=amount-(amount*f1);

                        total = Double.parseDouble(String.valueOf(famount)) + deli_charges;

                        tv_dis.setText(getResources().getString(R.string.vip_dis) + " " +temp);

                        tv_totalamount.setText(getResources().getString(R.string.total_amount) +
                                String.valueOf(famount) + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));

                    }
                    else {

                        total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;

                        tv_dis.setVisibility(View.GONE);

                        tv_totalamount.setText(getResources().getString(R.string.total_amount) +
                                db_cart.getTotalAmount() + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));

                    }
                } catch (JSONException e) {
                    total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;

                    tv_dis.setVisibility(View.GONE);

                    tv_totalamount.setText(getResources().getString(R.string.total_amount) +
                            db_cart.getTotalAmount() + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));

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

//    private void attemptOrder() {
//        // retrive data from cart database
//        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
//        if (items.size() > 0) {
//            JSONArray passArray = new JSONArray();
//            for (int i = 0; i < items.size(); i++) {
//                HashMap<String, String> map = items.get(i);
//                JSONObject jObjP = new JSONObject();
//                try {
//                    jObjP.put("product_id", map.get("product_id"));
//                    jObjP.put("qty", map.get("qty"));
//                    jObjP.put("unit_value", map.get("unit_value"));
//                    jObjP.put("unit", map.get("unit"));
//                    jObjP.put("price", map.get("price"));
//
//                    passArray.put(jObjP);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//
//            if (ConnectivityReceiver.isConnected()) {
//
//                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
//                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + "\ndata:" + passArray.toString());
//
//                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, passArray);
//            }
//        }
//    }

    /**
     * Method to make json object request where json response starts wtih
     */
//    private void makeAddOrderRequest(String date, String gettime, String userid, String location, JSONArray passArray) {
//        String tag_json_obj = "json_add_order_req";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("date", date);
//        params.put("time", gettime);
//        params.put("user_id", userid);
//        params.put("location", location);
//        params.put("data", passArray.toString());
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    Boolean status = response.getBoolean("responce");
//                    if (status) {
//
//                        String msg = response.getString("data");
//
////                        db_cart.clearCart();
////                        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getWishlistCount());
//                      //  Double total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;
//                        Bundle args = new Bundle();
//                        Fragment fm = new Payment_fragment();
//                        args.putString("msg", msg);
//
//                        args.putString("total", String.valueOf(total));
//                        fm.setArguments(args);
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                .addToBackStack(null).commit();
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }



    public void checkfirstorder(String userid, final String total){
        final SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        alertDialog.setCancelable(false);
        alertDialog.show();

        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("total_amount",total);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.checkfirstorder, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    alertDialog.dismiss();
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {

                            discount=(Double.parseDouble(total)*30)/100;
                            Toast.makeText(getActivity(), total+discount, Toast.LENGTH_SHORT).show();
                            tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                                    getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
                                    getResources().getString(R.string.delivery_charge) + deli_charges + "\n" +
                                    getResources().getString(R.string.Discount)+ discount+"\n"+
                                    getResources().getString(R.string.total_amount) +
                                    db_cart.getTotalAmount() + " + " + deli_charges +"-"+discount+ " = " + (Double.parseDouble(total)-discount)+" "+ getResources().getString(R.string.currency));


                                    checkfo=1;
                        }else{
                                    checkfo=2;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                    checkfo=0;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                alertDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Activity activity=getActivity();
                    if(activity !=null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();




                    checkfo
                            =0;
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
