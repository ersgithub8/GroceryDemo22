 package Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Adapter.ColorListAdapter;
import Adapter.SizeListAdapter;
import Adapter.pro_detail_interface;
import Config.BaseURL;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.AppController;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import gogrocer.tcc.RatingAndReviews;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;

import static android.content.Context.MODE_PRIVATE;

public class ProductDetailShow extends Fragment implements pro_detail_interface {
    boolean favcheckk;
    SharedPreferences sharedPreferences,preferences;
    String usrid,language,detail,qty;
    RelativeLayout ratingrl,Go_Cart;

    String store_id;
    String productid;
    String product_name;
    String size;
    String color;
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

    public LinearLayout bot_store,bot_cat,bot_fav,bot_profile;
    public RelativeLayout bot_cart,relative_size_color;

    RecyclerView colorrecycle,sizerecycle;
    private DatabaseHandler dbcart;
    ImageView iv_image,iv_fav_image,iv_minus,iv_plus,rateall;
    TextView tv_price, tv_reward, tv_total,tv_title,tv_detail,tv_contetiy,tv_add,tv_size,tv_color;
    ImageView iv_logo, iv_remove,iv_share;
    RatingBar ratingBar;

    ArrayList<String> priceList = new ArrayList<>();
    ArrayList<String> unitvalueList = new ArrayList<>();
    ArrayList<String> sizeList = new ArrayList<>();
    ArrayList<String> colorList = new ArrayList<>();
    public ProductDetailShow() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_product, container, false);
        sharedPreferences = getActivity().getSharedPreferences("lan", Context.MODE_PRIVATE);
        language = sharedPreferences.getString("language", "");

        iv_image = (ImageView) view.findViewById(R.id.iv_product_detail_img);
        iv_fav_image = (ImageView) view.findViewById(R.id.fav_product);
        iv_share = (ImageView) view.findViewById(R.id.fav_share);
        iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
        iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
        tv_title = (TextView) view.findViewById(R.id.tv_product_detail_title);
//        tv_size = (TextView) view.findViewById(R.id.size);
//        tv_color = (TextView) view.findViewById(R.id.color);
        tv_detail = (TextView) view.findViewById(R.id.tv_product_detail);
        tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
        tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
        rateall = view.findViewById(R.id.allrate);
        tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
        tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
        tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
        iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
        iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
        colorrecycle = (RecyclerView) view.findViewById(R.id.colorrecycler);
        sizerecycle = (RecyclerView) view.findViewById(R.id.sizerecyler);

        relative_size_color = (RelativeLayout) view.findViewById(R.id.rl6);
        Go_Cart = (RelativeLayout) view.findViewById(R.id.btn_cart);

        ratingBar = view.findViewById(R.id.ratingbarprod);
        ratingrl = view.findViewById(R.id.rl1);

//        bot_cat = view.findViewById(R.id.bot_categories);
//        bot_fav = view.findViewById(R.id.bot_fav);
//        bot_cart = (RelativeLayout) view.findViewById(R.id.bot_cart);
//        bot_fav = view.findViewById(R.id.bot_fav);
//        bot_store = view.findViewById(R.id.bot_store);
//        bot_profile = view.findViewById(R.id.bot_profile);

        //appbarmenu();

        dbcart = new DatabaseHandler(getActivity());

        productid = getArguments().getString("product_id");
        product_name = getArguments().getString("product_name");
        category_id = getArguments().getString("category_id");

        product_description = getArguments().getString("description");
        deal_price = getArguments().getString("deal_price");
        start_date = getArguments().getString("start_date");
        start_time = getArguments().getString("start_time");
        end_date = getArguments().getString("end_date");
        end_time = getArguments().getString("end_time");
        pricee = getArguments().getString("price");
        product_image = getArguments().getString("image");
        status = getArguments().getString("status");
        in_stock = getArguments().getString("stock");
        unit_value = getArguments().getString("unit_value");
        unit = getArguments().getString("unit");
        increament = getArguments().getString("increment");
        rewards = getArguments().getString("rewards");
        stock = getArguments().getString("stock");
        title = getArguments().getString("title");
        qty = getArguments().getString("qty");
        store_id = getArguments().getString("store_id");

        size = getArguments().getString("size");
        color = getArguments().getString("color");

        String[] unitArray = unit_value.split("\\|");
        unitvalueList = new ArrayList<>(Arrays.asList(unitArray));

        Double price;
        if (pricee.contains("|")){
            String currentString = pricee;
            String[] separated = currentString.split("\\|");
            priceList = new ArrayList<>(Arrays.asList(separated));
            price = Double.parseDouble(separated[0]);
        }
        else {
            price = Double.parseDouble(pricee);
            priceList.add(pricee);
        }
        tv_price.setText(price+" " + getResources().getString(R.string.currency));


        if (color == null){
            colorrecycle.setVisibility(View.GONE);
            sizerecycle.setVisibility(View.GONE);
        } else {
            colorrecycle.setVisibility(View.VISIBLE);
            String[] colorArray = color.split("\\|");
            colorList = new ArrayList<>(Arrays.asList(colorArray));
            setColorListData(colorList,pricee);
        }

        //Toast.makeText(getActivity(), size+"-"+color, Toast.LENGTH_SHORT).show();
//        if (size == null)
//        {
//            tv_size.setText(R.string.notavailabe);
//        }
//        else {
//            tv_size.setText(size);
//        }
//        if (color == null){
//            tv_color.setText(R.string.notavailabe);
//        }
//        else {
//            tv_color.setText(color);
//        }
//
//        if (size == null && color == null){
//            relative_size_color.setVisibility(View.GONE);
//        }


        rateall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), RatingAndReviews.class);
                intent.putExtra("prod_id", productid);
                startActivity(intent);
            }
        });


        //Double price = Double.parseDouble(pricee);
        Double reward = Double.parseDouble(rewards);
        Double items = Double.parseDouble(dbcart.getInCartItemQty(productid));

        tv_title.setText(product_name);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(product_description);

        Glide.with(getActivity())
                .load(BaseURL.IMG_PRODUCT_URL + product_image)
                .centerCrop()
                .crossFade()
                .into(iv_image);
        tv_reward.setText(rewards);

        if (stock.equals("")) {
            stock = "0";
        }
        if (Integer.valueOf(stock) <= 0) {
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        } else if (dbcart.isInCart(productid)) {
            tv_add.setText(getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(productid));
        } else {
            tv_add.setText(getResources().getString(R.string.tv_pro_add));
        }

        tv_total.setText("" + price * items);
        tv_reward.setText("" + reward * items);
        if (Integer.valueOf(stock) <= 0) {
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        } else if (dbcart.isInCart(productid)) {
            tv_add.setText(getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(productid));
        } else {
            tv_add.setText(getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ColorListAdapter.pos == -1) {
                    Toast.makeText(getActivity(), "Color Entered", Toast.LENGTH_SHORT).show();
                } else {

                    if (size.isEmpty()){
                        addtocart(colorList.get(ColorListAdapter.pos),"");
                    }
                    else {
                        if (SizeListAdapter.sizepos == -1){
                            Toast.makeText(getActivity(), "Size Entered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            addtocart(colorList.get(ColorListAdapter.pos),SizeListAdapter.sizevalue);
                        }
                    }

                }
            }

            private void addtocart(String scolor,String ssize) {
                HashMap<String, String> map = new HashMap<>();
                preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");

                pricee = priceList.get(ColorListAdapter.pos);
                unit_value = unitvalueList.get(ColorListAdapter.pos);

                map.put("product_id", productid);
                map.put("product_name", product_name);
                map.put("size", ssize);
                map.put("color", scolor);
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
                map.put("store_id", store_id);

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
                ((MainActivity) getActivity()).setCartCounter("" + dbcart.getCartCount());

            }
        });

        saverating(productid, ratingBar);
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
        sharedPreferences = getActivity().getSharedPreferences(BaseURL.PREFS_NAME, MODE_PRIVATE);

        usrid = sharedPreferences.getString(BaseURL.KEY_ID, "0");
        checkfavourate(usrid, productid, iv_fav_image);

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Check out this Product '" + product_name + "' in App at: " + BaseURL.Share_URL);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

//        Go_Cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(),MainActivity.class);
//                intent.putExtra("jaffery","laddan");
//                startActivity(intent);
//
//            }
//        });

        iv_fav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (favcheckk) {
                    removefromfav(usrid, productid, iv_fav_image);
                } else {
                    addinfav(usrid, productid, iv_fav_image);
                }
//                Toast.makeText(context, "abc", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



    public void removefromfav(String userid, String productid, final ImageView imageView){
        final SweetAlertDialog loading=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
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
                        imageView.setImageResource(R.drawable.heartnfw);

                    }
//                    }
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);



    }
    public void addinfav(String userid, final String productid, final ImageView imageView){
        final SweetAlertDialog loading=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
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
                    Toast.makeText(getActivity(), e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();

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
                                fav.setImageResource(R.drawable.heartnfw);
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
                                    fav.setImageResource(R.drawable.heartnfw);
                                    favcheckk=false;
                                }
                            }
                        }else{
                            fav.setVisibility(View.VISIBLE);
                            fav.setImageResource(R.drawable.heartnfw);
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(getActivity(), "Rating gone", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void appbarmenu() {
        bot_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bot_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        bot_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bot_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bot_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onclick(int position) {

        Toast.makeText(getActivity(), ""+size, Toast.LENGTH_SHORT).show();
        if (size.isEmpty()){
         sizerecycle.setVisibility(View.GONE);
        }
        else {
            String[] sizeArray = size.split("\\|");
            sizeList = new ArrayList<>(Arrays.asList(sizeArray));
            String tempy = sizeList.get(position);
            setSizeListData(tempy);
        }

    }

    private void setSizeListData(String tempy) {
        sizeList.clear();
        try {
            String[] sizeArray = tempy.split(",");
            sizeList = new ArrayList<>(Arrays.asList(sizeArray));
            sizerecycle.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
            sizerecycle.setLayoutManager(linearLayoutManager);
            SizeListAdapter topListAdapter = new SizeListAdapter(getActivity(), sizeList);
            sizerecycle.setAdapter(topListAdapter);
        }
        catch (Exception ignored){
        }

    }
    private void setColorListData(ArrayList<String> listofcolor, String prize) {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        colorrecycle.setLayoutManager(linearLayoutManager);
        ColorListAdapter topListAdapter = new ColorListAdapter(getActivity(), listofcolor,prize,tv_price,ProductDetailShow.this);
        colorrecycle.setAdapter(topListAdapter);
    }

}