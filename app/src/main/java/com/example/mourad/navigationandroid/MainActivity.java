package com.example.mourad.navigationandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected CircleImageView mDisplayImageView;
    protected TextView mNameTextView;
    protected TextView mEmailTextView;
    public User user;
    public FirebaseDatabase mFirebasedata;
    public DatabaseReference myref;
    private String UID;
    private long backPressedTime;
    private Toast Toastback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); */
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebasedata = FirebaseDatabase.getInstance();
        myref = mFirebasedata.getReference();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        UID = mFirebaseUser.getUid();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Showdata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SelectedScreen(R.id.nav_home);

        View navHeaderView = navigationView.getHeaderView(0);

        mDisplayImageView =navHeaderView.findViewById(R.id.image_nav);
        mNameTextView =  navHeaderView.findViewById(R.id.name_nav);
        mEmailTextView =  navHeaderView.findViewById(R.id.email_nav);



    }


    @Override
    public void onBackPressed() {
    /*    if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }  */

      if (backPressedTime + 2000 > System.currentTimeMillis()){
            Toastback.cancel();
            super.onBackPressed();
            return;
        }else {
            Toastback = Toast.makeText(this,"Press back again to exit",Toast.LENGTH_LONG);
            Toastback.show();
        }
        backPressedTime = System.currentTimeMillis();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void SelectedScreen(int id){

        Fragment fragment = null;
        switch (id){
            case R.id.nav_home :
                fragment = new HomeFragment();
                break;
            case R.id.nav_services :
                fragment = new ServicesFragment();
                break;
            case R.id.nav_ways :
                fragment = new WaysFragment();
                break;
            case R.id.nav_account :
                fragment = new AccountFragment();
                break;
            case R.id.nav_setings :
                fragment = new SettingsFragment();
                break;
            case R.id.nav_share :
                fragment = new ShareFragment();
                break;
            case R.id.nav_about :
                fragment = new AboutFragment();
                break;

        }
        if (fragment!=null){
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        SelectedScreen(id);
        return true;
    }
    private void Showdata(DataSnapshot dataSnapshot) {
        user=new User();
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            try {
            user.setBirthday(ds.child(UID).child("Information").getValue(User.class).getBirthday());
            user.setEmail(ds.child(UID).child("Information").getValue(User.class).getEmail());
            user.setFullName(ds.child(UID).child("Information").getValue(User.class).getFullName());
            user.setGender(ds.child(UID).child("Information").getValue(User.class).getGender());
            user.setId(ds.child(UID).child("Information").getValue(User.class).getId());
            user.setPhone(ds.child(UID).child("Information").getValue(User.class).getPhone());
            user.setPhotoUrl(ds.child(UID).child("Information").getValue(User.class).getPhotoUrl());

                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(mDisplayImageView);

            }catch (Exception e){
                e.getMessage();
            }
        }

        mNameTextView.setText(user.getFullName());
        mEmailTextView.setText(user.getEmail());
    }


}
