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
import android.widget.RelativeLayout;
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

public class PostFragment extends Fragment {
    String getPet_id;
    ImageView pet_profile1;
    String post_id,post_content,post_photo,post_time,pet_id,pet_name,pet_picture,check_photo;
    List<RowItem_Post> rowItems;
    RecyclerView post_rv;
    Button btnPost,btnPicture;
    EditText txtPost;
    String get_post_content;
    RelativeLayout relativePost;
//    int PICK_IMAGE_REQUEST = 1;
//    Bitmap bitmap;
//    Uri filePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");

//        Log.d("getPet_id2",getPet_id);

        Toast.makeText(getContext(), getPet_id, Toast.LENGTH_LONG).show();

        pet_profile1 = getView().findViewById(R.id.pet_profile1);
        txtPost = getView().findViewById(R.id.txtPost);
        btnPost = getView().findViewById(R.id.btnPost);
        btnPicture = getView().findViewById(R.id.btnPicture);
        relativePost = getView().findViewById(R.id.relativePost);

//
//        relativePost.setOnClickListener(new View.OnClickListener() {
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

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_post_content = txtPost.getText().toString();
                new PostFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_post.php");
                txtPost.setText("");
                post_rv = getView().findViewById(R.id.list_post);
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
                ft.replace(R.id.relativePost, ssf);
                ft.commit();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        post_rv = getView().findViewById(R.id.list_post);
        post_rv.setHasFixedSize(true);
        post_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        post_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();
        loadPetData();
        loadPostData();
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

        String url = "http://"+ConfigIP.IP+"/dogcat/get_pet_post.php?pet_id=" + getPet_id;
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

                Log.d("pet",pet_picture);

//                Picasso.get().load(pet_picture).into(pet_profile1);

                RowItem_Post item = new RowItem_Post(post_id,post_content,post_photo,post_time,pet_id,pet_name,pet_picture);
                rowItems.add(item);

            }

            PostAdapter adapter = new PostAdapter(getActivity(), rowItems);
//            Log.d("act1",getActivity().toString());
            post_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Post clickItem = rowItems.get(position);
                    if(clickItem.getPost_photo().equals("http://"+ConfigIP.IP+"/dogcat/no_photo")) {
                        check_photo = "no_photo";
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        CommentFragment ssf = new CommentFragment();
                        Bundle data = new Bundle();//create bundle instance
                        data.putString("pet_id", getPet_id);
                        data.putString("post_id", clickItem.getPost_id());
                        data.putString("post_content", post_content);
                        data.putString("post_photo", check_photo);
                        data.putString("post_time", post_time);
                        ssf.setArguments(data);
                        ft.replace(R.id.relativePost, ssf);
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
                        ft.replace(R.id.relativePost, ssf);
                        ft.commit();
                    }
                }
                public void onPopupClick(int position) {
                    RowItem_Post clickItem = rowItems.get(position);

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

//    public void onItemClick(int position) {
//        RowItem_Post clickItem = rowItems.get(position);
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ShowPostFragment ssf = new ShowPostFragment();
//        Bundle data = new Bundle();
//        data.putString("post_id", post_id);
//        data.putString("post_content", post_content);
//        data.putString("post_photo", post_photo);
//        data.putString("post_time", post_time);
//        ssf.setArguments(data);
//        ft.replace(R.id.content_fragment, ssf);
//        ft.commit();
//    }
}
