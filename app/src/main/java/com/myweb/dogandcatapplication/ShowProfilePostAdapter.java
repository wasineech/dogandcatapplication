package com.myweb.dogandcatapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowProfilePostAdapter extends RecyclerView.Adapter<ShowProfilePostAdapter.ShowProfilePostViewHolder> {
    Context context;
    List<RowItem_ShowProfilePost> rowItems;
    ShowProfilePostAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ShowProfilePostAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    ShowProfilePostAdapter(Context context, List<RowItem_ShowProfilePost> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public ShowProfilePostAdapter.ShowProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_profile_post_listview, null);
        return new ShowProfilePostAdapter.ShowProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowProfilePostAdapter.ShowProfilePostViewHolder holder, int position) {
        RowItem_ShowProfilePost rowItem_post = rowItems.get(position);

        String get_post_id = rowItem_post.getPost_id();
        String get_post_content = rowItem_post.getPost_content();
        String get_post_photo = rowItem_post.getPost_photo();
        String get_post_time = rowItem_post.getPost_time();
        String get_pet_id = rowItem_post.getPet_id();
        String get_pet_name = rowItem_post.getPet_name();
        String get_pet_profile = rowItem_post.getPet_profile();



        Log.d("pettwo",get_post_photo);



        if(get_post_photo.equals("http://"+ConfigIP.IP+"/dogcat/no_photo")) {
            holder.post_content.setText(get_post_content);
            holder.post_time.setText(get_post_time);
            holder.pet_name.setText(get_pet_name);
            holder.post_photo.setVisibility(View.GONE);
            Picasso.get().load(get_pet_profile).into(holder.pet_profile_post);
        }
        else{
            holder.post_content.setText(get_post_content);
            holder.post_time.setText(get_post_time);
            holder.pet_name.setText(get_pet_name);
            Picasso.get().load(get_post_photo).into(holder.post_photo);
            Picasso.get().load(get_pet_profile).into(holder.pet_profile_post);
        }
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ShowProfilePostViewHolder extends RecyclerView.ViewHolder{
        ImageView post_photo,pet_profile_post;
        TextView post_content,post_time,pet_name,btn_edit;
        Button btnLove,btnCm;


        public ShowProfilePostViewHolder(View itemView){
            super(itemView);

            post_photo = itemView.findViewById(R.id.post_photo);
            post_content = itemView.findViewById(R.id.post_content);
            post_time = itemView.findViewById(R.id.post_time);
            pet_name = itemView.findViewById(R.id.pet_name);
            pet_profile_post = itemView.findViewById(R.id.pet_profile_post);
            btnLove = itemView.findViewById(R.id.btnLove);
            btnCm = itemView.findViewById(R.id.btnCm);

            btnLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnLove.getBackground().getConstantState()==context.getResources().getDrawable(R.drawable.heart1).getConstantState()){
                        btnLove.setBackgroundResource(R.drawable.heart_fill);
                    }
                    else {
                        btnLove.setBackgroundResource(R.drawable.heart1);
                    }
                }
            });
            btnCm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowItems != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
//                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }




}
