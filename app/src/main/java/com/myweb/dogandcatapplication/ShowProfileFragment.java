package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ShowProfileFragment extends Fragment {
    PageAdapter pageAdapter;
    TabLayout tabLayout;
    TabItem tabPost;
    TabItem tabMed;
    TabItem tabOwner;
    LinearLayout header1,header2;
    ImageView btnPopup,btnUp2;
    String getPet_id,getUser_id,getPet_id2;
    TextView textPetName, textPetBreed;
    ImageView img_pet_profile;
    String pet_id,pet_name,pet_gender,pet_breed,pet_age,pet_birthday,pet_picture,user_id,output_date;
    Button btnMatch;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_profile, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");
        getPet_id2 = getArguments().getString("pet_id2");

        Toast.makeText(getContext(), getPet_id + getUser_id, Toast.LENGTH_LONG).show();

        textPetName = view.findViewById(R.id.textPetName);
        textPetBreed = view.findViewById(R.id.textPetBreed);
        img_pet_profile = view.findViewById(R.id.pet_profile);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabPost = view.findViewById(R.id.tabPost);
        tabMed = view.findViewById(R.id.tabMed);
        tabOwner = view.findViewById(R.id.tabOwner);
        btnPopup = view.findViewById(R.id.btnPopup);
        btnUp2 =  view.findViewById(R.id.btnUp2);
        btnMatch =  view.findViewById(R.id.btnMatch);
        header1 = view.findViewById(R.id.header1);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        loadPetData();

        btnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header1.setVisibility(View.GONE);
                btnUp2.setVisibility(View.VISIBLE);
            }
        });

        btnUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header1.setVisibility(View.VISIBLE);
                btnUp2.setVisibility(View.GONE);
            }
        });

//        btnMatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShowProfileFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_match.php?pet_id2=" + getPet_id2);
////                FragmentManager fm = getFragmentManager();
////                FragmentTransaction ft = fm.beginTransaction();
////                SearchFragment ssf = new SearchFragment();
////                ft.replace(R.id.content_fragment, ssf);
////                ft.commit();
//            }
//        });




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

        viewPager.setAdapter(new ShowProfileFragment.PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
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
                    ShowProfilePostFragment post_fragment = new ShowProfilePostFragment();
                    data.putString("pet_id", getPet_id2);
                    post_fragment.setArguments(data);
                    return post_fragment;
                case 1:
                    GalleryFragment gallery_fragment = new GalleryFragment();
                    data.putString("pet_id", getPet_id2);
                    gallery_fragment.setArguments(data);
                    return gallery_fragment;
                case 2:
                    MedFragment med_fragment = new MedFragment();
                    data.putString("pet_id", getPet_id2);
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

    private void loadPetData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/pet_profile.php?pet_id=" + getPet_id2;
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

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_gender = collectData.getString("pet_gender");
                pet_breed = collectData.getString("pet_breed");
                pet_age = collectData.getString("pet_age");
                pet_birthday = collectData.getString("pet_birthday");
                pet_picture = collectData.getString("pet_picture");

//                String ImagePath = "http://"+ConfigIP.IP+"/dogcat/upload/ei.jpg";
                //File sd = Environment.getExternalStorageDirectory();
                //File imgFile = new  File("http://"+ConfigIP.IP+"/upload/ei.jpg");
//                bitmap = BitmapFactory.decodeFile(ImagePath);

                Log.d("pet",pet_name+pet_gender+pet_age+pet_breed+pet_picture);


                textPetName.setText(pet_name+" (เพศ: "+pet_gender+")");
                textPetBreed.setText("อายุ: " + pet_age + " สายพันธ์: " + pet_breed);
                Picasso.get().load("http://"+ConfigIP.IP+"/dogcat/"+pet_picture).into(img_pet_profile);
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
                        .add("pet_id1", getPet_id)
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
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("user_id", getUser_id);
                data.putString("pet_id", getPet_id);
                FragmentTransaction ft = fm.beginTransaction();
                SearchFragment ssf = new SearchFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();

            }else {
//                Toast.makeText(getContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
