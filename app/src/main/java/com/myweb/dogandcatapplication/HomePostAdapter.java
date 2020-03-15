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

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder> {
    Context context;
    List<RowItem_Home_Post> rowItems;
    HomePostAdapter.OnItemClickListener mListener;
    String like_st = "0";

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onLikeClick(int position);
        void onUnLikeClick(int position);
    }

    public void setOnItemClickListener(HomePostAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    HomePostAdapter(Context context, List<RowItem_Home_Post> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public HomePostAdapter.HomePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_post_listview, null);
        return new HomePostAdapter.HomePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostAdapter.HomePostViewHolder holder, int position) {
        RowItem_Home_Post rowItem_home_post = rowItems.get(position);

        String get_post_id = rowItem_home_post.getPost_id();
        String get_post_content = rowItem_home_post.getPost_content();
        String get_post_photo = rowItem_home_post.getPost_photo();
        String get_post_time = rowItem_home_post.getPost_time();
        String get_pet_id = rowItem_home_post.getPet_id();
        String get_pet_name = rowItem_home_post.getPet_name();
        String get_pet_profile = rowItem_home_post.getPet_profile();
        String get_count_comment = rowItem_home_post.getCount_comment();
        String get_count_like = rowItem_home_post.getCount_like();
        String get_like_status = rowItem_home_post.getLike_status();



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

        if(get_like_status.equals("0")){
            holder.btnLove.setBackgroundResource(R.drawable.heart1);
//            like_st = "0";
        }
        else{
            holder.btnLove.setBackgroundResource(R.drawable.heart_fill);
//            like_st = "1";
        }

        if(get_count_comment.equals("0")){
            holder.pet_comment.setText("ยังไม่มีความคิดเห็น");
            holder.comment_count.setVisibility(View.GONE);

        }
        else{
            holder.pet_comment.setText("ดุความคิดเห็น "+get_count_comment+" ข้อความ");
            holder.comment_count.setText(get_count_comment);
        }
        if(get_count_like.equals("0")){
            holder.like_count.setVisibility(View.GONE);
        }
        else{
            holder.like_count.setText(get_count_like);
        }
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class HomePostViewHolder extends RecyclerView.ViewHolder{
        ImageView post_photo,pet_profile_post;
        TextView post_content,post_time,pet_name,btn_edit,pet_comment,like_count,comment_count;
        Button btnLove,btnCm;


        public HomePostViewHolder(View itemView){
            super(itemView);

            post_photo = itemView.findViewById(R.id.post_photo);
            post_content = itemView.findViewById(R.id.post_content);
            post_time = itemView.findViewById(R.id.post_time);
            pet_name = itemView.findViewById(R.id.pet_name);
            pet_profile_post = itemView.findViewById(R.id.pet_profile_post);
            btnLove = itemView.findViewById(R.id.btnLove);
            btnCm = itemView.findViewById(R.id.btnCm);
            pet_comment = itemView.findViewById(R.id.pet_comment);
            like_count = itemView.findViewById(R.id.like_count);
            comment_count = itemView.findViewById(R.id.comment_count);
//
//            if(like_st.equals("0")){
//               btnLove.setBackgroundResource(R.drawable.heart1);
//            }
//            else{
//                btnLove.setBackgroundResource(R.drawable.heart_fill);
//            }

            btnLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnLove.getBackground().getConstantState()==context.getResources().getDrawable(R.drawable.heart1).getConstantState()){
                        btnLove.setBackgroundResource(R.drawable.heart_fill);
                        if (rowItems != null){
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                mListener.onLikeClick(position);
//                            notifyItemChanged(position);
//                            notifyDataSetChanged();
//                            rowItems.notify();
//                            notifyItemChanged(position);
//                            notifyItemInserted(position);
                            }
                        }
                    }
                    else {
                        btnLove.setBackgroundResource(R.drawable.heart1);
                        if (rowItems != null){
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                mListener.onUnLikeClick(position);
//                            notifyItemChanged(position);
//                            notifyDataSetChanged();
//                            rowItems.notify();
//                            notifyItemChanged(position);
//                            notifyItemInserted(position);
                            }
                        }
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

