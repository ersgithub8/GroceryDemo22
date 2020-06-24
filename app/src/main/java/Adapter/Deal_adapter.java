package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
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

    private List<Product_model> modelList;
    private Context context;
    private DatabaseHandler dbcart;
    String language;

    SharedPreferences sharedPreferences;
    String usrid;
    boolean favcheckk=false;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_reward, tv_total, tv_contetiy, tv_add;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public Double reward;

        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
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
                map.put("store_id", modelList.get(position).getStoreid());


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

            } else if (id == R.id.iv_subcat_img) {
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language=preferences.getString("language","");
                Log.d("lang",language);
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

                Intent intent=new Intent(context, ProductActivity.class);
                if(language.contains("english")){

                    intent.putExtra("product_name",modelList.get(position).getProduct_name());//TODO
                    intent.putExtra("description",modelList.get(position).getProduct_description());//TODO
                }else{
                    intent.putExtra("product_name",modelList.get(position).getProduct_name_arb());//TODO
                    intent.putExtra("description",modelList.get(position).getProduct_description_arb());

                }
                intent.putExtra("product_id",modelList.get(position).getProduct_id());
                intent.putExtra("category_id",modelList.get(position).getCategory_id());
                intent.putExtra("deal_price",modelList.get(position).getDeal_price());
                intent.putExtra("start_date",modelList.get(position).getStart_date());
                intent.putExtra("start_time",modelList.get(position).getStart_time());
                intent.putExtra("end_date",modelList.get(position).getEnd_date());
                intent.putExtra("end_time",modelList.get(position).getEnd_time());
                intent.putExtra("price",modelList.get(position).getPrice());
                intent.putExtra("image",modelList.get(position).getProduct_image());
                intent.putExtra("status",modelList.get(position).getStatus());
                intent.putExtra("stock",modelList.get(position).getStock());
                intent.putExtra("unit_value",modelList.get(position).getUnit_value());
                intent.putExtra("unit",modelList.get(position).getUnit());
                intent.putExtra("increment",modelList.get(position).getStoreid());
                intent.putExtra("rewards",modelList.get(position).getRewards());
                intent.putExtra("stock",modelList.get(position).getStock());
                intent.putExtra("title",modelList.get(position).getTitle());
                intent.putExtra("qty",tv_contetiy.getText().toString());

                context.startActivity(intent);
            }

        }
    }

    public Deal_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public Deal_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);
        context = parent.getContext();
        return new Deal_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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
        holder.tv_price.setText(
//                context.getResources().getString(R.string.tv_pro_price) + mList.getUnit_value() + " " +
//                mList.getUnit() +
                mList.getPrice()+ context.getResources().getString(R.string.currency));
        if (Integer.valueOf(modelList.get(position).getStock())<=0){
            holder.tv_add.setText(R.string.tv_out);
            holder.tv_add.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.tv_add.setEnabled(false);
            holder.iv_minus.setEnabled(false);
            holder.iv_plus.setEnabled(false);
        }

        else  if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));
        } else {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }
        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
        Double price = Double.parseDouble(mList.getPrice());
        Double reward = Double.parseDouble(mList.getRewards());
        holder.tv_total.setText("" + price * items);
        holder.tv_reward.setText("" + reward * items);

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