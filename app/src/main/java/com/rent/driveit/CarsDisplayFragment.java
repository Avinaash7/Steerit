package com.rent.driveit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarsDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarsDisplayFragment extends Fragment {
    CarDisplayAdapter mAdapter;
    CollapsingToolbarLayout ctl2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarsDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarsDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarsDisplayFragment newInstance(String param1, String param2) {
        CarsDisplayFragment fragment = new CarsDisplayFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_cars_display, container, false);

        CollapsingToolbarLayout collapsingview = requireActivity().findViewById(R.id.collapsingtl);
        LinearLayout btmpay = requireActivity().findViewById(R.id.fixedbtmpay);

        NestedScrollView nsv = requireActivity().findViewById(R.id.nestedscrll);
        AppBarLayout abl = requireActivity().findViewById(R.id.appbarlay);
        Toolbar toolb = requireActivity().findViewById(R.id.toolbarx);


        btmpay.setVisibility(View.GONE);
        collapsingview.setTitle("Pick a Car");


        ViewCompat.setNestedScrollingEnabled(nsv, true);



        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
               menu.clear();
                menuInflater.inflate(R.menu.datadisplaytoolbar_menu, menu);




                abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = false;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (scrollRange == -1) {
                                    scrollRange = appBarLayout.getTotalScrollRange();
                                }
                                if (scrollRange + verticalOffset == 0) {
                                    isShow = true;
                                    try{
                                        showOption(menu.findItem(R.id.sort1));
                                    }
                                    catch(Exception e){
                                        Log.i("Error","Error1");
                                    }

                                } else if (isShow) {
                                    isShow = false;
                                    try{
                                        hideOption(menu.findItem(R.id.sort1));
                                    }
                                    catch(Exception e){
                                        Log.i("Error","Error2");
                                    }
                                }
                            }
                        });

                    }
                });

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                return false;
            }
        });

        RecyclerView mCarsView =rootView.findViewById(R.id.carsrv);
        mCarsView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        FirebaseRecyclerOptions<CarDetailsModel> options =
                new FirebaseRecyclerOptions.Builder<CarDetailsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CarDetails"),CarDetailsModel.class)
                        .build();
        mAdapter = new CarDisplayAdapter(options,requireActivity());
        mAdapter.notifyDataSetChanged();
        mCarsView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        mAdapter.stopListening();
    }

    private void hideOption(MenuItem item) {
        item.setVisible(false);
    }

    private void showOption(MenuItem item) {
        item.setVisible(true);
    }


}