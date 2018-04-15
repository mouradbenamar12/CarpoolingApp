package com.example.mourad.navigationandroid;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

class SharedPref {

    private SharedPreferences mySharedPref ;

    SharedPref(Context context) {
        mySharedPref = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    // this method will save the nightMode State : True or False
    @SuppressLint("ApplySharedPref")
    void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode",state)
        .commit();
    }
    // this method will load the Night Mode State
    Boolean loadNightModeState(){
        return mySharedPref.getBoolean("NightMode", false);
    }
}
