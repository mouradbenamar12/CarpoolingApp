package com.example.mourad.navigationandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
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

    //getting the context and product list with constructor
    WaysAdapter(List<Rider_Ways> list, Context context) {

        this.list=list;
        this.context=context;
    }



        @NonNull
        @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_ways, parent,false);
        return new WaysAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, @SuppressLint("RecyclerView") final int position) {
       //  Rider_Ways mylist = list.get(position);
        //binding the data with the viewholder views
        isFavorite(holder.getAdapterPosition(),holder);
        holder.tvFullName.setText(list.get(position).getFull_Name());
        holder.tvSource.setText(list.get(position).getSource());
        holder.tvDestination.setText(list.get(position).getDestination());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvPhone.setText(list.get(position).getPhone());
        holder.tvCarId.setText(list.get(position).getCarId());
        final int pp=holder.getAdapterPosition();
        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    addFavorite(pp);
                }else {
                    removeFavorite(pp);

                }

            }
        });

        holder.imageWtsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:" + list.get(position).getPhone());
                Intent i = new Intent(Intent.ACTION_SENDTO,uri);
                i.setPackage("com.whatsapp");
                context.startActivity(i);



            }
        });
        holder.imagePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+list.get(position).getPhone()));
                context.startActivity(intent);
            }
        });

        holder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody="Hey check out our app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus";
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                context.startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        if (list.get(position).getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.btn_delete.setVisibility(View.VISIBLE);
        }else {
            holder.btn_delete.setVisibility(View.GONE);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(holder.getPosition());
                FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                DatabaseReference Ways = database_user.getReference("Ways");
                Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();
                Toast.makeText(context,"Delete success",Toast.LENGTH_LONG).show();
            }
        });

        Glide.with(context)
           .load(list.get(position).getImage_ways())
           .into(holder.imageProfile);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageProfile,imageWtsp,imageShare,imagePhone;
        TextView tvFullName, tvSource, tvDestination, tvDate, tvTime,tvPhone, tvCarId;
        MaterialFavoriteButton favoriteButton;
        Button btn_delete;

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
            favoriteButton=itemView.findViewById(R.id.fav);
            imageWtsp=itemView.findViewById(R.id.wtsp);
            imageShare = itemView.findViewById(R.id.share);
            imagePhone = itemView.findViewById(R.id.imgPhone);
            btn_delete = itemView.findViewById(R.id.btn_delete);
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

    private void addFavorite(int pos){
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");
        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .child(list.get(pos).getUID())
                .setValue(list.get(pos).getUID(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    }
                });
    }
    private void removeFavorite(int pos){
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .child(list.get(pos).getUID())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    }
                });
    }
    private void isFavorite(int pos, final ProductViewHolder holder){
        final String UID=list.get(pos).getUID();
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites").
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    String UIDFav = dataSnapshot1.getValue(String.class);
                    if (UID.equals(UIDFav)){
                        holder.favoriteButton.setFavorite(true,false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void removeAt(int position) {
        list.remove(position);

    }

}
