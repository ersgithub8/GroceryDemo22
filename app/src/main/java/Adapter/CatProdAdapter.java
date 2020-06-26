package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;

import static Adapter.SuggestionAdapter.TAG;
import static android.content.Context.MODE_PRIVATE;

public class CatProdAdapter extends RecyclerView.Adapter<CatProdAdapter.CartProdVH> {

    List<Category_model> modelList;
    Context context;

    Product_adapter2 adapter_product;
    List<Product_model> product_modelList=new ArrayList<>();



    SharedPreferences preferences;
    String language;
    public CatProdAdapter(List<Category_model> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public CartProdVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_catprod,parent,false);
        context=parent.getContext();
        return new CartProdVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProdVH holder, int position) {
        Category_model mList=modelList.get(position);

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.catname.setText(mList.getTitle());
        }
        else {
            holder.catname.setText(mList.getArb_title());
        }

        holder.catproducts.setLayoutManager(new GridLayoutManager(context,3));

        makeGetProductRequest(mList.getId(),holder.catproducts,holder.relativeLayout);

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





    private void makeGetProductRequest(final String cat_id, final RecyclerView recyclerView, final RelativeLayout view) {

        final SweetAlertDialog loading=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");

        loading.getProgressHelper().setBarColor(context.getResources().getColor(R.color.green));

//        loading.show();

        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();

//        if(storeid !=null){
//            params.put("store_id", cat_id);
//        }else{
            params.put("cat_id", cat_id);
//        }

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter2(product_modelList, context);
                        recyclerView.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (context != null) {
                            if (product_modelList.isEmpty()) {
                                view.setVisibility(View.GONE);
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                                SweetAlertDialog error=
//                                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
//                                                .setTitleText("No Data Found")
//                                                .setConfirmButtonBackgroundColor(Color.RED)
//                                                .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
//                                                    @Override
//                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                        getActivity().onBackPressed();
//                                                    }
//                                                });
//                                error.show();
                            }
                        }

                    }else{
                        view.setVisibility(View.GONE);
//                        SweetAlertDialog error=
//                                new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("No Data Found")
//                                        .setConfirmButtonBackgroundColor(Color.RED)
//                                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                context.getActivity().onBackPressed();
//                                            }
//                                        });
//                        error.show();
                    }
                } catch (JSONException e) {
                    view.setVisibility(View.GONE);
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
