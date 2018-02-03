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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText editTextFullName,editTextEmail,editTextPhone,editTextPsw,editTextConPsw;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        editTextFullName=(EditText)findViewById(R.id.et_FullName);
        editTextEmail=(EditText)findViewById(R.id.et_email);
        editTextPhone=(EditText)findViewById(R.id.et_phone);
        editTextPsw=(EditText)findViewById(R.id.etPsw);
        editTextConPsw=(EditText)findViewById(R.id.et_confPsw);
        buttonSignUp=(Button)findViewById(R.id.buSignUpNow);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduser(editTextEmail.getText().toString(),editTextPsw.getText().toString(),editTextPhone.getText().toString(),editTextFullName.getText().toString(),editTextConPsw.getText().toString());
            }

            });
        mAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Toast.makeText(getApplicationContext(),user.getEmail(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"user null",Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    public void adduser(final String email, final String password, final String phone, final String fullname, final String confPassword){

        if (fullname.isEmpty()) {
            editTextFullName.setError("Full Name is required");
            editTextFullName.requestFocus();
            return;
        }

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
        if (phone.isEmpty()) {
            editTextPhone.setError("Phone is required");
            editTextPhone.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            editTextPhone.setError("Please enter a valid phone");
            editTextPhone.requestFocus();
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
                    FirebaseDatabase database_user=FirebaseDatabase.getInstance();
                    DatabaseReference Users=database_user.getReference("Users");
                    final String id = Users.push().getKey();
                    User user = new User(id,fullname,email,phone,password,confPassword);
                    Users.child(id).setValue(user);
                }
            }
        });

    }

    public void btnLogin(View view){

        Intent myIntent=new Intent(this,Login.class);
        startActivity(myIntent);
    }
}
