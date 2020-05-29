package gogrocer.tcc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fcm.MyFirebaseRegister;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.Session_management;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = LoginActivity.class.getSimpleName();
    private RelativeLayout btn_continue, btn_register;
    private EditText et_password, et_email;
    private TextView tv_password, tv_email, btn_forgot;
    private Session_management sessionManagement;


    //-------------------------------------------------
    SignInButton signInButton;
    LoginButton loginButton;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;




    String first_names="",last_names="",email="",id="",imgurl="",phone="";
    //---------------------------------------



    @Override
    protected void attachBaseContext(Context newBase) {

        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        setContentView(R.layout.activity_login);

        et_password = (EditText) findViewById(R.id.et_login_pass);
        et_email = (EditText) findViewById(R.id.et_login_email);
        tv_password = (TextView) findViewById(R.id.tv_login_password);
        tv_email = (TextView) findViewById(R.id.tv_login_email);
        btn_continue = (RelativeLayout) findViewById(R.id.btnContinue);
        btn_register = (RelativeLayout) findViewById(R.id.btnRegister);
        btn_forgot = (TextView) findViewById(R.id.btnForgot);

        btn_continue.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);




        disconnectFromFacebook();
        //-----------------------------------------------------------------------
        signInButton=findViewById(R.id.gsignin);
        loginButton=findViewById(R.id.loginfb);


//        auth=FirebaseAuth.getInstance();
//        reference= FirebaseDatabase.getInstance().getReference();


        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Google");


        callbackManager=CallbackManager.Factory.create();


        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                Toast.makeText(LoginActivity.this, loginResult+"", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(LoginActivity.this, "Operation Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

//                email_add.setText(error+"");
            }
        });





        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);


//        disconnectFromFacebook();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sigin();
            }
        });
        //-----------------------------------------------------------------------

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnContinue) {
            attemptLogin();
        } else if (id == R.id.btnRegister) {
            Intent startRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(startRegister);
        } else if (id == R.id.btnForgot) {
            Intent startRegister = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(startRegister);
        }
    }

    private void attemptLogin() {

        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_password.setText(getResources().getString(R.string.tv_login_password));
        tv_password.setTextColor(getResources().getColor(R.color.black));
        tv_email.setTextColor(getResources().getColor(R.color.black));
        String getpassword = et_password.getText().toString();
        String getemail = et_email.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(R.color.black));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {

            tv_email.setTextColor(getResources().getColor(R.color.black));
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
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
                makeLoginRequest(getemail, getpassword);
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeLoginRequest(String email, final String password) {

        final SweetAlertDialog loading=new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Loading...");

        loading.getProgressHelper().setBarColor(getResources().getColor(R.color.green));

        try {
            loading.show();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Tag used to cancel the request
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_email", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.LOGIN_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("user_id");
                        String user_fullname = obj.getString("user_fullname");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                        String user_image = obj.getString("user_image");
                        String wallet_ammount = obj.getString("wallet");
                        String reward_points = obj.getString("rewards");
                        Session_management sessionManagement = new Session_management(LoginActivity.this);
                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image, wallet_ammount, reward_points, "", "", "", "", password);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finishAffinity();
                        MyFirebaseRegister myFirebaseRegister=new MyFirebaseRegister(LoginActivity.this);
                        myFirebaseRegister.RegisterUser(user_id);

                        btn_continue.setEnabled(false);

                    } else {
                        String error = response.getString("error");
                        btn_continue.setEnabled(true);

                        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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


//                    makeRegisterRequest(first_names+last_names,"",email,Baseurl.fixpassword);
                    makeLoginRequest(email,BaseURL.fixpass);


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




            GoogleSignInAccount account1=GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if(account1 !=null){
                String personName=account1.getDisplayName();
//                String personGivenName=account.getGivenName();
//                String personFamilyName=account.getFamilyName();
                email=account1.getEmail();

                String personId=account1.getId();
                Uri personphoto=account1.getPhotoUrl();



//                makeRegisterRequest(personName,"",email,Baseurl.fixpassword);

                makeLoginRequest(email,BaseURL.fixpass);
            }else {
                Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();;
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


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
