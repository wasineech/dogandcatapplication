package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
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


public class CommentFragment extends Fragment {
    String getPet_id,getPost_id,post_id,post_content,post_photo,post_time,pet_id,pet_name,pet_picture,post_comment_content,get_post_comment,getCheck_photo;
    ImageView pet_profile1,post_picture,pet_profile_c2;
    TextView pet_name_txt,post_time_txt,post_caption;
    EditText edit_comment;
    ImageView btn_post_comment,btn_back;
    List<RowItem_Comment> rowItems;
    RecyclerView comment_rv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getPost_id = getArguments().getString("post_id");
        getCheck_photo = getArguments().getString("post_photo");

        pet_profile1 = getView().findViewById(R.id.pet_profile_post1);
        post_picture = getView().findViewById(R.id.post_picture);
        pet_name_txt = getView().findViewById(R.id.pet_name);
        post_time_txt = getView().findViewById(R.id.post_time);
        post_caption = getView().findViewById(R.id.post_caption);
        pet_profile_c2 = getView().findViewById(R.id.pet_profile_c2);
        edit_comment = getView().findViewById(R.id.edit_comment);
        btn_post_comment = getView().findViewById(R.id.btn_post_comment);
        comment_rv = getView().findViewById(R.id.list_comment);
        btn_back = getView().findViewById(R.id.btn_back);




        btn_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_post_comment = edit_comment.getText().toString();
                new CommentFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_post_comment.php");
                edit_comment.setText("");
                comment_rv.setHasFixedSize(true);
                comment_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//                comment_rv.addItemDecoration(new DividerItemDecoration(getContext(),
//                        DividerItemDecoration.VERTICAL));
                rowItems = new ArrayList<>();
                loadPostData();
                loadPostData2();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment ssf = new HomeFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }
        });


        comment_rv.setHasFixedSize(true);
        comment_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        comment_rv.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();

        loadPostData();
        loadPostData2();



        Log.d("post_id_get",getPost_id);
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
                    Picasso.get().load(pet_picture).into(pet_profile1);
//                    Picasso.get().load(pet_picture).into(pet_profile_c2);
                    post_picture.setVisibility(View.GONE);
                    pet_name_txt.setText(pet_name);
                    post_time_txt.setText(post_time);
                    post_caption.setText(post_content);
                }
                else {
                    Picasso.get().load(pet_picture).into(pet_profile1);
//                    Picasso.get().load(pet_picture).into(pet_profile_c2);
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
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id", getPet_id)
                        .add("post_id", getPost_id)
                        .add("post_comment", get_post_comment)
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
    private void loadPostData2(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_comment_post_id.php?post_id=" + getPost_id;
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
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData2 = result.getJSONObject(i);

                post_id = collectData2.getString("post_id");
                post_comment_content= collectData2.getString("post_comment_content");
                pet_id = collectData2.getString("pet_id");
                pet_name = collectData2.getString("pet_name");
                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData2.getString("pet_picture");

                Log.d("pet",pet_picture);

//                Picasso.get().load(pet_picture).into(pet_profile1);

                RowItem_Comment item = new RowItem_Comment(post_id,post_comment_content,pet_id,pet_name,pet_picture);
                rowItems.add(item);

            }
            CommentAdapter adapter = new CommentAdapter(getActivity(), rowItems);
            comment_rv.setAdapter(adapter);
            Log.d("act3",getActivity().toString());


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
