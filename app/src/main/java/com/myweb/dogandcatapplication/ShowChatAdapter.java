package com.myweb.dogandcatapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowChatAdapter extends RecyclerView.Adapter<ShowChatAdapter.ShowChatViewHolder> {
    Context context;
    List<RowItem_ShowChat> rowItems;
    ShowChatAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ShowChatAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    ShowChatAdapter(Context context, List<RowItem_ShowChat> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public ShowChatAdapter.ShowChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_chat_listview, null);
        return new ShowChatAdapter.ShowChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowChatAdapter.ShowChatViewHolder holder, int position) {
        RowItem_ShowChat rowItem_chat = rowItems.get(position);

        String get_pet_send_id = rowItem_chat.getPet_send_id();
        String get_pet_send_name = rowItem_chat.getPet_send_name();
        String get_pet_send_profile = rowItem_chat.getPet_send_profile();
        String get_message = rowItem_chat.getMessage();
        String get_message_timestamp = rowItem_chat.getMessage_timestamp();
        String get_message_status = rowItem_chat.getMessage_status();
        String get_pet_send = rowItem_chat.getPet_send();

        if(get_pet_send.equals("0")){
//            holder.pet_name1.setVisibility(View.VISIBLE);
//            holder.message1.setVisibility(View.VISIBLE);
//            holder.time_message1.setVisibility(View.VISIBLE);
//            holder.pet_profile1.setVisibility(View.VISIBLE);

//            holder.pet_name2.setVisibility(View.INVISIBLE);
            holder.message2.setVisibility(View.GONE);
            holder.time_message2.setVisibility(View.GONE);
            holder.pet_profile2.setVisibility(View.GONE);

//            holder.pet_name1.setText(get_pet_send_name);
            holder.message1.setText(get_message);
            holder.time_message1.setText(get_message_timestamp);
            Picasso.get().load(get_pet_send_profile).into(holder.pet_profile1);
        }
        else {
//            holder.pet_name2.setVisibility(View.VISIBLE);
//            holder.message2.setVisibility(View.VISIBLE);
//            holder.time_message2.setVisibility(View.VISIBLE);
//            holder.pet_profile2.setVisibility(View.VISIBLE);

//            holder.pet_name1.setVisibility(View.INVISIBLE);
            holder.message1.setVisibility(View.GONE);
            holder.time_message1.setVisibility(View.GONE);
            holder.pet_profile1.setVisibility(View.GONE);
            holder.invisible1.setVisibility(View.GONE);

            if(get_message_status.equals("1")) {
//                holder.pet_name2.setText(get_pet_send_name);
                holder.message2.setText(get_message);
                holder.time_message2.setText(get_message_timestamp+" (อ่านแล้ว)");
                Picasso.get().load(get_pet_send_profile).into(holder.pet_profile2);
            }
            else{
//                holder.pet_name2.setText(get_pet_send_name);
                holder.message2.setText(get_message);
                holder.time_message2.setText(get_message_timestamp);
                Picasso.get().load(get_pet_send_profile).into(holder.pet_profile2);
            }
        }
        if(get_message.equals("admin00000000")){
            holder.message1.setVisibility(View.GONE);
            holder.time_message1.setVisibility(View.GONE);
            holder.pet_profile1.setVisibility(View.GONE);
            holder.invisible1.setVisibility(View.GONE);
            holder.message2.setVisibility(View.GONE);
            holder.time_message2.setVisibility(View.GONE);
            holder.pet_profile2.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ShowChatViewHolder extends RecyclerView.ViewHolder{
        ImageView pet_profile1,pet_profile2;
//        TextView pet_name1,pet_name2;
        TextView message1,message2,time_message1,time_message2,invisible1;

        public ShowChatViewHolder(View itemView){
            super(itemView);

            pet_profile1 = itemView.findViewById(R.id.pet_profile1);
            pet_profile2 = itemView.findViewById(R.id.pet_profile2);
//            pet_name1 = itemView.findViewById(R.id.pet_name1);
//            pet_name2 = itemView.findViewById(R.id.pet_name2);
            message1 = itemView.findViewById(R.id.message1);
            message2 = itemView.findViewById(R.id.message2);
            time_message1 = itemView.findViewById(R.id.time_message1);
            time_message2 = itemView.findViewById(R.id.time_message2);
            invisible1 = itemView.findViewById(R.id.invisible1);
        }
    }

}
