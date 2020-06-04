package Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Store_Model;

public class Store_Adapter extends RecyclerView.Adapter<Store_Adapter.Store_VH> {

    List<Store_Model> store_models;
    Context context;

    public Store_Adapter(List<Store_Model> store_models, Context context) {
        this.store_models = store_models;
        this.context = context;
    }

    @NonNull
    @Override
    public Store_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Store_VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Store_VH extends RecyclerView.ViewHolder{

        public Store_VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
