package Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import gogrocer.tcc.LocaleHelper;
import gogrocer.tcc.LoginActivity;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.My_Order_activity;
import gogrocer.tcc.R;
import util.Session_management;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Account_Fragment extends Fragment {

    TextView my_order,my_wallet,j6points,my_profile,logout,rate,share,feedback,login,lEnglish,lSpanish;
    private Session_management sessionManagement;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_acount, container, false);

        lEnglish = view.findViewById(R.id.eng);
        lSpanish = view.findViewById(R.id.arab);

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


        sharedPreferences= getActivity().getSharedPreferences("lan", Context.MODE_PRIVATE);

        String current_lan = sharedPreferences.getString("language",null);

        if (current_lan.equals("english")){
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
                LocaleHelper.setLocale(getApplicationContext(),"en");
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                editor.putString("language", "english");
                editor.apply();

                getActivity().recreate();
            }
        });
        lSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleHelper.setLocale(getApplicationContext(),"ar");
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("language", "spanish");
                editor.apply();

                getActivity().recreate();

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
                sessionManagement.logoutSession();
                disconnectFromFacebook();
                getActivity().finish();
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
                } else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);

                }
            }
        });





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
}