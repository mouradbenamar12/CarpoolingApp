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

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogin;
    private EditText editTextEmail, editTextPsw;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        buttonLogin =(Button)findViewById(R.id.btnSignIn);
        editTextEmail=(EditText)findViewById(R.id.login_email);
        editTextPsw=(EditText)findViewById(R.id.etPsw);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
            }
        });
    }


    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPsw.getText().toString().trim();

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
            editTextPsw.setError("Minimum lenght of password should be 6");
            editTextPsw.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this,"profile user",Toast.LENGTH_LONG).show();
        }
    }


    public void btnSignUp(View view){

        Intent myIntent=new Intent(this,SignUp.class);
        startActivity(myIntent);
    }
    public void btnForgot(View view){

        Intent myIntent=new Intent(this,ForgetPsw.class);
        startActivity(myIntent);
    }
}
