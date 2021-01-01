package Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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
import Model.Product_model;
import Model.Store_Model;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static android.content.Context.MODE_PRIVATE;

public class Favourite extends Fragment {

    private ShimmerFrameLayout mShimmerViewContainer;

    CardView cardView_store,cardView_cat,cardView_product;
    RecyclerView rv_headre_icons,rv_store,rv_cat;
    TextView Texty;
    private Favourite_Adappter menu_adapter;
    private Product_adapter product_adapter;
    private Home_Icon_Adapter category_adapter;
    private Store_Adapter store_adapter;

    CardView fav_prod,fav_store,fav_cat;

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

        fav_cat=view.findViewById(R.id.card_cate);
        fav_prod=view.findViewById(R.id.card_product);
        fav_store=view.findViewById(R.id.card_store);

        cardView_cat = view.findViewById(R.id.card_cate);
        cardView_product = view.findViewById(R.id.card_product);
        cardView_store = view.findViewById(R.id.card_store);

        Texty = view.findViewById(R.id.no_product);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        sharedPreferences=getActivity().getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

        usrid=sharedPreferences.getString(BaseURL.KEY_ID,"0");

        rv_headre_icons = (RecyclerView) view.findViewById(R.id.rv_fav);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_headre_icons.setLayoutManager(gridLayoutManager);
        rv_headre_icons.setItemAnimator(new DefaultItemAnimator());
        rv_headre_icons.setNestedScrollingEnabled(false);

        rv_store = (RecyclerView) view.findViewById(R.id.rv_store);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        rv_store.setLayoutManager(gridLayoutManager1);
        rv_store.setItemAnimator(new DefaultItemAnimator());
        rv_store.setNestedScrollingEnabled(false);

        rv_cat = (RecyclerView) view.findViewById(R.id.rv_cat);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
        rv_cat.setLayoutManager(gridLayoutManager2);
        rv_cat.setItemAnimator(new DefaultItemAnimator());
        rv_cat.setNestedScrollingEnabled(false);



        ((MainActivity)getActivity()).bot_fav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MainActivity)getActivity()).bot_cat.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_cart.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_profile.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_store.setBackgroundColor(getResources().getColor(R.color.white));



        make_menu_items(usrid);
                fav_store.setOnClickListener(new View.OnClickListener() {
                  @Override
                      public void onClick(View view) {
                      fav_store.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                      fav_cat.setCardBackgroundColor(Color.parseColor("#ffffff"));
                      fav_prod.setCardBackgroundColor(Color.parseColor("#ffffff"));

//                      fav_store.setBackgroundColor(Color.parseColor("#7abcbc"));
//                      fav_cat.setBackgroundColor(Color.parseColor("#ffffff"));
//                      fav_prod.setBackgroundColor(Color.parseColor("#ffffff"));

                      rv_cat.setVisibility(View.GONE);
                      rv_headre_icons.setVisibility(View.GONE);
                      rv_store.setVisibility(View.VISIBLE);
                      mShimmerViewContainer.setVisibility(View.VISIBLE);
                      mShimmerViewContainer.startShimmerAnimation();
                      makestore(usrid);
                        }
                });
        fav_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_cat.setVisibility(View.GONE);
                rv_store.setVisibility(View.GONE);
                rv_headre_icons.setVisibility(View.VISIBLE);

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
//                fav_store.setBackgroundColor(Color.parseColor("#ffffff"));
//                fav_cat.setBackgroundColor(Color.parseColor("#ffffff"));
//                fav_prod.setBackgroundColor(Color.parseColor("#7abcbc"));


                fav_prod.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fav_cat.setCardBackgroundColor(Color.parseColor("#ffffff"));
                fav_store.setCardBackgroundColor(Color.parseColor("#ffffff"));

                make_menu_items(usrid);
            }
        });
        fav_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_store.setVisibility(View.GONE);
                rv_headre_icons.setVisibility(View.GONE);
                rv_cat.setVisibility(View.VISIBLE);

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
//                fav_store.setBackgroundColor(Color.parseColor("#ffffff"));
//                fav_cat.setBackgroundColor(Color.parseColor("#7abcbc"));
//                fav_prod.setBackgroundColor(Color.parseColor("#ffffff"));

                fav_cat.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fav_store.setCardBackgroundColor(Color.parseColor("#ffffff"));
                fav_prod.setCardBackgroundColor(Color.parseColor("#ffffff"));

                make_categories(usrid);
            }
        });

        rv_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_cat, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid;
                getid = menu_models.get(position).getId();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("name",menu_models.get(position).getTitle());
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        rv_store.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_store, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String storeid;
                storeid = store_models.get(position).getUser_id();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("storeid", storeid);
                args.putString("laddan_jaffery", "store");
                args.putString("user_email",store_models.get(position).getUser_email());
                args.putString("user_phone",store_models.get(position).getUser_phone());
                args.putString("name",store_models.get(position).getUser_name());
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

                            if (product_models.isEmpty()){
                                rv_headre_icons.setVisibility(View.GONE);
                                Texty.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            rv_headre_icons.setVisibility(View.GONE);
                            Texty.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rv_headre_icons.setVisibility(View.GONE);
                    Texty.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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
                            rv_store.setAdapter(store_adapter);
                            store_adapter.notifyDataSetChanged();
                            if (store_models.isEmpty()){
                                rv_store.setVisibility(View.GONE);
                                Texty.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            rv_store.setVisibility(View.GONE);
                            Texty.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    rv_store.setVisibility(View.GONE);
                    Texty.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
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
                            rv_cat.setAdapter(category_adapter);
                            category_adapter.notifyDataSetChanged();
                            if (menu_models.isEmpty()){
                                rv_cat.setVisibility(View.GONE);
                                Texty.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            rv_cat.setVisibility(View.GONE);
                            Texty.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    rv_cat.setVisibility(View.GONE);
                    Texty.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();


                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

                    Activity activity=getActivity();
                    if(activity != null)
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();

//                    Texty.setVisibility(View.VISIBLE);
//                    Texty.setText(getResources().getString(R.string.connection_time_out));
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