package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import Model.Deal_Model;
import Model.Product_model;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.ProductActivity;
import gogrocer.tcc.R;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;

import static android.content.Context.MODE_PRIVATE;

public class Deal_adapter extends RecyclerView.Adapter<Deal_adapter.MyViewHolder> {

    private List<Deal_Model> modelList;
    private Context context;

    SharedPreferences preferences;
    private DatabaseHandler dbcart;
    String languagee;

    SharedPreferences sharedPreferences;
    String usrid;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView product_nmae, product_prize, offer_product_prize, start_time, end_time, offer_textview;
        public ImageView image;
        LinearLayout showproduct;

        public MyViewHolder(View view) {
            super(view);

            product_nmae = (TextView) view.findViewById(R.id.product_name);
            product_prize = (TextView) view.findViewById(R.id.product_prize);
            offer_product_prize = (TextView) view.findViewById(R.id.offer_product_prize);
            start_time = (TextView) view.findViewById(R.id.start_time);
            end_time = (TextView) view.findViewById(R.id.end_time);
            offer_textview = (TextView) view.findViewById(R.id.ofer_textview);
            showproduct = (LinearLayout) view.findViewById(R.id.showproduct);
            product_prize.setPaintFlags(product_prize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            image = (ImageView) view.findViewById(R.id.iv_icon);
        }

//        @Override
//        public void onClick(View view) {
//
//
//            int id = view.getId();
//            int position = getAdapterPosition();
//            if (id == R.id.iv_subcat_plus) {
//                int qty = Integer.valueOf(tv_contetiy.getText().toString());
//                qty = qty + 1;
//                tv_contetiy.setText(String.valueOf(qty));
//
//            } else if (id == R.id.iv_subcat_minus) {
//
//                int qty = 0;
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
//                    qty = Integer.valueOf(tv_contetiy.getText().toString());
//
//                if (qty > 0) {
//                    qty = qty - 1;
//                    tv_contetiy.setText(String.valueOf(qty));
//                }
//
//            } else if (id == R.id.tv_subcat_add) {
//                HashMap<String, String> map = new HashMap<>();
//                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
//                language=preferences.getString("language","");
//
//
//                map.put("product_id", modelList.get(position).getProduct_id());
//                map.put("product_name", modelList.get(position).getProduct_name());
//                map.put("category_id", modelList.get(position).getCategory_id());
//                map.put("product_description", modelList.get(position).getProduct_description());
//                map.put("deal_price", modelList.get(position).getDeal_price());
//                map.put("start_date", modelList.get(position).getStart_date());
//                map.put("start_time", modelList.get(position).getStart_time());
//                map.put("end_date", modelList.get(position).getEnd_date());
//                map.put("end_time", modelList.get(position).getEnd_time());
//                map.put("price", modelList.get(position).getPrice());
//                map.put("product_image", modelList.get(position).getProduct_image());
//                map.put("status", modelList.get(position).getStatus());
//                map.put("in_stock", modelList.get(position).getIn_stock());
//                map.put("unit_value", modelList.get(position).getUnit_value());
//                map.put("unit", modelList.get(position).getUnit());
//                map.put("increament", modelList.get(position).getIncreament());
//                map.put("rewards", modelList.get(position).getRewards());
//                map.put("stock", modelList.get(position).getStock());
//                map.put("title", modelList.get(position).getTitle());
//                map.put("store_id", modelList.get(position).getStore_id());
//
//
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
//                    if (dbcart.isInCart(map.get("product_id"))) {
//                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                    } else {
//                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                    }
//                } else {
//                    dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//                }
//                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
//
//                Double price = Double.parseDouble(map.get("price").trim());
//                Double reward = Double.parseDouble(map.get("rewards"));
//                tv_reward.setText("" + reward * items);
//                tv_total.setText("" + price * items);
//                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

//            } else
//                if (id == R.id.iv_subcat_img) {
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

//                Intent intent=new Intent(context, ProductActivity.class);
//                if(language.contains("english")){
//
//                    intent.putExtra("product_name",modelList.get(position).getProduct_name());//TODO
//                    intent.putExtra("description",modelList.get(position).getProduct_description());//TODO
//                }else{
//                    intent.putExtra("product_name",modelList.get(position).getProduct_name_arb());//TODO
//                    intent.putExtra("description",modelList.get(position).getProduct_description_arb());
//
//                }
//                intent.putExtra("product_id",modelList.get(position).getProduct_id());
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
//                intent.putExtra("increment",modelList.get(position).getStore_id());
//                intent.putExtra("rewards",modelList.get(position).getRewards());
//                intent.putExtra("stock",modelList.get(position).getStock());
//                intent.putExtra("title",modelList.get(position).getTitle());
//                intent.putExtra("qty",dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
//
//                context.startActivity(intent);
//            }
//
        //}


    }
    public Deal_adapter(List<Deal_Model> modelList,Activity activity) {
        this.modelList=modelList;
    }

    @Override
    public Deal_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deal_of_the_day, parent, false);
        context = parent.getContext();
        return new Deal_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Deal_Model mList = modelList.get(position);

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");

        if (mList.getStatus().equals("1")) {

            holder.offer_product_prize.setText(context.getResources().getString(R.string.currency) + mList.getDeal_price());
            holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.green));
            holder.offer_textview.setTextColor(context.getResources().getColor(R.color.green));
//            holder.end_time.setText(mList.getEnd_time());
//            holder.end_time.setTextColor(context.getResources().getColor(R.color.green));

            if (language == null){
                holder.end_time.setText("End Time : ".concat(mList.getEnd_time()));
//                holder.end_time.setTextColor(context.getResources().getColor(R.color.green));
            }
            if (language.contains("english")) {
                holder.end_time.setText("End Time : ".concat(mList.getEnd_time()));
//                holder.end_time.setTextColor(context.getResources().getColor(R.color.green));

            }
            else {
                holder.end_time.setText("وقت النهاية : ".concat(mList.getEnd_time()));
//                holder.end_time.setTextColor(context.getResources().getColor(R.color.green));
            }

        } else if (mList.getStatus().equals("0")) {

            if (language == null){
                holder.offer_product_prize.setText("Expired");
                holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.color_3));
                holder.offer_textview.setTextColor(context.getResources().getColor(R.color.color_3));
                holder.end_time.setText("End Time : Expired");
                holder.end_time.setTextColor(context.getResources().getColor(R.color.color_3));
            }
            if (language.contains("english")) {
                holder.offer_product_prize.setText("Expired");
                holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.color_3));
                holder.offer_textview.setTextColor(context.getResources().getColor(R.color.color_3));
                holder.end_time.setText("End Time : Expired");
                holder.end_time.setTextColor(context.getResources().getColor(R.color.color_3));
            }
            else {
                holder.offer_product_prize.setText("تنقضي");
                holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.color_3));
                holder.offer_textview.setTextColor(context.getResources().getColor(R.color.color_3));
                holder.end_time.setText("وقت النهاية : تنقضي");
                holder.end_time.setTextColor(context.getResources().getColor(R.color.color_3));
            }

        }


        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.product_prize.setText(context.getResources().getString(R.string.currency) + mList.getPrice());
        if (language == null){
            holder.product_nmae.setText(mList.getProduct_name());
            holder.product_prize.setText( context.getResources().getString(R.string.currency) + mList.getPrice());
        }
        if (language.contains("english")) {
            holder.product_nmae.setText(mList.getProduct_name());
            holder.product_prize.setText( context.getResources().getString(R.string.currency) + mList.getPrice());
        }
        else {
            holder.product_nmae.setText(mList.getProduct_name_arb());
            holder.product_prize.setText(context.getResources().getString(R.string.currency) + mList.getPrice());
        }
        holder.start_time.setText(mList.getStart_time());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mList.getStatus().equals("0")){
                    SweetAlertDialog alertDialog=new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE).
                            setTitleText("Offer not Available")
                            .setConfirmButtonBackgroundColor(Color.RED);
                    alertDialog.show();
                    return;
                }
                dbcart=new DatabaseHandler(context);
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                languagee=preferences.getString("language","");
                Log.d("lang",languagee);


                Intent intent=new Intent(context, ProductActivity.class);
                if(languagee.contains("english")){

                    intent.putExtra("product_name",mList.getProduct_name());//TODO
                    intent.putExtra("description",mList.getProduct_description());//TODO
                }else{
                    intent.putExtra("product_name",mList.getProduct_name_arb());//TODO
                    intent.putExtra("description",mList.getProduct_description_arb());

                }

                intent.putExtra("product_id",mList.getProduct_id());
                intent.putExtra("category_id",mList.getCategory_id());
                intent.putExtra("deal_price",mList.getDeal_price());
                intent.putExtra("start_date",mList.getStart_date());
                intent.putExtra("start_time",mList.getStart_time());
                intent.putExtra("end_date",mList.getEnd_date());
                intent.putExtra("end_time",mList.getEnd_time());
                intent.putExtra("price",mList.getPrice());
                intent.putExtra("image",mList.getProduct_image());
                intent.putExtra("status",mList.getStatus());
                intent.putExtra("stock",mList.getStock());
                intent.putExtra("unit_value",mList.getUnit_value());
                intent.putExtra("unit",mList.getUnit());
                intent.putExtra("increment",mList.getStore_id());
                intent.putExtra("rewards",mList.getRewards());
                intent.putExtra("stock",mList.getStock());
                intent.putExtra("title",mList.getTitle());
                intent.putExtra("qty",dbcart.getCartItemQty(mList.getProduct_id()));

                context.startActivity(intent);
            }
        });
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

}