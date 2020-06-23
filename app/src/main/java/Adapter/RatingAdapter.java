package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

import Model.Rating_Model;
import gogrocer.tcc.R;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingVH> {

    List<Rating_Model> models;
    Context context;

    public RatingAdapter(List<Rating_Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ratings,parent,false);

        return new RatingVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingVH holder, int position) {

        Rating_Model modellist=models.get(position);

        holder.ratingBar.setRating(Float.valueOf(modellist.getStar()));
        holder.ratingText.setText(modellist.getComment());
        if(modellist.getComment().length()==0){
            holder.ratingText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class RatingVH extends RecyclerView.ViewHolder{

        RatingBar ratingBar;
        TextView ratingText;
        public RatingVH(@NonNull View itemView) {
            super(itemView);
        ratingBar=itemView.findViewById(R.id.ratingbar);
        ratingText=itemView.findViewById(R.id.ratingcoment);
        }
    }
}
