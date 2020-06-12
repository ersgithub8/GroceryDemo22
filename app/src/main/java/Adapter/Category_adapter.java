package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Fragment.Category_Fragment;
import Model.Home_Icon_model;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gogrocer.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class Category_adapter extends RecyclerView.Adapter<Category_adapter.MyViewHolder> {

    private List<Home_Icon_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;
    private Inerface inerface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.service_text);
            image = (ImageView) view.findViewById(R.id.service_image);

        }
    }

    public Category_adapter(List<Home_Icon_model> modelList, Category_Fragment inerface) {
        this.modelList = modelList;
        this.inerface= (Inerface) inerface;
    }

    @Override
    public Category_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_headre_catogaries, parent, false);

        context = parent.getContext();

        return new Category_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Home_Icon_model mList = modelList.get(position);
        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList.getTitle());
        }
        else {
            holder.title.setText(mList.getArb_title());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inerface.onclick(mList.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

