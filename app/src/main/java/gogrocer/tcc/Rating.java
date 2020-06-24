package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import cn.pedant.SweetAlert.SweetAlertDialog;
import util.CustomVolleyJsonRequest;

public class Rating extends AppCompatActivity {

    ImageView back;
    RatingBar ratingBar;
    TextView ratingtext;
    Button submit;
    EditText description;
    String productid,userid,status;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
    back=findViewById(R.id.back);
    ratingBar=findViewById(R.id.ratingbar);
    ratingtext=findViewById(R.id.rattv);
    submit=findViewById(R.id.submit);
    description=findViewById(R.id.ratdescription);

        sharedPreferences=getSharedPreferences(BaseURL.PREFS_NAME,MODE_PRIVATE);

        userid=sharedPreferences.getString(BaseURL.KEY_ID,"0");

        status = getIntent().getStringExtra("status");

        productid=getIntent().getStringExtra("prod_id");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 1:
                        ratingtext.setText("hate it");
                        break;
                    case 2:
                        ratingtext.setText("dislike it");

                    case 3:
                        ratingtext.setText("Good");
                        break;
                    case 4:
                        ratingtext.setText("like it");
                        break;
                    case 5:
                        ratingtext.setText("Love it");
                        break;

                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating()==0){
                    Toast.makeText(Rating.this, "Rate Product First", Toast.LENGTH_SHORT).show();
                }else{

                    if (status == null)
                    {

                    if(description.getText().toString().isEmpty()){
                        saverating(productid,userid,ratingBar.getRating(),"");
                    }else{
                        saverating(productid,userid,ratingBar.getRating(),description.getText().toString());
                    }

                }

                    //if store
                    else if (status.equals("store")){
                        if(description.getText().toString().isEmpty()){
                            savestorerating(getIntent().getStringExtra("store_id"),ratingBar.getRating(),"");
                        }else{
                            savestorerating(getIntent().getStringExtra("store_id"),ratingBar.getRating(),description.getText().toString());
                        }

                    }
                }
            }
        });

    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    });
    }

    private void savestorerating(String id,Float rating,String description){

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", id);
        params.put("star", String.valueOf(rating));
        //params.put("comment", description);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.SAVE_STORE_RATING, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("response");
                    if (status) {

                        String data=response.getString("data");
                        SweetAlertDialog alertDialog=new SweetAlertDialog(Rating.this,SweetAlertDialog.SUCCESS_TYPE).
                                setConfirmButtonBackgroundColor(Color.GREEN).
                                setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onBackPressed();
                                    }
                                });
                        alertDialog.setCancelable(false);
                        alertDialog.setTitleText(data);
                        alertDialog.show();
                    }else{
                        String data=response.getString("data");
                        SweetAlertDialog alertDialog=new SweetAlertDialog(Rating.this,SweetAlertDialog.ERROR_TYPE).
                                setConfirmButtonBackgroundColor(Color.RED).
                                setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onBackPressed();
                                    }
                                });
                        alertDialog.setCancelable(false);
                        alertDialog.setTitleText(data);
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Rating.this, "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Rating.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void saverating(String productid,String userid,Float rating,String description) {

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("prod_id", productid);
        params.put("star", String.valueOf(rating));
        params.put("comment", description);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.saverating, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String data=response.getString("data");
                        SweetAlertDialog alertDialog=new SweetAlertDialog(Rating.this,SweetAlertDialog.SUCCESS_TYPE).
                                setConfirmButtonBackgroundColor(Color.GREEN).
                                setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            onBackPressed();
                                    }
                                });
                        alertDialog.setCancelable(false);
                        alertDialog.setTitleText(data);
                        alertDialog.show();
                    }else{
                        String data=response.getString("data");
                        SweetAlertDialog alertDialog=new SweetAlertDialog(Rating.this,SweetAlertDialog.ERROR_TYPE).
                                setConfirmButtonBackgroundColor(Color.RED).
                                setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onBackPressed();
                                    }
                                });
                        alertDialog.setCancelable(false);
                        alertDialog.setTitleText(data);
                        alertDialog.show();
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
                    Toast.makeText(Rating.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}