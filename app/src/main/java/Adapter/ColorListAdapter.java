package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import Fragment.ProductDetailShow;
import gogrocer.tcc.R;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.MyViewHolder>{
    ArrayList<String> listData;
    Context context;
    TextView textView;
    public static int pos;
    String pricee;
    int x = -1;
    pro_detail_interface anInterface;
    ArrayList<String> prizeList = new ArrayList<>();

    public ColorListAdapter(Context context, ArrayList<String> listData,String pricee,TextView textView,pro_detail_interface anInterface) {
        this.context = context;
        this.listData = listData;
        this.pricee=pricee;
        this.textView = textView;
        this.anInterface=anInterface;
        pos = -1;
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
                    pos = position;
                    anInterface.onclick(position);
                    ProductDetailShow.cc = "-1";
                    x++;
                }
            }
            else {
                x++;
            }
        }
        catch (Exception ignored){ }

        if (position == pos) {
            holder.size.setTextColor(Color.WHITE);
            holder.size.setBackgroundResource(R.color.colorPrimary);
        } else {
            holder.size.setTextColor(Color.BLACK);
            holder.size.setBackgroundResource(R.color.gray);
        }

        if (pricee.contains("|")){
            String currentString = pricee;
            String[] separated = currentString.split("\\|");
            prizeList = new ArrayList<>(Arrays.asList(separated));
        }
        else {
            prizeList.add(pricee);
        }

        holder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                textView.setText(prizeList.get(position)+" "+context.getResources().getString(R.string.currency));
                anInterface.onclick(position);
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