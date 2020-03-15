package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ShowPetMatchFragment extends Fragment {
    String getPet_id2,get_pet_profile,get_pet_name,get_pet_gender,get_pet_age_breed;
    String pet_id,pet_gender,pet_breed,pet_picture,pet_rp,pet_name,pet_ng,pet_b,pet_age,pet_a;
    String getPet_id;
    TextView pet_name_gender, pet_age_breed;
    ImageView pet_profile;
    Button btnMatch,btnUnMatch;
    LinearLayout app_layer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_pet_match, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getPet_id2 = getArguments().getString("pet_id2");
        get_pet_profile = getArguments().getString("pet_profile");
        get_pet_name = getArguments().getString("pet_name");
        get_pet_gender = getArguments().getString("pet_gender");
        get_pet_age_breed = getArguments().getString("pet_age_breed");

        pet_name_gender = getView().findViewById(R.id.pet_name_gender);
        pet_age_breed = getView().findViewById(R.id.pet_age_breed);
        pet_profile = getView().findViewById(R.id.pet_profile);
        btnMatch = getView().findViewById(R.id.btnMatch);
        btnUnMatch = getView().findViewById(R.id.btnUnMatch);
        app_layer = getView().findViewById(R.id.show_pet_match);

        pet_name_gender.setText("ชื่อ: " + get_pet_name + " " + "( เพศ: " + get_pet_gender + ")");
        pet_age_breed.setText(get_pet_age_breed);;
        Picasso.get().load(get_pet_profile).into(pet_profile);

        app_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id2);
                FragmentTransaction ft = fm.beginTransaction();
                ShowProfileFragment ssf = new ShowProfileFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();

            }
        });

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowPetMatchFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/accept_match.php");

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
        btnUnMatch.setOnClickListener(new View.OnClickListener() {
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


    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id2", getPet_id)
                        .add("pet_id1", getPet_id2)
                        .build();


                Log.d("match","pet_id1:"+getPet_id2);
                Log.d("match","pet_id2:"+getPet_id);

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

}
