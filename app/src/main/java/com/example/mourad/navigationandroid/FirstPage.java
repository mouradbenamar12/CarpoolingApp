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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class FirstPage extends BaseActivity implements View.OnClickListener {
    private static final String TAG = FirstPage.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001; //Request code for signing in

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Facebook SDK init
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_first_page);

        loginButton = findViewById(R.id.fb_login);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }


                    @Override
                    public void onCancel () {
                        Log.d(TAG, "Login attempt cancelled.");
                    }

                    @Override
                    public void onError (FacebookException e){
                        e.printStackTrace();
                        Log.d(TAG, "Login attempt failed.");
                        deleteAccessToken();
                    }
                }
        );

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

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    PrefUtil prefUtil = new PrefUtil(FirstPage.this);
                    prefUtil.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

            PrefUtil prefUtil = new PrefUtil(FirstPage.this);
            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"),
                    object.getString("gender"), profile_pic.toString());

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
        //facebook requestCode:
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "FB_Authentication success",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser account = mAuth.getCurrentUser();
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
                                        }
                                    });
                            startActivity(new Intent(FirstPage.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "FB_Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
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
