package com.myweb.dogandcatapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class HomeFragment extends Fragment {
    String getPet_id;
    ImageView pet_profile1;
    String post_id,post_content,post_photo,post_time,pet_id,pet_name,pet_picture,check_photo,count_comment,count_like,like_status;
    List<RowItem_Home_Post> rowItems;
    RecyclerView post_rv;
    Button btnPost,btnPicture,btnNoti;
    EditText txtPost;
    String get_post_content;
    Boolean condition=false;
    String noti_count_str,noti_count_str_old,noti_count_str_new,noti_message;
    TextView cart_badge;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");


//        Log.d("getPet_id2",getPet_id);

        pet_profile1 = getView().findViewById(R.id.pet_profile1);
        txtPost = getView().findViewById(R.id.txtPost);
        btnPost = getView().findViewById(R.id.btnPost);
        btnPicture = getView().findViewById(R.id.btnPicture);
        btnNoti = view.findViewById(R.id.btnNoti);
        cart_badge = view.findViewById(R.id.cart_badge);

        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                FragmentTransaction ft = fm.beginTransaction();
                NotificationFragment ssf = new NotificationFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }

        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_post_content = txtPost.getText().toString();
                new HomeFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_post.php");
                txtPost.setText("");
                post_rv = getView().findViewById(R.id.list_home_post);
//                post_rv.setNestedScrollingEnabled(false);
                post_rv.setHasFixedSize(true);
                post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                post_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL));

                rowItems = new ArrayList<>();
                loadPostData();
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                FragmentTransaction ft = fm.beginTransaction();
                PostPictureFragment ssf = new PostPictureFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        post_rv = getView().findViewById(R.id.list_home_post);
        post_rv.setHasFixedSize(true);
        post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        post_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();

        loadNotiCount();
        setHandler();
        loadPetData();
        loadPostData();
    }

    @Override
    public void onResume() {
        super.onResume();
//        stophandler();
//        sensorManager.registerListener(this, proximidad, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(this, brillo, SensorManager.SENSOR_DELAY_NORMAL);
//        Log.e("Frontales","resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        stophandler();
//        sensorManager.unregisterListener(this);
//        Log.e("Frontales","Pause");

    }

    private void loadNotiCount(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti_count.php?pet_id=" + getPet_id;
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showNotiCount(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void showNotiCount(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if(result.length() > 0) {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject collectData = result.getJSONObject(i);

                    noti_count_str = collectData.getString("noti_count");
                    noti_count_str_old = noti_count_str;
                    Log.d("pet", noti_count_str);

                    if (noti_count_str.equals("0")) {
                        Log.d("puii", "IF equals0: " + noti_count_str);
                        cart_badge.setVisibility(View.GONE);
                    } else {
                        Log.d("puii", "IF equals else: " + noti_count_str);
                        cart_badge.setVisibility(View.VISIBLE);
                        cart_badge.setText(String.valueOf(noti_count_str));
                    }
                }
            }else{
                noti_count_str_old = "0";
                cart_badge.setVisibility(View.GONE);
            }

        }catch (JSONException ex) {
            ex.printStackTrace();
            noti_count_str = null;
            noti_count_str_old = "0";
            cart_badge.setVisibility(View.GONE);
//            Toast.makeText(getActivity().getApplicationContext(), "ไม่มีข้อมูล", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadPetData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_pet_data.php?pet_id=" + getPet_id;
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON2(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void showJSON2(String response){
        try {
            JSONObject jsonObject2 = new JSONObject(response);
            JSONArray result2 = jsonObject2.getJSONArray("result");

            for (int i = 0; i<result2.length(); i++){
                JSONObject collectData2 = result2.getJSONObject(i);

                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData2.getString("pet_picture");

                Log.d("pet",pet_picture);

                Picasso.get().load(pet_picture).into(pet_profile1);
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////

    private void loadPostData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_pet_home_post.php?pet_id=" + getPet_id;
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void showJSON(String response){
        try {
            Log.d("pet_home","response");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                post_id = collectData.getString("post_id");
                post_content= collectData.getString("post_content");
                post_photo = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("post_photo");
                post_time = collectData.getString("post_time");
                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData.getString("pet_picture");
                count_comment = collectData.getString("count_comment");
                count_like = collectData.getString("count_like");
                like_status = collectData.getString("like_status");

                Log.d("pet_home",post_id);
                Log.d("pet_home",pet_picture);

//                Picasso.get().load(pet_picture).into(pet_profile1);

                RowItem_Home_Post item = new RowItem_Home_Post(post_id,post_content,post_photo,post_time,pet_id,pet_name,pet_picture,count_comment,count_like,like_status);
                rowItems.add(item);

            }

            final HomePostAdapter adapter = new HomePostAdapter(getActivity(), rowItems);
//            Log.d("act1",getActivity().toString());
            post_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new HomePostAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Home_Post clickItem = rowItems.get(position);
                    if(clickItem.getPost_photo().equals("http://"+ConfigIP.IP+"/dogcat/no_photo")) {
                        check_photo = "no_photo";
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        CommentFragment ssf = new CommentFragment();
                        Bundle data = new Bundle();//create bundle instance
                        data.putString("pet_id", clickItem.getPet_id());
                        data.putString("post_id", clickItem.getPost_id());
                        data.putString("post_content", post_content);
                        data.putString("post_photo", check_photo);
                        data.putString("post_time", post_time);
                        ssf.setArguments(data);
                        ft.replace(R.id.content_fragment, ssf);
                        ft.commit();
                    }
                    else {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        CommentFragment ssf = new CommentFragment();
                        Bundle data = new Bundle();//create bundle instance
                        data.putString("pet_id", getPet_id);
                        data.putString("post_id", clickItem.getPost_id());
                        data.putString("post_content", post_content);
                        data.putString("post_photo", post_photo);
                        data.putString("post_time", post_time);
                        ssf.setArguments(data);
                        ft.replace(R.id.content_fragment, ssf);
                        ft.commit();
                    }
                }
                public void onLikeClick(int position) {
                    RowItem_Home_Post clickItem = rowItems.get(position);
                    new HomeFragment.InsertLike().execute("http://"+ConfigIP.IP+"/dogcat/add_like.php?post_id="+clickItem.getPost_id());
                    Log.d("like_add","http://"+ConfigIP.IP+"/dogcat/add_like.php?post_id="+clickItem.getPost_id());
//                    adapter.notifyItemChanged(position);
                }
                public void onUnLikeClick(int position) {
                    RowItem_Home_Post clickItem = rowItems.get(position);
                    new HomeFragment.InsertLike().execute("http://"+ConfigIP.IP+"/dogcat/edit_like.php?post_id="+clickItem.getPost_id());
                    Log.d("like_edit","http://"+ConfigIP.IP+"/dogcat/add_like.php?post_id="+clickItem.getPost_id());
//                    adapter.notifyItemChanged(position);
                }
            });
//            adapter.notifyDataSetChanged();


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id", getPet_id)
                        .add("post_content", get_post_content)
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
                Toast.makeText(getContext(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class InsertLike extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id", getPet_id)
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
                Toast.makeText(getContext(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void setHandler(){
        final Handler handler = new Handler();
        final int delay = 1000 ; //1000 milliseconds = 1 sec
//        final NotificationAdapter adapter2 = new NotificationAdapter(getActivity(), rowItems);

//        Log.d("puii","puii in1");

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!condition)
                {
//                    sp_notification = getActivity().getSharedPreferences("NOTI_COUNT", Context.MODE_PRIVATE);
//                    noti_count_str = sp_notification.getString("NOTI_COUNT"+getPet_id,null);

                    if (noti_count_str_old != null)
                    {
                        if(noti_count_str_old.equals("0")){
                            Log.d("puii","postDelayed IF equals0: "+noti_count_str_old);
                            cart_badge.setVisibility(View.GONE);
                        }
                        else{
                            Log.d("puii","postDelayed IF equals else: "+noti_count_str_old);
                            cart_badge.setVisibility(View.VISIBLE);
                            cart_badge.setText(String.valueOf(noti_count_str));
                        }
                    }
                    else{
                        Log.d("puii","postDelayed ELSE: null");
                        cart_badge.setVisibility(View.GONE);
                    }
//                    Log.d("puii","puii in2");
                    loadNotiCount2();
//                    loadRefresh();
                    handler.postDelayed(this, delay);
//                    Log.d("puii","puii in3");
                }


            }
        }, delay);
    }

    public void stophandler()
    {
        condition=true;
    }

    private void loadNotiCount2(){
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti_count.php?pet_id=" + getPet_id;
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();
                showNotiCount2(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void showNotiCount2(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if(result.length() > 0) {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject collectData = result.getJSONObject(i);

                    noti_count_str = collectData.getString("noti_count");
                    noti_message = collectData.getString("noti_message");
                    noti_count_str_new = noti_count_str;
                    int noti_count_int_new = Integer.parseInt(noti_count_str_new);
                    int noti_count_int_old = Integer.parseInt(noti_count_str_old);
                    Log.d("pet", noti_count_str);
                    if (noti_count_int_new > noti_count_int_old) {

                        addNotification();
                        Log.d("puii_max", "more: " + noti_message);
                    } else {
                        Log.d("puii_max", "less");
                    }
                    //                    Log.d("puii","IF equals0: "+noti_count_str);
                    //                    cart_badge.setVisibility(View.GONE);
                    //                }
                    //                else{
                    //                    Log.d("puii","IF equals else: "+noti_count_str);
                    //                    cart_badge.setVisibility(View.VISIBLE);
                    //                    cart_badge.setText(String.valueOf(noti_count_str));
                    //                }

                    //                if(noti_count_str.equals("0")){
                    //                    Log.d("puii","IF equals0: "+noti_count_str);
                    //                    cart_badge.setVisibility(View.GONE);
                    //                }
                    //                else{
                    //                    Log.d("puii","IF equals else: "+noti_count_str);
                    //                    cart_badge.setVisibility(View.VISIBLE);
                    //                    cart_badge.setText(String.valueOf(noti_count_str));
                    //                }
                }
                noti_count_str_old = noti_count_str;
            }
            else{
                noti_count_str_old = "0";
            }

        }catch (JSONException ex) {
            ex.printStackTrace();
            noti_count_str = null;
            noti_count_str_old = "0";
            cart_badge.setVisibility(View.GONE);
//            Toast.makeText(getActivity().getApplicationContext(), "ไม่มีข้อมูล", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNotification() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        String channelId = "notification1";
        String channelName = "my_notification1";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        NotificationManager manager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);


        int notificationId = 0;
        String contentTitle = "แจ้งเตือนจาก: "+pet_name;
        String contentText = noti_message;
        Notification notification = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setChannelId(channelId)
                .build();
        manager.notify(notificationId, notification);

//        Builds your notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("John's Android Studio Tutorials")
//                .setContentText("A video has just arrived!")
//                .setChannel("notification1");
//
//
//         Creates the intent needed to show the notification
//        Intent notificationIntent = new Intent(getActivity(), HomeActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//
//         Add as notification
    }

}
