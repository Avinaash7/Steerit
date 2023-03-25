package com.rent.driveit;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CarDisplayAdapter extends FirebaseRecyclerAdapter<CarDetailsModel,CarDisplayAdapter.myviewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    Bundle bundle;
    SharedPreferences sh_get;
    long pickupdate,dropoffDate;
    float pickuptime,dropofftime;
    int freekms=0;

    public CarDisplayAdapter(@NonNull FirebaseRecyclerOptions<CarDetailsModel> options, Context context) {
        super(options);
        this.context = context;
        bundle = new Bundle();
        sh_get = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        pickupdate = sh_get.getLong("pickupDate",0);
        dropoffDate = sh_get.getLong("dropoffDate",0);
        pickuptime = sh_get.getFloat("pickupTime",0.0f);
        dropofftime = sh_get.getFloat("dropoffTime",0.0f);
        Log.i("Called","XX");
    }



    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull CarDetailsModel model) {

        holder.fuelType.setText(model.getFuel_Type());
        holder.fuelLimit.setText(String.valueOf(freekms)+" Kms");
        holder.carType.setText(model.getCar_type());
        holder.seaterTv.setText(model.getSeater());
        holder.carName.setText(model.getCar_Name());
        Picasso.get().load(model.getCar_Url()).into(holder.carimg);
        Log.i("Called","Bruhh");

        holder.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("FuelType",model.getFuel_Type());
                bundle.putString("FuelLimit",model.getFuel_Limit());
                bundle.putString("CarType",model.getCar_type());
                bundle.putString("CarName",model.getCar_Name());
                bundle.putString("CarUrl",model.getCar_Url());
                bundle.putString("CarLocation",model.getCar_Location());
                Navigation.findNavController(view).navigate(R.id.action_carsDisplayFragment_to_bookingDetails,bundle);
            }
        });

    }

    public void removeItem(int position, String key) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CarDetails");
        mDatabase.child(key).removeValue();
        notifyItemRemoved(position);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.i("Called","Oops");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_display_layout,parent,false);
        Log.i("Called","Wowww");

        long starttime = (long) (pickupdate + (pickuptime * 60 * 60 * 1000));
        long endtime = (long) (dropoffDate + (dropofftime * 60 * 60 * 1000));
        float duration = Math.round((float)(endtime - starttime) / ( 60 * 60 * 1000));

        if(duration <= 3.0){
            freekms = 80;
        }
        else if(duration <= 4.0){
            freekms = 110;
        }
        else if(duration <= 5.0){
            freekms = 150;
        }
        else if(duration<=6.0){
            freekms = 180;
        }
        else if(duration<=7.0){
            freekms = 200;
        }
        else if(duration<=8.0){
            freekms = 210;
        }
        else if(duration<=9.0){
            freekms = 220;
        }
        else if(duration<=10.0){
            freekms = 230;
        }
        else if(duration<=11.0){
            freekms = 240;
        }
        else if(duration<=12.0){
                freekms = 240;
        }

        else if(duration<=24.0){
            freekms = 350;
        }
        else{
            freekms = 350 + (int) (duration - 24.0) * 10;
        }


        Log.i("Time Start", String.valueOf(duration));


        return new myviewHolder(v);
    }

    static class myviewHolder extends RecyclerView.ViewHolder
    {

        TextView fuelType;
        TextView fuelLimit;
        TextView carType;
        ImageView carimg;
        TextView seaterTv;
        TextView carName;
        MaterialButton continuebtn;


        public myviewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("Called","POOL");
            fuelType = itemView.findViewById(R.id.fueltv);
            fuelLimit = itemView.findViewById(R.id.fuellimittv);
            carimg = itemView.findViewById(R.id.carimageiv);
            carType = itemView.findViewById(R.id.cartypetv);
            seaterTv = itemView.findViewById(R.id.seatertv);
            carName = itemView.findViewById(R.id.carnametv);
            continuebtn = itemView.findViewById(R.id.continuebtn);



        }
    }
}
