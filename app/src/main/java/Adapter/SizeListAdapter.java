package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import Fragment.ProductDetailShow;
import gogrocer.tcc.R;

public class SizeListAdapter extends RecyclerView.Adapter<SizeListAdapter.MyViewHolder>{
    ArrayList<String> listData;
    Context context;
    public static int sizepos;
    public static String sizevalue;

    int x = -1;

    public SizeListAdapter(Context context, ArrayList<String> listData) {
        this.context = context;
        this.listData = listData;
        sizepos = -1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_list_items, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.size.setText(listData.get(position));

        try {
            if (x == -1) {
                if (listData.get(position).contains(listData.get(0))) {
                    sizepos = position;
                    sizevalue = listData.get(position);
                    ProductDetailShow.ss = "-1";
                    x++;
                }
            }
            else {
                x++;
            }
        }
        catch (Exception ignored){}

        if (position == sizepos) {
            holder.size.setTextColor(Color.WHITE);
            holder.size.setBackgroundResource(R.color.colorPrimary);
        } else {
            holder.size.setTextColor(Color.BLACK);
            holder.size.setBackgroundResource(R.color.gray);
        }

        holder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizepos = position;
                sizevalue = listData.get(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView size;

        public MyViewHolder(View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.size);

        }
    }
}