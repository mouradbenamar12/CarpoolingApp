package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ForgetPsw extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_psw);
    }
    public void btnLogin(View view){

        Intent myIntent=new Intent(this,Login.class);
        startActivity(myIntent);
    }
    public void btnSignUp(View view){

        Intent myIntent=new Intent(this,SignUp.class);
        startActivity(myIntent);
    }
}
