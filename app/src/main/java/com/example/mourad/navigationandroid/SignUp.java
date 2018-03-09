package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity {

    private EditText editTextEmail,editTextPsw,editTextConPsw;
    protected Button buttonSignUp;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        editTextEmail=findViewById(R.id.et_email);
        editTextPsw= findViewById(R.id.etPsw);
        editTextConPsw= findViewById(R.id.et_confPsw);
        buttonSignUp= findViewById(R.id.buSignUpNow);
        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduser(editTextEmail.getText().toString(),editTextPsw.getText().toString(),editTextConPsw.getText().toString());
            }

            });
    }

    @Override
    public void onStart(){
        super.onStart();

    }
    public void adduser(final String email, final String password, final String confPassword){



        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPsw.setError("Password is required");
            editTextPsw.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPsw.setError("Minimum length of password should be 6");
            editTextPsw.requestFocus();
            return;
        }
        if (!confPassword.equals(password)) {
            editTextConPsw.setError(" Your password and confirmation password not equal");
            editTextConPsw.requestFocus();
            return;
        }
        if (confPassword.isEmpty()) {
            editTextConPsw.setError("Confirm your password is required");
            editTextConPsw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "Authentication success.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this,VerifyEmail.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void btnLogin(View view){

        Intent myIntent=new Intent(this,Login.class);
        startActivity(myIntent);
    }
}
