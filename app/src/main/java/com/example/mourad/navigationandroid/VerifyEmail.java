package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyEmail extends AppCompatActivity {

    Button btnSend,btnRefresh;
    TextView txtEmail,txtStatus,et_email,login_email;
    Timer t;
    boolean start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        btnSend = findViewById(R.id.btn_send);
        btnRefresh = findViewById(R.id.btn_refresh);
        txtEmail = findViewById(R.id.txt_email);
        txtStatus = findViewById(R.id.txt_status);
        et_email = findViewById(R.id.et_email);
        login_email = findViewById(R.id.login_email);
        t = new Timer();
        setInfo();
        start=true;

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSend.setEnabled(false);
                FirebaseAuth.getInstance().getCurrentUser()
                        .sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                btnSend.setEnabled(true);
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Verification email sent to :" +email,Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Failed to send Verification email ",Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                if (start){
                    start=false;
                    t.scheduleAtFixedRate(new TimerTask() {

                                              @Override
                                              public void run() {
                                                  refresh();
                                              }

                                          },
                            0,
                            3000);
                }
                start=false;

            }
        });

    }

    private void setInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            txtEmail.setText(new StringBuilder("Email : ").append(user.getEmail()));
        }
        if (user != null) {
            txtStatus.setText(new StringBuilder("Verify :").append(String.valueOf(user.isEmailVerified())));
        }

    }
    private void refresh(){
        FirebaseAuth.getInstance().getCurrentUser()
                .reload()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()){
                            t.cancel();
                            Toast.makeText(getApplicationContext(),"your email "+" << "+user.getEmail()+" >> "+" is verified",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

}
