package Fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Favourite_Adappter;
import Adapter.Home_Icon_Adapter;
import Config.BaseURL;
import Model.Home_Icon_model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;

import static android.content.Context.MODE_PRIVATE;


public class Favourite extends Fragment {

    RecyclerView rv_headre_icons,rv_cat,rv_store;
    TextView fav_prod,fav_store,fav_cat;
    private Favourite_Adappter menu_adapter;
    SharedPreferences sharedPreferences;
    String usrid;
    private Home_Icon_Adapter menu_adapter1;
    private List<Home_Icon_model> menu_models = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourite, container, false);

        fav_cat=view.findViewById(R.id.fav_category);
        fav_prod=view.findViewById(R.id.fav_prod);
        fav_store=view.findViewById(R.id.fav_store);

        sharedPreferences=getActivity().getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

        usrid=sharedPreferences.getString(BaseURL.KEY_ID,"0");

        rv_headre_icons = (RecyclerView) view.findViewById(R.id.rv_fav);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_headre_icons.setLayoutManager(gridLayoutManager);
        rv_headre_icons.setItemAnimator(new DefaultItemAnimator());
        rv_headre_icons.setNestedScrollingEnabled(false);

        rv_cat = (RecyclerView) view.findViewById(R.id.rv_cat);
        GridLayoutManager gridLayoutManager1= new GridLayoutManager(getActivity(), 3);
        rv_cat.setLayoutManager(gridLayoutManager1);
        rv_cat.setItemAnimator(new DefaultItemAnimator());
        rv_cat.setNestedScrollingEnabled(false);

        rv_store = (RecyclerView) view.findViewById(R.id.rv_store);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 3);
        rv_store.setLayoutManager(gridLayoutManager2);
        rv_store.setItemAnimator(new DefaultItemAnimator());
        rv_store.setNestedScrollingEnabled(false);

            make_menu_items(usrid);
                fav_store.setOnClickListener(new View.OnClickListener() {
                  @Override
                      public void onClick(View view) {
                      rv_headre_icons.setVisibility(View.GONE);
                      rv_cat.setVisibility(View.GONE);
                      rv_store.setVisibility(View.VISIBLE);
                      makestore(usrid);
                        }
                });
        fav_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_cat.setVisibility(View.GONE);
                rv_store.setVisibility(View.GONE);
                rv_headre_icons.setVisibility(View.VISIBLE);
                make_menu_items(usrid);
            }
        });
        fav_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_store.setVisibility(View.GONE);
                rv_headre_icons.setVisibility(View.GONE);
                rv_cat.setVisibility(View.VISIBLE);
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

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Favourite_Adappter(menu_models);
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
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Favourite_Adappter(menu_models);
                            rv_store.setAdapter(menu_adapter);
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
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter1 = new Home_Icon_Adapter(menu_models);
                            rv_cat.setAdapter(menu_adapter1);
                            menu_adapter1.notifyDataSetChanged();
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

}