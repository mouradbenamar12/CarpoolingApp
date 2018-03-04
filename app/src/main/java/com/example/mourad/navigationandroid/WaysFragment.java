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
    DatabaseReference myRef ;
    RecyclerView.Adapter adapter ;
    boolean a;
    private DatabaseReference Users,uid,favorite;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ways, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Ways");
        a=false;
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
       // ShowFavorites();

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
                    if (riderDetails == null){
                        recyclerView_favorites.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    }else if(isFavorite(riderDetails.getUID())){
                        list.add(riderDetails);

                    }

                }

                adapter  = new WaysAdapter(list,getContext());
                recyclerView_favorites.setAdapter(adapter);
                progress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                progress.dismiss();
            }
        });
    }
    public boolean isFavorite(final String UID){
        Users = FirebaseDatabase.getInstance().getReference("Users");
        uid=Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        favorite=uid.child("Favorites");
        favorite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    String UIDFav;
                    UIDFav = dataSnapshot1.getValue(String.class);
                    if (UID.equals(UIDFav)){
                        a=true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return a;
    }
}
