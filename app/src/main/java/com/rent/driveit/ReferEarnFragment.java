package com.rent.driveit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReferEarnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferEarnFragment extends Fragment {

    ViewPager2 myViewPager2;
    ReferralViewPagerAdapter myAdapter;

    String mInvitationUrl = null;

    String url1 = "https://gweiland.com/wp-content/uploads/2023/02/banner-1.png";
    String url2 = "https://gweiland.com/wp-content/uploads/2023/02/share_and_earn_icon-2.png";
    String url3 = "https://gweiland.com/wp-content/uploads/2023/02/share_and_earn_icon.jpg";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReferEarnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReferEarnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReferEarnFragment newInstance(String param1, String param2) {
        ReferEarnFragment fragment = new ReferEarnFragment();
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
        View v = inflater.inflate(R.layout.fragment_refer_earn, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        MaterialButton sharebtn = v.findViewById(R.id.sharebtn);

        myViewPager2 = v.findViewById(R.id.viewpager2l);
        TabLayout tl = v.findViewById(R.id.tablayout);
        myAdapter = new ReferralViewPagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());



        myAdapter.addFragment(new ReferralStepsFragment());
        myAdapter.addFragment(new CreditsDisplayFragemnt());
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);

        new TabLayoutMediator(tl, myViewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position==0){
                            tab.setText("Refer");
                        }
                        else{
                            tab.setText("Your Credits");
                        }

                    }
                }).attach();

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null){
                    createreferallink();
                    if(mInvitationUrl!=null){
                        String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        String subject = String.format("%s wants you to play Steerit!!", referrerName);
                        String invitationLink = mInvitationUrl;
                        String msg = "Let's play Steerit together! Use my referrer link: "
                                + invitationLink;

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);

                    }
                }
                else{
                    Navigation.findNavController(view).navigate(R.id.action_referEarnFragment_to_loginActivity);

                }

            }
        });

        // we are creating array list for storing our image urls.
        ArrayList<ImageSliderDataModel> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = v.findViewById(R.id.referslider);

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



        if(user!=null){
            createreferallink();
            sharebtn.setText("Refer Link");
        }else{
            sharebtn.setText("Create an Account");
        }

        return v;
    }

    void createreferallink(){
        Log.i("Package",requireActivity().getPackageName());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String link = "https://steerit.co.in/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://driveit.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.rent.driveit")
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.rent.driveit")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = String.valueOf(shortDynamicLink.getShortLink());
                        Log.i("Success",mInvitationUrl);
                        // ...
                    }
                });
    }
}