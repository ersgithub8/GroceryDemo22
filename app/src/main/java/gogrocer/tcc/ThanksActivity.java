package gogrocer.tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThanksActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_info;
    RelativeLayout btn_home, btn_order;

    SharedPreferences preferences;

    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);


//        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.thank_you));
        preferences = getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    Fragment fm = new Home_fragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
//                    return true;
//                }
//                return false;
//            }
//        });

        String data = getIntent().getStringExtra("msg");
        String dataarb= getIntent().getStringExtra("msgarb");
        tv_info = (TextView) findViewById(R.id.tv_thank_info);
        btn_home = (RelativeLayout) findViewById(R.id.btn_thank_home);
        btn_order = (RelativeLayout) findViewById(R.id.btn_track_order);

        if (language.contains("english")) {
            tv_info.setText(Html.fromHtml(data));
        }else {
            tv_info.setText(Html.fromHtml(dataarb));       }


        btn_home.setOnClickListener(this);
        btn_order.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btn_thank_home) {
//            Fragment fm = new StoreFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                    .addToBackStack(null).commit();
            Intent myIntent = new Intent(ThanksActivity.this,MainActivity.class);
            startActivity(myIntent);
            finishAffinity();
        }
        if (id == R.id.btn_track_order) {
            Intent myIntent = new Intent(ThanksActivity.this, My_Order_activity.class);
            startActivity(myIntent);
            finishAffinity();
        }



    }
}