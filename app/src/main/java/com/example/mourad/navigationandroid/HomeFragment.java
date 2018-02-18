package com.example.mourad.navigationandroid;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    List<Rider_Ways> ridersList;
    RecyclerView recyclerView;
    Button Propose;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
        

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Propose = getView().findViewById(R.id.propose);

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

        ridersList = new ArrayList<>();

        //adding some items to our list
        ridersList.add(
                new Rider_Ways(R.drawable.clara1,
                        "Full Name : Benamar Mourad",
                        "Source : Meknes",
                        "Destination : Rabat",
                        "Date : 18/02/2018",
                        "Time : 16:00PM",
                        4.3));

        ridersList.add(
                new Rider_Ways(R.drawable.clara2,
                        "Full Name : Amine Radi",
                        "Source : Casablanca",
                        "Destination : Agadir",
                        "Date : 8/10/2018",
                        "Time : 08:00PM",
                        3.3));
        ridersList.add(
                new Rider_Ways(R.drawable.clara3,
                        "Full Name : Ayoub semrani",
                        "Source : Marrakech",
                        "Destination : Ait Heddo",
                        "Date : 22/08/2019",
                        "Time : 22:10PM",
                        4.9));

       // ProductAdapter adapter = new ProductAdapter(this, productList);

        WaysAdapter adapter = new WaysAdapter(getContext(), ridersList);


        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        getActivity().setTitle("Home");
    }
}
