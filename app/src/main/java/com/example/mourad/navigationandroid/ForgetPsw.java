package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPsw extends AppCompatActivity implements View.OnClickListener {

    private EditText input_email;
    protected Button btnResetPass;
    private RelativeLayout activity_forgot;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_psw);

        input_email = findViewById(R.id.frg_email);
        btnResetPass = findViewById(R.id.reset_psw);
        activity_forgot = findViewById(R.id.activity_forgot_psw);

        btnResetPass.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }
    public void btnLogin(View view){

        Intent myIntent=new Intent(this,Login.class);
        startActivity(myIntent);
    }
    public void btnSignUp(View view){

        Intent myIntent=new Intent(this,SignUp.class);
        startActivity(myIntent);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.reset_psw)
        {
            resetPassword(input_email.getText().toString());
        }
    }

    private void resetPassword(final String email) {

        if (email.isEmpty()) {
            input_email.setError("Email is required");
            input_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Please enter a valid email");
            input_email.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Snackbar snackBar = Snackbar.make(activity_forgot,"We have sent password to email: "+email,Snackbar.LENGTH_SHORT);
                            snackBar.show();
                        }
                        else{
                            Snackbar snackBar = Snackbar.make(activity_forgot,"Failed to send password",Snackbar.LENGTH_SHORT);
                            snackBar.show();
                        }
                    }
                });
    }
}
