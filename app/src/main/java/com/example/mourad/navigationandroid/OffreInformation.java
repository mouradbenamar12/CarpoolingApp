package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class OffreInformation extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_information);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        TextView tvName,tvSource,tvDestination,tvDate,tvTime,tvPhone,tvCarid;
        ImageView imageWay;

        imageWay = findViewById(R.id.ivDetails);
        tvName = findViewById(R.id.tvName);
        tvSource= findViewById(R.id.txtvSource);
        tvDestination=findViewById(R.id.txtvDestiination);
        tvDate=findViewById(R.id.txtvDate);
        tvTime=findViewById(R.id.txtvTime);
        tvPhone =findViewById(R.id.tv_phone);
        tvCarid = findViewById(R.id.txtvCarid);

        Intent intent = getIntent();

        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("full Name");
        String source=intent.getStringExtra("source");
        String destination = intent.getStringExtra("destination");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String phone = intent.getStringExtra("phone");
        String carId= intent.getStringExtra("carid");

        String LatlngSrc=intent.getStringExtra("LatLngSrc");
        String LatlngDes=intent.getStringExtra("LatLngDes");





        tvName.setText(name);
        tvSource.setText(String.format("Source :%s", source));
        tvDestination.setText(String.format("Destination :%s", destination));
        tvDate.setText(String.format("Date :%s", date));
        tvTime.setText(String.format("Time :%s", time));
        tvPhone.setText(String.format("Phone :%s", phone));
        tvCarid.setText(String.format("CarId :%s", carId));

        Glide.with(getApplicationContext()).load(image).into(imageWay);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
