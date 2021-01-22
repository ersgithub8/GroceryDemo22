package Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import Config.BaseURL;
import Config.SharedPref;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import gogrocer.tcc.LocaleHelper;
import gogrocer.tcc.LoginActivity;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.My_Order_activity;
import gogrocer.tcc.R;
import gogrocer.tcc.Rating;
import gogrocer.tcc.WebView;
import util.DatabaseHandler;
import util.Session_management;

import static Config.BaseURL.PREFS_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Account_Fragment extends Fragment {

    TextView my_order,my_wallet,j6points,my_profile,logout,login,lEnglish,lSpanish,top_selling,deals;
    private Session_management sessionManagement;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    LinearLayout rewards,walletl,rate,share,feedback;

    TextView name,phone,reward,wallet,contact;
    TextView fb,twitter,tele,insta,whatsapp;
    TextView terms,privacy,returnp,helpcenter;
    CircleImageView iv_profile;
    ImageView maroof;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_acount, container, false);

        lEnglish = view.findViewById(R.id.eng);
        lSpanish = view.findViewById(R.id.arab);
        maroof = view.findViewById(R.id.maroof_id);
//        Glide.with(getActivity()).load("https://maroof.sa/Business/GetStamp?bid=95194").into(maroof);

        top_selling = view.findViewById(R.id.top_selling);

        iv_profile=view.findViewById(R.id.iv_header_img);

        deals = view.findViewById(R.id.deals);

        sessionManagement = new Session_management(getActivity());
        my_order=view.findViewById(R.id.my_orders);
        my_wallet=view.findViewById(R.id.my_wallet);
        j6points=view.findViewById(R.id.J6points);
        my_profile=view.findViewById(R.id.nav_my_profile);
        logout=view.findViewById(R.id.nav_logout);
        login=view.findViewById(R.id.nav_login);

        rate=view.findViewById(R.id.rate);
        share=view.findViewById(R.id.share);
        feedback=view.findViewById(R.id.feedback);


        name=view.findViewById(R.id.tv_header_name);
        phone=view.findViewById(R.id.tv_adres_phone);
        reward=view.findViewById(R.id.reward);
        wallet=view.findViewById(R.id.wallet);
        walletl=view.findViewById(R.id.llwallet);
        rewards=view.findViewById(R.id.llpoints);
        //pages
        terms=view.findViewById(R.id.term);
        privacy=view.findViewById(R.id.privacy);
        returnp=view.findViewById(R.id.ret_policy);
        helpcenter=view.findViewById(R.id.help);
        contact= (TextView) view.findViewById(R.id.contact_us);

        fb=view.findViewById(R.id.fb);
        twitter=view.findViewById(R.id.twitter);
        insta=view.findViewById(R.id.insta);
        tele=view.findViewById(R.id.telegram);
        whatsapp=view.findViewById(R.id.whatsapp);

        ((MainActivity)getActivity()).bot_profile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((MainActivity)getActivity()).bot_cat.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_fav.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_cart.setBackgroundColor(getResources().getColor(R.color.white));
        ((MainActivity)getActivity()).bot_store.setBackgroundColor(getResources().getColor(R.color.white));

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.j6stores.com/feedback");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out this app at: https://play.google.com/store/apps/details?id="
                                +getApplicationContext().getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
            }
        });

        sharedPreferences= getActivity().getSharedPreferences("lan", Context.MODE_PRIVATE);

        final String current_lan = sharedPreferences.getString("language",null);

        if (current_lan == null){
//            lEnglish.setBackgroundColor(Color.parseColor("#7abcbc"));
//            lEnglish.setTextColor(Color.parseColor("#ffffff"));
        }
        else if (current_lan.equals("english")){
//            lEnglish.setBackgroundColor(Color.parseColor("#7abcbc"));
//            lEnglish.setTextColor(Color.parseColor("#ffffff"));
        }
        else if (current_lan.equals("spanish")){
//            lSpanish.setBackgroundColor(Color.parseColor("#7abcbc"));
//            lSpanish.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
//            lEnglish.setBackgroundColor(Color.parseColor("#7abcbc"));
//            lEnglish.setTextColor(Color.parseColor("#ffffff"));
        }

        editor = sharedPreferences.edit();

        lEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_lan.equals("english")){
                    Toast.makeText(getActivity(),"Already In English",Toast.LENGTH_SHORT).show();
                }
                else {
                    LocaleHelper.setLocale(getApplicationContext(), "en");
                    getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                    editor.putString("language", "english");
                    editor.apply();

                    getActivity().recreate();
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

                    LocaleHelper.setLocale(getApplicationContext(), "ar");
                    getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("language", "spanish");
                    editor.apply();

                    getActivity().recreate();

                }
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fm = new Reward_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@j6stores.com"});
//                email.setType("message/rfc822");
//                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                Bundle args = new Bundle();
                Fragment fm = new Terms_and_Condition_fragment();
                args.putString("url", BaseURL.contactus);
                args.putString("title", getResources().getString(R.string.nav_contact));
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });

        walletl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fm = new Wallet_fragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();
                    }
                });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment fm = new Terms_and_Condition_fragment();
                args.putString("url", BaseURL.termsandcondition);
                args.putString("title", getResources().getString(R.string.nav_terms));
                        fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment fm = new Terms_and_Condition_fragment();
                args.putString("url", BaseURL.privacypolicy);
                args.putString("title", getResources().getString(R.string.privacy));
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });

        maroof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://maroof.sa/95194");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        helpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), WebView.class));
                Bundle args = new Bundle();
                Fragment fm = new Terms_and_Condition_fragment();
                args.putString("url", BaseURL.helpcentre);
                args.putString("title", getResources().getString(R.string.help_centre));
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });
        returnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment fm = new Terms_and_Condition_fragment();
                args.putString("url", BaseURL.returnpolicy);
                args.putString("title", getResources().getString(R.string.return_p));
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });

        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String previouslyEncodedImage = shre.getString("image_data", "");
        if (!previouslyEncodedImage.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            iv_profile.setImageBitmap(bitmap);
        }else{
            iv_profile.setImageResource(R.drawable.user);
        }


        String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        name.setText(getname);

        //cart.setText(new DatabaseHandler(getActivity()).getCartCount()+"");
        String getnumber = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        if(!getnumber.equals(""))
        phone.setText(getnumber);
        else
            phone.setText(getResources().getString(R.string.mblnumber));

        String getreward = sessionManagement.getUserDetails().get(BaseURL.KEY_REWARDS_POINTS);
        reward.setText(getreward);

        String getwallet = sessionManagement.getUserDetails().get(BaseURL.KEY_WALLET_Ammount);
        if (getwallet == null){
            wallet.setText("0 " + getResources().getString(R.string.currency));
        }
        else {
            wallet.setText(getwallet + " " + getResources().getString(R.string.currency));
        }

        getRewards();
        getRefresrh();


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://www.facebook.com/J6stores");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        tele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://t.me/J6stores");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://twitter.com/J6stores");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://wa.me/966535262321");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://instagram.com/j6stores");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        if(sessionManagement.isLoggedIn())
        {
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        }else {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        my_order.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), My_Order_activity.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
                dialog.setCancelText(getResources().getString(R.string.no));
                dialog.setConfirmText(getResources().getString(R.string.yes));
                dialog.setTitle(getResources().getString(R.string.surety));
                dialog.show();
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sessionManagement.logoutSession();
                        disconnectFromFacebook();
                        getActivity().finish();
                        new DatabaseHandler(getActivity()).clearCart();
                        Intent i=new Intent(getActivity(),LoginActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                    }
                });
            }
        });

        top_selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_top_selling", "2");
                args.putString("laddan_jaffery", "murshid");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });


        deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment fm = new Deal_Fragemnt();
                args.putString("laddan_jaffery", "deals");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();


            }
        });

        my_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Wallet_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });
        my_profile.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    Fragment fm = new Edit_profile_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
//                startActivity(new Intent(getActivity(), Rating.class));
                }
                else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);

                }
            }
        });




//        if(current_lan.equals("english")){
//            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/kellyslab.ttf");
////            textView.setTypeface(type);
//            logout.setTypeface(type);
//            my_wallet.setTypeface(type);
//            my_order.setTypeface(type);
//            my_profile.setTypeface(type);
//
//        }else{
//            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/janna.ttf");
////            textView.setTypeface(type);
//            logout.setTypeface(type);
//
//            my_wallet.setTypeface(type);
//            my_order.setTypeface(type);
//            my_profile.setTypeface(type);
//        }

        return view;
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





    public void getRewards() {

        final SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading...")
                ;
        alertDialog.setCancelable(false);
        alertDialog.show();

        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.GET, BaseURL.REWARDS_REFRESH + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            alertDialog.dismiss();
                            JSONObject jObj = new JSONObject(response);

                            if (jObj.optString("success").equalsIgnoreCase("success")) {
                                String rewards_points = jObj.getString("total_rewards");
                                if (rewards_points.equals("null")) {
                                    reward.setText("0");
                                } else {
                                    reward.setText(rewards_points);
                                    SharedPref.putString(getActivity(), BaseURL.KEY_REWARDS_POINTS, rewards_points);

                                    SharedPreferences pref;
                                    SharedPreferences.Editor editor;

                                    pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                    editor = pref.edit();

                                    editor.putString(BaseURL.KEY_REWARDS_POINTS,rewards_points);
                                    editor.apply();
                                }

                            } else {
                                // Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            alertDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                alertDialog.dismiss();
            }
        }) {

        };
        rq.add(strReq);

    }

    public void getRefresrh() {
        final SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
        alertDialog.setCancelable(false);
        alertDialog.show();

        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.GET, BaseURL.WALLET_REFRESH + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            alertDialog.dismiss();
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.optString("success").equalsIgnoreCase("success")) {
                                String wallet_amount = jObj.getString("wallet");
                                wallet.setText(wallet_amount +" "+getResources().getString(R.string.currency));
                                SharedPref.putString(getActivity(), BaseURL.KEY_WALLET_Ammount, wallet_amount);
                                editor.putString(BaseURL.KEY_WALLET_Ammount,wallet_amount);
                                editor.apply();
                            } else {
                                // Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            alertDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                alertDialog.dismiss();
            }
        }) {

        };
        rq.add(strReq);
    }

}