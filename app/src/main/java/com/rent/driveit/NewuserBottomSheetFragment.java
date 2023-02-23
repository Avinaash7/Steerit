package com.rent.driveit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewuserBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewuserBottomSheetFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "phonenumber";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static NewuserBottomSheetFragment newInstance() {
        return new NewuserBottomSheetFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewuserBottomSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewuserBottomSheetFragment newInstance(String param1, String param2) {
        NewuserBottomSheetFragment fragment = new NewuserBottomSheetFragment();
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
        View v = inflater.inflate(R.layout.fragment_newuser_bottom_sheet, container, false);
        MaterialButton regbtn = (MaterialButton) v.findViewById(R.id.regibtn);
        TextView descr = (TextView) v.findViewById(R.id.descrtv);
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.activityloginnavhost);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        Bundle bundle = new Bundle();
        bundle.putString("phonenumber",mParam1);


        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                navController.navigate(R.id.action_newuserBottomSheetFragment_to_registrationFragment,bundle);
            }
        });

        return v;
    }
}