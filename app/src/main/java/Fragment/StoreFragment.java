package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapter.CatProdAdapter;
import Adapter.Home_Icon_Adapter;
import Adapter.Product_adapter;
import Adapter.Store_Adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Home_Icon_model;
import Model.Product_model;
import Model.Store_Model;
import Model.Store_main_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.CustomSlider;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import gogrocer.tcc.WebView;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;
import static gogrocer.tcc.AppController.TAG;

public class StoreFragment extends Fragment implements Main_new {
    private Home_Icon_Adapter menu_adapter;
    private List<Home_Icon_model> menu_models = new ArrayList<>();
    SliderLayout banner_slider,deal_banner;
    RecyclerView stores;
    LinearLayout Search_layout;
    String storeid,getid;
    LinearLayout dealday;
    String city;
    private ShimmerFrameLayout mShimmerViewContainer,shimmy;
    private RecyclerView rv_headre_icons,catprod,rv_top_selling;
    List<Store_main_model> store_models=new ArrayList<>();
    Store_Adapter store_adapter;
    FloatingActionButton floatingActionButton;
    TextView t1,t2;

    List<Category_model> models=new ArrayList<>();
    CatProdAdapter catProdAdapter;
    private Product_adapter top_selling_adapter;
    private List<Product_model> top_selling_models = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stores, container, false);

        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        deal_banner = (SliderLayout) view.findViewById(R.id.deal_bannery);

        stores=(RecyclerView)view.findViewById(R.id.rv_stores);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        shimmy = view.findViewById(R.id.shimmer_view_container2);

        catprod =view.findViewById(R.id.catprodrv);
        t1 =view.findViewById(R.id.catname);
        t2 =view.findViewById(R.id.t2);
        rv_top_selling = (RecyclerView) view.findViewById(R.id.top_selling_recycler);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_top_selling.setLayoutManager(gridLayoutManager2);
        rv_top_selling.setItemAnimator(new DefaultItemAnimator());
        rv_top_selling.setNestedScrollingEnabled(false);


        makeGetBannerSliderRequest();
        makeGetBannerSliderRequestdeal();

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        String lat, longi;
        lat=sharedPreferences.getString("lat","123");
        longi=sharedPreferences.getString("long","123");
        if(!lat.equals("123"))
        {
            double laty = Double.parseDouble(lat);
            double longy = Double.parseDouble(longi);

            Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);

        try {
            List<Address> addresses = new ArrayList<Address>();
            addresses = geocoder.getFromLocation(laty, longy, 4);

            //Toast.makeText(getActivity(),String.valueOf(addresses), Toast.LENGTH_SHORT).show();

            if(addresses.size()>0) {
                city = addresses.get(0).getSubAdminArea();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("city",city);
                editor.apply();

//                Toast.makeText(getActivity(), city
//                        , Toast.LENGTH_LONG).show();

            }

        } catch (IOException e) {
            e.printStackTrace();
  //          Toast.makeText(getActivity(),String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
}

        if(city == null){
               city=sharedPreferences.getString("city",null);
//               Toast.makeText(getActivity(), "From SharedPreference : "+sharedPreferences.getString("city",null), Toast.LENGTH_SHORT).show();
        }

//        dealday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle args = new Bundle();
//                Fragment fm = new Deal_Fragemnt();
//                args.putString("laddan_jaffery", "deals");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//            }
//        });

//        Toast.makeText(getActivity(), city+"", Toast.LENGTH_SHORT).show();

        floatingActionButton = view.findViewById(R.id.fab_id);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WebView.class));
            }
        });

        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
        Search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Search_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });

        ((MainActivity)getActivity()).bot_store.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MainActivity)getActivity()).bot_cat.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_fav.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_profile.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_cart.setBackgroundColor(getResources().getColor(R.color.white));

        rv_headre_icons = (RecyclerView) view.findViewById(R.id.collapsing_recycler);

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
        rv_headre_icons.setLayoutManager(layoutManager);
        rv_headre_icons.setHasFixedSize(true);
        rv_headre_icons.setItemViewCacheSize(10);
        rv_headre_icons.setDrawingCacheEnabled(true);
        rv_headre_icons.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        catprod.setLayoutManager(new LinearLayoutManager(getActivity()));

        make_menu_items1();  //Category For Stores

        //make_menu_items(); //For Categoris , which is not shown in page. So i comment it !!!

        //getstores();

        make_top_selling(); // In the End , Popular Products Shown

        rv_top_selling.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_top_selling, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                getid = top_selling_models.get(position).getProduct_id();
//                Bundle args = new Bundle();
//                Fragment fm = new Product_fragment();
//                args.putString("cat_top_selling", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
                SharedPreferences preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
                String language=preferences.getString("language","");

                Bundle args = new Bundle();
                Fragment fm = new ProductDetailShow();
                if(language.contains("spanish")){
                    args.putString("product_name",top_selling_models.get(position).getProduct_name_arb());//TODO
                    args.putString("description",top_selling_models.get(position).getProduct_description_arb());
                }else{
                    args.putString("product_name",top_selling_models.get(position).getProduct_name());
                    args.putString("description",top_selling_models.get(position).getProduct_description());
                }
                args.putString("size", String.valueOf(top_selling_models.get(position).getSize()));
                args.putString("color", String.valueOf(top_selling_models.get(position).getColor()));
                args.putString("unit_value", String.valueOf(top_selling_models.get(position).getUnit_value()));

                args.putString("product_id",top_selling_models.get(position).getProduct_id());
                args.putString("category_id",top_selling_models.get(position).getCategory_id());
                args.putString("deal_price",top_selling_models.get(position).getDeal_price());
                args.putString("start_date",top_selling_models.get(position).getStart_date());
                args.putString("start_time",top_selling_models.get(position).getStart_time());
                args.putString("end_date",top_selling_models.get(position).getEnd_date());
                args.putString("end_time",top_selling_models.get(position).getEnd_time());
                args.putString("price",top_selling_models.get(position).getPrice());
                args.putString("image",top_selling_models.get(position).getProduct_image());
                args.putString("status",top_selling_models.get(position).getStatus());
                args.putString("stock",top_selling_models.get(position).getStock());
                args.putString("unit",top_selling_models.get(position).getUnit());
                args.putString("increment",top_selling_models.get(position).getIncreament());
                args.putString("rewards",top_selling_models.get(position).getRewards());
                args.putString("stock",top_selling_models.get(position).getStock());
                args.putString("title",top_selling_models.get(position).getTitle());
                args.putString("store_id",top_selling_models.get(position).get_Storeid());
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

        rv_headre_icons.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_headre_icons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = menu_models.get(position).getId();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("name",menu_models.get(position).getTitle());
                args.putString("user_email",store_models.get(position).getStore_details());
                args.putString("user_phone",store_models.get(position).getUser_phone());
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                fragmentManager.popBackStack();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        stores.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), stores, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                storeid = store_models.get(position).getStoer_id();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("storeid", storeid);
                args.putString("laddan_jaffery", "store");
                args.putString("name",store_models.get(position).getStore_name());
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
        return view;
    }

    public void getstores(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);

        CustomVolleyJsonRequest jsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getStores, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    if(response !=null && response.length()>0){

                        Boolean status=response.getBoolean("response");
                        if(status)
                        {
                            t1.setVisibility(View.VISIBLE);
                            Gson gson=new Gson();
                            Type listtype=new TypeToken<List<Store_main_model>>(){

                            }.getType();

                            store_models=gson.fromJson(response.getString("data"),listtype);
                            store_adapter=new Store_Adapter(getActivity(),store_models);
                            stores.setLayoutManager(new GridLayoutManager(getActivity(),3));
                            stores.setAdapter(store_adapter);
                            store_adapter.notifyDataSetChanged();
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




    private void makeGetBannerSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        fm.setArguments(args);
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
//                                    }
//                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    if(activity !=null && isAdded()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    private void makeGetBannerSliderRequestdeal() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_FEAATURED_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray2 = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray2.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray2) {
                                CustomSlider textSliderView2 = new CustomSlider(getActivity());
                                textSliderView2.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView2.bundle(new Bundle());
                                textSliderView2.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView2.getBundle().putString("extra", name.get("sub_cat"));
                                deal_banner.addSlider(textSliderView2);
//                                final String sub_cat = (String) textSliderView.getBundle().get("extra");

                                textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();
                                        Fragment fm = new Deal_Fragemnt();
                                        args.putString("laddan_jaffery", "deals");
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    if(activity !=null && isAdded()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }
    private void make_menu_items() {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            shimmy.stopShimmerAnimation();
                            shimmy.setVisibility(View.GONE);

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Home_Icon_Adapter(menu_models);
                            rv_headre_icons.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();
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
                    Activity activity=getActivity();
                    if(activity !=null && isAdded()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        shimmy.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
        shimmy.startShimmerAnimation();
    }


    private void make_menu_items1() {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            models = gson.fromJson(response.getString("data"), listType);
                            catProdAdapter = new CatProdAdapter(models,city,StoreFragment.this);
                            catprod.setAdapter(catProdAdapter);
                            catProdAdapter.notifyDataSetChanged();

//                            getProducts(menu_models.get(0).getId());
                        }
                    }
                } catch (JSONException e) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Activity activity=getActivity();
                    if(activity !=null && isAdded()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    private void make_top_selling() {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            t2.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Product_model>>() {
                            }.getType();
                            top_selling_models = gson.fromJson(response.getString("top_selling_product"), listType);
                            top_selling_adapter = new Product_adapter(top_selling_models,getActivity());
                            rv_top_selling.setAdapter(top_selling_adapter);
                            top_selling_adapter.notifyDataSetChanged();
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
                    if(getActivity() != null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

//    @Override
//    public void OnClick(String id, String name, String store) {
//
//
//    }


    @Override
    public void OnClick(String id, String name, String store, String user_email, String user_phone) {
        Bundle args = new Bundle();
        Fragment fm = new Product_fragment();
        args.putString("storeid", id);
//        args.putString("laddan_jaffery", "store");
//        args.putString("name",name);
        args.putString("name",name);
        args.putString("user_email",user_email);
        args.putString("user_phone",user_phone);
//        Toast.makeText(getActivity(), "onclick", Toast.LENGTH_SHORT).show();
//        args.putString("user_email",store_models.get(position).getUser_email());
//        args.putString("user_phone",store_models.get(position).getUser_phone());
        fm.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                .addToBackStack(null).commit();
    }
}
