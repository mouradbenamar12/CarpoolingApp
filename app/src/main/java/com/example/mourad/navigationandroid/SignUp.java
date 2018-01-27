package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText editTextFullName,editTextEmail,editTextPhone,editTextPsw,editTextConPsw;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
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
        editTextPsw=(EditText)findViewById(R.id.et_psw);
        editTextConPsw=(EditText)findViewById(R.id.et_confPsw);
        buttonSignUp=(Button)findViewById(R.id.buSignUpNow);

        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduser(editTextEmail.getText().toString(),editTextPsw.getText().toString(),editTextPhone.getText().toString(),editTextFullName.getText().toString());
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
    public void adduser(final String email, final String password, final String phone, final String fullname){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Authentication failed."+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Authentication success.",Toast.LENGTH_SHORT).show();
                    FirebaseDatabase database_user=FirebaseDatabase.getInstance();
                    DatabaseReference Users=database_user.getReference("Users");
                    String id = Users.push().getKey();
                    User user = new User(id,fullname,email,phone,password);
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
