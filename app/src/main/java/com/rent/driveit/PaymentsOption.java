package com.rent.driveit;

import static android.app.Activity.RESULT_OK;

import static com.google.android.gms.wallet.WalletConstants.ENVIRONMENT_PRODUCTION;
import static com.google.android.gms.wallet.WalletConstants.ENVIRONMENT_TEST;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import com.stripe.android.PaymentConfiguration;

import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentsOption#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentsOption extends Fragment {

    MaterialButton stripeButton;
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret, amount;
    PaymentSheet.CustomerConfiguration customerConfig;
    CollapsingToolbarLayout ctl;

    private static JSONObject baseConfigurationJson() throws JSONException {
        return new JSONObject()
                .put("apiVersion",2)
                .put("apiVersionMinor",0)
                .put("allowedPaymentMethods",new JSONArray().put(getGooglePaymentMethod()));
    }



    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    String name = "Steerit";
    String upiId = "steerit@icici";
    String transactionNote = "pay test";
    String status;
    Uri uri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaymentsOption() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentsOption.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentsOption newInstance(String param1, String param2) {
        PaymentsOption fragment = new PaymentsOption();
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

    void getDetailsStripe(){
        Fuel.INSTANCE.post("https://us-central1-web3job-666ca.cloudfunctions.net/stripe_payments?amt=120000", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(requireActivity(), result.getString("publishableKey"));

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            presentPaymentSheet();
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) { /* handle error */ }
        });
    }


        private void presentPaymentSheet() {
            final PaymentSheet.GooglePayConfiguration googlePayConfiguration =
                    new PaymentSheet.GooglePayConfiguration(
                            PaymentSheet.GooglePayConfiguration.Environment.Test,
                            "US"
                    );

            final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Steerit")
                    .customer(customerConfig)
                    .googlePay(googlePayConfiguration)
                    // Set `allowsDelayedPaymentMethods` to true if your business can handle payment methods
                    // that complete payment after a delay, like SEPA Debit and Sofort.
                    .allowsDelayedPaymentMethods(true)
                    .build();

            paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);



        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_payments_option, container, false);

        FrameLayout gpaybtn = (FrameLayout) rootView.findViewById(R.id.gpaybtn);
        LinearLayout btmpay = requireActivity().findViewById(R.id.fixedbtmpay);
        AppBarLayout abl = requireActivity().findViewById(R.id.appbarlay);
        NestedScrollView nsv = requireActivity().findViewById(R.id.nestedscrll);
        stripeButton = rootView.findViewById(R.id.stripeButton);
        View collapsingview = requireActivity().findViewById(R.id.collapsingtl);

        if( collapsingview instanceof CollapsingToolbarLayout) {
            ctl = (CollapsingToolbarLayout) collapsingview;
            ctl.setTitle("Choose Payment Method");
        }

        MaterialButton upiButton = rootView.findViewById(R.id.upibtn);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        stripeButton.setOnClickListener(view ->{

        getDetailsStripe();
        });



        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(ENVIRONMENT_PRODUCTION).build();
         PaymentsClient paymentsClient = Wallet.getPaymentsClient(requireActivity(),walletOptions);

        btmpay.setVisibility(View.INVISIBLE);
        abl.setExpanded(false);
        ViewCompat.setNestedScrollingEnabled(nsv, false);

        upiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final JSONObject paymentRequestJson = baseConfigurationJson();
                    paymentRequestJson.put("transactionInfo",new JSONObject()
                            .put("totalPriceStatus","FINAL"))
                            .put("totalPrice","1.00")
                            .put("currencyCode","INR")
                            .put("transactionNote","TEST");
                    IsReadyToPayRequest readyToPayRequest;
                    readyToPayRequest = IsReadyToPayRequest.fromJson(baseConfigurationJson().toString());
                    Task<Boolean> task = paymentsClient.isReadyToPay(readyToPayRequest);
                    task.addOnCompleteListener(new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if(task.isSuccessful()){

                                final PaymentDataRequest request = PaymentDataRequest.fromJson(paymentRequestJson.toString());
                                AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),requireActivity(),LOAD_PAYMENT_DATA_REQUEST_CODE);
                                Toast.makeText(requireActivity(), "Succesfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(uri);
                                intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                                someActivityResultLauncher.launch(intent);
                            }
                            else{
                                Toast.makeText(requireActivity(), "Unsuccesfull", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                //makePayment();



            }
        });


        gpaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    uri = getUpiPaymentUri();
                    payWithGPay();

            }
        });

        return rootView;
    }






    private void payWithGPay() {
        if (isAppInstalled(requireActivity())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            someActivityResultLauncher.launch(intent);
            //(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(PaymentsOption.GOOGLE_PAY_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            status = data.getStringExtra("Status").toLowerCase();
                        }
                        if ((RESULT_OK == result.getResultCode()) && status.equals("success")) {
                            Toast.makeText(getContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Transaction Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    requireActivity(), "billingName",
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // value passed in AutoResolveHelper
//            case 991:
//                switch (resultCode) {
//
//                    case Activity.RESULT_OK:
//                        PaymentData paymentData = PaymentData.getFromIntent(data);
//                        handlePaymentSuccess(paymentData);
//                        break;
//
//                    case Activity.RESULT_CANCELED:
//                        // The user cancelled the payment attempt
//                        break;
//
//                    case AutoResolveHelper.RESULT_ERROR:
//                        Status status = AutoResolveHelper.getStatusFromIntent(data);
//                        Toast.makeText(requireActivity(), "Error Bruh", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//
//                // Re-enables the Google Pay payment button.
//
//        }
//    }

    private static Uri getUpiPaymentUri() {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "7305279133@apl")
                .appendQueryParameter("pn", "Avinaash M")
                .appendQueryParameter("mc","0000")
                .appendQueryParameter("tr","9858")
                .appendQueryParameter("tn", "SEND")
                .appendQueryParameter("am", "1.00")
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("mode","02")
                .appendQueryParameter("purpose","00")
                .appendQueryParameter("url","https://steerit.co.in/")
                .build();
    }

    private static JSONObject getGooglePaymentMethod() throws JSONException {
       JSONObject upi = new JSONObject();
       upi.put("type","UPI");
       upi.put("tokenizationSpecification",new JSONObject()
               .put("type","DIRECT"));
       upi.put("parameters",new JSONObject()
               .put("payeeVpa","steerit@icici")
               .put("payeeName","Steerit")
               .put("referenceUrl","https://steerit.co.in/")
               .put("mcc","0000")
               .put("transactionReferenceId","12345"));

       return upi;
    }




    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(requireActivity(),((PaymentSheetResult.Failed) paymentSheetResult).getError().toString(), Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Toast.makeText(requireActivity(), "Completed", Toast.LENGTH_SHORT).show();
            Log.d("Completed","Completed");
        }
    }

}
