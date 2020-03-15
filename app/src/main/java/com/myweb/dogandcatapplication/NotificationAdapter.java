package com.myweb.dogandcatapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    Context context;
    List<RowItem_Notification> rowItems;
    NotificationAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NotificationAdapter.OnItemClickListener listener){
        mListener = listener;
    }


    NotificationAdapter(Context context, List<RowItem_Notification> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_listview, null);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.NotificationViewHolder holder, int position) {
        RowItem_Notification rowItem_noti = rowItems.get(position);

        String get_name = rowItem_noti.getPet_name();
        String get_noti_message = rowItem_noti.getPet_noti_message();
        String get_picture = rowItem_noti.getPet_profile();
        String get_noti_time = rowItem_noti.getPet_noti_time();
        String get_noti_isread = rowItem_noti.getPet_noti_isread();

        holder.pet_name.setText(get_name);
        holder.noti_message.setText(get_noti_message);
        holder.noti_time.setText(get_noti_time);
        Picasso.get().load(get_picture).into(holder.pet_profile);

        if(get_noti_isread.equals("0")){
            holder.app_layer.setBackgroundResource(R.color.colorNoti_notRead);
            holder.app_layer2.setBackgroundResource(R.color.colorNoti_notRead);
            holder.noti_message.setTypeface(holder.noti_message.getTypeface(), Typeface.BOLD);
            holder.noti_message.setTextColor(Color.parseColor("#000000"));
//            holder.app_layer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorNoti_notRead));
        }
    }

//    public void timer() {
//        for(int i = 0; i < rowItems.size(); i++) {
//            notifyItemChanged(i);
//            notifyItemInserted(i);
//            notifyItemRemoved(i);
//            notifyItemRangeChanged(i,rowItems.size());
//        }
//    }

//
//    public void resetData(List<RowItem_Notification> notiItems) {
//        this.rowItems = notiItems;
//        for(int i = 0; i < rowItems.size(); i++) {
//
//            notifyItemChanged(i);
//            notifyItemInserted(i);
//            notifyItemRemoved(i);
//            notifyItemRangeChanged(i,rowItems.size());
//        }
//        this.notifyDataSetChanged();
//    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView pet_name, noti_message,noti_time;
        ImageView pet_profile;
        LinearLayout app_layer,app_layer2;

        public NotificationViewHolder(View itemView){
            super(itemView);

            pet_name = itemView.findViewById(R.id.pet_name);
            pet_profile = itemView.findViewById(R.id.pet_profile);
            noti_message = itemView.findViewById(R.id.noti_message);
            noti_time = itemView.findViewById(R.id.noti_time);
            app_layer = itemView.findViewById(R.id.noti_layout);
            app_layer2 = itemView.findViewById(R.id.noti_layout2);


            app_layer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowItems != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }




}
