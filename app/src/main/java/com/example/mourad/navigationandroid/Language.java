package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public class Language extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,SettingsFragment.class));
    }
    public void fr_img(View view){
        setLanguage("fr");
        Toast.makeText(this, R.string.change_language_succes, Toast.LENGTH_SHORT).show();
    }

    public void ara_img(View view){
        setLanguage("ar");
        Toast.makeText(this, R.string.change_language_succes, Toast.LENGTH_SHORT).show();

    }

    public void eng_img(View view){
        setLanguage("en");
        Toast.makeText(this, R.string.change_language_succes, Toast.LENGTH_SHORT).show();

    }

    public void ger_img(View view){
        setLanguage("de");
        Toast.makeText(this, R.string.change_language_succes, Toast.LENGTH_SHORT).show();

    }

    public void spn_img(View view){
        setLanguage("es");
        Toast.makeText(this, R.string.change_language_succes, Toast.LENGTH_SHORT).show();
    }
}
