package com.myweb.dogandcatapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class ProfileFragment extends Fragment {
    PageAdapter pageAdapter;
    TabLayout tabLayout;
    TabItem tabPost;
    TabItem tabGallery;
    TabItem tabMed;
    ImageView btnPopup;
    Button btnNoti;
    String getPet_id,getUser_id;
    TextView textPetName, textPetBreed,cart_badge;
    ImageView img_pet_profile;
    ImageView btnUp,btnUp2;
    LinearLayout header1;
    String pet_id,pet_name,pet_gender,pet_breed,pet_age,pet_birthday,pet_picture,user_id,output_date;
//    String noti_pet_id,noti_pet_kind,noti_pet_gender,noti_pet_breed,noti_pet_picture,noti_pet_profile,noti_pet_age,noti_noti_time,noti_noti_status,noti_province,noti_pet_name,noti_pet_name_gender,noti_pet_age_breed,noti_noti_pet_age,noti_status,noti_message,noti_time;
    List<RowItem_Notification> rowItems;
    int sum_noti = 0;
    Boolean condition=false;
    String noti_count_str,noti_count_str_old,noti_count_str_new,noti_message;
//    SharedPreferences sp_notification;
//    SharedPreferences.Editor editor;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        Toast.makeText(getContext(), getPet_id + getUser_id, Toast.LENGTH_LONG).show();

        textPetName = view.findViewById(R.id.textPetName);
        textPetBreed = view.findViewById(R.id.textPetBreed);
        cart_badge = view.findViewById(R.id.cart_badge);
        img_pet_profile = view.findViewById(R.id.pet_profile);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabPost = view.findViewById(R.id.tabPost);
        tabGallery = view.findViewById(R.id.tabGallery);
        tabMed = view.findViewById(R.id.tabMed);
        btnPopup = view.findViewById(R.id.btnPopup);
        btnUp = view.findViewById(R.id.btnUp);
        btnUp2 =  view.findViewById(R.id.btnUp2);
        header1 = view.findViewById(R.id.header1);
        btnNoti = view.findViewById(R.id.btnNoti);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//
//        sp_notification = getActivity().getSharedPreferences("NOTI_COUNT", Context.MODE_PRIVATE);
//        noti_count_str = sp_notification.getString("NOTI_COUNT"+getPet_id,null);
//


//        if (noti_count_str != null)
//        {
//           if(noti_count_str.equals("0")){
//               cart_badge.setVisibility(View.GONE);
//           }
//           else{
//               cart_badge.setVisibility(View.VISIBLE);
//               cart_badge.setText(String.valueOf(noti_count_str));
//           }
//        }
//        else{
//            cart_badge.setVisibility(View.GONE);
//        }

        rowItems = new ArrayList<>();
        Log.d("puii","BEFORE SET");
        loadNotiCount();
        setHandler();
//        Log.d("puii","AFTER SET");
//        if(condition=true)
//        {
//            condition=false;
//            Log.d("puii","condition");
//        }
//


        loadPetData();

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stophandler();
                header1.setVisibility(View.GONE);
                btnUp2.setVisibility(View.VISIBLE);
            }
        });

        btnUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stophandler();
                header1.setVisibility(View.VISIBLE);
                btnUp2.setVisibility(View.GONE);
            }
        });

        btnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getContext(), btnPopup);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_profile, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        stophandler();
                        if(item.getTitle().equals("แก้ไขโปรไฟล์")){
                            DateFormat after = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat before = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;

                            try {
                                date = before.parse(pet_birthday);
                                output_date = after.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            FragmentManager fm = getFragmentManager();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("user_id", getUser_id);
                            data.putString("pet_id", getPet_id);
                            data.putString("pet_name", pet_name);
                            data.putString("pet_gender", pet_gender);
                            data.putString("pet_breed", pet_breed);
                            data.putString("pet_birthday", output_date);
                            FragmentTransaction ft = fm.beginTransaction();
                            EditPetFragment ssf = new EditPetFragment();
                            ssf.setArguments(data);
                            ft.replace(R.id.content_fragment, ssf);
                            ft.commit();
                        }
                        else if(item.getTitle().equals("แก้ไขที่อยู่")){
                            FragmentManager fm = getFragmentManager();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("user_id", getUser_id);
                            data.putString("pet_id", getPet_id);
                            FragmentTransaction ft = fm.beginTransaction();
                            EditLocationFragment ssf = new EditLocationFragment();
                            ssf.setArguments(data);
                            ft.replace(R.id.content_fragment, ssf);
                            ft.commit();
                        }
                        else if(item.getTitle().equals("เปลี่ยนสัตว์เลี้ยง")){
                            Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
                            intent.putExtra("user_id", getUser_id);
                            startActivity(intent);
                        }
                        else if(item.getTitle().equals("ลบสัตว์เลี้ยง")){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
                            builder.setCancelable(true);
                            builder.setMessage("คุณจะลบสัตว์เลี้ยงใช่หรือไม่?");
                            builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new ProfileFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/del_pet.php");
                                    //dialog.cancel();
                                }
                            });
                            android.app.AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else if(item.getTitle().equals("ออกจากระบบ")){
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                        else if(item.getTitle().equals("แมทช์")){
                            FragmentManager fm = getFragmentManager();
                            Bundle data = new Bundle();//create bundle instance
                            data.putString("user_id", getUser_id);
                            data.putString("pet_id", getPet_id);
                            FragmentTransaction ft = fm.beginTransaction();
                            ShowMatchFragment ssf = new ShowMatchFragment();
                            ssf.setArguments(data);
                            ft.replace(R.id.content_fragment, ssf);
                            ft.commit();
                        }
                        Toast.makeText(
                                getContext(),
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                editor = sp_notification.edit();
//                editor.putString("NOTI_COUNT","0");
//                editor.commit();
                stophandler();
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                data.putString("pet_name", pet_name);
                FragmentTransaction ft = fm.beginTransaction();
                NotificationFragment ssf = new NotificationFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }

        });

        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//        viewPager.setOnClickListener(new View.OnClickListener() {
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

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public Fragment getItem(int position) {
            Bundle data = new Bundle();

            switch (position) {
                case 0:
                    PostFragment post_fragment = new PostFragment();
                    data.putString("pet_id", getPet_id);
                    data.putString("user_id", getUser_id);
                    post_fragment.setArguments(data);
                    return post_fragment;
                case 1:
                    GalleryFragment gal_fragment = new GalleryFragment();
                    data.putString("pet_id", getPet_id);
                    data.putString("user_id", getUser_id);
                    gal_fragment.setArguments(data);
                    return gal_fragment;
                case 2:
                    MedFragment med_fragment = new MedFragment();
                    data.putString("pet_id", getPet_id);
                    data.putString("user_id", getUser_id);
                    med_fragment.setArguments(data);
                    return med_fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
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
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/pet_profile.php?pet_id=" + getPet_id;
        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();
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
                pet_breed = collectData.getString("pet_breed");
                pet_age = collectData.getString("pet_age");
                pet_birthday = collectData.getString("pet_birthday");
                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData.getString("pet_picture");

//                String ImagePath = "http://"+ConfigIP.IP+"/dogcat/upload/ei.jpg";
                //File sd = Environment.getExternalStorageDirectory();
                //File imgFile = new  File("http://"+ConfigIP.IP+"/upload/ei.jpg");
//                bitmap = BitmapFactory.decodeFile(ImagePath);

                Log.d("pet",pet_name+pet_gender+pet_age+pet_breed+pet_picture);


                textPetName.setText(pet_name+" (เพศ: "+pet_gender+")");
                textPetBreed.setText("อายุ: " + pet_age + " สายพันธ์: " + pet_breed);
                Picasso.get().load(pet_picture).into(img_pet_profile);
//                ImgPet.setImageBitmap(bitmap);


            }

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
                Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("user_id", getUser_id);
                startActivity(intent);
            }else {
                Toast.makeText(getContext(), "ไม่สามารถถลบ้อมูลได้",Toast.LENGTH_SHORT).show();
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
//
//    private void loadRefresh(){
//
//        loadNotiCount();
//
//        String url = "http://"+ConfigIP.IP+"/dogcat/get_noti_profile.php?pet_id="+getPet_id;
//        Log.d("get_url",url);
//        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                refreshJSON(response);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
//            }
//        }
//        );
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
//        requestQueue.add(stringRequest);
//    }
//    public void refreshJSON(String response){
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray result = jsonObject.getJSONArray("result");
//
//            int row_count = rowItems.size();
//            int get_count = 0;
//
//            Log.d("puii","row_count 1111: "+row_count);
//            Log.d("puii","get_count 1111: "+get_count);
//
////            sp_notification = getActivity().getSharedPreferences("NOTI_COUNT", Context.MODE_PRIVATE);
////            noti_count_str = sp_notification.getString("NOTI_COUNT"+getPet_id,null);
//
//            rowItems.clear();
//
//            for (int i = 0; i<result.length(); i++){
//                JSONObject collectData = result.getJSONObject(i);
//                get_count++;
//
//
//                noti_pet_id = collectData.getString("pet_id");
//                noti_pet_name = collectData.getString("pet_name");
//                noti_pet_kind = collectData.getString("pet_kind");
//                noti_pet_gender = collectData.getString("pet_gender");
//                noti_pet_age = collectData.getString("pet_age");
//                noti_pet_breed = collectData.getString("pet_breed");
//                noti_pet_picture = collectData.getString("pet_picture");
//                noti_time = collectData.getString("match_timestamp");
//                noti_status = collectData.getString("noti_status");
//                noti_pet_profile = "http://"+ConfigIP.IP+"/dogcat/"+pet_picture;
//                noti_pet_name_gender = "ชื่อ: " + pet_name + " " + "( เพศ: " + pet_gender + ")";
//                noti_pet_age_breed = "อายุ: " +  pet_age + " สายพันธ์: " + pet_breed;
//
//                if(noti_status.equals("0")){
//                    noti_message = noti_pet_name+"ได้ทำการกดแมทซ์คุณ คุณจะตอบรับหรือไม่?";
//                }
//                else if(noti_status.equals("1")){
//                    noti_message = noti_pet_name+"ได้ตอบรับการแมทซ์ของคุณแล้ว เริ่มแชทได้เลย";
//                }
//
//                Log.d("puii","row_count 2222: "+row_count);
//                Log.d("puii","get_count 2222: "+get_count);
//                int row_count2 = rowItems.size();
//
//                Log.d("puii","row_count 22224444: "+row_count2);
//                if(row_count == 0) {
//                    if (noti_count_str != null)
//                    {
//                        if(noti_count_str.equals("0")){
//                            RowItem_Notification item = new RowItem_Notification(noti_pet_id, noti_pet_name, noti_pet_gender, noti_pet_age_breed, noti_pet_profile, noti_message, noti_time, noti_status);
//                            rowItems.add(item);
//                        }
//                        else{
////                            editor = sp_notification.edit();
////                            editor.putString("NOTI_COUNT",noti_count_str);
//                        }
//                    }
//                    else{
//                        RowItem_Notification item = new RowItem_Notification(noti_pet_id, noti_pet_name, noti_pet_gender, noti_pet_age_breed, noti_pet_profile, noti_message, noti_time, noti_status);
//                        rowItems.add(item);
//                    }
//                }
//
//                else{
//                    if (noti_count_str != null)
//                    {
//                        if(noti_count_str.equals("0")){
//                            if(get_count > row_count){
//                                addNotification();
////                        int sum_noti = get_count-row_count;
//                                sum_noti = sum_noti+1;
//                                String sum_noti_str = String.valueOf(sum_noti);
//                                noti_count_str = sum_noti_str;
//                                new ProfileFragment.InsertCount().execute("http://"+ConfigIP.IP+"/dogcat/edit_noti_count.php");
////                                editor = sp_notification.edit();
////                                editor.putString("NOTI_COUNT",sum_noti_str);
////                                editor.commit();
////                        cart_badge.setVisibility(View.VISIBLE);
////                        cart_badge.setText(String.valueOf(sum_noti));
////                        Log.d("puii","VISIBLE: VISIBLE");
//                                Log.d("puii","onrefresh noti_count_str.equals0: "+sum_noti_str);
////                    showNotification(getView());
//                            }
//                            else{
////
//                            }
//                        }
//                        else{
//                            if(get_count > row_count) {
//                                addNotification();
//                                //                        int sum_noti = get_count-row_count;
//                                sum_noti = sum_noti + 1;
//                                int noti_count_int = Integer.parseInt(noti_count_str);
//                                int sum_noti_new = sum_noti + noti_count_int;
//                                String sum_noti_str = String.valueOf(sum_noti_new);
//                                noti_count_str = sum_noti_str;
//                                new ProfileFragment.InsertCount().execute("http://"+ConfigIP.IP+"/dogcat/edit_noti_count.php");
////                                Log.d("puii", "onrefresh ELSE sum_noti: " + sum_noti);
////                                Log.d("puii", "onrefresh ELSE noti_count_int: " + noti_count_int);
////                                String sum_noti_str = String.valueOf(sum_noti_new);
////                                Log.d("puii", "onrefresh ELSE sum_noti_new: " + sum_noti_new);
////                                Log.d("puii", "onrefresh ELSE sum_noti+noti_count_int STR: " + sum_noti_str);
////                                editor = sp_notification.edit();
////                                editor.putString("NOTI_COUNT", sum_noti_str);
////                                editor.commit();
//                                //                            editor = sp_notification.edit();
//                                //                            editor.putString("NOTI_COUNT",noti_count_str);
//                            }
//                            else{
//
//                            }
//                        }
//                    }
//                    else{
//                        if(get_count > row_count){
//                            addNotification();
////                        int sum_noti = get_count-row_count;
//                            sum_noti = sum_noti+1;
//                            String sum_noti_str = String.valueOf(sum_noti);
//                            noti_count_str = sum_noti_str;
//                            new ProfileFragment.InsertCount().execute("http://"+ConfigIP.IP+"/dogcat/add_noti_count.php");
////                            Log.d("puii","onrefresh noti_count_str != null: "+sum_noti_str);
////                            editor = sp_notification.edit();
////                            editor.putString("NOTI_COUNT",sum_noti_str);
////                            editor.commit();
////                        cart_badge.setVisibility(View.VISIBLE);
////                        cart_badge.setText(String.valueOf(sum_noti));
////                        Log.d("puii","VISIBLE: VISIBLE");
////                            Log.d("puii","onrefresh"+sum_noti_str);
//
////                    showNotification(getView());
//                        }
//                        else{
////                        Log.d("puii","VISIBLE: GONE");
//                        }
////                        RowItem_Notification item = new RowItem_Notification(noti_pet_id, noti_pet_name, noti_pet_gender, noti_pet_age_breed, noti_pet_profile, noti_message, noti_time, noti_status);
////                        rowItems.add(item);
//                    }
////                    if(get_count > row_count){
////                        addNotification();
//////                        int sum_noti = get_count-row_count;
////                        sum_noti = sum_noti+1;
////                        String sum_noti_str = String.valueOf(sum_noti);
////                        editor = sp_notification.edit();
////                        editor.putString("NOTI_COUNT",sum_noti_str);
////                        editor.commit();
//////                        cart_badge.setVisibility(View.VISIBLE);
//////                        cart_badge.setText(String.valueOf(sum_noti));
//////                        Log.d("puii","VISIBLE: VISIBLE");
////                        Log.d("puii","onrefresh"+sum_noti_str);
////
//////                    showNotification(getView());
////                    }
////                    else{
//////                        Log.d("puii","VISIBLE: GONE");
////                    }
//                }
//                RowItem_Notification item = new RowItem_Notification(noti_pet_id, noti_pet_name, noti_pet_gender, noti_pet_age_breed, noti_pet_profile, noti_message, noti_time, noti_status);
//                rowItems.add(item);
//
//                //Log.d("item",item);
//
//                // showsearch_listview.setOnItemClickListener(this);
//
//            }
//
//
//
//
//
//        }catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//    }
//

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