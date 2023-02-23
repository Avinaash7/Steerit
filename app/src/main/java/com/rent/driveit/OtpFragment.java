package com.rent.driveit;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtpFragment extends Fragment {
    String verificationId;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;

    //Adding a member variable for the key verification in progress
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    //Adding a member variable for PhoneAuthProvider.ForceResendingToken callback.
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseFirestore db;
    AuthCredential credent;
    GoogleSignInAccount signedInAccount;
    Map<String, Object> data;
    UserProfileChangeRequest profileUpdates;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String phoneParam;
    private String passwordParam;
    private String googletokenParam;
    private String emailParam;
    private String fullnameParam;
    private String phoneauthid;
    private String googlemailid;


    public OtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtpFragment newInstance(String param1, String param2) {
        OtpFragment fragment = new OtpFragment();
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
            passwordParam = getArguments().getString("Password");
            phoneParam = getArguments().getString("PhoneNumber");
            googletokenParam = getArguments().getString("GoogleToken");
            emailParam = getArguments().getString("Emailid");
            fullnameParam = getArguments().getString("FullName");
            phoneauthid = getArguments().getString("PhoneAuthVerificationId");
            googlemailid = getArguments().getString("GoogleMaidID");
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_otp, container, false);
        PinView pinview = v.findViewById(R.id.firstPinView);
        pinview.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        data = new HashMap<>();
        profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullnameParam)
                .build();

        if(googletokenParam!=null){
            credent = GoogleAuthProvider.getCredential(googletokenParam, null);
            signedInAccount = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(requireActivity()));
        }
        else{

            credent = EmailAuthProvider.getCredential(emailParam, passwordParam);
        }





        pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().length()==6){
                    //Toast.makeText(requireActivity(), "Done", Toast.LENGTH_SHORT).show();
                    //Log.i("verifycode",LoginActivity.userdata.getVerifyId());
                    Log.i("code",charSequence.toString());

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneauthid, charSequence.toString());

                    if(mAuth.getCurrentUser()==null){
                            //Non-reffered users
                        mAuth
                                .signInWithCredential(credent)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                                                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("TAG", "linkWithCredential:success");
                                                                FirebaseUser user = task.getResult().getUser();


                                                                if(user!=null){
                                                                    // Add a new document with a generated id.

                                                                    data.put("FullName", fullnameParam);

                                                                    if(googletokenParam!=null){
                                                                        data.put("EmailID", signedInAccount.getEmail());
                                                                    }
                                                                    else{
                                                                        data.put("EmailID",emailParam);
                                                                        data.put("Password",passwordParam);
                                                                    }


                                                                    data.put("PhoneNumber", "+91"+phoneParam);

                                                                    db.collection("Users")
                                                                            .add(data)
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    Log.d("Doc done", "DocumentSnapshot written with ID: " + documentReference.getId());
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.w("Doc Error", "Error adding document", e);
                                                                                }
                                                                            });

                                                                    if(googletokenParam==null){
                                                                        user.updateProfile(profileUpdates)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            Log.d("TAG", "User profile updated.");
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }


                                                                }

                                                            } else {
                                                                Log.w("TAG", "linkWithCredential:failure", task.getException());
                                                                Toast.makeText(requireActivity(), "Authentication failed.",
                                                                      Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });






                                            //binding.progressBarVerify.setVisibility(View.VISIBLE);
                                            // btnVerify.setVisibility(View.INVISIBLE);

                                            Toast.makeText(requireActivity(), "Welcome...", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // binding.progressBarVerify.setVisibility(View.GONE);
                                            // binding.btnVerify.setVisibility(View.VISIBLE);
                                            Toast.makeText(requireActivity(), "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{

                        //Reffered people

                        mAuth.getCurrentUser().linkWithCredential(credent)
                                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            mAuth.getCurrentUser().linkWithCredential(credential)
                                                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if(task.isSuccessful()){
                                                                        Log.d("Hello", "linkWithCredential:success");
                                                                        FirebaseUser user = task.getResult().getUser();

                                                                        if(user!=null){
                                                                            // Add a new document with a generated id.

                                                                            data.put("FullName", fullnameParam);

                                                                            if(googletokenParam!=null){
                                                                                data.put("EmailID", googlemailid);
                                                                            }
                                                                            else{
                                                                                data.put("EmailID",emailParam);
                                                                                data.put("Password",passwordParam);
                                                                            }


                                                                            data.put("PhoneNumber", "+91"+phoneParam);

                                                                            db.collection("Users")
                                                                                    .add(data)
                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                                            Log.d("Doc done", "DocumentSnapshot written with ID: " + documentReference.getId());
                                                                                            user.updateProfile(profileUpdates)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                DatabaseReference userRecord =
                                                                                                                        FirebaseDatabase.getInstance().getReference()
                                                                                                                                .child("users")
                                                                                                                                .child(user.getUid());
                                                                                                                userRecord.child("last_signin_at").setValue(ServerValue.TIMESTAMP);
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.w("Doc Error", "Error adding document", e);
                                                                                        }
                                                                                    });




                                                                        }
                                                                        else {
                                                                            Log.w("Hello", "linkWithCredential:failure", task.getException());
                                                                            Toast.makeText(requireActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                    else {
                                                                        Log.w("Hello", "linkWithCredential2:failure", task.getException());
                                                                        Toast.makeText(requireActivity(), "Authentication failed2.",
                                                                                Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }
                                                            });



                                        } else {
                                            Log.w("Hello", "linkWithCredential:failure", task.getException());
                                            Toast.makeText(requireActivity(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }




                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return v;
    }


}