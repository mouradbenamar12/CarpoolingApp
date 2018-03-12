package com.example.mourad.navigationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    Button btnSend;
    TextView txtEmail,et_email,login_email;
    Timer t;
    private  int cpt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_email);

        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

        //Timer refresh
        t= new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      refresh();
                                  }

                              },
                0,
                3000);

        btnSend = findViewById(R.id.btn_send);
        txtEmail = findViewById(R.id.txt_email);
        et_email = findViewById(R.id.et_email);
        login_email = findViewById(R.id.login_email);
        setInfo();
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
            }
        });

    }

    private void setInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String str1="A verification link was sent to ";
            String str2=" when you first created this account. Please click on that link to verify your email address. if you cannot find it you may request a new one below";
            String Email= "<font color='#EE0000'>"+ user.getEmail() +"</font>";
            txtEmail.setText(Html.fromHtml(str1+Email+str2));
        }

    }
    private void refresh(){
        FirebaseAuth.getInstance().getCurrentUser()
                .reload()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            if (cpt==0){
                            t.cancel();
                            Toast.makeText(getApplicationContext(), "your email is verified", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), SignUpComplete.class);
                            startActivity(intent);
                            finish();
                            cpt=cpt+1;
                            }
                            else if (cpt>0){
                                t.cancel();
                            }
                        }
                    }
                });
    }

}
