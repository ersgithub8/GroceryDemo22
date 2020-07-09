package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

import Adapter.RatingAdapter;
import Config.BaseURL;
import Model.Rating_Model;
import util.CustomVolleyJsonRequest;

import static gogrocer.tcc.AppController.TAG;

public class RatingAndReviews extends AppCompatActivity {


    RatingAdapter ratingAdapter;
    List<Rating_Model> rating_models=new ArrayList<>();
    RecyclerView recyclerView;
    String productid;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_and_reviews);
    recyclerView=findViewById(R.id.recyclerreview);

    back=findViewById(R.id.back);


    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    });
    productid=getIntent().getStringExtra("prod_id");

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    getReviews(productid);
    }



    public void getReviews( String prod_id){
        String tag_json_obj = "json_category_req";

//        Toast.makeText(getActivity(), cat_id+"", Toast.LENGTH_SHORT).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("prod_id", prod_id);

       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getallrating, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("response");
                        if (status) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Rating_Model>>() {
                            }.getType();
                            rating_models = gson.fromJson(response.getString("data"), listType);
                            ratingAdapter = new RatingAdapter(rating_models,RatingAndReviews.this);
                            recyclerView.setAdapter(ratingAdapter);
                            ratingAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RatingAndReviews.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}