package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapter.Product_adapter;
import Adapter.Search_adapter;
import Adapter.Store_Adapter;
import Adapter.SuggestionAdapter;
import Config.BaseURL;
import Model.Product_model;
import Model.Store_Model;
import Model.Store_main_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hamza Ali on 26/6/2020.
 */

public class Search_fragment extends Fragment {

    private static String TAG = Search_fragment.class.getSimpleName();
    //    String[] fruits = {"MIlk butter & cream", "Bread Buns & Pals", "Dals Mix Pack", "buns-pavs", "cakes", "Channa Dal", "Toor Dal", "Wheat Atta"
//            , "Beson", "Almonds", "Packaged Drinking", "Cola drinks", "Other soft drinks", "Instant Noodles", "Cup Noodles", "Salty Biscuits", "cookie", "Sanitary pads", "sanitary Aids"
//            , "Toothpaste", "Mouthwash", "Hair oil", "Shampoo", "Pure & pomace olive", "ICE cream", "Theme Egg", "Amul Milk", "AMul Milk Pack power", "kaju pista dd"};
    private EditText acTextView;
    private RelativeLayout btn_search;
    private RecyclerView rv_search,stores;

    ShimmerFrameLayout shimmer_store,shimmer_product;
    String city;
    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;

    private TextView Texty;
    List<Store_main_model> store_models=new ArrayList<>();
    Store_Adapter store_adapter;
    RadioButton store,product;

    public Search_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.search));
        store=view.findViewById(R.id.rbstore);
        product=view.findViewById(R.id.rbprod);

        shimmer_product = view.findViewById(R.id.shimmer_product);
        shimmer_store = view.findViewById(R.id.shimmer_store);

        Texty = view.findViewById(R.id.no_record);
        store.setChecked(true);
        acTextView = (EditText) view.findViewById(R.id.et_search);
//        acTextView.setThreshold(1);
//
//        acTextView.setAdapter(new SuggestionAdapter(getActivity(), acTextView.getText().toString()));

        acTextView.setTextColor(getResources().getColor(R.color.green));
        btn_search = (RelativeLayout) view.findViewById(R.id.btn_search);
        rv_search = (RecyclerView) view.findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new GridLayoutManager(getActivity(),3));
        stores=view.findViewById(R.id.rv_store);
        stores.setLayoutManager(new GridLayoutManager(getActivity(),3));

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        String lat, longi;
        lat=sharedPreferences.getString("lat",null);
        longi=sharedPreferences.getString("long",null);
        double laty = Double.parseDouble(lat);
        double longy = Double.parseDouble(longi);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(laty, longy, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size()>0) {
            city = addresses.get(0).getLocality();
        }
//        Toast.makeText(getActivity(),cityName ,Toast.LENGTH_LONG).show();

        if(city == null){
            city="Lahore";
        }


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_search.setVisibility(View.GONE);
                stores.setVisibility(View.GONE);

                Texty.setVisibility(View.GONE);
//                shimmer_product.setVisibility(View.GONE);
//                shimmer_product.stopShimmerAnimation();
//                shimmer_store.setVisibility(View.GONE);
//                shimmer_store.stopShimmerAnimation();

                String get_search_txt ="%"+ acTextView.getText().toString() +"%";
                if (TextUtils.isEmpty(get_search_txt)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        if(product.isChecked()){
                            shimmer_product.setVisibility(View.VISIBLE);
                            shimmer_product.startShimmerAnimation();
                            makeGetProductRequest(get_search_txt);
                        }else{
                            shimmer_store.setVisibility(View.VISIBLE);
                            shimmer_store.startShimmerAnimation();
                        getstores(get_search_txt);
                        }
                    } else {
                        ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                    }
                }

            }
        });
        
        stores.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), stores, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String storeid = store_models.get(position).getStoer_id();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("storeid", storeid);
                args.putString("laddan_jaffery", "store");
                args.putString("name",store_models.get(position).getUser_name());
                args.putString("user_email",store_models.get(position).getStore_details());
                args.putString("user_phone",store_models.get(position).getUser_phone());
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        rv_search.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_search, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SharedPreferences preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
                String language=preferences.getString("language","");

                Bundle args = new Bundle();
                Fragment fm = new ProductDetailShow();
                if(language.contains("spanish")){
                    args.putString("product_name",product_modelList.get(position).getProduct_name_arb());//TODO
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

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        
        

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetProductRequest(String search_text) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("search", search_text);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelList = gson.fromJson(response.getString("data"), listType);

                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_search.setAdapter(adapter_product);

                        shimmer_product.setVisibility(View.GONE);
                        shimmer_product.stopShimmerAnimation();

                        rv_search.setVisibility(View.VISIBLE);
                        adapter_product.notifyDataSetChanged();

                        
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                                shimmer_product.setVisibility(View.GONE);
                                shimmer_product.stopShimmerAnimation();

                                rv_search.setVisibility(View.GONE);
                                Texty.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                } catch (JSONException e) {
                    shimmer_product.setVisibility(View.GONE);
                    shimmer_product.stopShimmerAnimation();
                    rv_search.setVisibility(View.GONE);
                    Texty.setVisibility(View.VISIBLE);
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






    public void getstores(String search){

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("search",search);

        CustomVolleyJsonRequest jsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getStores, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response !=null && response.length()>0){

                        Boolean status=response.getBoolean("response");
                        if(status)
                        {
                            Gson gson=new Gson();
                            Type listtype=new TypeToken<List<Store_main_model>>(){

                            }.getType();
                            store_models=gson.fromJson(response.getString("data"),listtype);
                            store_adapter=new Store_Adapter(getActivity(),store_models);
                            stores.setLayoutManager(new GridLayoutManager(getActivity(),3));
                            stores.setAdapter(store_adapter);

                            shimmer_store.setVisibility(View.GONE);
                            shimmer_store.stopShimmerAnimation();

                            stores.setVisibility(View.VISIBLE);
                            store_adapter.notifyDataSetChanged();

                            if(store_models.size()==0){
//                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE).setTitleText("No data Found")
//                                        .setConfirmButtonBackgroundColor(Color.RED).show();
                                shimmer_store.setVisibility(View.GONE);
                                shimmer_store.stopShimmerAnimation();
                                stores.setVisibility(View.GONE);
                                Texty.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                }catch (Exception e){
                    stores.setVisibility(View.GONE);
                    shimmer_store.setVisibility(View.GONE);
                    shimmer_store.stopShimmerAnimation();
                    Texty.setVisibility(View.VISIBLE);
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
    @Override
    public void onResume() {
        super.onResume();
        shimmer_store.startShimmerAnimation();
        shimmer_product.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmer_store.stopShimmerAnimation();
        shimmer_product.startShimmerAnimation();
    }

}
