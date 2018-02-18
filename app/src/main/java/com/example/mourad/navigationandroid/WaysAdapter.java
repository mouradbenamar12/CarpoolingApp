package com.example.mourad.navigationandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

//public class WaysAdapter Rider_Ways= Product

public class WaysAdapter extends RecyclerView.Adapter<WaysAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Rider_Ways> ridersList;

    //getting the context and product list with constructor
    WaysAdapter(Context mCtx, List<Rider_Ways> ridersList) {
        this.mCtx = mCtx;
        this.ridersList = ridersList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_ways, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Rider_Ways rider_ways = ridersList.get(position);

        //binding the data with the viewholder views
        holder.tvFullName.setText(rider_ways.getFull_Name());
        holder.tvSource.setText(rider_ways.getSource());
        holder.tvDestination.setText(rider_ways.getDestination());
        holder.tvDate.setText(rider_ways.getDate());
        holder.tvTime.setText(rider_ways.getTime());
        holder.tvRating.setText(String.valueOf(rider_ways.getRating()));
        holder.imageProfile.setImageDrawable(mCtx.getResources().getDrawable(rider_ways.getImage_ways()));

    }


    @Override
    public int getItemCount() {
        return ridersList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView tvFullName, tvSource, tvDestination, tvDate, tvTime, tvRating;

        ProductViewHolder(View itemView) {
            super(itemView);

            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvRating = itemView.findViewById(R.id.tvRating);
            imageProfile = itemView.findViewById(R.id.imageProfile);
        }
    }
}
