package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Store_Model;
import de.hdodenhof.circleimageview.CircleImageView;
import gogrocer.tcc.R;

public class Store_Adapter extends RecyclerView.Adapter<Store_Adapter.Store_VH> {

    List<Store_Model> store_models;
    Context context;

    public Store_Adapter(List<Store_Model> store_models) {
        this.store_models = store_models;
//        this.context = context;
    }

    @NonNull
    @Override
    public Store_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store,parent,false);


        context=parent.getContext();
        return new Store_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Store_VH holder, int position) {

        Store_Model modellist=store_models.get(position);

        Glide.with(context).load(modellist.getUser_image()).placeholder(R.drawable.icon).into(holder.storeimage);
        holder.storename.setText(modellist.getUser_name());



    }

    @Override
    public int getItemCount() {
        return store_models.size();
    }

    public class Store_VH extends RecyclerView.ViewHolder{

        CircleImageView storeimage;
        TextView storename;
        public Store_VH(@NonNull View itemView) {
            super(itemView);


            storename=itemView.findViewById(R.id.store_name);
            storeimage=itemView.findViewById(R.id.store_image);

        }
    }
}
