package com.rent.driveit;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pools;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.installations.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageListAdapter extends FirebaseRecyclerAdapter<PoolRequestMessageModel,MessageListAdapter.messageViewHolder> {
    private Context mContext;


    public MessageListAdapter(@NonNull FirebaseRecyclerOptions<PoolRequestMessageModel> options,Context context) {
        super(options);
        mContext = context;

    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.i("Called","Oops");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageListAdapter.messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other,parent,false);


        return new messageViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull messageViewHolder holder, int position, @NonNull PoolRequestMessageModel model) {
        holder.time.setText("9:00 PM");
        holder.message.setText(model.getMessageText());
        holder.name.setText(model.getName());
        holder.date.setVisibility(View.GONE);
        Picasso.get().load(model.getProfileUrl()).into(holder.image);
    }

    static class messageViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        TextView time;
        ImageView image;
        TextView message;
        TextView date;



        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("Called","POOL");
            name = itemView.findViewById(R.id.text_gchat_user_other);
            image = itemView.findViewById(R.id.image_gchat_profile_other);
            message = itemView.findViewById(R.id.text_gchat_message_other);
            time = itemView.findViewById(R.id.text_gchat_timestamp_other);
            date = itemView.findViewById(R.id.text_gchat_date_other);



        }


    }
}



