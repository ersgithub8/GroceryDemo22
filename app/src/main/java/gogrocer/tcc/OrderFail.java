package gogrocer.tcc;


import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import Config.SharedPref;
import Fragment.Payment_fragment;
public class OrderFail extends AppCompatActivity {
    RelativeLayout oredr_fail;
    SharedPreferences sharedPreferences;
    String language="";

    private Payment_fragment mItemsFragment;
    @Override
    protected void attachBaseContext(Context newBase) {

        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

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
        setContentView(R.layout.activity_order_fail);
        mItemsFragment = new Payment_fragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.order_fail));
        oredr_fail = (RelativeLayout) findViewById(R.id.retry_order);
        oredr_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(OrderFail.this,MainActivity.class);
              startActivity(intent);
              finish();
            }
        });


    }


}
