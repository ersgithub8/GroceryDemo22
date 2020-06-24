package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.networkconnectivity.RatingAndReviews;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;


public class ProductActivity extends AppCompatActivity {
    boolean favcheckk;
    SharedPreferences sharedPreferences,preferences;
    String usrid,language,detail,qty;
    RelativeLayout ratingrl;
//            ,productid,product_name,category_id,dealprice,startdate,starttime,endtime,enddate,price,status,instock,
//            ,title
//            ,description
//            ,detail
//            ,qty
//            ,image;

    String productid;
    String product_name;
    String category_id;
    String product_description;
    String deal_price;
    String start_date;
    String start_time;
    String end_date;
    String end_time;
    String pricee;
    String product_image;
    String product_name_arb;
    String product_description_arb;
    String status;
    String in_stock;
    String unit_value;
    String unit;
    String increament;
    String rewards;
    String stock;
    String title;

    private DatabaseHandler dbcart;
    ImageView iv_image,iv_fav_image,iv_minus,iv_plus,rateall;
   TextView tv_price, tv_reward, tv_total,tv_title,tv_detail,tv_contetiy,tv_add;
   ImageView iv_logo, iv_remove;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);





        iv_image = (ImageView) findViewById(R.id.iv_product_detail_img);
        iv_fav_image = (ImageView)findViewById(R.id.fav_product);
        iv_minus = (ImageView) findViewById(R.id.iv_subcat_minus);
        iv_plus = (ImageView) findViewById(R.id.iv_subcat_plus);
        tv_title = (TextView) findViewById(R.id.tv_product_detail_title);
        tv_detail = (TextView) findViewById(R.id.tv_product_detail);
        tv_contetiy = (TextView) findViewById(R.id.tv_subcat_contetiy);
        tv_add = (TextView) findViewById(R.id.tv_subcat_add);
        rateall=findViewById(R.id.allrate);
        tv_price = (TextView) findViewById(R.id.tv_subcat_price);
        tv_reward = (TextView) findViewById(R.id.tv_reward_point);
        tv_total = (TextView) findViewById(R.id.tv_subcat_total);
        iv_logo = (ImageView) findViewById(R.id.iv_subcat_img);
        iv_remove = (ImageView)findViewById(R.id.iv_subcat_remove);


        ratingBar=findViewById(R.id.ratingbarprod);
        ratingrl=findViewById(R.id.rl1);



        dbcart=new DatabaseHandler(this);


        productid=getIntent().getStringExtra("product_id");
        product_name=getIntent().getStringExtra("product_name");
        category_id=getIntent().getStringExtra("category_id");
        product_description=getIntent().getStringExtra("description");
        deal_price=getIntent().getStringExtra("deal_price");
        start_date=getIntent().getStringExtra("start_date");
        start_time=getIntent().getStringExtra("start_time");
        end_date=getIntent().getStringExtra("end_date");
        end_time=getIntent().getStringExtra("end_time");
        pricee=getIntent().getStringExtra("price");
        product_image=getIntent().getStringExtra("image");
//        product_name_arb=;
//        product_description_arb=;
        status=getIntent().getStringExtra("status");
        in_stock=getIntent().getStringExtra("stock");
        unit_value=getIntent().getStringExtra("unit_value");
        unit=getIntent().getStringExtra("unit");
        increament=getIntent().getStringExtra("increment");
        rewards=getIntent().getStringExtra("rewards");
        stock=getIntent().getStringExtra("stock");
        title=getIntent().getStringExtra("title");


        qty=getIntent().getStringExtra("qty");


        rateall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ProductActivity.this, RatingAndReviews.class);
                intent.putExtra("prod_id",productid);
                startActivity(intent);
            }
        });
        Double price = Double.parseDouble(pricee);
        Double reward = Double.parseDouble(rewards);
        Double items = Double.parseDouble(dbcart.getInCartItemQty(productid));

        tv_title.setText(title);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(product_description);

        Glide.with(this)
                .load(BaseURL.IMG_PRODUCT_URL + product_image)
                .centerCrop()
                .crossFade()
                .into(iv_image);
        tv_reward.setText(rewards);
        tv_price.setText(getResources().getString(R.string.tv_pro_price) + unit_value + " " +
                unit + pricee+getResources().getString(R.string.currency));
        if (Integer.valueOf(stock)<=0){
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        }

        else  if (dbcart.isInCart(productid)) {
            tv_add.setText(getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(productid));
        } else {
            tv_add.setText(getResources().getString(R.string.tv_pro_add));
        }

        tv_total.setText("" + price * items);
        tv_reward.setText("" + reward * items);
        if (Integer.valueOf(stock)<=0){
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        }

        else if (dbcart.isInCart(productid)) {
            tv_add.setText(getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(productid));
        } else {
            tv_add.setText(getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                preferences = getSharedPreferences("lan", MODE_PRIVATE);
                language=preferences.getString("language","");

                map.put("product_id", productid);
                map.put("product_name", product_name);
                map.put("category_id", category_id);
                map.put("product_description", product_description);
                map.put("deal_price", deal_price);
                map.put("start_date", start_date);
                map.put("start_time", start_time);
                map.put("end_date", end_date);
                map.put("end_time", end_time);
                map.put("price", pricee);
                map.put("product_image", product_image);
                map.put("status", status);
                map.put("in_stock", in_stock);
                map.put("unit_value", unit_value);
                map.put("unit", unit);
                map.put("increament", increament);
                map.put("rewards", rewards);
                map.put("stock", stock);
                map.put("title", title);


                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));
                tv_total.setText("" + price * items);
//                ((MainActivity) Activity).setCartCounter("" + dbcart.getCartCount());

//                notifyItemChanged(position);

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
        sharedPreferences=getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

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
        final SweetAlertDialog loading=new SweetAlertDialog(ProductActivity.this,SweetAlertDialog.PROGRESS_TYPE)
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
                    Toast.makeText(ProductActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);



    }
    public void addinfav(String userid, final String productid, final ImageView imageView){
        final SweetAlertDialog loading=new SweetAlertDialog(ProductActivity.this,SweetAlertDialog.PROGRESS_TYPE)
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
                    Toast.makeText(ProductActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
                            ratingrl.setVisibility(View.GONE);
                            ratingBar.setVisibility(View.GONE);
                        }else
                        {
                            ratingrl.setVisibility(View.VISIBLE);
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
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}