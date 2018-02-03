package com.example.mourad.navigationandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstPage extends BaseActivity implements View.OnClickListener {
    private static final String TAG = FirstPage.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001; //Request code for signing in

    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_page);

        findViewById(R.id.btn_gmail).setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    if (mFirebaseUser != null) {
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "onAuthStateChanged:signed_in " + mFirebaseUser.getDisplayName());
                    } else {
                        if (BuildConfig.DEBUG) Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gmail:
                showProgressDialog();
                signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAutWithGoogle(account);
                } else {
                    hideProgressDialog();
                }
            } else {
                hideProgressDialog();
            }
        } else {
            hideProgressDialog();
        }
    }

    private void firebaseAutWithGoogle(final GoogleSignInAccount account) {
        if (BuildConfig.DEBUG) Log.d(TAG, "firebaseAuthWithGoogle: " + account.getDisplayName());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "signInWithCredential:onComplete: " + task.isSuccessful());

                        if (task.isSuccessful()) {
                            String photoUrl = null;
                            if (account.getPhotoUrl() != null) {
                                photoUrl = account.getPhotoUrl().toString();
                            }
                            User user = new User(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    account.getDisplayName(),
                                    account.getEmail(),
                                    null,
                                    photoUrl);

                            FirebaseDatabase database_user=FirebaseDatabase.getInstance();
                            DatabaseReference Users=database_user.getReference("Users");
                            Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                                    .setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Log.v(TAG, "onComplete Set vaLUE");
                                            startActivity(new Intent(FirstPage.this, MainActivity.class));
                                        }
                                    });
                            if (BuildConfig.DEBUG) Log.v(TAG, "Authentification successful");
                        } else {
                            hideProgressDialog();
                            if (BuildConfig.DEBUG) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Log.v(TAG, "Authentification failed");
                                Toast.makeText(FirstPage.this, "Authentification failed", Toast.LENGTH_SHORT).show();
                                signOut();
                            }
                        }
                    }
                });
    }

    public void btnSignUp(View view){
        Intent myIntent=new Intent(this,SignUp.class);
        startActivity(myIntent);
    }
    public void btnLogin(View view){
        Intent myIntent=new Intent(this,Login.class);
        startActivity(myIntent);
    }
}
