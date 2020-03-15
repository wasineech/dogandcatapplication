package com.myweb.dogandcatapplication;

import android.content.Context;
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

public class ShowMatchAdapter extends RecyclerView.Adapter<ShowMatchAdapter.ShowViewHolder> {
    Context context;
    List<RowItem_Match> rowItems;
    ShowMatchAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ShowMatchAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    ShowMatchAdapter(Context context, List<RowItem_Match> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public ShowMatchAdapter.ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.match_listview, null);
        return new ShowMatchAdapter.ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowMatchAdapter.ShowViewHolder holder, int position) {
        RowItem_Match rowItem_match = rowItems.get(position);

        String get_petname = rowItem_match.getPet_name();
        String get_match_time = rowItem_match.getMatch_time();
        String get_picture = rowItem_match.getPet_profile();

        holder.pet_name.setText(get_petname);
        holder.match_time.setText("แมทช์เมื่อ: "+get_match_time);
        Picasso.get().load(get_picture).into(holder.pet_profile);
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder{
        TextView pet_name,match_time;
        ImageView pet_profile;

        public ShowViewHolder(View itemView){
            super(itemView);

            pet_name = itemView.findViewById(R.id.account_name);
            match_time = itemView.findViewById(R.id.match_time);
            pet_profile = itemView.findViewById(R.id.account_profile);
            RelativeLayout app_layer = itemView.findViewById(R.id.match);

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
