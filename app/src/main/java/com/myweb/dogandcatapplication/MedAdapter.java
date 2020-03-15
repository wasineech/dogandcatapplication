package com.myweb.dogandcatapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.MedViewHolder> {
    Context context;
    List<RowItem_Med> rowItems;
    MedAdapter.OnItemClickListener mListener, mListener2;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MedAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    MedAdapter(Context context, List<RowItem_Med> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public MedAdapter.MedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.med_listview, null);
        return new MedAdapter.MedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedAdapter.MedViewHolder holder, final int position) {
        RowItem_Med rowItem_med = rowItems.get(position);

        final String get_med_id = rowItem_med.getMed_id();
        final String get_med_name = rowItem_med.getMed_name();
        final String get_med_description = rowItem_med.getMed_description();
        final String get_med_timestamp = rowItem_med.getMed_timestamp();
        final String get_med_pet_id = rowItem_med.getPet_id();
        final String get_med_pet_user = rowItem_med.getUser_id();


        holder.med_name.setText(get_med_name);
        holder.med_description.setText(get_med_description);
        holder.med_time.setText(get_med_timestamp);

        holder.popup_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(context, holder.popup_med);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_med, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("แก้ไขประวัติการรักษา")){
//                            FragmentManager fm = context.getSupportFragmentManager();
                            FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("user_id", get_med_pet_user);
                            data.putString("pet_id", get_med_pet_id);
                            data.putString("med_id", get_med_id);
                            data.putString("med_name", get_med_name);
                            data.putString("med_description", get_med_description);
                            data.putString("med_time", get_med_timestamp);
                            FragmentTransaction ft = fm.beginTransaction();
                            EditMedFragment ssf = new EditMedFragment();
                            ssf.setArguments(data);
                            ft.replace(R.id.med_content, ssf);
                            ft.commit();
                        }
                        else if(item.getTitle().equals("ลบประวัติการรักษา")){

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.AlertDialogStyle);
                            builder.setCancelable(true);
                            builder.setMessage("คุณจะลบประวัติการรักษาใช่หรือไม่?");
                            builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mListener.onItemClick(position);
//                                    new MedFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/del_med.php?med_id="+get_med_id);
                                    //dialog.cancel();
                                }
                            });
                            android.app.AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });



    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class MedViewHolder extends RecyclerView.ViewHolder {
        TextView med_name, med_description, med_time;
        ImageView popup_med;

        public MedViewHolder(View itemView) {
            super(itemView);

            med_name = itemView.findViewById(R.id.med_name);
            med_description = itemView.findViewById(R.id.med_description);
            med_time = itemView.findViewById(R.id.med_time);
            popup_med = itemView.findViewById(R.id.popup_med);


//            popup_med.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (rowItems != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }

//    private class InsertAsyn extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            try{
//                OkHttpClient _okHttpClient = new OkHttpClient();
//                RequestBody _requestBody = new FormBody.Builder()
//                        .build();
//
//                Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
//                _okHttpClient.newCall(_request).execute();
//                return "successfully";
//
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
////            if (result != null){
////                Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
////                intent.putExtra("user_id", getUser_id);
////                startActivity(intent);
////            }else {
////                Toast.makeText(getContext(), "ไม่สามารถถลบ้อมูลได้",Toast.LENGTH_SHORT).show();
////            }
//        }
//    }
}