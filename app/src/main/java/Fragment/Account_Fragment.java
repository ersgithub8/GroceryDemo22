package Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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

import gogrocer.tcc.LoginActivity;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.My_Order_activity;
import gogrocer.tcc.R;
import util.Session_management;


public class Account_Fragment extends Fragment {

    TextView my_order,my_wallet,j6points,my_profile,logout,rate,share,feedback,login;
    private Session_management sessionManagement;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_acount, container, false);
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