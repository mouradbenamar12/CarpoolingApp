package com.example.mourad.navigationandroid;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProposeFragment extends Fragment {

    private EditText time,date,number,carid;
    private String src,des;
    protected Button post;
    protected Calendar calender;
    protected String LatlngSrc,LatlngDes;
    protected CircleImageView carImg;
    private Uri mCropImageUri;
    private Uri image;
    protected ProgressBar PG_propose;
    private StorageReference mStorageRef;
    private RelativeLayout activity_car_missed;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_propose, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.propose_a_way);

        date = getView().findViewById(R.id.et_date);
        time = getView().findViewById(R.id.et_time);
        number=getView().findViewById(R.id.et_Number);
        carid = getView().findViewById(R.id.et_carID);
        post = getView().findViewById(R.id.btn_post);
        carImg=getView().findViewById(R.id.car_photo);
        activity_car_missed = getView().findViewById(R.id.activity_car_missed);
        PG_propose=getView().findViewById(R.id.progressbar_propose);


        carImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getContext(),ProposeFragment.this);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                final DatabaseReference Ways = database_user.getReference("Ways");
                final String nom=user.getFullName();
                final String source = src;
                final String destination = des;
                final String date = ProposeFragment.this.date.getText().toString();
                final String time = ProposeFragment.this.time.getText().toString();
                final String numero = number.getText().toString();
                final String carID = carid.getText().toString();
                final String Image_ways = user.getPhotoUrl();
                final String latlngSrc = LatlngSrc;
                final String latlngDes = LatlngDes;
                final String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();

                Calendar calendar2 = Calendar.getInstance();
                String dtnow= ""+calendar2.get(Calendar.DAY_OF_MONTH)+"-"+calendar2.get(Calendar.MONTH)+
                        "-"+calendar2.get(Calendar.YEAR);
                Date daate=null;
                Date daate2=null;
                SimpleDateFormat format = new SimpleDateFormat("d-m-yyyy", Locale.ENGLISH);
                try {
                    daate = format.parse(dtnow);
                    daate2 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date.isEmpty()) {
                    ProposeFragment.this.date.setError("Date is required");
                    ProposeFragment.this.date.requestFocus();
                    return;
                }
                if (daate2.before(daate)){
                    Snackbar snackBar = Snackbar.make(activity_car_missed,"Enter Date Valid",Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    return;
                }
                if (time.isEmpty()) {
                    ProposeFragment.this.time.setError("Time is required");
                    ProposeFragment.this.time.requestFocus();
                    return;
                }
                if (numero.isEmpty()) {
                    number.setError("Phone is required");
                    number.requestFocus();
                    return;
                }
                if (carID.isEmpty()) {
                    carid.setError("CarID is required");
                    carid.requestFocus();
                    return;
                }
                if (image==null){
                    Snackbar snackBar = Snackbar.make(activity_car_missed,"Image Car is Required",Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    return;
                }
                PG_propose.setVisibility(View.VISIBLE);
                if(image!=null){
                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference riversRef = mStorageRef.child("CarImg/"+UID);
                    riversRef.putFile(image)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Rider_Ways way = new Rider_Ways(Image_ways, nom, source, destination, date, time, numero, carID, latlngSrc, latlngDes, UID);
                                    Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                                            .setValue(way, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    Toast.makeText(getContext(), "Posted", Toast.LENGTH_LONG).show();
                                                    PG_propose.setVisibility(View.GONE);
                                                    startActivity(new Intent(getContext(), MainActivity.class));
                                                }
                                            });
                                }
                            });

                }
            }
        });

        SupportPlaceAutocompleteFragment autocompleteFragmentSource  = (SupportPlaceAutocompleteFragment)getChildFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        //ImageView searchSrc = (ImageView)((LinearLayout)autocompleteFragmentSource.getView()).getChildAt(0);
      //  searchSrc.setImageDrawable(getResources().getDrawable(R.drawable.source));

        autocompleteFragmentSource.setHint(" Source ...");
        autocompleteFragmentSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
             //   Log.i(TAG, "Place: " + place.getName());
                Toast.makeText(getContext(),"Place: " + place.getName(),Toast.LENGTH_LONG).show();
                src=place.getName().toString();
                LatlngSrc = place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getContext(),"An error occurred: " + status,Toast.LENGTH_LONG).show();

            }
        });

        SupportPlaceAutocompleteFragment autocompleteFragmentDestination  = (SupportPlaceAutocompleteFragment)getChildFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment2);
        //ImageView searchDest = (ImageView)((LinearLayout)autocompleteFragmentDestination.getView()).getChildAt(0);
        //searchDest.setImageDrawable(getResources().getDrawable(R.drawable.clara1));

        autocompleteFragmentDestination.setHint(" Destination ");
        autocompleteFragmentDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //   Log.i(TAG, "Place: " + place.getName());
                Toast.makeText(getContext(),"Place: " + place.getName(),Toast.LENGTH_LONG).show();
                des=place.getName().toString();
                LatlngDes=place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getContext(),"An error occurred: " + status,Toast.LENGTH_LONG).show();

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }

    private void showTimePicker() {
        //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
        // if you are using the nested fragment then user the
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getFragmentManager(), "timePicker");

    }

    private void showDatePicker() {

        DatePickerFragment date = new DatePickerFragment();
        /*
         * Set Up Current Date Into dialog
         */
        calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /*
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        carImg = getView().findViewById(R.id.car_photo);
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

                Glide.with(ProposeFragment.this).load(image).dontAnimate().into(carImg);

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
                .start(getContext(),ProposeFragment.this);
    }




}
