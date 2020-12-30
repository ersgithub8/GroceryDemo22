package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import Adapter.Deal_OfDay_Adapter;
import Adapter.Deal_adapter;
import Adapter.Product_adapter;
import Adapter.Store_Adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Deal_Model;
import Model.Product_model;
import Model.Slider_subcat_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.CustomSlider;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import gogrocer.tcc.Rating;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Deal_Fragemnt extends Fragment {
    private static String TAG = Product_fragment.class.getSimpleName();
    private RecyclerView rv_cat;
    private ShimmerFrameLayout mShimmerViewContainer;
    private List<Deal_Model> product_modelList = new ArrayList<>();
    private Deal_adapter deal_product;
    private SliderLayout banner_slider;
    String storeid;
    SharedPreferences sharedPreferences;
    String usrid;

    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deal, container, false);

        textView = view.findViewById(R.id.no_prod);

        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        rv_cat.setLayoutManager((new GridLayoutManager(getActivity(), 2)));

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        sharedPreferences = getActivity().getSharedPreferences(BaseURL.PREFS_NAME, MODE_PRIVATE);

        usrid = sharedPreferences.getString(BaseURL.KEY_ID, "0");

        storeid = getArguments().getString("storeid");

//        String yeh_to_hoga = getArguments().getString("laddan_jaffery");
//        if (yeh_to_hoga == null) {
//        }
//        else if (yeh_to_hoga.equals("murshid")) {
//
//        } else if (yeh_to_hoga.equals("deals")){

            makeDealRequest();

//        }

        if (ConnectivityReceiver.isConnected()) {
            //Shop by Catogary
//            makeGetCategoryRequest(getcat_id);

            //
            //Deal Of The Day Products
//            makedealIconProductRequest(get_deal_id);
            //Top Sale Products
//            maketopsaleProductRequest(get_top_sale_id);
//
//            //Slider
            makeGetBannerSliderRequest();


            rv_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_cat, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//                getid = product_modelList.get(position).getProduct_id();
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
                    args.putString("store_id",product_modelList.get(position).getStore_id());
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



        }


        return view;
    }


    //Get Shop By Catogary Products
    private void makeDealRequest() {

        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DEALS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("responce");
                    if (status.equals("true")) {
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Deal_Model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("Deal_of_the_day"), listType);
                        deal_product = new Deal_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(deal_product);
                        deal_product.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                                SweetAlertDialog error =
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                    } else {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        SweetAlertDialog error =
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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
                    e.printStackTrace();
                    textView.setVisibility(View.VISIBLE);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    ////Get Top Sale Products
    private void maketopsaleProductRequest(String cat_id) {

        final SweetAlertDialog loading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");

        loading.getProgressHelper().setBarColor(getResources().getColor(R.color.green));

//        loading.show();

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("top_selling_product", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
//                    Toast.makeText(getActivity(), "aaaa", Toast.LENGTH_SHORT).show();
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("top_selling_product"), listType);
                        //deal_product = new Deal_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(deal_product);
                        deal_product.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {Activity activity=getActivity();
                    if(activity !=null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
                    if(activity !=null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


}