package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Config.BaseURL;
import Model.Store_Model;
import Model.Store_main_model;
import de.hdodenhof.circleimageview.CircleImageView;
import gogrocer.tcc.R;

public class Store_Adapter extends RecyclerView.Adapter<Store_Adapter.Store_VH> {

    List<Store_main_model> store_models;
    Context context;
    float rate;
    public Store_Adapter(Context context,List<Store_main_model> store_models) {
        this.store_models = store_models;
        this.context=context;
//        this.context = context;
    }

    @NonNull
    @Override
    public Store_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store,parent,false);

        return new Store_Adapter.Store_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Store_VH holder, int position) {

        Store_main_model modellist=store_models.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PROFILE_URL + modellist.getUser_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.storeimage);

        holder.storename.setText(modellist.getUser_name());

//        if (modellist.getStar() == null){
//            rate = 0;
//        }
//        else {
//            rate = Float.parseFloat(modellist.getStar());
//        }
        //holder.ratingBar.setRating(rate);

    }

    @Override
    public int getItemCount() {
        return store_models.size();
    }

    public class Store_VH extends RecyclerView.ViewHolder{

        ImageView storeimage;
        TextView storename;
        RatingBar ratingBar;
        public Store_VH(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.rating_bar);
            storename=itemView.findViewById(R.id.store_name);
            storeimage=itemView.findViewById(R.id.store_image);

        }
    }
}
