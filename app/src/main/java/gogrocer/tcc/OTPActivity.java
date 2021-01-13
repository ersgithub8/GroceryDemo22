package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.chaos.view.PinView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import Config.SharedPref;
import cn.pedant.SweetAlert.SweetAlertDialog;
import util.CustomVolleyJsonRequest;

public class OTPActivity extends AppCompatActivity {

    private PinView pinView;
    private TextView desc;
    SharedPreferences sharedPreferences;
    String language="";
    private String number,email,password,referalcode,name,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);
        language=sharedPreferences.getString("language","");
        if (language.equals("spanish")){
            setTheme(R.style.AppTheme1);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_otp);


        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        number=getIntent().getStringExtra("phone");
        referalcode=getIntent().getStringExtra("referalcode");

        getOTP(number);


        desc=findViewById(R.id.tvdes);
        pinView=findViewById(R.id.pinview);

        desc.append("\n"+number);


        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                String codee=pinView.getText().toString();
                if(pinView.getText().toString().length()==4){
                    if(codee.equals(code)){
                        makeRegisterRequest(name,number,email,password);
                    }else{
                        final SweetAlertDialog loading=new SweetAlertDialog(OTPActivity.this,SweetAlertDialog.ERROR_TYPE);
                        loading.setTitleText("Invalid Code");
                        loading.show();
                    }
                }
            }
        });




    }


    public void getOTP(String phone){
        final SweetAlertDialog loading=new SweetAlertDialog(OTPActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");
        loading.getProgressHelper().setBarColor(getResources().getColor(R.color.green));
        loading.show();

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.getOTP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        code = response.getString("code");
//                        Toast.makeText(OTPActivity.this, code, Toast.LENGTH_SHORT).show();

                    } else {
                        String error = response.getString("error");
//                        btn_register.setEnabled(true);
                        Toast.makeText(OTPActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {

        final SweetAlertDialog loading=new SweetAlertDialog(OTPActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");

        loading.getProgressHelper().setBarColor(getResources().getColor(R.color.green));

        loading.show();

        String refercode=referalcode;

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);
        if(!refercode.isEmpty()){
            params.put("referal_code",refercode);
        }
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(OTPActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(OTPActivity.this, LoginActivity.class);
                        startActivity(i);


                            finish();

                    } else {
                        String error = response.getString("error");
//                        btn_register.setEnabled(true);
                        Toast.makeText(OTPActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}