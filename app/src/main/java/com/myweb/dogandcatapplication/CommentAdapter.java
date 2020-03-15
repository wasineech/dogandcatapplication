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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    Context context;
    List<RowItem_Comment> rowItems;
    CommentAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    CommentAdapter(Context context, List<RowItem_Comment> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_listview, null);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        RowItem_Comment rowItem_comment = rowItems.get(position);

        String get_post_id = rowItem_comment.getPost_id();
        String get_post_comment_content = rowItem_comment.getPost_comment_content();
        String get_pet_id = rowItem_comment.getPet_id();
        String get_pet_name = rowItem_comment.getPet_name();
        String get_pet_profile = rowItem_comment.getPet_profile();

            holder.post_c.setText(get_post_comment_content);
            holder.pet_name_cc.setText(get_pet_name);
            Picasso.get().load(get_pet_profile).into(holder.pet_profile_c);
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView pet_profile_c;
        TextView pet_name_cc,post_c;

        public CommentViewHolder(View itemView){
            super(itemView);

            pet_profile_c = itemView.findViewById(R.id.pet_profile_c);
            pet_name_cc = itemView.findViewById(R.id.pet_name_cc);
            post_c = itemView.findViewById(R.id.post_c);
        }
    }

}
