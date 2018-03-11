package com.example.mourad.navigationandroid;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {

    private boolean notificationsStatut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Settings");

        final Switch notificationSwitch = view.findViewById(R.id.notifications);
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");
        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Settings")
                .child("Notification")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        notificationsStatut = dataSnapshot.getValue(Boolean.class);
                        notificationSwitch.setChecked(notificationsStatut);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //set the switch to ON

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){

                    addSwitchNotification(true);

                }else{

                    addSwitchNotification(false);

                }

            }
        });
    }
    private void addSwitchNotification(boolean notifItem){
        FirebaseDatabase database_user = FirebaseDatabase.getInstance();
        DatabaseReference Users = database_user.getReference("Users");
        Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Settings")
                .child("Notification")
                .setValue(notifItem, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    }
                });
    }
}
