package com.rent.driveit;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingDetails extends Fragment {

    Menu menu;
    CollapsingToolbarLayout ctl;
    private FirebaseAuth mAuth;
    private FirebaseUser fuser;
    private DatabaseReference mDatabase;
    AutoCompleteTextView til;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String carlocation;
    private String carname;
    private String fueltype;
    private String fuellimit;
    private String cartype;
    private String carurl;


    public BookingDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingDetails newInstance(String param1, String param2) {
        BookingDetails fragment = new BookingDetails();
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
            carlocation = getArguments().getString("CarLocation");
            carname = getArguments().getString("CarName");
            fuellimit = getArguments().getString("FuelLimit");
            fueltype = getArguments().getString("FuelType");
            cartype = getArguments().getString("CarType");
            carurl = getArguments().getString("CarUrl");
        }


        View collapsingview = requireActivity().findViewById(R.id.collapsingtl);


       // NestedScrollView nsv = requireActivity().findViewById(R.id.nestedscrll);


        //appbarlay.setExpanded(false);
        //ViewCompat.setNestedScrollingEnabled(nsv, false);


        til = requireActivity().findViewById(R.id.payusingbtn);



        if( collapsingview instanceof CollapsingToolbarLayout) {
            ctl = (CollapsingToolbarLayout) collapsingview;
            ctl.setTitle("Car Details");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_booking_details, container, false);

        LinearLayout btmpay = requireActivity().findViewById(R.id.fixedbtmpay);
        btmpay.setVisibility(View.VISIBLE);

        TextView securitystrike = rootView.findViewById(R.id.securitydetv);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();

        securitystrike.setPaintFlags(securitystrike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        ImageView image = rootView.findViewById(R.id.carimagebd);
        TextView sitelocation = rootView.findViewById(R.id.locationtv);
        Chip fuelchip = rootView.findViewById(R.id.fueltypechip);
        ImageView mapicon = rootView.findViewById(R.id.mapicon);
        TextView cartitle = rootView.findViewById(R.id.carnametitle);
        MaterialCheckBox creditbox = rootView.findViewById(R.id.creditcheckbox);


        Picasso.get().load(carurl).into(image);

        sitelocation.setText(carlocation);
        fuelchip.setText(fueltype);
        cartitle.setText(carname);

        creditbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                
            }
        });


        mapicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://goo.gl/maps/MxJRpHbHS9es4sfq6"));
                startActivity(intent);
            }
        });

        mDatabase.child("users").child(fuser.getUid()).child("credits").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    creditbox.setText(String.format("Use your ₹%s Steer credits",task.getResult().getValue()));
                    Log.d("firebase", String.format("Use your ₹%s credit",task.getResult().getValue()));
                }
            }
        });


        //TextInputLayout til = (TextInputLayout) requireActivity().findViewById(R.id.payusingbtn);
        AppBarLayout appbarlay = requireActivity().findViewById(R.id.appbarlay);
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment2);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();



        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.bookingdetailsmenu, menu);
                //menu.findItem(R.id.sort1).setVisible(false);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                return false;
            }
        });

        til.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Clicked","Cliekd");
                navController.navigate(R.id.action_bookingDetails_to_paymentsOption);
            }
        });




        return rootView;
    }

    private void hideOption(MenuItem item) {
        item.setVisible(false);
    }

    private void showOption(MenuItem item) {
        item.setVisible(true);
    }









}