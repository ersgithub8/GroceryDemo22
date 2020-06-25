package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Fragment.Favourite;
import Model.Home_Icon_model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Favourite_Adappter extends RecyclerView.Adapter<Favourite_Adappter.MyViewHolder> {

    private List<Home_Icon_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_subcat_title);
            image = (ImageView) view.findViewById(R.id.iv_subcat_img);

        }
    }

    public Favourite_Adappter(List<Home_Icon_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Favourite_Adappter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_fav_rv, parent, false);

        context = parent.getContext();
        return new Favourite_Adappter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Favourite_Adappter.MyViewHolder holder, int position) {
        Home_Icon_model mList = modelList.get(position);

        if (mList.getProduct_name() != null) {
            Glide.with(context)
                    .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                    .placeholder(R.drawable.icon)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.image);
            preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
            language = preferences.getString("language", "");

            if (language.contains("english")) {
                holder.title.setText(mList.getProduct_name());
            } else {
                holder.title.setText(mList.getProduct_arb_name());
            }
        }

        else if (mList.getUser_name() != null) {
            Glide.with(context)
                    .load(mList.getUser_image())
                    .placeholder(R.drawable.icon)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.image);
            preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
            language = preferences.getString("language", "");

            if (language.contains("english")) {
                holder.title.setText(mList.getUser_name());
            } else {
                holder.title.setText(mList.getUser_name());
            }
        }

        else if (mList.getTitle() != null) {
            Glide.with(context)
                    .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
                    .placeholder(R.drawable.icon)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.image);
            preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
            language = preferences.getString("language", "");

            if (language.contains("english")) {
                holder.title.setText(mList.getTitle());
            } else {
                holder.title.setText(mList.getArb_title());
            }
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}


