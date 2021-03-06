package gogrocer.tcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import Config.BaseURL;
import cn.pedant.SweetAlert.SweetAlertDialog;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getSimpleName();

    private TextInputEditText et_phone, et_name, et_password, et_email,et_refer;
    private RelativeLayout btn_register;
    private TextView tv_phone, tv_name, tv_password, tv_email,lEnglish,lSpanish,Agreed;

    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    String language="";
    //-------------------------------------------------
    SignInButton signInButton;
    LoginButton loginButton;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;


    Boolean f;
    String first_names="",last_names="",email="",id="",imgurl="",phone="";
    //-------------------------------------------------
    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);
        language=sharedPreferences.getString("language","");
        if (language.equals("spanish")){
            setTheme(R.style.AppTheme1);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_register);

        lEnglish = findViewById(R.id.eng);
        lSpanish = findViewById(R.id.arab);
        et_phone = findViewById(R.id.et_reg_phone);
        et_name = findViewById(R.id.et_reg_name);
        et_password =  findViewById(R.id.et_reg_password);
        et_email =  findViewById(R.id.et_reg_email);
        et_refer =findViewById(R.id.et_reg_referal);

        checkBox=findViewById(R.id.checkbox1);

        Agreed = (TextView) findViewById(R.id.agree);
        tv_password = (TextView) findViewById(R.id.tv_reg_password);
        tv_phone = (TextView) findViewById(R.id.tv_reg_phone);
        tv_name = (TextView) findViewById(R.id.tv_reg_name);
        tv_email = (TextView) findViewById(R.id.tv_reg_email);
        btn_register = (RelativeLayout) findViewById(R.id.btnRegister);

        Agreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,Agreed_Activtiy.class));
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked())
                 {attemptRegister();}
                else{
                    Toast.makeText(RegisterActivity.this, getString(R.string.agree), Toast.LENGTH_SHORT).show();
                }
            }
        });

        f=false;


        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(et_phone.getText().toString().length()<4){
                    et_phone.setText("+966");

                    et_phone.setSelection(et_phone.getText().toString().length());
                }
            }
        });
        //-----------------------------------------------------------------------
        signInButton=findViewById(R.id.gsignin);
        loginButton=findViewById(R.id.loginfb);


//        auth=FirebaseAuth.getInstance();
//        reference= FirebaseDatabase.getInstance().getReference();


        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Google");


        callbackManager=CallbackManager.Factory.create();


        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);

        final String current_lan = sharedPreferences.getString("language",null);

        if (current_lan == null){
            lEnglish.setBackgroundColor(Color.parseColor("#7abcbc"));
            lEnglish.setTextColor(Color.parseColor("#ffffff"));
        }
        else if (current_lan.equals("english")){
            lEnglish.setBackgroundColor(Color.parseColor("#7abcbc"));
            lEnglish.setTextColor(Color.parseColor("#ffffff"));
        }
        else if (current_lan.equals("spanish")){
            lSpanish.setBackgroundColor(Color.parseColor("#7abcbc"));
            lSpanish.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            lEnglish.setBackgroundColor(Color.parseColor("#7abcbc"));
            lEnglish.setTextColor(Color.parseColor("#ffffff"));
        }

        editor = sharedPreferences.edit();

        lEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (current_lan.equals("english")){
                    Toast.makeText(getApplicationContext(),"Already In English",Toast.LENGTH_SHORT).show();
                }
                else {
                LocaleHelper.setLocale(getApplication(),"en");
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                editor.putString("language", "english");
                editor.apply();

                recreate();
            }
            }
        });
        lSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_lan.equals("spanish")){
                    Toast.makeText(getApplicationContext(),"بالفعل باللغة العربية",Toast.LENGTH_SHORT).show();
                }
                else {

                    LocaleHelper.setLocale(getApplication(), "ar");
                    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("language", "spanish");
                    editor.apply();

                    recreate();
                }
            }
        });
        


        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                Toast.makeText(RegisterActivity.this, loginResult+"", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

//                Toast.makeText(.this, "abc", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

//                email_add.setText(error+"");
            }
        });



//        disconnectFromFacebook();


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sigin();
            }
        });
        //-----------------------------------------------------------------------
    }

    private void attemptRegister() {

        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        tv_password.setText(getResources().getString(R.string.tv_login_password));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_password.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpassword = et_password.getText().toString();
        String getemail = et_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            et_phone.setError(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.black));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            et_phone.setError(getResources().getString(R.string.phone_too_short));
        tv_phone.setTextColor(getResources().getColor(R.color.black));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.black));
            et_name.setError("Invalid Name");
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.black));
            et_password.setError("Invalid Password");
            focusView = et_password;

            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            et_password.setError(getResources().getString(R.string.password_too_short));

            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            et_email.setError("Invalid Email");
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
            et_email.setError(getResources().getString(R.string.invalide_email_address));

            tv_email.setTextColor(getResources().getColor(R.color.black));
            focusView = et_email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {
                Intent intent=new Intent(RegisterActivity.this,OTPActivity.class);
                intent.putExtra("name",getname);
                intent.putExtra("password",getpassword);
                intent.putExtra("email",getemail);
                intent.putExtra("phone",getphone);
                String refer=et_refer.getText().toString();
                if(refer.isEmpty()){
                    refer="";
                }
                intent.putExtra("referalcode",refer);
                startActivity(intent);
//                makeRegisterRequest(getname, getphone, getemail, getpassword);
            }
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            return true;
        }else {
            return false;
        }
//        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {

        final SweetAlertDialog loading=new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");

        loading.getProgressHelper().setBarColor(getResources().getColor(R.color.green));

        loading.show();

        String refercode=et_refer.getText().toString();

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
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);

                        if(!f)
                        {
                            finish();
                        }
                        btn_register.setEnabled(false);

                    } else {
                        String error = response.getString("error");
                        btn_register.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    //----------------------------------------------------------------------
    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){

            }else{
                loaduserprofile(currentAccessToken);
            }
        }
    };
    public  void loaduserprofile(AccessToken accessToken){
        GraphRequest graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    first_names=object.getString("first_name");
                    last_names=object.getString("last_name");
                    email=object.getString("email");
                    id=object.getString("id");
//                     phone=object.getString("mobile_phone");
                    imgurl="https://graph.facebook.com/"+id+"/picture?type=normal";


                    makeRegisterRequest(first_names+" "+last_names,"",email,BaseURL.fixpass);


                    f=true;
//                    makeRegisterRequest(first_names+last_names,"",email,Baseurl.fixpassword);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
    //-----------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

//-----------------------GOOGLE-------------------------------------
        if(requestCode==0){
//            Toast.makeText(this, data+"", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSigninResult(task);
        }
    }

    //---------------------------------------------------------------------------------------
    private void Sigin() {
        Intent signinIntent=googleSignInClient.getSignInIntent();
        startActivityForResult(signinIntent,0);
    }

    private void handleSigninResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account=task.getResult(ApiException.class);




            GoogleSignInAccount account1=GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
            if(account1 !=null){
                String personName=account1.getDisplayName();
//                String personGivenName=account.getGivenName();
//                String personFamilyName=account.getFamilyName();
                email=account1.getEmail();

                String personId=account1.getId();
                Uri personphoto=account1.getPhotoUrl();



                makeRegisterRequest(personName,"",email,BaseURL.fixpass);

            }else {
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setConfirmButtonBackgroundColor(Color.RED)
                        .setTitleText("Something Went Wrong")
                        .show();
            }

        }catch (Exception e){
//            email_add.setText(e+"");
            Toast.makeText(this, e+"", Toast.LENGTH_LONG).show();
        }
    }




    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }
    //--------------------------------------------------------------------------------------


}
