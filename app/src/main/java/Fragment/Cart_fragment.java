package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.swipe.util.Attributes;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bouncycastle.util.Store;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Cart_adapter;
import Adapter.Delivery_get_address_adapter;
import Adapter.Product_adapter;
import Adapter.Product_adapter2;
import Adapter.Socity_adapter;
import Config.BaseURL;
import Model.Delivery_address_model;
import Model.Product_model;
import Model.Product_model;
import Model.Socity_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.LoginActivity;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.ProductActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonArrayRequest;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.RecyclerTouchListener;
import util.Session_management;

import static android.content.Context.MODE_PRIVATE;

public class Cart_fragment extends Fragment implements View.OnClickListener,related_interface {

    private static String TAG = Cart_fragment.class.getSimpleName();

    ArrayList<Product_model> product_modelList = new ArrayList<>();
    Product_adapter2 product_adapter;
    private RecyclerView rv_cart,rv_realted;
    private TextView tv_clear, tv_total, tv_item;
    private RelativeLayout btn_checkout;
    private ShimmerFrameLayout mShimmerViewContainer;

    ArrayList<String> store_ids = new ArrayList<>();
    Button continueshoping;
    private DatabaseHandler db;
    String delicharge,Storecharges;

    private Session_management sessionManagement;

    public Cart_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.cart));


        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        rv_realted = (RecyclerView) view.findViewById(R.id.rv_related);
        tv_clear = (TextView) view.findViewById(R.id.tv_cart_clear);
        tv_total = (TextView) view.findViewById(R.id.tv_cart_total);
        tv_item = (TextView) view.findViewById(R.id.tv_cart_item);
        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_cart_checkout);
        rv_cart = (RecyclerView) view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        continueshoping=view.findViewById(R.id.btnContinue);
        db = new DatabaseHandler(getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_realted.setLayoutManager(layoutManager);
        rv_realted.setHasFixedSize(true);
        rv_realted.setItemViewCacheSize(10);
        rv_realted.setDrawingCacheEnabled(true);
        rv_realted.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        ((MainActivity)getActivity()).bot_cart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MainActivity)getActivity()).bot_cat.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_fav.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_profile.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_store.setBackgroundColor(getResources().getColor(R.color.white));

        ArrayList<HashMap<String, String>> map = db.getCartAll();

        Cart_adapter adapter = new Cart_adapter(getActivity(), map);
        rv_cart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateData();

        getRelated();

        tv_clear.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        continueshoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fm = new StoreFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });


//        rv_realted.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_realted, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                SharedPreferences preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
//                String language=preferences.getString("language","");
//
//                Bundle args = new Bundle();
//                Fragment fm = new ProductDetailShow();
//                if(language.contains("spanish")){
//                    args.putString("product_name",product_modelList.get(position).getProduct_name_arb());
//                    args.putString("description",product_modelList.get(position).getProduct_description_arb());
//                }else{
//                    args.putString("product_name",product_modelList.get(position).getProduct_name());
//                    args.putString("description",product_modelList.get(position).getProduct_description());
//                }
//                args.putString("size",product_modelList.get(position).getSize());
//                args.putString("color",product_modelList.get(position).getColor());
//                args.putString("product_id",product_modelList.get(position).getProduct_id());
//                args.putString("category_id",product_modelList.get(position).getCategory_id());
//                args.putString("deal_price",product_modelList.get(position).getDeal_price());
//                args.putString("start_date",product_modelList.get(position).getStart_date());
//                args.putString("start_time",product_modelList.get(position).getStart_time());
//                args.putString("end_date",product_modelList.get(position).getEnd_date());
//                args.putString("end_time",product_modelList.get(position).getEnd_time());
//                args.putString("price",product_modelList.get(position).getPrice());
//                args.putString("image",product_modelList.get(position).getProduct_image());
//                args.putString("status",product_modelList.get(position).getStatus());
//                args.putString("stock",product_modelList.get(position).getStock());
//                args.putString("unit_value",product_modelList.get(position).getUnit_value());
//                args.putString("unit",product_modelList.get(position).getUnit());
//                args.putString("increment",product_modelList.get(position).getIncreament());
//                args.putString("rewards",product_modelList.get(position).getRewards());
//                args.putString("stock",product_modelList.get(position).getStock());
//                args.putString("title",product_modelList.get(position).getTitle());
//                args.putString("store_id",product_modelList.get(position).getStoreid());
//                args.putString("qty","0");
//
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));

        return view;
    }

    private void getRelated() {

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        product_adapter = new Product_adapter2(product_modelList, getActivity(),Cart_fragment.this);
                        rv_realted.setAdapter(product_adapter);
                        product_adapter.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                                SweetAlertDialog error=
                                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("No Data Found")
                                                .setConfirmButtonBackgroundColor(Color.RED)
                                                .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        getActivity().onBackPressed();
                                                    }
                                                });
                                error.show();
                            }
                        }

                    }else{
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        SweetAlertDialog error=
                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No Data Found")
                                        .setConfirmButtonBackgroundColor(Color.RED)
                                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                getActivity().onBackPressed();
                                            }
                                        });
                        error.show();
                    }
                } catch (JSONException e) {
//                    loading.dismiss();
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
//                    loading.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_cart_clear) {
            // showdialog
            showClearDialog();
        } else if (id == R.id.btn_cart_checkout) {

            if (ConnectivityReceiver.isConnected()) {
                makeGetLimiteRequest();
            } else {
                ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
            }

        }
    }

    // update UI
    private void updateData() {
        tv_total.setText("" + db.getTotalAmount());
        tv_item.setText("(" + db.getCartCount()+")");
        ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());
    }

    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(getResources().getString(R.string.sure_del));
        alertDialog.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db.clearCart();
                ArrayList<HashMap<String, String>> map = db.getCartAll();
                Cart_adapter adapter = new Cart_adapter(getActivity(), map);
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                updateData();

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetLimiteRequest() {

        final SweetAlertDialog loading=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        loading.setCancelable(false);
        loading.show();

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_LIMITE_SETTING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        Double total_amount = Double.parseDouble(db.getTotalAmount());


                        try {
                            // Parsing json array response
                            // loop through each json object

                            boolean issmall = false;
                            boolean isbig = false;

                            // arraylist list variable for store data;
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                int value;

                                if (jsonObject.getString("id").equals("1")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount < value) {
                                        issmall = true;
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (jsonObject.getString("id").equals("2")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount > value) {
                                        isbig = true;
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                }else if (jsonObject.getString("id").equals("3")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount > value) {

                                        delicharge="1";
                                        //Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }else {
                                        delicharge="0";
                                    }
                                }

                            }

                            if (!issmall && !isbig) {
                                if (sessionManagement.isLoggedIn()) {


                                    getDeliveryChargesfromstore();

//                                    Bundle args = new Bundle();
//                                    Fragment fm = new Delivery_fragment();
//                                    args.putString("ischarge",delicharge);
//                                    fm.setArguments(args);
//
//                                    FragmentManager fragmentManager = getFragmentManager();
//                                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                            .addToBackStack(null).commit();
                                } else {
                                    //Toast.makeText(getActivity(), "Please login or regiter.\ncontinue", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(i);
                                }
                            }
                            loading.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.dismiss();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Activity activity=getActivity();
                    if(activity != null && isAdded())
                    Toast.makeText(getActivity(), "Connection Time out", Toast.LENGTH_SHORT).show();

                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    private void getDeliveryChargesfromstore() {

        store_ids.clear();
        store_ids = db.getColumnStoreId();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Get_Delivery_Store, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                //Toast.makeText(getActivity(),String.valueOf(response), Toast.LENGTH_SHORT).show();

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        int charges = 0;


                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (store_ids.contains(object.getString("user_id"))){
                                //int tempy = Integer.parseInt(object.getString("delivery_charges"));
                                charges = charges + 1;
                            }

                        }

                        //charges calculation
                        if (charges == 0){
                            charges = 35;
                        }
                        else {
                            charges = (charges - 1) * 10;
                            charges = charges + 35;
                        }
                        Storecharges = String.valueOf(charges);

//                        Toast.makeText(getActivity(), Storecharges, Toast.LENGTH_SHORT).show();

                        Bundle args = new Bundle();
                        Fragment fm = new Delivery_fragment();
                        args.putString("ischarge",delicharge);
                        args.putString("delivery_charges",Storecharges);
                        fm.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();

                        Intent updates = new Intent("Grocery_delivery_charge");
                        //updates.putExtra("type", "update");
                        updates.putExtra("charge", Storecharges);
                        getActivity().sendBroadcast(updates);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "json_get_address_req");

}

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        mShimmerViewContainer.stopShimmerAnimation();

        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();

        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    @Override
    public void OnClick(int position) {
        SharedPreferences preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
                String language=preferences.getString("language","");

                Bundle args = new Bundle();
                Fragment fm = new ProductDetailShow();
                if(language.contains("spanish")){
                    args.putString("product_name",product_modelList.get(position).getProduct_name_arb());
                    args.putString("description",product_modelList.get(position).getProduct_description_arb());
                }else{
                    args.putString("product_name",product_modelList.get(position).getProduct_name());
                    args.putString("description",product_modelList.get(position).getProduct_description());
                }
                args.putString("size",product_modelList.get(position).getSize());
                args.putString("color",product_modelList.get(position).getColor());
                args.putString("product_id",product_modelList.get(position).getProduct_id());
                args.putString("category_id",product_modelList.get(position).getCategory_id());
                args.putString("deal_price",product_modelList.get(position).getDeal_price());
                args.putString("start_date",product_modelList.get(position).getStart_date());
                args.putString("start_time",product_modelList.get(position).getStart_time());
                args.putString("end_date",product_modelList.get(position).getEnd_date());
                args.putString("end_time",product_modelList.get(position).getEnd_time());
                args.putString("price",product_modelList.get(position).getPrice());
                args.putString("image",product_modelList.get(position).getProduct_image());
                args.putString("status",product_modelList.get(position).getStatus());
                args.putString("stock",product_modelList.get(position).getStock());
                args.putString("unit_value",product_modelList.get(position).getUnit_value());
                args.putString("unit",product_modelList.get(position).getUnit());
                args.putString("increment",product_modelList.get(position).getIncreament());
                args.putString("rewards",product_modelList.get(position).getRewards());
                args.putString("stock",product_modelList.get(position).getStock());
                args.putString("title",product_modelList.get(position).getTitle());
                args.putString("store_id",product_modelList.get(position).get_Storeid());
                args.putString("qty","0");

                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

    }
}
