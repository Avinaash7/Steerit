package com.rent.driveit;

import android.content.Context;
import android.os.Bundle;
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

    public CarDisplayAdapter(@NonNull FirebaseRecyclerOptions<CarDetailsModel> options, Context context) {
        super(options);
        this.context = context;
        bundle = new Bundle();
    }



    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull CarDetailsModel model) {

        holder.fuelType.setText(model.getFuel_Type());
        holder.fuelLimit.setText(model.getFuel_Limit());
        holder.carType.setText(model.getCar_type());
        holder.seaterTv.setText(model.getSeater());
        holder.carName.setText(model.getCar_Name());
        Picasso.get().load(model.getCar_Url()).into(holder.carimg);

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

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_display_layout,parent,false);
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
