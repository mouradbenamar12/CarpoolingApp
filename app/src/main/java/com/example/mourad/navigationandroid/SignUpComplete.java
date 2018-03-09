package com.example.mourad.navigationandroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SignUpComplete extends BaseActivity {
    private EditText birthday,fullName,Phone;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private Uri mCropImageUri;
    private ImageButton imageButton;
    private Spinner spinner;
    protected Button complet;
    private User user;
    private Uri image;
    private String imageStorage;
    protected StorageReference mStorageRef;
    //private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_complete);

        fullName=findViewById(R.id.et_fullName);
        Phone=findViewById(R.id.et_phone);
        birthday=findViewById(R.id.birthdayField);
        imageButton=findViewById(R.id.profilephoto);
        complet=findViewById(R.id.button2);
        //progressBar = findViewById(R.id.progressbarUP);

        // Profile Google and Fb
        FirebaseUser _user=FirebaseAuth.getInstance().getCurrentUser();
        image=null;
        assert _user != null;
        if(_user.getPhotoUrl()!=null){
        Glide.with(SignUpComplete.this).load(_user.getPhotoUrl()).into(imageButton);
        fullName.setText(_user.getDisplayName());
        Phone.setText(_user.getPhoneNumber());
        }

        //Spinner
        spinner = findViewById(R.id.spinner);
        // Initializing a String Array
        String[] gender = new String[]{
                "Gender",
                "Male",
                "Female",
        };
        final List<String> genderList = new ArrayList<>(Arrays.asList(gender));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,genderList){
            @Override
            public boolean isEnabled(int position){
                switch (position) {
                    case 0:
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    default:
                        return true;
                }


    }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            // Birthday Calendar
        myCalendar = Calendar.getInstance();

        date  = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpComplete.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        complet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                confirme_Signup(fullName.getText().toString(),Phone.getText().toString(),birthday.getText().toString(),spinner.getSelectedItem().toString());
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthday.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageButton = findViewById(R.id.profilephoto);
        super.onActivityResult(requestCode, resultCode, data);
            // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
            // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                }
            } else {
            // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image= result.getUri();
                Glide.with(SignUpComplete.this).load(image).into(imageButton);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
          //      Exception error = result.getError();
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setMinCropResultSize(500,500)
                .setMaxCropResultSize(800,800)
                .start(this);
    }
    public void editphoto(View view){
        CropImage.startPickImageActivity(this);
    }



    public void confirme_Signup(String fullname_string, String phone_string, String Birthday_string, String gender){

        if (fullname_string.isEmpty()) {
            fullName.setError("Email is required");
            fullName.requestFocus();
            return;
        }
        if (phone_string.isEmpty()) {
            Phone.setError("Email is required");
            Phone.requestFocus();
            return;
        }
        if (Birthday_string.isEmpty()) {
            birthday.setError("Email is required");
            birthday.requestFocus();
            return;
        }
        if (gender.isEmpty()||spinner.getSelectedItem().toString().equals("Gender")) {
            spinner.requestFocus();
            return;
        }

        //Storage
        FirebaseUser _user=FirebaseAuth.getInstance().getCurrentUser();
        assert _user != null;
        String id = _user.getUid();
        if(image==null){
            FirebaseDatabase database_user=FirebaseDatabase.getInstance();
            DatabaseReference Users=database_user.getReference("Users");
            String Name=fullName.getText().toString();
            String PHone=Phone.getText().toString();
            String BIrthday=birthday.getText().toString();
            String Gender=spinner.getSelectedItem().toString();
            String Email= _user.getEmail();
            String Id =_user.getUid();
            user = new User(Id,Name,Email,PHone,BIrthday,Gender,_user.getPhotoUrl().toString());
            Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                    .child("Information")
                    .setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            startActivity(new Intent(SignUpComplete.this, MainActivity.class));
                            finish();


                        }
                    });
        }
        if(image!=null) {
            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference riversRef = mStorageRef.child("Image/" + id);
            riversRef.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                           // progressBar.setVisibility(View.GONE);
                            imageStorage = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(getApplicationContext(), "Image is: " + imageStorage,
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                            DatabaseReference Users = database_user.getReference("Users");
                            String name = fullName.getText().toString();
                            String phone = Phone.getText().toString();
                            String Birthday = birthday.getText().toString();
                            String gender = spinner.getSelectedItem().toString();
                            String email = _user.getEmail();
                            String id = _user.getUid();
                            user = new User(id, name, email, phone, Birthday, gender, imageStorage);
                            Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Information").setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            startActivity(new Intent(SignUpComplete.this, MainActivity.class));
                                            finish();


                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                    //int currentprogress = (int) progress;
                    //progressBar.setProgress(currentprogress);
                }
            });

        }
    }
}
