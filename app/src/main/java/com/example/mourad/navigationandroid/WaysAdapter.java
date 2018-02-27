package com.example.mourad.navigationandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class WaysAdapter extends RecyclerView.Adapter<WaysAdapter.ProductViewHolder> {

    private List<Rider_Ways> list;
    private Context context;

    //getting the context and product list with constructor
    WaysAdapter(List<Rider_Ways> list, Context context) {

        this.list=list;
        this.context=context;
    }


        @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_ways, parent,false);
        return new WaysAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
       //  Rider_Ways mylist = list.get(position);
        //binding the data with the viewholder views
        holder.tvFullName.setText(list.get(position).getFull_Name());
        holder.tvSource.setText(list.get(position).getSource());
        holder.tvDestination.setText(list.get(position).getDestination());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvPhone.setText(list.get(position).getPhone());
        holder.tvCarId.setText(list.get(position).getCarId());


        Glide.with(context)
           .load(list.get(position).getImage_ways())
           .into(holder.imageProfile);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageProfile;
        TextView tvFullName, tvSource, tvDestination, tvDate, tvTime,tvPhone, tvCarId;

        ProductViewHolder(View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.imageProfile);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPhone =itemView.findViewById(R.id.tvPhone);
            tvCarId = itemView.findViewById(R.id.tvCarId);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,OffreInformation.class);
            intent.putExtra("image",list.get(getLayoutPosition()).getImage_ways());
            intent.putExtra("full Name",list.get(getLayoutPosition()).getFull_Name());
            intent.putExtra("source",list.get(getLayoutPosition()).getSource());
            intent.putExtra("destination",list.get(getLayoutPosition()).getDestination());
            intent.putExtra("date",list.get(getLayoutPosition()).getDate());
            intent.putExtra("time",list.get(getLayoutPosition()).getTime());
            intent.putExtra("phone",list.get(getLayoutPosition()).getPhone());
            intent.putExtra("carid",list.get(getLayoutPosition()).getCarId());
            intent.putExtra("LatLngSrc",list.get(getLayoutPosition()).getLatLngSrc());
            intent.putExtra("LatLngDes",list.get(getLayoutPosition()).getLatLngDes());

            context.startActivity(intent);
        }
    }
}
