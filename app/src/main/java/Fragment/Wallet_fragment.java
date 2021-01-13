package Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import Config.BaseURL;
import Config.SharedPref;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gogrocer.tcc.MainActivity;
import gogrocer.tcc.R;
import gogrocer.tcc.RechargeWallet;
import util.ConnectivityReceiver;
import util.Session_management;

import static Config.BaseURL.BASE_URL;
import static Config.BaseURL.PREFS_NAME;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Wallet_fragment extends Fragment {

    private static String TAG = Wallet_fragment.class.getSimpleName();

    TextView Wallet_Ammount;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RelativeLayout Recharge_Wallet;
    private Session_management sessionManagement;

    public Wallet_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wallet_ammount, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.tv_app_name));
        sessionManagement = new Session_management(getActivity());
        //   String getwallet = sessionManagement.getUserDetails().get(BaseURL.KEY_WALLET_Ammount);
        Wallet_Ammount = (TextView) view.findViewById(R.id.wallet_ammount);
        //     Wallet_Ammount.setText(getwallet);
        Recharge_Wallet = (RelativeLayout) view.findViewById(R.id.recharge_wallet);
        Recharge_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargeWallet.class);
                startActivity(intent);
            }
        });

        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        editor = pref.edit();

        if (ConnectivityReceiver.isConnected()) {
            getRefresrh();
        }




        return view;

    }

    public void getRefresrh() {
        final SweetAlertDialog alertDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...")
                ;
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
                                Wallet_Ammount.setText(wallet_amount);
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