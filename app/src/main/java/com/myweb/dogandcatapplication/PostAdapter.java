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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context context;
    List<RowItem_Post> rowItems;
    PostAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    PostAdapter(Context context, List<RowItem_Post> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_listview, null);
        return new PostAdapter.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostAdapter.PostViewHolder holder, int position) {
        RowItem_Post rowItem_post = rowItems.get(position);

        final String get_post_id = rowItem_post.getPost_id();
        final String get_post_content = rowItem_post.getPost_content();
        final String get_post_photo = rowItem_post.getPost_photo();
        final String get_post_time = rowItem_post.getPost_time();
        final String get_pet_id = rowItem_post.getPet_id();
        final String get_pet_name = rowItem_post.getPet_name();
        final String get_pet_profile = rowItem_post.getPet_profile();



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

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, holder.btn_edit);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_post, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("แก้ไขโพสต์")){
                            FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            EditPostFragment ssf = new EditPostFragment();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("pet_id", get_pet_id);
                            data.putString("post_id", get_post_id);
                            data.putString("post_content", get_post_content);
                            data.putString("post_photo", get_post_photo);
                            data.putString("post_time", get_post_time);
                            ssf.setArguments(data);
                            ft.replace(R.id.relativePost, ssf);
                            ft.commit();


                        }
                        else if(item.getTitle().equals("ลบโพสต์")){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.AlertDialogStyle);
                            builder.setCancelable(true);
                            builder.setMessage("คุณจะลบโพสต์ใช่หรือไม่?");
                            builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteAsyn().execute("http://"+ConfigIP.IP+"/dogcat/del_post.php?post_id="+get_post_id);
                                    notifyDataSetChanged();
                                }
                            });
                            builder.setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
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
        }); //closing the setOnClickListener method
    }

    private class DeleteAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .build();

                Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                _okHttpClient.newCall(_request).execute();
                return "successfully";

            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null){
                Toast.makeText(context, "ลบข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "ไม่สามารถถลบ้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView post_photo,pet_profile_post,btn_edit;
        TextView post_content,post_time,pet_name;
        Button btnLove,btnCm;


        public PostViewHolder(View itemView){
            super(itemView);

            post_photo = itemView.findViewById(R.id.post_photo);
            post_content = itemView.findViewById(R.id.post_content);
            post_time = itemView.findViewById(R.id.post_time);
            pet_name = itemView.findViewById(R.id.pet_name);
            pet_profile_post = itemView.findViewById(R.id.pet_profile_post);
            btn_edit = itemView.findViewById(R.id.btn_edit);
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
