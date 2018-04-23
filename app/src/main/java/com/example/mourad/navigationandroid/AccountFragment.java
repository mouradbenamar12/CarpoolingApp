package com.example.mourad.navigationandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {
    private EditText birthday,fullName,Phone;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private Uri mCropImageUri;
    private CircleImageView imageButton;
    private Spinner spinner;
    protected Button complet;
    private User user;
    private Uri image;
    private String imageStorage;
    protected StorageReference mStorageRef;
    DatabaseReference usr;
    protected ProgressBar progressBar;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_account, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = new User();
        getActivity().setTitle(R.string.my_account);
        usr=FirebaseDatabase.getInstance().getReference("Users");
        usr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Information").getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fullName=getView().findViewById(R.id.et_fullName_acc);
        Phone=getView().findViewById(R.id.et_phone_acc);
        birthday=getView().findViewById(R.id.birthdayField_acc);
        imageButton=getView().findViewById(R.id.profilephoto_acc);
        complet=getView().findViewById(R.id.button2_acc);
        spinner=getView().findViewById(R.id.spinner_acc);
        progressBar = getView().findViewById(R.id.progressbarUP);

        // Profile Google and Fb

        //Spinner
        spinner = getView().findViewById(R.id.spinner_acc);
        // Initializing a String Array
        String[] gender = new String[]{
                "Gender",
                "Male",
                "Female",
        };
        final List<String> genderList = new ArrayList<>(Arrays.asList(gender));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_item,genderList){
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
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        complet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showProgressDialog();
                confirme_Signup(fullName.getText().toString(),Phone.getText().toString(),birthday.getText().toString(),spinner.getSelectedItem().toString());


            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getContext(),AccountFragment.this);
            }
        });
        image=null;
        if(user.getPhotoUrl()!=null){
            Glide.with(AccountFragment.this).load(user.getPhotoUrl()).into(imageButton);
            fullName.setText(user.getFullName());
            Phone.setText(user.getPhone());
            birthday.setText(user.getBirthday());
            spinner.setSelection(spinnerArrayAdapter.getPosition(user.getGender()));
        }

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthday.setText(sdf.format(myCalendar.getTime()));
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        imageButton = getView().findViewById(R.id.profilephoto_acc);
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == -1) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
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
            if (resultCode == -1) {
                image= result.getUri();

                Glide.with(AccountFragment.this).load(image).into(imageButton);

            } else switch (resultCode) {
                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                    //      Exception error = result.getError();
                    break;
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .setMinCropWindowSize(500,500)
                .start(getContext(),AccountFragment.this);
    }

    public void confirme_Signup(String fullname_string, String phone_string, String Birthday_string, String gender){

        if (fullname_string.isEmpty()) {
            fullName.setError("Full Name is required");
            fullName.requestFocus();
            return;
        }
        if (phone_string.isEmpty()) {
            Phone.setError("Phone is required");
            Phone.requestFocus();
            return;
        }
        if (Birthday_string.isEmpty()) {
            birthday.setError("Birthday is required");
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
            final DatabaseReference Users=database_user.getReference("Users");
            final String Name=fullName.getText().toString();
            final String PHone=Phone.getText().toString();
            String BIrthday=birthday.getText().toString();
            String Gender=spinner.getSelectedItem().toString();
            String Email= user.getEmail();
            String Id =user.getId();

            if (Name.isEmpty()) {
                fullName.setError("Full Name is required");
                fullName.requestFocus();
                return;
            }
            if (PHone.isEmpty()) {
                Phone.setError("Phone is required");
                Phone.requestFocus();
                return;
            }
            if (BIrthday.isEmpty()) {
                birthday.setError("Birthday is required");
                birthday.requestFocus();
                return;
            }
            if (Gender.isEmpty()||spinner.getSelectedItem().toString().equals("Gender")) {
                spinner.requestFocus();
                return;
            }

            user = new User(Id,Name,Email,PHone,BIrthday,Gender, user.getPhotoUrl());

            Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                    .child("Information")
                    .setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        }
                    });
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference Ways = db.getReference("Ways");
            Ways.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                                .child("full_Name")
                                .setValue(Name);
                        Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                                .child("phone")
                                .setValue(PHone);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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
                            progressBar.setVisibility(View.GONE);

                            imageStorage = taskSnapshot.getDownloadUrl().toString();
                            FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                            DatabaseReference Users = database_user.getReference("Users");
                            final DatabaseReference Ways = database_user.getReference("Ways");
                            String name = fullName.getText().toString();
                            String phone = Phone.getText().toString();
                            String Birthday = birthday.getText().toString();
                            String gender = spinner.getSelectedItem().toString();
                            String email = user.getEmail();
                            String id = user.getId();
                            user = new User(id, name, email, phone, Birthday, gender, imageStorage);
                            Users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Information").setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                  //  Toast.makeText(getContext(),"Update Success",Toast.LENGTH_LONG).show();
                                }
                            });

                            Ways.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("phone".replace(".", ","))
                                                .setValue(user.getPhone(), new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    }
                                                });
                                        Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("full_Name".replace(".", ","))
                                                .setValue(user.getFullName(), new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    }
                                                });
                                        Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("image_ways".replace(".", ","))
                                                .setValue(user.getPhotoUrl(), new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

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
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int currentprogress = (int) progress;
                    System.out.println(currentprogress);
                    progressBar.setProgress(currentprogress);
                }
            });

        }
    }
}
