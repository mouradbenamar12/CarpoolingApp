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
    DatabaseReference myRef ;
    RecyclerView.Adapter adapter ;

    Button Propose,Search;


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
