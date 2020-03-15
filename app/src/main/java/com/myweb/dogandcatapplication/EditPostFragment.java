package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EditPostFragment extends Fragment {
    String getPet_id,getPost_id,post_id,post_content,post_photo,post_time,pet_id,pet_name,pet_picture,
            get_post_comment,getCheck_photo,get_post_content,get_post_photo,get_post_time;
    ImageView pet_profile,post_picture,pet_profile_c2;
    TextView pet_name_txt,post_time_txt;
    EditText edit_comment,post_caption;
    Button btn_edit_post;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_post, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getPost_id = getArguments().getString("post_id");
        get_post_content  = getArguments().getString("post_content");
        getCheck_photo = getArguments().getString("post_photo");
        get_post_time  = getArguments().getString("post_time");

        pet_profile = getView().findViewById(R.id.pet_profile);
        post_picture = getView().findViewById(R.id.post_picture);
        pet_name_txt = getView().findViewById(R.id.pet_name);
        post_time_txt = getView().findViewById(R.id.post_time);
        post_caption = getView().findViewById(R.id.post_caption);
        btn_edit_post = getView().findViewById(R.id.btn_edit_post);

        loadPostData();

        btn_edit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditPostFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/edit_post.php");
            }
        });
    }

    private void loadPostData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_pet_post_id.php?post_id=" + getPost_id;
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

                post_id = collectData.getString("post_id");
                post_content= collectData.getString("post_content");
                post_photo = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("post_photo");
                post_time = collectData.getString("post_time");
                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData.getString("pet_picture");
                String p_photo = collectData.getString("post_photo");

                Log.d("pet",pet_picture);

                if(p_photo.equals("no_photo")){
                    Picasso.get().load(pet_picture).into(pet_profile);
                    post_picture.setVisibility(View.GONE);
                    pet_name_txt.setText(pet_name);
                    post_time_txt.setText(post_time);
                    post_caption.setText(post_content);
                }
                else {
                    Picasso.get().load(pet_picture).into(pet_profile);
                    Picasso.get().load(post_photo).into(post_picture);
                    pet_name_txt.setText(pet_name);
                    post_time_txt.setText(post_time);
                    post_caption.setText(post_content);
                }

            }

        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                String get_post_caption_edit = post_caption.getText().toString();
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("post_id", getPost_id)
                        .add("post_caption", get_post_caption_edit)
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
                Toast.makeText(getContext(), "แก้ไขข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "ไม่สามารถแก้ไขข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

}