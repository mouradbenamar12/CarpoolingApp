package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProposeFragment extends Fragment {
    private EditText src;
    private EditText des;
    private EditText time;
    private EditText date;
    private EditText carid;
    private Button post;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_propose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Propose a Way");

        src = getView().findViewById(R.id.et_source);
        des = getView().findViewById(R.id.et_des);
        date = getView().findViewById(R.id.et_date);
        time = getView().findViewById(R.id.et_time);
        carid = getView().findViewById(R.id.et_carID);
        post = getView().findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                DatabaseReference Ways = database_user.getReference("Ways");
                String source = src.getText().toString();
                String destination = des.getText().toString();
                String dt = date.getText().toString();
                String tm = time.getText().toString();
                String carID = carid.getText().toString();
                Way way = new Way(source,destination,carID,dt,tm);
                Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                        .setValue(way, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getContext(),"Posted",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }
}
