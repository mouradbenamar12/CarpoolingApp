package com.example.mourad.navigationandroid;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog progress;
    List<Rider_Ways> list = new ArrayList<>();
    DatabaseReference myRef;
    RecyclerView.Adapter adapter ;

    ImageView fav_image;
    Button Propose,Search;
    String src;
    String des;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef = FirebaseDatabase.getInstance().getReference("Ways");

       progress = new ProgressDialog(getActivity());
        progress.setTitle("loading ... ");
        progress.setMessage("Syncing ...");
        progress.setCancelable(false);
        progress.show();

        fav_image = view.findViewById(R.id.fav);


        Propose = getView().findViewById(R.id.propose);
        Search = getView().findViewById(R.id.search);

        Propose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Fragment fragment = new ProposeFragment();
                if (fragment!=null){
                    FragmentTransaction ft;
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment).commit();
                }
            }
        });

        loaddata();

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            Rider_Ways riderDetails = dataSnapshot1.getValue(Rider_Ways.class);
                            String src_rider=riderDetails.getSource();
                            String des_rider=riderDetails.getDestination();
                            if (src_rider.equals(src) && des_rider.equals(des))
                                list.add(riderDetails);

                        }

                        adapter  = new WaysAdapter(list,getContext());
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progress.dismiss();
                    }
                });
            }
        });



        SupportPlaceAutocompleteFragment autocompleteFragmentSource  = (SupportPlaceAutocompleteFragment)getChildFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment_source);
        //ImageView searchSrc = (ImageView)((LinearLayout)autocompleteFragmentSource.getView()).getChildAt(0);
        //  searchSrc.setImageDrawable(getResources().getDrawable(R.drawable.source));

        autocompleteFragmentSource.setHint(" Source ...");
        autocompleteFragmentSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //   Log.i(TAG, "Place: " + place.getName());
                Toast.makeText(getContext(),"Place: " + place.getName(),Toast.LENGTH_LONG).show();
                src=place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getContext(),"An error occurred: " + status,Toast.LENGTH_LONG).show();

            }
        });

        SupportPlaceAutocompleteFragment autocompleteFragmentDestination  = (SupportPlaceAutocompleteFragment)getChildFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment_destination);
        //ImageView searchDest = (ImageView)((LinearLayout)autocompleteFragmentDestination.getView()).getChildAt(0);
        //searchDest.setImageDrawable(getResources().getDrawable(R.drawable.clara1));

        autocompleteFragmentDestination.setHint(" Destination ... ");
        autocompleteFragmentDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //   Log.i(TAG, "Place: " + place.getName());
                Toast.makeText(getContext(),"Place: " + place.getName(),Toast.LENGTH_LONG).show();
                des=place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getContext(),"An error occurred: " + status,Toast.LENGTH_LONG).show();

            }
        });



    }
public void loaddata(){
    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                Rider_Ways riderDetails = dataSnapshot1.getValue(Rider_Ways.class);
                list.add(riderDetails);
            }

            adapter  = new WaysAdapter(list,getContext());
            recyclerView.setAdapter(adapter);
            progress.dismiss();

        }

        @Override
        public void onCancelled(DatabaseError error) {
            progress.dismiss();
        }
    });
}



}
