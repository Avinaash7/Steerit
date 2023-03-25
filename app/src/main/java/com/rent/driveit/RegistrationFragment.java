package com.rent.driveit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    AutoCompleteTextView mobnumber;
    MaterialButton regbtn;
    NavController navController;
    Bundle bundle;

    private FirebaseAuth mAuth;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "number";

    // TODO: Rename and change types of parameters
    private String tokenParam;
    private String phonenu;
    private String gmailid;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tokenParam = getArguments().getString("token");
            phonenu = getArguments().getString("phonenumber");
            gmailid = getArguments().getString("googlemailid");

        }

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  (ViewGroup) inflater.inflate(R.layout.fragment_registration, container, false);
        //String number = LoginActivity.userdata.getNumber();
        AutoCompleteTextView emailv = v.findViewById(R.id.emailid);
        AutoCompleteTextView fullnamev = v.findViewById(R.id.fullname);
        AutoCompleteTextView passwordv = v.findViewById(R.id.password);
        TextInputLayout emaillayout = v.findViewById(R.id.emailtextil);
        TextInputLayout passlayout = v.findViewById(R.id.passwordil);

        regbtn = (MaterialButton) v.findViewById(R.id.arfregisterbtn);
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.activityloginnavhost);
        assert navHostFragment != null;
         navController = navHostFragment.getNavController();

        mobnumber = (AutoCompleteTextView) v.findViewById(R.id.mobnotv);
        bundle = new Bundle();

        if(phonenu !=null) {
            mobnumber.setText(phonenu);

        }

        if(tokenParam!=null){
            emaillayout.setVisibility(View.GONE);
            passlayout.setVisibility(View.GONE);
            bundle.putString("GoogleToken",tokenParam);
            bundle.putString("GoogleMaidID",gmailid);
        }



        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobnumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(requireActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                } else if (mobnumber.getText().toString().trim().length() != 10) {
                    mobnumber.setError("Type valid Phone Number");
                }
                else  if(tokenParam == null && emailv.getText().toString().trim().isEmpty()){
                    emailv.setError("Enter Email ID");
                }
                else  if(fullnamev.getText().toString().trim().isEmpty()){
                    fullnamev.setError("Enter Name");
                }
                else  if(tokenParam == null && passwordv.getText().toString().trim().isEmpty()){
                    passwordv.setError("Enter Password");
                }
                else {
                    bundle.putString("PhoneNumber",mobnumber.getText().toString().trim());
                    bundle.putString("Password",passwordv.getText().toString().trim());
                    bundle.putString("Emailid",emailv.getText().toString().trim());
                    bundle.putString("FullName",fullnamev.getText().toString().trim());

                    otpSend();
                }

            }
        });




        return v;
    }

    private void otpSend() {
        //progressBar.setVisibility(View.VISIBLE);
        regbtn.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d("Instant", "onVerificationCompleted:" + credential);
                //mVerificationInProgress = false;
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //binding.progressBar.setVisibility(View.GONE);
                regbtn.setVisibility(View.VISIBLE);
                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                //binding.progressBar.setVisibility(View.GONE);
                Log.d("Instant", "CodeSent:" + token);
                regbtn.setVisibility(View.VISIBLE);
                Toast.makeText(requireActivity(), "OTP is successfully sent.", Toast.LENGTH_SHORT).show();
                //LoginActivity.userdata.setVerifyId(verificationId);
                bundle.putString("PhoneAuthVerificationId",verificationId);
                navController.navigate(R.id.action_registrationFragment_to_otpFragment,bundle);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobnumber.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}