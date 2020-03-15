package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ShowChatFragment extends Fragment {
    String getPet_id,getUser_id,getPet_id2,getPet_name2;
    String pet_send_id,pet_send_name,pet_send_profile,message,message_timestamp,message_status,pet_send;
    List<RowItem_ShowChat> rowItems;
    RecyclerView show_chat_rv;
    TextView pet_send_name_txt;
    EditText edit_chat;
    ImageView btn_send_chat,btn_back;
    String messageStr;
    Boolean condition=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_chat, null);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");
        getPet_id2 = getArguments().getString("pet_id2");
        getPet_name2 = getArguments().getString("pet_name2");

        pet_send_name_txt = getView().findViewById(R.id.pet_send_name_txt);
        edit_chat = getView().findViewById(R.id.edit_chat);
        btn_send_chat = getView().findViewById(R.id.btn_send_chat);
        btn_back = getView().findViewById(R.id.btn_back);

        pet_send_name_txt.setText(getPet_name2);

        show_chat_rv = getView().findViewById(R.id.list_show_chat);
        show_chat_rv.setHasFixedSize(true);
        show_chat_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        show_chat_rv.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();
        Log.d("puii3","here");
        setHandler();
        if(condition=true)
        {
            condition=false;
        }
        loadShowChatData();


        btn_send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageStr = edit_chat.getText().toString();
                edit_chat.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                new ShowChatFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_chat_message.php");

            }

        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stophandler();
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                data.putString("user_id", getUser_id);
                FragmentTransaction ft = fm.beginTransaction();
                ChatFragment ssf = new ChatFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }

        });

//        btn_send_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                messageStr = edit_chat.getText().toString();
//                new ShowChatFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_chat_message.php");
//            }
//
//        });

//
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
    public void setHandler(){
        final Handler handler = new Handler();
        final int delay = 1000 ; //1000 milliseconds = 1 sec

        Log.d("puii","puii in1");

        handler.postDelayed(new Runnable(){
            public void run(){
                Log.d("puii", "condition: "+condition);
                if(!condition) {
                    Log.d("puii", "puii in2");
                    loadShowChatData2();
                    handler.postDelayed(this, delay);
                    Log.d("puii", "puii in3");
                }
            }
        }, delay);
    }

    public void stophandler()
    {
        condition=true;
    }


    private void loadShowChatData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_show_chat.php?pet_id1=" + getPet_id + "&pet_id2=" +getPet_id2;
        Log.d("urlChatData",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSONChatData(response);
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
    public void showJSONChatData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result_showchat");
            Log.d("puii3","showChatData");

            String ss_count = String.valueOf(result.length());
            int ii_count = result.length();

            Log.d("puii3","showChatData : cc : " + ss_count);

            JSONObject collectData22 = result.getJSONObject(ii_count);

            String scd_id = collectData22.getString("pet_send_id");

            Log.d("puii3","showChatData : tt : " + scd_id);

            for (int i = 0; i<result.length(); i++){
                Log.d("puii3","showChatData 1111111111");

                JSONObject collectData = result.getJSONObject(i);

                pet_send_id = collectData.getString("pet_send_id");
                pet_send_name= collectData.getString("pet_send_name");
                pet_send_profile = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("pet_send_profile");
                message = collectData.getString("message");
                message_timestamp = collectData.getString("message_timestamp");
                message_status = collectData.getString("message_isread");
                pet_send = collectData.getString("pet_send");

                Log.d("puii3","id:"+pet_send_id);
                Log.d("puii3","message:"+message);

                Log.d("puii3","showChatData wwwwwwwwwwww");


                RowItem_ShowChat item = new RowItem_ShowChat(pet_send_id,pet_send_name,pet_send_profile,message,message_timestamp,message_status,pet_send);
                rowItems.add(item);

            }
            ShowChatAdapter adapter = new ShowChatAdapter(getActivity(), rowItems);
            show_chat_rv.setAdapter(adapter);
            Log.d("puii3",getActivity().toString());




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
                        .add("pet_send", getPet_id)
                        .add("pet_receive", getPet_id2)
                        .add("message", messageStr)
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
    private void loadShowChatData2(){
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_show_chat.php?pet_id1=" + getPet_id + "&pet_id2=" +getPet_id2;
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();
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
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            Log.d("puii3","show");

            rowItems.clear();

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_send_id = collectData.getString("pet_send_id");
                pet_send_name= collectData.getString("pet_send_name");
                pet_send_profile = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("pet_send_profile");
                message = collectData.getString("message");
                message_timestamp = collectData.getString("message_timestamp");
                message_status = collectData.getString("message_isread");
                pet_send = collectData.getString("pet_send");

                Log.d("puii3","id:"+pet_send_id);
                Log.d("puii3","message:"+message);



                RowItem_ShowChat item = new RowItem_ShowChat(pet_send_id,pet_send_name,pet_send_profile,message,message_timestamp,message_status,pet_send);
                rowItems.add(item);

            }
            ShowChatAdapter adapter = new ShowChatAdapter(getActivity(), rowItems);
            show_chat_rv.setAdapter(adapter);
//            Log.d("puii3",getActivity().toString());




        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


}
