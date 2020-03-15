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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    Context context;
    List<RowItem_Gallery> rowItems;
    GalleryAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(GalleryAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    GalleryAdapter(Context context, List<RowItem_Gallery> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gallery_listview, null);
        return new GalleryAdapter.GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.GalleryViewHolder holder, int position) {
        RowItem_Gallery rowItem_gallery = rowItems.get(position);

        String get_post_id = rowItem_gallery.getPost_id();
        String get_post_photo = rowItem_gallery.getPost_photo();
        String get_pet_id = rowItem_gallery.getPet_id();

        Picasso.get().load(get_post_photo).into(holder.post_img);

    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView post_img;


        public GalleryViewHolder(View itemView){
            super(itemView);

            post_img = itemView.findViewById(R.id.post_img);

            post_img.setOnClickListener(new View.OnClickListener() {
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
