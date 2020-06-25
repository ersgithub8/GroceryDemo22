package Fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Category_adapter;
import Adapter.Favourite_Adappter;
import Adapter.Home_Icon_Adapter;
import Adapter.Product_adapter;
import Adapter.Store_Adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Home_Icon_model;
import Model.Product_model;
import Model.Store_Model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;

import static android.content.Context.MODE_PRIVATE;


public class Favourite extends Fragment {

    private ShimmerFrameLayout mShimmerViewContainer;

    RecyclerView rv_headre_icons;
    TextView fav_prod,fav_store,fav_cat;
    private Favourite_Adappter menu_adapter;
    private Product_adapter product_adapter;
    private Home_Icon_Adapter category_adapter;
    private Store_Adapter store_adapter;

    SharedPreferences sharedPreferences;
    String usrid;
    private List<Home_Icon_model> menu_models = new ArrayList<>();

    private List<Product_model> product_models = new ArrayList<>();
    private List<Store_Model> store_models = new ArrayList<>();
    private List<Category_model> category_models = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourite, container, false);

        fav_cat=view.findViewById(R.id.fav_category);
        fav_prod=view.findViewById(R.id.fav_prod);
        fav_store=view.findViewById(R.id.fav_store);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        sharedPreferences=getActivity().getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

        usrid=sharedPreferences.getString(BaseURL.KEY_ID,"0");

        rv_headre_icons = (RecyclerView) view.findViewById(R.id.rv_fav);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_headre_icons.setLayoutManager(gridLayoutManager);
        rv_headre_icons.setItemAnimator(new DefaultItemAnimator());
        rv_headre_icons.setNestedScrollingEnabled(false);

        make_menu_items(usrid);
                fav_store.setOnClickListener(new View.OnClickListener() {
                  @Override
                      public void onClick(View view) {
                      fav_store.setBackgroundColor(Color.parseColor("#7abcbc"));
                      fav_cat.setBackgroundColor(Color.parseColor("#ffffff"));
                      fav_prod.setBackgroundColor(Color.parseColor("#ffffff"));

                      mShimmerViewContainer.setVisibility(View.VISIBLE);
                      mShimmerViewContainer.startShimmerAnimation();

                      makestore(usrid);
                        }
                });
        fav_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                fav_store.setBackgroundColor(Color.parseColor("#ffffff"));
                fav_cat.setBackgroundColor(Color.parseColor("#ffffff"));
                fav_prod.setBackgroundColor(Color.parseColor("#7abcbc"));

                make_menu_items(usrid);
            }
        });
        fav_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                fav_store.setBackgroundColor(Color.parseColor("#ffffff"));
                fav_cat.setBackgroundColor(Color.parseColor("#7abcbc"));
                fav_prod.setBackgroundColor(Color.parseColor("#ffffff"));

                make_categories(usrid);
            }
        });

        return view;
    }
    private void make_menu_items(String userid) {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_FAVOURITE, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Product_model>>() {
                            }.getType();
                            product_models = gson.fromJson(response.getString("data"), listType);
                            product_adapter = new Product_adapter(product_models,getActivity());
                            rv_headre_icons.setAdapter(product_adapter);
                            product_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void makestore(String userid) {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getfavouratestore, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Store_Model>>() {
                            }.getType();
                            store_models = gson.fromJson(response.getString("data"), listType);
                            store_adapter = new Store_Adapter(getActivity(),store_models);
                            rv_headre_icons.setAdapter(store_adapter);
                            store_adapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    private void make_categories(String userid) {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getfavouratecat, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

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
                            category_adapter = new Home_Icon_Adapter(menu_models);
                            rv_headre_icons.setAdapter(category_adapter);
                            category_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

}