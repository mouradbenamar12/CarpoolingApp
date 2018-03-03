package com.example.mourad.navigationandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class WaysAdapter extends RecyclerView.Adapter<WaysAdapter.ProductViewHolder> {

    private List<Rider_Ways> list;
    private Context context;
    private String UID;
    private  ImageView fav_image;
    private DatabaseReference Users,uid,favorite;


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
        UID=list.get(position).getUID();


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

        ProductViewHolder(final View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.imageProfile);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPhone =itemView.findViewById(R.id.tvPhone);
            tvCarId = itemView.findViewById(R.id.tvCarId);
            fav_image = itemView.findViewById(R.id.fav);

            itemView.setOnClickListener(this);

            isFavorite();

         fav_image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if(fav_image.getDrawable().getConstantState()==
              v.getResources().getDrawable(R.drawable.ic_favorite_black_24dp).getConstantState()){
                removeFavorite();
            }else{
                addFavorite();
                }
          }
            });
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

    public void addFavorite(){
        fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .child(UID)
                .setValue(UID, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        Toast.makeText(context,"add to favorite",Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void removeFavorite(){
        fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .child(UID)
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(context,"remove from favorite",Toast.LENGTH_LONG).show();

                    }
                });
    }
    public void isFavorite(){
        Users = FirebaseDatabase.getInstance().getReference("Users");
        uid=Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        favorite=uid.child("Favorites");
        favorite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    String UIDFav;
                    UIDFav = dataSnapshot1.getValue(String.class);
                    Toast.makeText(context,"is value "+UIDFav,Toast.LENGTH_LONG).show();
                    if (UID.equals(UIDFav)){
                        Toast.makeText(context,"is favorite ",Toast.LENGTH_LONG).show();
                        fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
