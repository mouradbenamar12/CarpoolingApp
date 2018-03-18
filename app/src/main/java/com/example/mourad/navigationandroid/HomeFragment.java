package com.example.mourad.navigationandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressDialog progress;
    private List<Rider_Ways> list = new ArrayList<>();
    private DatabaseReference myRef,mynotif;
    private RecyclerView.Adapter adapter ;
    protected Context MyContext;

    private boolean notificationsStatut;
    ImageView fav_image;
    Button Propose,Search;
    String src;
    String des;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        MyContext= getActivity();
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.home);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef = FirebaseDatabase.getInstance().getReference("Ways");
        mynotif  = FirebaseDatabase.getInstance().getReference("Users");

        progress = new ProgressDialog(getActivity());
        showProgressDialog(true,60000);

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


        adapter  = new WaysAdapter(list,getContext());
        recyclerView.setAdapter(adapter);


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
                        showProgressDialog(false,0);

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void updateNotification() {
        Notification notification = new NotificationCompat.Builder(MyContext)
                //Title of the notification
                .setContentTitle("hello")
                //Content of the notification once opened
                .setContentText("hhhhh")
                //This bit will show up in the notification area in devices that support that
                .setTicker("gggggg")
                //Icon that shows up in the notification area
                .setSmallIcon(R.drawable.logo_icon)
                //Set the intent
              //  .setContentIntent(pendingIntentForNotification())
                .setAutoCancel(true)
                //Build the notification with all the stuff you've just set.
                .build();

                NotificationManager notificationManager = (NotificationManager) MyContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
 /*   private PendingIntent pendingIntentForNotification() {
        //Create the intent you want to show when the notification is clicked
        Intent intent = new Intent(getActivity(), MainActivity.class);
        //Add any extras (in this case, that you want to relaunch this fragment)
        //intent.putExtra(MainActivity.EXTRA_FRAGMENT_TO_LAUNCH, MainActivity.TAG_NOTIFICATION_FRAGMENT);

        //This will hold the intent you've created until the notification is tapped.
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, intent, 0);
        return pendingIntent;
    } */

    public void loaddata(){
        myRef.addValueEventListener(new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
            recyclerView.clearOnScrollListeners();
            recyclerView.clearOnChildAttachStateChangeListeners();
            for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                Rider_Ways riderDetails = dataSnapshot1.getValue(Rider_Ways.class);
                list.add(riderDetails);
            }

            adapter  = new WaysAdapter(list,getContext());
            recyclerView.setAdapter(adapter);
            showProgressDialog(false,0);

            FirebaseDatabase database_user = FirebaseDatabase.getInstance();
            DatabaseReference Users = database_user.getReference("Users");
            Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Settings")
                    .child("Notification")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                     notificationsStatut = dataSnapshot.getValue(Boolean.class);

                    if (notificationsStatut){

              mynotif.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int newCount = adapter.getItemCount();
                                int previousItem;
                                try {
                                    previousItem = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Integer.class);
                                }catch (Exception e){
                                    previousItem = 0;
                                }
                                if (previousItem==0 || previousItem > newCount){
                                    Notification_item.setCountItem(adapter.getItemCount());
                                    addNotificationItem(Notification_item.getCountItem());
                                }
                                else if(previousItem < newCount && notificationsStatut){
                                    updateNotification();
                                    Notification_item.setCountItem(newCount);
                                    addNotificationItem(Notification_item.getCountItem());
                                } else if (!notificationsStatut){
                                    Notification_item.setCountItem(newCount);
                                    addNotificationItem(Notification_item.getCountItem());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                showProgressDialog(false,0);
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onCancelled(DatabaseError error) {
            showProgressDialog(false,0);
        }

    });
}
    private void addNotificationItem(int notifItem){
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");
        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Notifications")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(notifItem, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    }
                });
    }
    private void showProgressDialog(boolean show, long time) {
        try {
            if (progress != null) {
                if (show) {

                    progress.setTitle(R.string.loading);
                    progress.setMessage("Syncing …");
                    progress.setCancelable(false);
                    progress.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if(progress!=null && progress.isShowing()) {
                                progress.dismiss();
                                Toast.makeText(getActivity(), "Couldn't connect, please try again later.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, time);
                } else {
                    progress.dismiss();
                }
            }
        }catch(IllegalArgumentException ignored){
        }catch(Exception ignored){
        }
    }



}
