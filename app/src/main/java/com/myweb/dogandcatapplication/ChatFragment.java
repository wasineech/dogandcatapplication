package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ChatFragment extends Fragment {
    Button btnNoti;
    String getPet_id,getUser_id;
    String pet_send_id,pet_send_name,pet_send_profile,message,message_timestamp,message_isread;
    List<RowItem_Chat> rowItems;
    RecyclerView chat_rv;
    Boolean condition=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, null);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        chat_rv = getView().findViewById(R.id.list_chat);
        chat_rv.setHasFixedSize(true);
        chat_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        chat_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();
        Log.d("puii3","here");
        setHandler();
        if(condition=true)
        {
            condition=false;
        }
        loadChatData();
//        btnNoti = view.findViewById(R.id.btnNoti);

//        btnNoti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getFragmentManager();
//                Bundle data = new Bundle();//create bundle instance
//                data.putString("pet_id", getPet_id);
//                FragmentTransaction ft = fm.beginTransaction();
//                NotificationFragment ssf = new NotificationFragment();
//                ssf.setArguments(data);
//                ft.replace(R.id.content_fragment, ssf);
//                ft.commit();
//            }
//
//        });

//        LinearLayout app_layer = (LinearLayout) view.findViewById(R.id.chat1);
//        app_layer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ShowChatFragment ssf = new ShowChatFragment();
//                ft.replace(R.id.content_fragment, ssf);
//                ft.commit();
//
//            }
//        });
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

                if(!condition)
                {
                    //Do something after 100ms
                    Log.d("puii","puii in2");
                    loadChatData2();
                    handler.postDelayed(this, delay);
                    Log.d("puii","puii in3");
                }


            }
        }, delay);
    }

    public void stophandler()
    {
        condition=true;
    }

    private void loadChatData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_chat.php?pet_id=" + getPet_id;
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
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            Log.d("puii3","show");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_send_id = collectData.getString("pet_send_id");
                pet_send_name= collectData.getString("pet_send_name");
                pet_send_profile = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("pet_send_profile");
                message = collectData.getString("message");
                message_timestamp = collectData.getString("message_timestamp");
                message_isread = collectData.getString("message_isread");

                Log.d("puii3","id:"+pet_send_id);
                Log.d("puii3","message:"+message);



                RowItem_Chat item = new RowItem_Chat(pet_send_id,pet_send_name,pet_send_profile,message,message_timestamp,message_isread);
                rowItems.add(item);

            }
            ChatAdapter adapter = new ChatAdapter(getActivity(), rowItems);
            chat_rv.setAdapter(adapter);
            Log.d("puii3",getActivity().toString());

            adapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Chat clickItem = rowItems.get(position);
                    FragmentManager fm = getFragmentManager();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id", getPet_id);
                    data.putString("user_id", getUser_id);
                    data.putString("pet_id2", clickItem.getPet_send_id());
                    data.putString("pet_name2", clickItem.getPet_send_name());
                    FragmentTransaction ft = fm.beginTransaction();
                    ShowChatFragment ssf = new ShowChatFragment();
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();

                }
            });


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    private void loadChatData2(){
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_chat.php?pet_id=" + getPet_id;
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

//            int row_count = rowItems.size();
//            int get_count = 0;

//            Log.d("puii","row_count 1111: "+row_count);
//            Log.d("puii","get_count 1111: "+get_count);
//
            rowItems.clear();

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
//                get_count++;

                pet_send_id = collectData.getString("pet_send_id");
                pet_send_name= collectData.getString("pet_send_name");
                pet_send_profile = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("pet_send_profile");
                message = collectData.getString("message");
                message_timestamp = collectData.getString("message_timestamp");
                message_isread = collectData.getString("message_isread");

                Log.d("puii3","id:"+pet_send_id);
                Log.d("puii3","message:"+message);



                RowItem_Chat item = new RowItem_Chat(pet_send_id,pet_send_name,pet_send_profile,message,message_timestamp,message_isread);
                rowItems.add(item);

            }
            ChatAdapter adapter = new ChatAdapter(getActivity(), rowItems);
            chat_rv.setAdapter(adapter);
//            Log.d("puii3",getActivity().toString());

            adapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    stophandler();
                    RowItem_Chat clickItem = rowItems.get(position);
                    FragmentManager fm = getFragmentManager();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id", getPet_id);
                    data.putString("user_id", getUser_id);
                    data.putString("pet_id2", clickItem.getPet_send_id());
                    data.putString("pet_name2", clickItem.getPet_send_name());
                    FragmentTransaction ft = fm.beginTransaction();
                    ShowChatFragment ssf = new ShowChatFragment();
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();

                }
            });


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}