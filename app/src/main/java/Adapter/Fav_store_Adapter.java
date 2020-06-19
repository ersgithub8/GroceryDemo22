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
import Model.Home_Icon_model;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Fav_store_Adapter extends RecyclerView.Adapter<Fav_store_Adapter.MyViewHolder> {

    private List<Home_Icon_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.service_text);
            image = (ImageView) view.findViewById(R.id.service_image);


        }
    }

    public Fav_store_Adapter(List<Home_Icon_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Fav_store_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_headre_catogaries, parent, false);

        context = parent.getContext();

        return new Fav_store_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Fav_store_Adapter.MyViewHolder holder, int position) {
        Home_Icon_model mList = modelList.get(position);
        Glide.with(context)
                .load(BaseURL.IMG_STORE_URL + mList.getUser_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

        if (language.contains("english")) {
            holder.title.setText(mList.getUser_name());
        }
        else {
            holder.title.setText(mList.getUser_name());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

