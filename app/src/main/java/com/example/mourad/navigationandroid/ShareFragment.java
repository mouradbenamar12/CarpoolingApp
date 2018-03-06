package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class ShareFragment extends Fragment {

    Button share_image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Share");

       share_image= getView().findViewById(R.id.sharapp);
        share_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody="Hey check out app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus";
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                getActivity().startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });
    }
}
