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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowSearchAdapter extends RecyclerView.Adapter<ShowSearchAdapter.ShowViewHolder> {
    Context context;
    List<RowItem_ShowSearch> rowItems;
    ShowSearchAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onMatchClick(int position);
    }

    public void setOnItemClickListener(ShowSearchAdapter.OnItemClickListener listener){
        mListener = listener;
    }


    ShowSearchAdapter(Context context, List<RowItem_ShowSearch> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public ShowSearchAdapter.ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_search_listview, null);
        return new ShowSearchAdapter.ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowSearchAdapter.ShowViewHolder holder, int position) {
        RowItem_ShowSearch rowItem_show = rowItems.get(position);

        String get_name_gender = rowItem_show.getPet_name_gender();
        String get_age_breed = rowItem_show.getPet_age_breed();
        String get_picture = rowItem_show.getPet_profile();

        holder.pet_name_gender.setText(get_name_gender);
        holder.pet_age_breed.setText(get_age_breed);;
        Picasso.get().load(get_picture).into(holder.pet_profile);

    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder{
        TextView pet_name_gender, pet_age_breed;
        ImageView pet_profile;
        Button btnMatch;
        LinearLayout app_layer;

        public ShowViewHolder(View itemView){
            super(itemView);

            pet_name_gender = itemView.findViewById(R.id.pet_name_gender);
            pet_age_breed = itemView.findViewById(R.id.pet_age_breed);
            pet_profile = itemView.findViewById(R.id.pet_profile);
            btnMatch = itemView.findViewById(R.id.btnMatch);
            app_layer = itemView.findViewById(R.id.home_layout2);

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

            btnMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowItems != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onMatchClick(position);
                            rowItems.remove(position);
                            notifyItemRemoved(position);
                            Log.d("ShowSearch2","position:"+position);
//                        holder.app_layer.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }




}
