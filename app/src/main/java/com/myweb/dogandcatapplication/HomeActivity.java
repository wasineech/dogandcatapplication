package com.myweb.dogandcatapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    String pet_id,user_id;
    View notificationBadge;
    Boolean condition=false;
    String noti_count_str,noti_count_str_old,noti_count_str_new,noti_message,pet_send,pet_message;
    BottomNavigationMenuView menuView;
    BottomNavigationItemView itemView;
    TextView cart_badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            pet_id = _bundle.getString("pet_id");
            user_id = _bundle.getString("user_id");
        }


//                if(condition=true)
//        {
//            condition=false;
//        }

        loadNotiCount();
        setHandler();


        Fragment fragment = new HomeFragment();
        Bundle data = new Bundle();//create bundle instance
        data.putString("pet_id", pet_id);
        data.putString("user_id", user_id);
        fragment.setArguments(data);
        loadFragment(fragment);
//        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

       menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
       itemView = (BottomNavigationItemView) menuView.getChildAt(3);

        String menu_count = String.valueOf(menuView.getChildCount());

        Log.i("menu_child", "menu_count: " + menu_count);

        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);

//        cart_badge = findViewById(R.id.cart_badge);
        itemView.addView(notificationBadge);
//        itemView.removeView(notificationBadge);

    }

    @Override
    public void onResume() {
        super.onResume();
//        setHandler();

    }

    @Override
    public void onPause() {
        super.onPause();
//        setHandler();
//        stophandler();
//        sensorManager.unregisterListener(this);
//        Log.e("Frontales","Pause");

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;
            case R.id.navigation_nearme:
                fragment = new MapNearmeFragment();
                break;
            case R.id.navigation_chat:
                fragment = new ChatFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }

        Bundle data = new Bundle();//create bundle instance
        data.putString("pet_id", pet_id);
        data.putString("user_id", user_id);
        fragment.setArguments(data);
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadNotiCount(){
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti_message_count.php?pet_id=" + pet_id;
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
                Toast.makeText(HomeActivity.this.getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void showNotiCount(String response){
        try {

            Log.d("puii_load","load noti count");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if(result.length() > 0){
                Log.d("puii_load","load noti count NORMAL");
                for (int i = 0; i<result.length(); i++){
                    JSONObject collectData = result.getJSONObject(i);

                    noti_count_str = collectData.getString("noti_count");
                    noti_count_str_old = noti_count_str;
                    Log.d("puii_load",noti_count_str);
                    Log.d("puii_load","load count: " + noti_count_str_old);

                    if(noti_count_str.equals("0")){
                        Log.d("puii_load","load IF equals0: "+noti_count_str);
                        cart_badge = findViewById(R.id.cart_badge_message);
                        cart_badge.setVisibility(View.GONE);
//                    itemView.removeView(notificationBadge);
                    }
                    else{
                        Log.d("puii_load","load IF equals else: "+noti_count_str);
//                    itemView.addView(notificationBadge);
                        cart_badge = findViewById(R.id.cart_badge_message);
                        cart_badge.setVisibility(View.VISIBLE);
                        cart_badge.setText(String.valueOf(noti_count_str));
                    }
                }
            }
            else{
                Log.d("puii_load","load noti count CATCH");
                noti_count_str_old = "0";
                Log.d("puii_load","load noti count CATCH cont"+noti_count_str_old);
                cart_badge = findViewById(R.id.cart_badge_message);
                cart_badge.setVisibility(View.GONE);
            }

        }catch (JSONException ex) {
            Log.d("puii_load","load_catch1");
            ex.printStackTrace();
            noti_count_str = null;
            noti_count_str_old = "0";
            cart_badge = findViewById(R.id.cart_badge_message);
            cart_badge.setVisibility(View.GONE);
//            Toast.makeText(getActivity().getApplicationContext(), "ไม่มีข้อมูล", Toast.LENGTH_SHORT).show();
        }
    }

    public void setHandler(){
        final Handler handler = new Handler();
        final int delay = 1000 ; //1000 milliseconds = 1 sec
//        final NotificationAdapter adapter2 = new NotificationAdapter(getActivity(), rowItems);

        Log.d("puii","puii in1");

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!condition)
                {

                    if (noti_count_str_old != null)
                    {
                        if(noti_count_str_old.equals("0")){
                            Log.d("puii","postDelayed IF equals0: "+noti_count_str);
                            cart_badge = findViewById(R.id.cart_badge_message);
                            cart_badge.setVisibility(View.GONE);
                        }
                        else{
                            Log.d("puii","postDelayed IF equals else: "+noti_count_str);
                            cart_badge = findViewById(R.id.cart_badge_message);
                            cart_badge.setVisibility(View.VISIBLE);
                            cart_badge.setText(String.valueOf(noti_count_str_old));
                        }
                    }
                    else{
                        Log.d("puii","postDelayed ELSE: null");
                        cart_badge = findViewById(R.id.cart_badge_message);
                        cart_badge.setVisibility(View.GONE);
                    }
                    Log.d("puii_handle","Handle load noti count CATCH cont"+noti_count_str_old);
                    loadNotiCount2();
                    handler.postDelayed(this, delay);
                }


            }
        }, delay);
    }

    public void stophandler()
    {
        condition=true;
    }

    private void loadNotiCount2(){
//        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti_message_count.php?pet_id=" + pet_id;
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
                Toast.makeText(HomeActivity.this.getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this.getApplicationContext());
        requestQueue.add(stringRequest);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(HomeActivity.this.getApplicationContext());
        }
    }

    public void showNotiCount2(String response){
        try {
            Log.d("puii_load","Refresh load noti count");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if(result.length() > 0) {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject collectData = result.getJSONObject(i);

                    noti_count_str = collectData.getString("noti_count");
                    noti_count_str_new = noti_count_str;
                    Log.d("pet", noti_count_str);

//                    if (noti_count_str.equals("0")) {
//                        Log.d("puii", "IF equals0: " + noti_count_str);
//                        cart_badge = findViewById(R.id.cart_badge);
//                        cart_badge.setVisibility(View.GONE);
//                        //                    itemView.removeView(notificationBadge);
//                    } else {
//                        Log.d("puii", "IF equals else: " + noti_count_str);
//                        //                    itemView.addView(notificationBadge);
//                        cart_badge = findViewById(R.id.cart_badge);
//                        cart_badge.setVisibility(View.VISIBLE);
//                        cart_badge.setText(String.valueOf(noti_count_str));
//                    }

                    Log.d("puii", "Refresh load count: " + noti_count_str_old);

                    int noti_count_int_new = Integer.parseInt(noti_count_str_new);
                    int noti_count_int_old = Integer.parseInt(noti_count_str_old);
                    Log.d("pet", noti_count_str);
                    if (noti_count_int_new > noti_count_int_old) {
                        if (noti_count_str.equals("0")) {
                            Log.d("puii", "IF equals0: " + noti_count_str);
                            cart_badge = findViewById(R.id.cart_badge_message);
                            cart_badge.setVisibility(View.GONE);
                            //                    itemView.removeView(notificationBadge);
                        } else {
                            Log.d("puii", "IF equals else: " + noti_count_str);
                            //                    itemView.addView(notificationBadge);
                            cart_badge = findViewById(R.id.cart_badge_message);
                            cart_badge.setVisibility(View.VISIBLE);
                            cart_badge.setText(String.valueOf(noti_count_str));
                        }
                        addNotification();
                        Log.d("puii_max", "more: " + noti_message);
                    } else {
                        Log.d("puii_max", "less");
                    }
                }
                noti_count_str_old = noti_count_str;
            }
            else{
                noti_count_str_old = "0";
//                cart_badge = findViewById(R.id.cart_badge);
            }
        }catch (JSONException ex) {
            Log.d("puii","catch1");
            ex.printStackTrace();
//            noti_count_str = null;
//            noti_count_str_old = "0";
//            cart_badge.setVisibility(View.GONE);
//            Toast.makeText(getActivity().getApplicationContext(), "ไม่มีข้อมูล", Toast.LENGTH_SHORT).show();
        }
    }

//    private void loadNotiCount2(){
////        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
////        progressDialog.setMessage("Loading...");
////        progressDialog.show();
//
//        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti_message_count.php?pet_id=" + pet_id;
//        Log.d("url",url);
//
//        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
////                progressDialog.dismiss();
//                showNotiCount2(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeActivity.this.getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
//            }
//        }
//        );
//        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this.getApplicationContext());
//        requestQueue.add(stringRequest);
//    }
//    public void showNotiCount2(String response){
//        try {
//            Log.d("puii_max","try");
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray result = jsonObject.getJSONArray("result");
//
//            for (int i = 0; i<result.length(); i++){
//                JSONObject collectData = result.getJSONObject(i);
//
//
//                noti_count_str = collectData.getString("noti_count");
//                Log.d("puii_max","try: "+noti_count_str);
//                noti_message = collectData.getString("noti_message");
//                noti_count_str_new = noti_count_str;
//                int noti_count_int_new = Integer.parseInt(noti_count_str_new);
//                int noti_count_int_old = Integer.parseInt(noti_count_str_old);
//                Log.d("pet",noti_count_str);
//                if(noti_count_int_new > noti_count_int_old) {
//                    addNotification();
//                    Log.d("puii_max","more: " + noti_message);
//                }
//                else{
//                    Log.d("puii_max","less");
//                }
//            }
//            noti_count_str_old = noti_count_str;
//
//        }catch (JSONException ex) {
//            ex.printStackTrace();
//            Log.d("puii_max","catch");
//            noti_count_str = null;
//            cart_badge.setVisibility(View.GONE);
////            Toast.makeText(getActivity().getApplicationContext(), "ไม่มีข้อมูล", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void addNotification() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        String channelId = "notification1";
        String channelName = "my_notification1";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        NotificationManager manager = (NotificationManager) getSystemService(HomeActivity.this.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);


        int notificationId = 0;
//        String contentTitle = "แจ้งเตือนจาก: "+pet_name;
        String contentText = noti_message;
        Notification notification = new NotificationCompat.Builder(HomeActivity.this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setChannelId(channelId)
                .build();
        manager.notify(notificationId, notification);
    }


}
