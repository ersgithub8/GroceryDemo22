package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import Fragment.ProductDetailShow;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import Model.Home_Icon_model;
import Model.Product_model;
import Model.Product_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.ProductActivity;
import gogrocer.tcc.R;
import gogrocer.tcc.Rating;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;

import static android.content.Context.MODE_PRIVATE;

public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder> {

    private List<Product_model> modelList;
    private Context context;
    private DatabaseHandler dbcart;
    String language;

    SharedPreferences sharedPreferences;
    String usrid;
    boolean favcheckk=false;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_reward, tv_total, tv_contetiy, tv_add,tv_size,tv_color;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public Double reward;

        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
//            tv_size = (TextView) view.findViewById(R.id.size);
//            tv_color = (TextView) view.findViewById(R.id.color);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            iv_remove.setVisibility(View.GONE);
            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (id == R.id.iv_subcat_plus) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;
                tv_contetiy.setText(String.valueOf(qty));

            } else if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            } else if (id == R.id.tv_subcat_add) {
                HashMap<String, String> map = new HashMap<>();
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language=preferences.getString("language","");


    map.put("product_id", modelList.get(position).getProduct_id());
    map.put("product_name", modelList.get(position).getProduct_name());
    map.put("size", modelList.get(position).getSize());
    map.put("color", modelList.get(position).getColor());
    map.put("category_id", modelList.get(position).getCategory_id());
    map.put("product_description", modelList.get(position).getProduct_description());
    map.put("deal_price", modelList.get(position).getDeal_price());
    map.put("start_date", modelList.get(position).getStart_date());
    map.put("start_time", modelList.get(position).getStart_time());
    map.put("end_date", modelList.get(position).getEnd_date());
    map.put("end_time", modelList.get(position).getEnd_time());
    map.put("price", modelList.get(position).getPrice());
    map.put("product_image", modelList.get(position).getProduct_image());
    map.put("status", modelList.get(position).getStatus());
    map.put("in_stock", modelList.get(position).getIn_stock());
    map.put("unit_value", modelList.get(position).getUnit_value());
    map.put("unit", modelList.get(position).getUnit());
    map.put("increament", modelList.get(position).getIncreament());
    map.put("rewards", modelList.get(position).getRewards());
    map.put("stock", modelList.get(position).getStock());
    map.put("title", modelList.get(position).getTitle());
    map.put("store_id", modelList.get(position).get_Storeid());

                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));

                Double price = Double.parseDouble(map.get("price").trim());
                Double reward = Double.parseDouble(map.get("rewards"));
                tv_reward.setText("" + reward * items);
                tv_total.setText("" + price * items);
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

            }
//            else if (id == R.id.iv_subcat_img) {
//                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
//                language=preferences.getString("language","");
//                Log.d("lang",language);

//                if (language.contains("english")) {
//                    showProductDetail(modelList.get(position).getProduct_image(),
//                            modelList.get(position).getProduct_name(),
//                            modelList.get(position).getProduct_description(),
//                            "",
//                            position, tv_contetiy.getText().toString()
//                            ,modelList.get(position).getProduct_id());
//                }else {
//
//
//                    showProductDetail(modelList.get(position).getProduct_image(),
//                            modelList.get(position).getProduct_name_arb(),
//                            modelList.get(position).getProduct_description_arb(),
//                            "",
//                            position, tv_contetiy.getText().toString()
//                    ,modelList.get(position).getProduct_id());
//                }

//                Bundle args = new Bundle();
//                Fragment fm = new ProductDetailShow();
//                if(language.contains("spanish")){
//                    args.putString("product_name",modelList.get(position).getProduct_name_arb());//TODO
//                    args.putString("description",modelList.get(position).getProduct_description_arb());
//                    args.putString("size",modelList.get(position).getSize());
//                    args.putString("color",modelList.get(position).getColor());
//                }else{
//                    args.putString("product_name",modelList.get(position).getProduct_name());
//                    args.putString("description",modelList.get(position).getProduct_description());
//                    args.putString("size",modelList.get(position).getSize());
//                    args.putString("color",modelList.get(position).getColor());
//                }
//                args.putString("product_id",modelList.get(position).getProduct_id());
//                args.putString("category_id",modelList.get(position).getCategory_id());
//                args.putString("deal_price",modelList.get(position).getDeal_price());
//                args.putString("start_date",modelList.get(position).getStart_date());
//                args.putString("start_time",modelList.get(position).getStart_time());
//                args.putString("end_date",modelList.get(position).getEnd_date());
//                args.putString("end_time",modelList.get(position).getEnd_time());
//                args.putString("price",modelList.get(position).getPrice());
//                args.putString("image",modelList.get(position).getProduct_image());
//                args.putString("status",modelList.get(position).getStatus());
//                args.putString("stock",modelList.get(position).getStock());
//                args.putString("unit_value",modelList.get(position).getUnit_value());
//                args.putString("unit",modelList.get(position).getUnit());
//                args.putString("increment",modelList.get(position).getIncreament());
//                args.putString("rewards",modelList.get(position).getRewards());
//                args.putString("stock",modelList.get(position).getStock());
//                args.putString("title",modelList.get(position).getTitle());
//                args.putString("size",modelList.get(position).getSize());
//                args.putString("store_id",modelList.get(position).getStoreid());
//                args.putString("qty","0");
//
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();


     //Move To Activity ...................................................
//                Intent intent=new Intent(context, ProductActivity.class);
//                if(language.contains("english")){
//                    intent.putExtra("product_name",modelList.get(position).getProduct_name());//TODO
//                    intent.putExtra("size",modelList.get(position).getSize());//TODO
//                    intent.putExtra("color",modelList.get(position).getColor());//TODO
//                    intent.putExtra("description",modelList.get(position).getProduct_description());//TODO
//                }else{
//                    intent.putExtra("product_name",modelList.get(position).getProduct_name_arb());//TODO
//                    intent.putExtra("size",modelList.get(position).getSize());//TODO
//                    intent.putExtra("color",modelList.get(position).getColor());//TODO
//                    intent.putExtra("description",modelList.get(position).getProduct_description_arb());
//                }
//                intent.putExtra("product_id",modelList.get(position).getProduct_id());
//                intent.putExtra("size",modelList.get(position).getSize());//TODO
//                intent.putExtra("color",modelList.get(position).getColor());//TODO
//                intent.putExtra("category_id",modelList.get(position).getCategory_id());
//                intent.putExtra("deal_price",modelList.get(position).getDeal_price());
//                intent.putExtra("start_date",modelList.get(position).getStart_date());
//                intent.putExtra("start_time",modelList.get(position).getStart_time());
//                intent.putExtra("end_date",modelList.get(position).getEnd_date());
//                intent.putExtra("end_time",modelList.get(position).getEnd_time());
//                intent.putExtra("price",modelList.get(position).getPrice());
//                intent.putExtra("image",modelList.get(position).getProduct_image());
//                intent.putExtra("status",modelList.get(position).getStatus());
//                intent.putExtra("stock",modelList.get(position).getStock());
//                intent.putExtra("unit_value",modelList.get(position).getUnit_value());
//                intent.putExtra("unit",modelList.get(position).getUnit());
//                intent.putExtra("increment",modelList.get(position).getStoreid());
//                intent.putExtra("rewards",modelList.get(position).getRewards());
//                intent.putExtra("stock",modelList.get(position).getStock());
//                intent.putExtra("title",modelList.get(position).getTitle());
//                intent.putExtra("qty",tv_contetiy.getText().toString());
//                intent.putExtra("store_id",modelList.get(position).getStoreid());
//                context.startActivity(intent);

//            }

        }
    }

    public Product_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);
        context = parent.getContext();
        return new Product_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Product_adapter.MyViewHolder holder, int position) {
        Product_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.tv_title.setText(mList.getProduct_name());
        }
        else {
            holder.tv_title.setText(mList.getProduct_name_arb());
        }
        holder.tv_reward.setText(mList.getRewards());

        if (mList.getPrice().contains("|")){
            String currentString = mList.getPrice();
            String[] separated = currentString.split("\\|");
            holder.tv_price.setText(separated[0]+" "+context.getResources().getString(R.string.currency));
        }
        else {
            holder.tv_price.setText(mList.getPrice()+" "+context.getResources().getString(R.string.currency));
        }

        String stockk=modelList.get(position).getStock();
                if(stockk.equals("")){
                    stockk="0";
                }

//                if (Integer.valueOf(stockk)<=0){
//            holder.tv_add.setText(R.string.tv_out);
//            holder.tv_add.setTextColor(context.getResources().getColor(R.color.black));
//            holder.tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
//            holder.tv_add.setEnabled(false);
//            holder.iv_minus.setEnabled(false);
//            holder.iv_plus.setEnabled(false);
//        }

//        else  if (dbcart.isInCart(mList.getProduct_id())) {
//            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));
//        } else {
//            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//        }

//        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
//        Double reward = Double.parseDouble(mList.getRewards());
//        holder.tv_reward.setText("" + reward * items);

//        if (mList.getPrice().contains("|")){
//            String currentString = mList.getPrice();
//            String[] separated = currentString.split("\\|");
//            holder.tv_total.setText(separated[0]);
//        }
//        else {
//            Double price = Double.parseDouble(mList.getPrice());
//            holder.tv_total.setText("" + price * items);
//        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_image_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }




    ////////////////////////
    private void showProductDetail(String image, String title, String size, String color, String description, String detail, final int position, String qty
    , final String productid) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_detail);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        final TextView  tv_price, tv_reward, tv_total;
        ImageView iv_logo, iv_remove;

        RatingBar ratingBar;

        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        final ImageView iv_fav_image = (ImageView) dialog.findViewById(R.id.fav_product);
        final ImageView iv_minus = (ImageView) dialog.findViewById(R.id.iv_subcat_minus);
        final ImageView iv_plus = (ImageView) dialog.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);
//        TextView tv_size = (TextView) dialog.findViewById(R.id.size);
//        TextView tv_color = (TextView) dialog.findViewById(R.id.color);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) dialog.findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) dialog.findViewById(R.id.tv_subcat_add);
        ratingBar=dialog.findViewById(R.id.ratingbarprod);


        Double price = Double.parseDouble(modelList.get(position).getPrice());
        Double reward = Double.parseDouble(modelList.get(position).getRewards());
        Double items = Double.parseDouble(dbcart.getInCartItemQty(modelList.get(position).getProduct_id()));


        tv_price = (TextView) dialog.findViewById(R.id.tv_subcat_price);
        tv_reward = (TextView) dialog.findViewById(R.id.tv_reward_point);
        tv_total = (TextView) dialog.findViewById(R.id.tv_subcat_total);
        iv_logo = (ImageView) dialog.findViewById(R.id.iv_subcat_img);
        iv_remove = (ImageView) dialog.findViewById(R.id.iv_subcat_remove);

        tv_title.setText(title);
//        tv_size.setText(size);
//        tv_color.setText(color);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(description);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .crossFade()
                .into(iv_image);
       tv_reward.setText(modelList.get(position).getRewards());
       tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + modelList.get(position).getUnit_value() + " " +
              modelList.get(position).getUnit() + modelList.get(position).getPrice()+ context.getResources().getString(R.string.currency));
        if (Integer.valueOf(modelList.get(position).getStock())<=0){
           tv_add.setText(R.string.tv_out);
           tv_add.setTextColor(context.getResources().getColor(R.color.black));
           tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
           tv_add.setEnabled(false);
           iv_minus.setEnabled(false);
           iv_plus.setEnabled(false);
        }

        else  if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
           tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_total.setText("" + price * items);
        tv_reward.setText("" + reward * items);
        if (Integer.valueOf(modelList.get(position).getStock())<=0){
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(context.getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        }

        else if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language=preferences.getString("language","");

                    map.put("product_id", modelList.get(position).getProduct_id());
                    map.put("product_name", modelList.get(position).getProduct_name());
//                    map.put("size", modelList.get(position).getSize());
//                    map.put("color", modelList.get(position).getColor());
                    map.put("category_id", modelList.get(position).getCategory_id());
                    map.put("product_description", modelList.get(position).getProduct_description());
                    map.put("deal_price", modelList.get(position).getDeal_price());
                    map.put("start_date", modelList.get(position).getStart_date());
                    map.put("start_time", modelList.get(position).getStart_time());
                    map.put("end_date", modelList.get(position).getEnd_date());
                    map.put("end_time", modelList.get(position).getEnd_time());
                    map.put("price", modelList.get(position).getPrice());
                    map.put("product_image", modelList.get(position).getProduct_image());
                    map.put("status", modelList.get(position).getStatus());
                    map.put("in_stock", modelList.get(position).getIn_stock());
                    //map.put("unit_value", modelList.get(position).getUnit_value());
                    map.put("unit", modelList.get(position).getUnit());
                    //map.put("increament", modelList.get(position).getStoreid());
                    map.put("rewards", modelList.get(position).getRewards());
                    map.put("stock", modelList.get(position).getStock());
                    map.put("title", modelList.get(position).getTitle());
//                    map.put("store_id",modelList.get(position).getStoreid());



                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));
                tv_total.setText("" + price * items);
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                notifyItemChanged(position);

            }
        });

        saverating(productid,ratingBar);
        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });
        sharedPreferences=context.getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

        usrid=sharedPreferences.getString(BaseURL.KEY_ID,"0");
        checkfavourate(usrid,productid,iv_fav_image);

        iv_fav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(favcheckk){
                    removefromfav(usrid,productid,iv_fav_image);
                }else{
                    addinfav(usrid,productid,iv_fav_image);
                }
//                Toast.makeText(context, "abc", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void removefromfav(String userid, String productid, final ImageView imageView){
        final SweetAlertDialog loading=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Removing From Favourate");
        loading.setCancelable(false);
        loading.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("prod_id",productid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.removefavourate, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    loading.dismiss();
//                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("response");
                        if (status) {

                            loading.dismiss();
                            favcheckk=false;
                            imageView.setImageResource(R.drawable.heartnf);

                        }
//                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    public void addinfav(String userid, final String productid, final ImageView imageView){
        final SweetAlertDialog loading=new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Adding in Favourate");
        loading.setCancelable(false);
        loading.show();


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("prod_id",productid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.addfavourate, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    loading.dismiss();
//                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("response");
                        if (status) {
                            favcheckk=true;
//                            Toast.makeText(context, "ADD", Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(R.drawable.heartf);

                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();

                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    public void checkfavourate(String userid, final String productid, final ImageView fav){
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
                            JSONArray array=response.getJSONArray("data");
                            if(array.length()==0){
                                fav.setVisibility(View.VISIBLE);
                                fav.setImageResource(R.drawable.heartnf);
                                favcheckk=false;
                                return;
                            }
                           for (int i =0 ;i<array.length();i++){
                               JSONObject object=array.getJSONObject(i);
                               if(productid.equals(object.getString("product_id"))){
                                   fav.setVisibility(View.VISIBLE);
                                   fav.setImageResource(R.drawable.heartf);
                                   favcheckk=true;
                                   return;
                               }else{
                                   fav.setVisibility(View.VISIBLE);
                                   fav.setImageResource(R.drawable.heartnf);
                                    favcheckk=false;
                               }
                           }
                        }else{
                            fav.setVisibility(View.VISIBLE);
                            fav.setImageResource(R.drawable.heartnf);
                            favcheckk=false;
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
                    Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void saverating(String productid, final RatingBar ratingBar) {

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("prod_id", productid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getrating, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("response");
                    if (status) {

                        JSONArray array=response.getJSONArray("data");
                        JSONObject object=array.getJSONObject(0);
                        String rating=object.getString("rating");
                        if(rating.equals("null")){
                            ratingBar.setVisibility(View.GONE);
                        }else
                        {
                            ratingBar.setVisibility(View.VISIBLE);
                            ratingBar.setRating(Float.valueOf(rating));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}