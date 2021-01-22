package Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import Config.BaseURL;
import gogrocer.tcc.R;
import util.DatabaseHandler;

import static android.content.Context.MODE_PRIVATE;

public class Bill_adapter extends RecyclerView.Adapter<Bill_adapter.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    SharedPreferences preferences;
    String language;
    DatabaseHandler dbHandler;

    public Bill_adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;
        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.b_row_bill, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final HashMap<String, String> map = list.get(position);

        preferences = activity.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

        if (map.get("size").contains("Free") || map.get("size").isEmpty()){
            holder.tv_title.setText(map.get("product_name"));
        }
        else {
            holder.tv_title.setText(map.get("product_name")+ "(" + map.get("size")+")");
        }

        holder.tv_contetiy.setText(map.get("qty"));
        Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get(DatabaseHandler.COLUMN_Primary)));
        Double price = Double.parseDouble(map.get("price"));
        holder.tv_total.setText("" + price * items);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_total, tv_contetiy;
        public ProductHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
        }
    }
}

