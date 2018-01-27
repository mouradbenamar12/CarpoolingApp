package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText editTextFullName,editTextEmail,editTextPhone,editTextPsw,editTextConPsw;
    private Button buttonSignUp;

    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        editTextFullName=(EditText)findViewById(R.id.et_full_name);
        editTextEmail=(EditText)findViewById(R.id.et_email);
        editTextPhone=(EditText)findViewById(R.id.et_phone);
        editTextPsw=(EditText)findViewById(R.id.et_psw);
        editTextConPsw=(EditText)findViewById(R.id.et_confPsw);
        buttonSignUp=(Button)findViewById(R.id.buSignUpNow);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addUser();
            }
        });
    }

    private void addUser() {
        String fullName = editTextFullName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPsw.getText().toString();
        String confPassword = editTextConPsw.getText().toString();

        if ((!TextUtils.isEmpty(fullName)) && (!TextUtils.isEmpty(email)) && (!TextUtils.isEmpty(phone)) &&
                (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(confPassword))) {

            String id = databaseUsers.push().getKey();
            User user = new User(id, fullName,email,phone,password,confPassword);
            databaseUsers.child(id).setValue(user);
            Toast.makeText(this,"User added",Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "You should fill all the fields", Toast.LENGTH_LONG).show();
        }
    }

    public void btnLogin(View view){

        Intent myIntent=new Intent(this,Login.class);
        startActivity(myIntent);
    }
}
