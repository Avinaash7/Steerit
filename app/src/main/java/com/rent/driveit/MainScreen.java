package com.rent.driveit;


import static android.content.Context.MODE_PRIVATE;
import static com.rent.driveit.MainActivity.bookingselection;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.smarteist.autoimageslider.SliderView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreen extends Fragment {

    String url1 = "https://gweiland.com/wp-content/uploads/2023/01/car-png-16845.png";
    String url2 = "https://gweiland.com/wp-content/uploads/2023/01/car5.png";
    String url3 = "https://gweiland.com/wp-content/uploads/2023/01/car-png-16829.png";

    AutoCompleteTextView simpleautoc;
    long CurrentExactTimeInMillis;
    long todaystimeinmillisecond;
    Calendar calendar;
    MaterialDatePicker<Long> materialdate;
    SharedPreferences sharedPreferences;
    SharedPreferences sh_get;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static MainScreen newInstance(String param1, String param2) {
        MainScreen fragment = new MainScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        CurrentExactTimeInMillis = Instant.now().toEpochMilli();
        todaystimeinmillisecond = MaterialDatePicker.todayInUtcMilliseconds();
        calendar.clear();
        calendar.setTimeInMillis(todaystimeinmillisecond);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Okayy","yess");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);
        simpleautoc = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        MaterialButton btn = (MaterialButton) rootView.findViewById(R.id.findcarbtn);

        Slider slidervpick =(Slider)rootView.findViewById(R.id.slidervpick);
        AutoCompleteTextView datepickertv = (AutoCompleteTextView) rootView.findViewById(R.id.datepicker);
        MaterialButtonToggleGroup btn_togglegrp = (MaterialButtonToggleGroup) rootView.findViewById(R.id.toggleButton);


        // we are creating array list for storing our image urls.
        ArrayList<ImageSliderDataModel> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = rootView.findViewById(R.id.imgslider);

        // adding the urls inside array list
        sliderDataArrayList.add(new ImageSliderDataModel(url1));
        sliderDataArrayList.add(new ImageSliderDataModel(url2));
        sliderDataArrayList.add(new ImageSliderDataModel(url3));

        // passing this array list inside our adapter class.
        ImageSliderAdapter adapter = new ImageSliderAdapter(requireContext(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();



        CurrentExactTimeInMillis = Instant.now().toEpochMilli();

        long december = calendar.getTimeInMillis();
        long yesterdayInMillis = todaystimeinmillisecond - 86400000;
        long duration = CurrentExactTimeInMillis - todaystimeinmillisecond;

        //5.5 hrs + 2 hrs buffer
        CalendarConstraints.Builder constraintbuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.from(yesterdayInMillis + duration + 19800000  + 7200000));
        constraintbuilder.setStart(todaystimeinmillisecond);
        constraintbuilder.setEnd(december);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select the Dates !");

        sh_get = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        long date1 = sh_get.getLong("pickupDate",1);

        String datetext = sh_get.getString("headerText","");

        if(date1!=1 ){
            builder.setSelection(date1);
            datepickertv.setText(datetext);
        }

        builder.setCalendarConstraints(constraintbuilder.build());
         materialdate = builder.build();





        datepickertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialdate.show(getParentFragmentManager(),"Date Picker");

            }
        });

        materialdate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);

                datepickertv.setText(materialdate.getHeaderText());
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("headerText", materialdate.getHeaderText());
                myEdit.apply();
            }
        });



        slidervpick.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                if(value == 0.0f)
                    return "12 AM";
                else if(value == 1.0f)
                    return "1 AM";
                else if(value == 2.0f)
                    return "2 AM";
                else if(value == 3.0f)
                    return "3 AM";
                else if(value == 4.0f)
                    return "4 AM";
                else if(value == 5.0f)
                    return "5 AM";
                else if(value == 6.0f)
                    return "6 AM";
                else if(value == 7.0f)
                    return "7 AM";
                else if(value == 8.0f)
                    return "8 AM";
                else if(value == 9.0f)
                    return "9 AM";
                else if(value == 10.0f)
                    return "10 AM";
                else if(value == 11.0f)
                    return "11 AM";
                else if(value == 12.0f)
                    return "12 PM";
                else if(value == 13.0f)
                    return "1 PM";
                else if(value == 14.0f)
                    return "2 PM";
                else if(value == 15.0f)
                    return "3 PM";
                else if(value == 16.0f)
                    return "4 PM";
                else if(value == 17.0f)
                    return "5 PM";
                else if(value == 18.0f)
                    return "6 PM";
                else if(value == 19.0f)
                    return "7 PM";
                else if(value == 20.0f)
                    return "8 PM";
                else if(value == 21.0f)
                    return "9 PM";
                else if(value == 22.0f)
                    return "10 PM";
                else if(value == 23.0f)
                    return "11 PM";
                return String.format(Locale.US, "%.0f", value);
            }
        });



        slidervpick.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

            }
        });








        btn_togglegrp.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialButton btn = btn_togglegrp.findViewById(btn_togglegrp.getCheckedButtonId());
                Log.i(btn.getText().toString(),"ButtonText");
                long x = Instant.now().toEpochMilli();
                ZonedDateTime dateTime = Instant.ofEpochMilli(x).atZone(ZoneId.of("Asia/Kolkata"));
                int current_hour = dateTime.getHour();
                int current_minute = dateTime.getMinute();

                sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);

                if(materialdate.getSelection() == null){
                    Toast.makeText(getContext(),"Pick dates before proceeding",Toast.LENGTH_LONG).show();
                    Log.i("Error","Null");
                }else{
                    Log.i(String.valueOf(materialdate.getSelection()),"Not Nullx");

                    if(materialdate.getSelection()==todaystimeinmillisecond){
                        if(current_minute<30){
                            if(slidervpick.getValue()<current_hour+2){
                                Toast.makeText(getContext(), "Minimum 2 Hours Prior Booking", Toast.LENGTH_SHORT).show();

                            }
                            else{

                                bookingselection.setPickupHour(slidervpick.getValue());
                                bookingselection.setPickupDateInMillis(materialdate.getSelection());
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putLong("pickupDate",materialdate.getSelection() );
                                myEdit.putFloat("pickupTime",slidervpick.getValue() );

                                myEdit.apply();

                                Navigation.findNavController(view).navigate(R.id.action_home_to_dataDisplayActivity);
                            }
                        }
                        else {
                            if(slidervpick.getValue()<=current_hour+2){
                                Toast.makeText(getContext(), "Minimum 2 Hours Prior Booking", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                bookingselection.setPickupHour(slidervpick.getValue());
                                bookingselection.setPickupDateInMillis(materialdate.getSelection());
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putLong("pickupDate",materialdate.getSelection() );
                                myEdit.putFloat("pickupTime",slidervpick.getValue() );

                                myEdit.apply();
                                Navigation.findNavController(view).navigate(R.id.action_home_to_dataDisplayActivity);
                            }
                        }

                    }

                    else{
                        bookingselection.setPickupHour(slidervpick.getValue());
                        bookingselection.setPickupDateInMillis(materialdate.getSelection());
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putLong("pickupDate",materialdate.getSelection() );
                        myEdit.putFloat("pickupTime",slidervpick.getValue() );

                        myEdit.apply();
                        Navigation.findNavController(view).navigate(R.id.action_home_to_dataDisplayActivity);
                    }
                }

                //


            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        String locations[] = getResources().getStringArray(R.array.Locations);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(),R.layout.dropdown_item,locations);
        simpleautoc.setAdapter(arrayAdapter);
    }


}