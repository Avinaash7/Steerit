package com.rent.driveit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MainActivity extends AppCompatActivity  {

    public static BookingSelectionModel bookingselection = new BookingSelectionModel();


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    NavController navController;
    FirebaseUser user;
    View headerView;
    TextView navUsername;
    TextView navEmail;
    ImageView img;



    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setupNavigation();

        headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.headername);
        navEmail = (TextView) headerView.findViewById(R.id.headerid);
        img = (ImageView) headerView.findViewById(R.id.profileimg);

        if(user!=null){

            navUsername.setText(user.getDisplayName());

            if(user.getPhotoUrl() != null){
                Picasso.get().load(user.getPhotoUrl()).into(img);
            }
            navEmail.setText(user.getEmail());

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();

        headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.headername);
        navEmail = (TextView) headerView.findViewById(R.id.headerid);
        img = (ImageView) headerView.findViewById(R.id.profileimg);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.i("DeepLink",deepLink.toString());
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            Log.i("Deeplink","Entered");
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            Log.i("Deeplink",referrerUid);
                            createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    }
                });








        navUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.loginActivity);
            }
        });

        navEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.loginActivity);
            }
        });


    }

    private void createAnonymousAccountWithReferrerInfo(final String referrerUid) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Keep track of the referrer in the RTDB. Database calls
                        // will depend on the structure of your app's RTDB.
                        Log.i("Deeplink","Success1");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Log.i("Deeplink","Success2");
                        Log.i("Deeplink",user.getUid());
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("referred_by").setValue(referrerUid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Deeplink",e.getMessage());
                    }
                });
    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this::OnNavigationItemSelected);
    }





    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
            //finish();
        }
    }


    public boolean OnNavigationItemSelected(@NonNull MenuItem menuItem) {
        String title = getString(R.string.app_name);

        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                navController.navigate(R.id.home);
                break;

            case R.id.nav_login:
                navigationView.getMenu().findItem(R.id.nav_login).setChecked(true);
                navController.navigate(R.id.loginActivity);
                
                break;
                
            case R.id.nav_logout:
                // menu.findItem(R.id.nav_logout).setVisible(true);
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(Objects.requireNonNull(getApplicationContext()),gso);
                googleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_okay:
                navigationView.getMenu().findItem(R.id.nav_okay).setChecked(true);
                navController.navigate(R.id.referEarnFragment);
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.smart.betiract");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;

            case R.id.nav_rate:
                String url = "https://play.google.com/store/apps/details?id=com.smart.betiract";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

        }


    // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }


        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }




}