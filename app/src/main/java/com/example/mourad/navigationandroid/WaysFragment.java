package com.example.mourad.navigationandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaysFragment extends Fragment {
    RecyclerView recyclerView_current,recyclerView_favorites;
    TextView empty_view,empty_favorites;
    ProgressDialog progress;
    List<Rider_Ways> list = new ArrayList<>();
    List<Rider_Ways> list2 = new ArrayList<>();
    DatabaseReference myRef ;
    RecyclerView.Adapter adapter ;
    RecyclerView.Adapter adapter2 ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ways, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Ways");
        myRef = FirebaseDatabase.getInstance().getReference("Ways");
        empty_view=view.findViewById(R.id.empty_view);
        recyclerView_current = view.findViewById(R.id.recyclerView_current);
        recyclerView_current.setHasFixedSize(true);
        recyclerView_current.setLayoutManager(new LinearLayoutManager(getContext()));

        empty_favorites=view.findViewById(R.id.empty_favotites);
        recyclerView_favorites = view.findViewById(R.id.recyclerView_favorites);
        recyclerView_favorites.setHasFixedSize(true);
        recyclerView_favorites.setLayoutManager(new LinearLayoutManager(getContext()));

        progress = new ProgressDialog(getActivity());
        progress.setTitle("loading ... ");
        progress.setMessage("Syncing ...");
        progress.setCancelable(false);
        progress.show();
        getCurrentWay();
        ShowFavorites();
    }
    public void getCurrentWay(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Rider_Ways riderDetails = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Rider_Ways.class);
                if (riderDetails == null){
                    recyclerView_current.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                }
                else{
                    list.add(riderDetails);
                    adapter  = new WaysAdapter(list,getContext());
                    recyclerView_current.setAdapter(adapter);
                   }
                progress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                progress.dismiss();
            }
        });
    }
    public void ShowFavorites(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Rider_Ways riderDetails = dataSnapshot1.getValue(Rider_Ways.class);
                    if (riderDetails != null){
                        isFavorite(riderDetails);
                    }else{
                        recyclerView_favorites.setVisibility(View.GONE);
                        empty_favorites.setVisibility(View.VISIBLE);
                    }
                }
                progress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                progress.dismiss();
            }
        });
    }
    private void isFavorite(final Rider_Ways RiderDetails){
       FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            String UIDFav = dataSnapshot1.getValue(String.class);
                            if (RiderDetails.getUID().equals(UIDFav)){
                                list2.add(RiderDetails);
                            }
                        }
                        if (list2.isEmpty()){
                            recyclerView_favorites.setVisibility(View.GONE);
                            empty_favorites.setVisibility(View.VISIBLE);
                        }else {
                            adapter2 = new WaysAdapter(list2,getContext());
                            recyclerView_favorites.setAdapter(adapter2);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }
}
