package com.example.mourad.navigationandroid;

import android.os.Bundle;
import android.view.View;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public class Language extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }
    public void fr_img(View view){
        setLanguage("fr");
    }
    public void ara_img(View view){
        setLanguage("ar");

    }
    public void eng_img(View view){
        setLanguage("en");

    }
    public void ger_img(View view){
        setLanguage("de");

    }
    public void spn_img(View view){
        setLanguage("es");

    }
}
