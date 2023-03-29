package com.rent.driveit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarPoolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarPoolFragment extends Fragment {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarPoolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarPoolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarPoolFragment newInstance(String param1, String param2) {
        CarPoolFragment fragment = new CarPoolFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_car_pool, container, false);

        MaterialButton createpoolbtn = (MaterialButton) rootView.findViewById(R.id.createpoolbtn);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mMessageRecycler = rootView.findViewById(R.id.recycler_gchat);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        FirebaseRecyclerOptions<PoolRequestMessageModel> options =
                new FirebaseRecyclerOptions.Builder<PoolRequestMessageModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PoolRequests"),PoolRequestMessageModel.class)
                        .build();
        mMessageAdapter = new MessageListAdapter(options,requireActivity());
        mMessageAdapter.notifyDataSetChanged();
        mMessageRecycler.setAdapter(mMessageAdapter);

        createpoolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user==null){
                    navController.navigate(R.id.action_carPoolFragment_to_createPoolBottomSheet);
                    //Toast.makeText(requireActivity(), "Login to create a pool", Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        });






        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        mMessageAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        mMessageAdapter.stopListening();
    }
}