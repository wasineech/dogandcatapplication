package com.myweb.dogandcatapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationFragment extends Fragment {
    String pet_id,pet_kind,pet_gender,pet_breed,pet_picture,pet_profile,province,pet_name,pet_name_gender,pet_age_breed,pet_age,noti_status,noti_message,noti_time,noti_isread,match_id,post_id,match_status,noti_id;
    String getPet_id,getPet_name;
    List<RowItem_Notification> rowItems;
    RecyclerView notification_rv;
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    Boolean condition=false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getPet_name = getArguments().getString("pet_name");

        Toast.makeText(getContext(), getPet_id , Toast.LENGTH_LONG).show();

        notification_rv = getView().findViewById(R.id.list_notification);
        notification_rv.setHasFixedSize(true);
        notification_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        notification_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();

        setHandler();
        if(condition=true)
        {
            condition=false;
        }
        loadShowNoti();
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

    private class InsertAsyn extends AsyncTask<String, Void, String> {
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
                Log.d("match","สำเร็จ");
            }else {
                Log.d("match","ไม่สามารถบันทึกข้อมูลได้");
            }
        }
    }

    private void loadShowNoti(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti.php?pet_id="+getPet_id;
        Log.d("get_url",url);
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
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_gender = collectData.getString("pet_gender");
                pet_age = collectData.getString("pet_age");
                pet_breed = collectData.getString("pet_breed");
                pet_picture = collectData.getString("pet_picture");
                noti_message = collectData.getString("noti_message");
                noti_time = collectData.getString("noti_time");
                noti_isread = collectData.getString("noti_isread");
                match_id = collectData.getString("match_id");
                match_status = collectData.getString("match_status");
                post_id = collectData.getString("post_id");
                noti_id = collectData.getString("noti_id");
                pet_profile = "http://"+ConfigIP.IP+"/dogcat/"+pet_picture;
                pet_age_breed = "อายุ: " +  pet_age + " สายพันธ์: " + pet_breed;

//                if(noti_status.equals("0")){
//                    noti_message = pet_name+"ได้ทำการกดแมทซ์คุณ คุณจะตอบรับหรือไม่?";
//                }
//                else if(noti_status.equals("1")){
//                    noti_message = pet_name+"ได้ตอบรับการแมทซ์ของคุณแล้ว เริ่มแชทได้เลย";
//                }

                RowItem_Notification item = new RowItem_Notification(pet_id,pet_name,pet_gender,pet_age_breed,pet_profile,noti_message,noti_time,noti_isread,match_id,match_status,post_id,noti_id);
                rowItems.add(item);

                //Log.d("item",item);

                // showsearch_listview.setOnItemClickListener(this);

            }


            NotificationAdapter adapter = new NotificationAdapter(getActivity(), rowItems);
            notification_rv.setAdapter(adapter);

            adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Notification clickItem = rowItems.get(position);
                    if(clickItem.getPet_post_id().equals("0")) {
                        if(clickItem.getPet_post_id().equals("0")) {
                            stophandler();
                            new NotificationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
                            Log.d("url","url: " + "http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
                            Log.d("url","click");
                            FragmentManager fm = getFragmentManager();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("pet_id", getPet_id);
                            data.putString("pet_id2", clickItem.getPet_id());
                            data.putString("pet_profile", clickItem.getPet_profile());
                            data.putString("pet_name", clickItem.getPet_name());
                            data.putString("pet_gender", clickItem.getPet_gender());
                            data.putString("pet_age_breed", clickItem.getPet_age_breed());
                            FragmentTransaction ft = fm.beginTransaction();
                            ShowPetMatchFragment ssf = new ShowPetMatchFragment();
                            ssf.setArguments(data);
                            ft.replace(R.id.content_fragment, ssf);
                            ft.commit();
                        }
//                        else{
//                            stophandler();
//                            new NotificationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
//                            FragmentManager fm = getFragmentManager();
//                            FragmentTransaction ft = fm.beginTransaction();
//                            ChatFragment ssf = new ChatFragment();
//                            ft.replace(R.id.content_fragment, ssf);
//                            ft.commit();
//                        }
                    }
                    else{
                        stophandler();
                        new NotificationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
                        FragmentManager fm = getFragmentManager();
                        Bundle data = new Bundle();//create bundle instance
                        FragmentTransaction ft = fm.beginTransaction();
                        CommentFragment ssf = new CommentFragment();
                        data.putString("post_id", clickItem.getPet_post_id());
                        ssf.setArguments(data);
                        ft.replace(R.id.content_fragment, ssf);
                        ft.commit();
                    }

                }
            });

        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void setHandler(){
        final Handler handler = new Handler();
        final int delay = 1000 ; //1000 milliseconds = 1 sec
        final NotificationAdapter adapter2 = new NotificationAdapter(getActivity(), rowItems);

        Log.d("puii","puii in1");

        handler.postDelayed(new Runnable(){
            public void run(){
                Log.d("puii","puii in2");

                if(!condition)
                {
                    //Do something after 100ms
                    loadRefresh();
                    handler.postDelayed(this, delay);
                }
                Log.d("puii","puii in3");
            }
        }, delay);
    }

    public void stophandler()
    {
        condition=true;
    }

    private void loadRefresh(){


        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti.php?pet_id="+getPet_id;
        Log.d("get_url",url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshJSON(response);

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
    public void refreshJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            int row_count = rowItems.size();
            int get_count = 0;

            rowItems.clear();

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
                get_count++;

                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_gender = collectData.getString("pet_gender");
                pet_age = collectData.getString("pet_age");
                pet_breed = collectData.getString("pet_breed");
                pet_picture = collectData.getString("pet_picture");
                noti_message = collectData.getString("noti_message");
                noti_time = collectData.getString("noti_time");
                noti_isread = collectData.getString("noti_isread");
                match_id = collectData.getString("match_id");
                match_status = collectData.getString("match_status");
                post_id = collectData.getString("post_id");
                noti_id = collectData.getString("noti_id");
                pet_profile = "http://"+ConfigIP.IP+"/dogcat/"+pet_picture;
                pet_age_breed = "อายุ: " +  pet_age + " สายพันธ์: " + pet_breed;

//                if(noti_status.equals("0")){
//                    noti_message = pet_name+"ได้ทำการกดแมทซ์คุณ คุณจะตอบรับหรือไม่?";
//                }
//                else if(noti_status.equals("1")){
//                    noti_message = pet_name+"ได้ตอบรับการแมทซ์ของคุณแล้ว เริ่มแชทได้เลย";
//                }

                if(get_count > row_count){
                    addNotification();
//                    showNotification(getView());
//
                }

                RowItem_Notification item = new RowItem_Notification(pet_id,pet_name,pet_gender,pet_age_breed,pet_profile,noti_message,noti_time,noti_isread,match_id,match_status,post_id,noti_id);
                rowItems.add(item);


                //Log.d("item",item);

                // showsearch_listview.setOnItemClickListener(this);

            }


            NotificationAdapter adapter = new NotificationAdapter(getActivity(), rowItems);
            notification_rv.setAdapter(adapter);

            adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Notification clickItem = rowItems.get(position);
                    if(clickItem.getPet_post_id().equals("0")) {
                        if(clickItem.getPet_match_status().equals("0")) {
                            stophandler();
                            new NotificationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
                            FragmentManager fm = getFragmentManager();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("pet_id", getPet_id);
                            data.putString("pet_id2", clickItem.getPet_id());
                            data.putString("pet_profile", clickItem.getPet_profile());
                            data.putString("pet_name", clickItem.getPet_name());
                            data.putString("pet_gender", clickItem.getPet_gender());
                            data.putString("pet_age_breed", clickItem.getPet_age_breed());
                            FragmentTransaction ft = fm.beginTransaction();
                            ShowPetMatchFragment ssf = new ShowPetMatchFragment();
                            ssf.setArguments(data);
                            ft.replace(R.id.content_fragment, ssf);
                            ft.commit();
                        }
//                        else{
//                            stophandler();
//                            new NotificationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
//                            FragmentManager fm = getFragmentManager();
//                            Bundle data = new Bundle();//create bundle instance
//                            FragmentTransaction ft = fm.beginTransaction();
//                            data.putString("pet_id", getPet_id);
//                            data.putString("user_id", getPet_id);
//                            ChatFragment ssf = new ChatFragment();
//                            ft.replace(R.id.content_fragment, ssf);
//                            ft.commit();
//                        }
                    }
                    else{
                        stophandler();
                        new NotificationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/update_noti_isread.php?noti_id="+clickItem.getNoti_id());
                        FragmentManager fm = getFragmentManager();
                        Bundle data = new Bundle();//create bundle instance
                        FragmentTransaction ft = fm.beginTransaction();
                        CommentFragment ssf = new CommentFragment();
                        data.putString("post_id", clickItem.getPet_post_id());
                        ssf.setArguments(data);
                        ft.replace(R.id.content_fragment, ssf);
                        ft.commit();
                    }

                }
            });

        }catch (JSONException ex) {
            ex.printStackTrace();
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
        String contentTitle = "แจ้งเตือนจาก: "+getPet_name;
        String contentText = noti_message;
        Notification notification = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setChannelId(channelId)
                .build();
        manager.notify(notificationId, notification);

        // Builds your notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("John's Android Studio Tutorials")
//                .setContentText("A video has just arrived!")
//                .setChannel("notification1");
//

        // Creates the intent needed to show the notification
//        Intent notificationIntent = new Intent(getActivity(), HomeActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);

        // Add as notification
    }

//    public void showNotification(View view) {
//
////        Intent intent = new Intent(Intent.ACTION_VIEW,
////                Uri.parse("https://devahoy.com/blog/2015/08/android-notification/"));
////        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
//
//        Notification notification =
//                new NotificationCompat.Builder(getActivity())
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(pet_name)
//                        .setContentText(noti_message)
//                        .setAutoCancel(true)
////                                    .setContentIntent(pendingIntent)
//                        .build();
//
//        NotificationManager notificationManager = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
//        notificationManager.notify(0, notification);
//    }



}
