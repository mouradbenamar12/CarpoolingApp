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

        getActivity().setTitle(R.string.my_ways);
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
        ShowFavorites();

    }
    public void ShowFavorites(){
        WaysFragment.this.list.clear();
        WaysFragment.this.list2.clear();
        recyclerView_favorites.clearOnScrollListeners();
        recyclerView_favorites.clearOnChildAttachStateChangeListeners();
        recyclerView_current.clearOnScrollListeners();
        recyclerView_current.clearOnChildAttachStateChangeListeners();
        final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    final Rider_Ways riderDetails = dataSnapshot1.getValue(Rider_Ways.class);
                    if (riderDetails == null){
                        recyclerView_current.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                        recyclerView_favorites.setVisibility(View.GONE);
                        empty_favorites.setVisibility(View.VISIBLE);
                    }else if(riderDetails.getUID().equals(uid)){
                        WaysFragment.this.list.add(riderDetails);
                        adapter  = new WaysAdapter(WaysFragment.this.list,getContext());
                        recyclerView_current.setAdapter(adapter);
                        recyclerView_current.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                    }else{

                        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                        DatabaseReference Users = database_user.getReference("Users");
                        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Favorites")
                                .addValueEventListener(new ValueEventListener(){
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                            String UIDFav = dataSnapshot1.getValue(String.class);
                                            if (riderDetails.getUID().equals(UIDFav)){
                                                WaysFragment.this.list2.add(riderDetails);
                                            }
                                        }
                                        if (!WaysFragment.this.list2.isEmpty()){
                                            adapter2 = new WaysAdapter(WaysFragment.this.list2,getContext());
                                            recyclerView_favorites.setAdapter(adapter2);
                                            recyclerView_favorites.setVisibility(View.VISIBLE);
                                            empty_favorites.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                    }
                                });

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
    /*private void isFavorite(Rider_Ways rider){
        final Rider_Ways RiderDetails = rider;
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        ValueEventListener listener1 = Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            String UIDFav = dataSnapshot1.getValue(String.class);
                            if (RiderDetails.getUID().equals(UIDFav)){
                                WaysFragment.this.list2.add(RiderDetails);
                            }
                        }
                        if (!WaysFragment.this.list2.isEmpty()){
                            adapter2 = new WaysAdapter(WaysFragment.this.list2,getContext());
                            recyclerView_favorites.setAdapter(adapter2);
                            recyclerView_favorites.setVisibility(View.VISIBLE);
                            empty_favorites.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
        Users.removeEventListener(listener1);
    }*/
    /*public void Reload(){

        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");

        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favorites")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        WaysFragment.this.list.clear();
                        WaysFragment.this.list2.clear();
                        recyclerView_favorites.clearOnScrollListeners();
                        recyclerView_favorites.clearOnChildAttachStateChangeListeners();
                        recyclerView_current.clearOnScrollListeners();
                        recyclerView_current.clearOnChildAttachStateChangeListeners();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }*/
}
