package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class OffreInformation extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    protected double latSrc,longSrc;
    protected double latDes,longDes;
    TextView tvName,tvSource,tvDestination,tvDate,tvTime,tvPhone,tvCarid,tvDuration,tvDistance;
    ImageView imageWay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_offre_information);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        imageWay = findViewById(R.id.ivDetails);
        tvName = findViewById(R.id.tvName);
        tvSource= findViewById(R.id.txtvSource);
        tvDestination=findViewById(R.id.txtvDestiination);
        tvDate=findViewById(R.id.txtvDate);
        tvTime=findViewById(R.id.txtvTime);
        tvPhone =findViewById(R.id.tv_phone);
        tvCarid = findViewById(R.id.txtvCarid);
        tvDistance=findViewById(R.id.tvDistance);
        tvDuration=findViewById(R.id.tvDuration);

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
        tvSource.setText(source);
        tvDestination.setText(destination);
        tvDate.setText(date);
        tvTime.setText(time);
        tvPhone.setText(phone);
        tvCarid.setText(carId);

        Glide.with(getApplicationContext()).load(image).into(imageWay);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        final LatLng source = new LatLng(latSrc, longSrc);


        final LatLng destination = new LatLng(latDes, longDes);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source,5f));

        String serverKey = "AIzaSyCbc1dGroLKt1IoA6MMw1H8bbBGqTFBQf8";
        GoogleDirection.withServerKey(serverKey)
                .from(source)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        mMap.addMarker(new MarkerOptions().position(source).title("Source"));
                        mMap.addMarker(new MarkerOptions().position(destination).title("destination"));
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(OffreInformation.this, directionPositionList, 5, Color.BLUE);
                        Info distanceInfo = leg.getDistance();
                        Info durationInfo = leg.getDuration();
                        String distance = distanceInfo.getText();
                        String duration = durationInfo.getText();
                        tvDistance.setText(distance);
                        tvDuration.setText(duration);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(source);
                        builder.include(destination);
                        LatLngBounds bounds = builder.build();
                        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                        mMap.addPolyline(polylineOptions);
                        mMap.animateCamera(cu);
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });

    }
}
