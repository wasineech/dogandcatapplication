package com.myweb.dogandcatapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    Context context;
    List<RowItem_Chat> rowItems;
    ChatAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ChatAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    ChatAdapter(Context context, List<RowItem_Chat> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_listview, null);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        RowItem_Chat rowItem_chat = rowItems.get(position);

        String get_pet_send_id = rowItem_chat.getPet_send_id();
        String get_pet_send_name = rowItem_chat.getPet_send_name();
        String get_pet_send_profile = rowItem_chat.getPet_send_profile();
        String get_message = rowItem_chat.getMessage();
        String get_message_timestamp = rowItem_chat.getMessage_timestamp();
        String get_message_isread = rowItem_chat.getMessage_isread();

        holder.pet_name1.setText(get_pet_send_name);
        holder.post_message1.setText(get_message);
        holder.post_time1.setText(get_message_timestamp);
        Picasso.get().load(get_pet_send_profile).into(holder.pet_profile1);

        if(get_message_isread.equals("0")){
            holder.app_layer.setBackgroundResource(R.color.colorNoti_notRead);
            holder.pet_name1.setTypeface(holder.pet_name1.getTypeface(), Typeface.BOLD);
            holder.post_message1.setTypeface(holder.post_message1.getTypeface(), Typeface.BOLD);
            holder.post_time1.setTypeface(holder.post_time1.getTypeface(), Typeface.BOLD);
            holder.post_message1.setTextColor(Color.parseColor("#000000"));
        }

        if(get_message.equals("admin00000000")){
            holder.post_message1.setText("  ");
            holder.post_time1.setVisibility(View.GONE);
            holder.pet_name1.setText(get_pet_send_name);
            Picasso.get().load(get_pet_send_profile).into(holder.pet_profile1);
        }
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        ImageView pet_profile1;
        TextView pet_name1,post_message1,post_time1;
        RelativeLayout app_layer;

        public ChatViewHolder(View itemView){
            super(itemView);

            pet_profile1 = itemView.findViewById(R.id.pet_profile1);
            pet_name1 = itemView.findViewById(R.id.pet_name1);
            post_message1 = itemView.findViewById(R.id.post_message1);
            post_time1 = itemView.findViewById(R.id.post_time1);
            app_layer = itemView.findViewById(R.id.chat1);

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
