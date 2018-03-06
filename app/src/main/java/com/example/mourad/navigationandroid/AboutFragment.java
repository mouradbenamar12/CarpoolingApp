package com.example.mourad.navigationandroid;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


public class AboutFragment extends Fragment {

    WebView tvAbout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About");

        String htmlAsString = getString(R.string.html);
        tvAbout = getActivity().findViewById(R.id.tvAbout);
        tvAbout.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
    }
}
