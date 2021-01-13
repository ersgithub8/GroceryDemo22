package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import Adapter.Category_adapter;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Product_adapter;
import Config.BaseURL;
import Model.Home_Icon_model;
import Model.Product_model;
import Model.Product_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;
import static gogrocer.tcc.AppController.TAG;

public class Category_Fragment extends Fragment {
    private Category_adapter menu_adapter;
    private ShimmerFrameLayout mShimmerViewContainer,mShimmerViewContainer1;
    private List<Home_Icon_model> menu_models = new ArrayList<>();
    private RecyclerView rv_headre_icons;
    private TextView textView;
    Inerface inerface;

    String userid;
    View viewb;
    Product_adapter product_adapter;
    List<Product_model> product_models=new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        textView = (TextView) view.findViewById(R.id.no_product);
        recyclerView = (RecyclerView) view.findViewById(R.id.rect);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);
        userid=sharedPreferences.getString(BaseURL.KEY_ID,"0");

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer1 = view.findViewById(R.id.shimmer_view_container1);

        ((MainActivity)getActivity()).bot_cat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MainActivity)getActivity()).bot_cart.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_fav.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_profile.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_store.setBackgroundColor(getResources().getColor(R.color.white));


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

        rv_headre_icons.setLayoutManager((new GridLayoutManager(getActivity(), 1)));
        recyclerView.setLayoutManager((new GridLayoutManager(getActivity(),2)));

//        rv_headre_icons.setHasFixedSize(true);
//        rv_headre_icons.setItemViewCacheSize(10);
//        rv_headre_icons.setDrawingCacheEnabled(true);
//        rv_headre_icons.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        make_menu_items();
        product_adapter = new Product_adapter(product_models,getActivity());
        rv_headre_icons.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_headre_icons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                product_models.clear();
                product_adapter.notifyDataSetChanged();

                getProducts(menu_models.get(position).getId());

                if(view != viewb){
                if(viewb ==  null){

                    view.setBackgroundColor(getResources().getColor(R.color.green));
                    viewb=view;
                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                    viewb.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    viewb=view;
                }
                }
            }

            @Override
            public void onLongItemClick(View view, final int position) {

                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                dialog.setTitleText(getResources().getString(R.string.wanttoadd));
                dialog.setCustomImage(R.drawable.heartf);
                dialog.setConfirmText(getResources().getString(R.string.tv_pro_add));
                dialog.setCancelText(getResources().getString(R.string.Remove));
                dialog.show();

                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                        addinfavcat(userid,menu_models.get(position).getId());
                    }
                });
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        removefromfavcat(userid,menu_models.get(position).getId());
                        dialog.dismiss();
                    }
                });

            }
        }));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
                SharedPreferences preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
                String language=preferences.getString("language","");

                Bundle args = new Bundle();
                Fragment fm = new ProductDetailShow();
                if(language.contains("spanish")){
                    args.putString("product_name",product_models.get(position).getProduct_name_arb());
                    args.putString("description",product_models.get(position).getProduct_description_arb());
                }else{
                    args.putString("product_name",product_models.get(position).getProduct_name());
                    args.putString("description",product_models.get(position).getProduct_description());
                }
                args.putString("size",product_models.get(position).getSize());
                args.putString("color",product_models.get(position).getColor());
                args.putString("product_id",product_models.get(position).getProduct_id());
                args.putString("category_id",product_models.get(position).getCategory_id());
                args.putString("deal_price",product_models.get(position).getDeal_price());
                args.putString("start_date",product_models.get(position).getStart_date());
                args.putString("start_time",product_models.get(position).getStart_time());
                args.putString("end_date",product_models.get(position).getEnd_date());
                args.putString("end_time",product_models.get(position).getEnd_time());
                args.putString("price",product_models.get(position).getPrice());
                args.putString("image",product_models.get(position).getProduct_image());
                args.putString("status",product_models.get(position).getStatus());
                args.putString("stock",product_models.get(position).getStock());
                args.putString("unit_value",product_models.get(position).getUnit_value());
                args.putString("unit",product_models.get(position).getUnit());
                args.putString("increment",product_models.get(position).getIncreament());
                args.putString("rewards",product_models.get(position).getRewards());
                args.putString("stock",product_models.get(position).getStock());
                args.putString("title",product_models.get(position).getTitle());
                args.putString("store_id",product_models.get(position).get_Storeid());
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

    private void make_menu_items() {
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
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Category_adapter(menu_models);
                            rv_headre_icons.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();

//                            viewb=rv_headre_icons.findViewHolderForLayoutPosition(1).itemView;
//                            viewb.setBackgroundColor(getResources().getColor(R.color.green));
                            getProducts(menu_models.get(0).getId());

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
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {Activity activity=getActivity();
                    if(activity !=null)
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void getProducts(String cat_id){
        String tag_json_obj = "json_category_req";

        mShimmerViewContainer1.setVisibility(View.VISIBLE);
        mShimmerViewContainer1.startShimmerAnimation();

//        Toast.makeText(getActivity(), cat_id+"", Toast.LENGTH_SHORT).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            mShimmerViewContainer1.stopShimmerAnimation();
                            mShimmerViewContainer1.setVisibility(View.GONE);

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Product_model>>() {
                            }.getType();

                            product_models = gson.fromJson(response.getString("data"), listType);
                            product_adapter = new Product_adapter(product_models,getActivity());
                            recyclerView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            recyclerView.setAdapter(product_adapter);
                            product_adapter.notifyDataSetChanged();

                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
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
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer1.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer1.stopShimmerAnimation();
    }

    public void addinfavcat(String userid, final String productid){

        final SweetAlertDialog loading=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
        .setTitleText("Adding in Favourite");
        loading.setCancelable(false);
        loading.show();

        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("cat_id",productid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.addfavouratecat, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("response");
                    if (status) {
                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(getActivity(), e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void removefromfavcat(String userid, String productid){
        final SweetAlertDialog loading=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
        .setTitleText("Removing From Favourite");
        loading.setCancelable(false);
        loading.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("cat_id",productid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.removefavouratecat, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("response");
                    if (status) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), response.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loading.dismiss();
                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Activity activity=getActivity();
                    loading.dismiss();
                    if(activity !=null)
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}