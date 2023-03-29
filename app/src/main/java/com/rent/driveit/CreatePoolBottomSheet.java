package com.rent.driveit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePoolBottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePoolBottomSheet extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePoolBottomSheet() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePoolBottomSheet.
     */
    // TODO: Rename and change types and number of parameters

    public static CreatePoolBottomSheet newInstance(String param1, String param2) {
        CreatePoolBottomSheet fragment = new CreatePoolBottomSheet();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_pool_bottom_sheet, container, false);
        RangeSlider slidervpick2 =(RangeSlider) v.findViewById(R.id.rangeslide2);
        Slider x = (Slider) v.findViewById(R.id.maxpoolsize);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Create Pool");

        x.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                if(value == 0.0f)
                    return "1";
                else if(value == 1.0f)
                    return "2";
                else if(value == 2.0f)
                    return "3";
                else if(value == 3.0f)
                    return "4";
                else if(value == 4.0f)
                    return "5";
                else if(value == 5.0f)
                    return "6";
                else if(value == 6.0f)
                    return "7";
                return String.format(Locale.US, "%.0f", value);
            }
        });

        slidervpick2.setLabelFormatter(new LabelFormatter() {
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


        return v;
    }
}