package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class OffreInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_information);

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

        tvName.setText(name);
        tvSource.setText(String.format("Source :%s", source));
        tvDestination.setText(String.format("Destination :%s", destination));
        tvDate.setText(String.format("Date :%s", date));
        tvTime.setText(String.format("Time :%s", time));
        tvPhone.setText(String.format("Phone :%s", phone));
        tvCarid.setText(String.format("CarId :%s", carId));

        Glide.with(getApplicationContext()).load(image).into(imageWay);


    }
}
