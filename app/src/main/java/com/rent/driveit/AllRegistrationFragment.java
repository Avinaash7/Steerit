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
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRegistrationFragment extends Fragment {

    private FirebaseAuth mAuth;
    GoogleSignInAccount gaccount;
    NavController navController;
    GoogleSignInOptions gso;
    AuthCredential gcredent;
    private final static int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    Bundle bundle;
    private FirebaseFunctions mFunctions;
    CallbackManager mCallbackManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "number";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllRegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRegistrationFragment newInstance(String param1, String param2) {
        AllRegistrationFragment fragment = new AllRegistrationFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_registration, container, false);
        mFunctions = FirebaseFunctions.getInstance();
        MaterialButton btn = (MaterialButton) view.findViewById(R.id.arfcontinuebtn);
        ImageView gsignbtn = (ImageView) view.findViewById(R.id.googlebtn);
        LoginButton fbbtn = (LoginButton) view.findViewById(R.id.facebookloginbtn);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        fbbtn.setReadPermissions("email", "public_profile");
        fbbtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
            }
        });

        bundle = new Bundle();

        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.activityloginnavhost);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        mAuth = FirebaseAuth.getInstance();



        AutoCompleteTextView autotv = (AutoCompleteTextView) view.findViewById(R.id.numberatv);

        gsignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bundle.putString("phonenumber", autotv.getText().toString().trim());
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                try {
                    Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(autotv.getText().toString().trim(),"IN");
                    Log.i("Number",phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164));

                    Task<String> phoneres = isphonenumberexists(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164)).addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().equals("true")){
                                    Toast.makeText(requireActivity(), "Number Already Exists, Login", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.action_allRegistrationFragment_to_loginFragment);
                                }
                                else if(task.getResult().equals("false")){
                                    Toast.makeText(requireActivity(), "New Number", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.action_allRegistrationFragment_to_newuserBottomSheetFragment,bundle);
                                }
                            }
                            else{
                                Toast.makeText(requireActivity(), "Server is busy,Try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (NumberParseException e) {
                    Log.i("NumberParseException was thrown: ",e.getMessage());
                }



            }
        });



        return view;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                 gaccount = task.getResult(ApiException.class);
                gcredent = GoogleAuthProvider.getCredential(gaccount.getIdToken(), null);

                bundle.putString("token", gaccount.getIdToken());
                bundle.putString("googlemailid",gaccount.getEmail());
                Task<String> res = isemailexists(gaccount.getEmail()).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()){
                            Log.i("AddMessage1",task.getResult());
                            if(task.getResult().equals("true")){
                                Toast.makeText(requireActivity(), "User already exists, Signin", Toast.LENGTH_SHORT).show();
                                mAuth.signInWithCredential(gcredent)
                                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("TAG", "signInWithCredential:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("TAG", "signInWithCredential:failure", task.getException());

                                                }
                                            }
                                        });
                            }
                            else if(task.getResult().equals("false")) {
                                Toast.makeText(requireActivity(), "New User", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.action_allRegistrationFragment_to_registrationFragment,bundle);
                            }

                        }
                        else{
                            Toast.makeText(requireActivity(), "Server is busy, try again later", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            } catch (ApiException e) {
                //pb4.setVisibility(View.INVISIBLE);
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {

                    case CommonStatusCodes.NETWORK_ERROR:
                        Toast.makeText(requireActivity(), "Check your internet", Toast.LENGTH_SHORT).show();
                        break;

                    case CommonStatusCodes.CANCELED:
                        Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        break;

                    case CommonStatusCodes.INVALID_ACCOUNT:
                        Toast.makeText(requireActivity(), "Invalid Account", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }
    }


    private Task<String> isemailexists(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("email", text);


        return mFunctions
                .getHttpsCallable("checkifemailexists")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                        return (String) task.getResult().getData().toString();
                    }
                });
    }

    private Task<String> isphonenumberexists(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("phone", text);


        return mFunctions
                .getHttpsCallable("checkIfPhoneExists")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                        return (String) task.getResult().getData().toString();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(requireActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }






}