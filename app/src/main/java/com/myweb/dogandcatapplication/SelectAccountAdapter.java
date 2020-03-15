package com.myweb.dogandcatapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.GONE;

public class SelectAccountAdapter extends RecyclerView.Adapter<SelectAccountAdapter.ShowViewHolder> {
        Context context;
        List<RowItem_Account> rowItems;
        OnItemClickListener mListener,mListener2;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

        SelectAccountAdapter(Context context, List<RowItem_Account> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
        }

@NonNull
@Override
public SelectAccountAdapter.ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_listview, null);
        return new SelectAccountAdapter.ShowViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull SelectAccountAdapter.ShowViewHolder holder, int position) {
    RowItem_Account rowItem_account = rowItems.get(position);

        String get_petname = rowItem_account.getPet_name();
        String get_picture = rowItem_account.getPet_profile();
        String get_noti_count = rowItem_account.getNoti_count();

        holder.pet_name.setText(get_petname);
        Picasso.get().load(get_picture).into(holder.pet_profile);

        if (get_noti_count.equals("0")) {
            holder.cart_badge_account.setVisibility(View.GONE);
        }
        else {
            holder.cart_badge_account.setVisibility(View.VISIBLE);
            holder.cart_badge_account.setText(get_noti_count);

        }

    }


@Override
public int getItemCount() {
        return rowItems.size();
        }

class ShowViewHolder extends RecyclerView.ViewHolder{
    TextView pet_name,cart_badge_account;
    ImageView pet_profile;

    public ShowViewHolder(View itemView){
        super(itemView);

        pet_name = itemView.findViewById(R.id.account_name);
        pet_profile = itemView.findViewById(R.id.account_profile);
        cart_badge_account = itemView.findViewById(R.id.cart_badge_account);
        RelativeLayout app_layer = itemView.findViewById(R.id.accout);

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

