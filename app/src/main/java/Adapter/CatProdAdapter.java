package Adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import Fragment.Main_new;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import org.bouncycastle.util.Store;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import Model.Category_model;
import Model.Product_model;
import Model.Store_main_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

import static Adapter.SuggestionAdapter.TAG;
import static android.content.Context.MODE_PRIVATE;

public class CatProdAdapter extends RecyclerView.Adapter<CatProdAdapter.CartProdVH> {

    List<Category_model> modelList;
    Context context;
    Main_new main_new;
    String City;

    Store_main_adapter adapter_product;
    List<Store_main_model> product_modelList=new ArrayList<>();

    SharedPreferences preferences;
    String language;
    public CatProdAdapter(List<Category_model> modelList, String city, Main_new main_new) {
        this.modelList = modelList;
        this.City = city;
        this.main_new=main_new;
    }

    @NonNull
    @Override
    public CartProdVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_catprod,parent,false);
        context=parent.getContext();
        return new CartProdVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProdVH holder, final int position) {

        Category_model mList=modelList.get(position);

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.catname.setText(mList.getTitle());
            makeGetProductRequest(mList.getId(),mList.getTitle(),City,holder.catproducts,holder.relativeLayout);
        }
        else {
            holder.catname.setText(mList.getArb_title());
            makeGetProductRequest(mList.getId(),mList.getArb_title(),City,holder.catproducts,holder.relativeLayout);

        }

        holder.catproducts.setLayoutManager(new GridLayoutManager(context,3));

//        makeGetProductRequest(mList.getId(),mList.getTitle(),holder.catproducts,holder.relativeLayout);

    }

    @Override
    public int getItemCount() {
//        if(modelList.size()>=3){
//            return 3;
//        }else{
            return modelList.size();
//        }
    }

    public class  CartProdVH extends RecyclerView.ViewHolder{
        TextView catname;
        RecyclerView catproducts;
        RelativeLayout relativeLayout;
        public CartProdVH(@NonNull View itemView) {
            super(itemView);

            catname=itemView.findViewById(R.id.catname);
            catproducts=itemView.findViewById(R.id.productscat);
            relativeLayout=itemView.findViewById(R.id.rl);

        }
    }


    private void makeGetProductRequest(final String cat_id,final String cat_name,final String city, final RecyclerView recyclerView, final RelativeLayout view) {

        final SweetAlertDialog loading=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");

        loading.getProgressHelper().setBarColor(context.getResources().getColor(R.color.green));

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_STORE_MAIN, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("response");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Store_main_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Store_main_adapter(context,product_modelList,main_new);
                        recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (context != null) {
                            if (product_modelList.isEmpty()) {
//                                view.setVisibility(View.GONE);
                            }
                        }
                        else if (product_modelList.isEmpty()){
  //                          view.setVisibility(View.GONE);

                        }
                    }else{
//                        view.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
//                    view.setVisibility(View.GONE);
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

//                        view.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
