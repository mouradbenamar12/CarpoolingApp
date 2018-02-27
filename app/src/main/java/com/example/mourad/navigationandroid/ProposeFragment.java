package com.example.mourad.navigationandroid;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;


public class ProposeFragment extends Fragment {

    private EditText name,time,date,number,carid;
    private String src,des;
    protected Button post;
    protected Calendar calender;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_propose, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Propose a Way");

        name = getView().findViewById(R.id.et_Full_Name);
        date = getView().findViewById(R.id.et_date);
        time = getView().findViewById(R.id.et_time);
        number=getView().findViewById(R.id.et_Number);
        carid = getView().findViewById(R.id.et_carID);
        post = getView().findViewById(R.id.btn_post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User();
                FirebaseDatabase database_user = FirebaseDatabase.getInstance();
                DatabaseReference Ways = database_user.getReference("Ways");
                String nom=name.getText().toString();
                String source = src;
                String destination = des;
                String date = ProposeFragment.this.date.getText().toString();
                String time = ProposeFragment.this.time.getText().toString();
                String numero = number.getText().toString();
                String carID = carid.getText().toString();
                String Image_ways = user.getPhotoUrl();

                Rider_Ways way = new Rider_Ways(Image_ways, nom, source, destination,date,time,numero,carID);
                Ways.child(FirebaseAuth.getInstance().getCurrentUser().getUid().replace(".", ","))
                        .setValue(way, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getContext(),"Posted",Toast.LENGTH_LONG).show();
                            }
                        });
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




}
