package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OffreInformation extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    protected double latSrc,longSrc;
    protected double latDes,longDes;

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
        LatlngSrc=LatlngSrc.substring(10,LatlngSrc.length()-1);
        LatlngDes=LatlngDes.substring(10,LatlngDes.length()-1);

        String[] latlong1 =  LatlngSrc.split(",");
        latSrc = Double.parseDouble(latlong1[0]);
        longSrc = Double.parseDouble(latlong1[1]);

        String[] latlong2 =  LatlngDes.split(",");
        latDes = Double.parseDouble(latlong2[0]);
        longDes = Double.parseDouble(latlong2[1]);


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
        LatLng source = new LatLng(latSrc, longSrc);

        mMap.addMarker(new MarkerOptions().position(source).title("Source"));

        LatLng destination = new LatLng(latDes, longDes);
        mMap.addMarker(new MarkerOptions().position(destination).title("destination"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(source));

    }
}
