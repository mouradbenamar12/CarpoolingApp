package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OffreInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_information);
        TextView tvName,tvPhone;
        tvName = findViewById(R.id.tvName);
        tvPhone =findViewById(R.id.tvDetails);

        Intent intent = getIntent();
        String name = intent.getStringExtra("full Name");
        String phone = intent.getStringExtra("phone");
        tvName.setText(phone);
        tvPhone.setText(name);


    }
}
