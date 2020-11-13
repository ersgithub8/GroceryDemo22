package Adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.internal.$Gson$Preconditions;

import Config.BaseURL;
import Fragment.Product_fragment;
import Fragment.Main_new;
import Fragment.StoreFragment;
import java.util.ArrayList;
import java.util.List;

import Model.Store_main_model;
import gogrocer.tcc.R;

public class Store_main_adapter extends RecyclerView.Adapter<Store_main_adapter.Store_VH> {

    Main_new main_new;
    List<Store_main_model> store_models;
    Context context;
    FragmentManager fragmentManager;
    float rate;

    public Store_main_adapter(Context context, List<Store_main_model> store_models,Main_new main_new) {
        this.store_models = store_models;
        this.context=context;
        this.main_new=main_new;
    }

    @NonNull
    @Override
    public Store_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store,parent,false);

        return new Store_main_adapter.Store_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Store_VH holder, int position) {

        final Store_main_model modellist = store_models.get(position);
        Glide.with(context)
                .load(BaseURL.IMG_PROFILE_URL + modellist.getUser_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.storeimage);

        holder.storename.setText(modellist.getStore_name());

        if (modellist.getStar() == null){
            rate = 0;
        }
        else {
            rate = Float.parseFloat(modellist.getStar());
        }
        holder.ratingBar.setRating(rate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Product_fragment fm = new Product_fragment();
                args.putString("storeid", modellist.getStoer_id());
                args.putString("laddan_jaffery", "store");
//                args.putString("name",modellist.getStore_name());
//                args.putString("name",modellist.getStore_name());
                args.putString("user_email",modellist.getUser_email());
                args.putString("user_phone",modellist.getUser_phone());
                fm.setArguments(args);

                main_new.OnClick(modellist.getStoer_id(),modellist.getStore_name(),modellist.getStore_name(),modellist.getUser_email(),modellist.getUser_phone());
            }
        });

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
