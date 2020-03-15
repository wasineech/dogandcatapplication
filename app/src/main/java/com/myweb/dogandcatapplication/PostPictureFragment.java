package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class PostPictureFragment extends Fragment {
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filePath;
    String getPet_id;
    ImageView img_pet_profile,post_photo;
    EditText txtPost;
    String pet_id,pet_name,pet_picture,post_content,post_picture;
    Button btnPost;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_picture, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPet_id = getArguments().getString("pet_id");
        img_pet_profile = view.findViewById(R.id.pet_profile1);
        txtPost = view.findViewById(R.id.txtPost);
        post_photo = view.findViewById(R.id.post_photo);
        btnPost = view.findViewById(R.id.btnPost);

        loadPetData();

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pet_image = getStringImage(bitmap);
                post_content = txtPost.getText().toString();
                new PostPictureFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_post_picture.php");

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                post_photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void loadPetData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/pet_profile.php?pet_id=" + getPet_id;
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
                pet_picture = collectData.getString("pet_picture");

//              String ImagePath = "http://"+ConfigIP.IP+"/dogcat/upload/ei.jpg";
                //File sd = Environment.getExternalStorageDirectory();
                //File imgFile = new  File("http://"+ConfigIP.IP+"/upload/ei.jpg");
//                bitmap = BitmapFactory.decodeFile(ImagePath);

                Log.d("pet",pet_picture);

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
                post_picture = getStringImage(bitmap);
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id", getPet_id)
                        .add("post_content", post_content)
                        .add("post_picture", post_picture)
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
//                Toast.makeText(getContext(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                FragmentTransaction ft = fm.beginTransaction();
                PostFragment ssf = new PostFragment();
                ssf.setArguments(data);
                ft.replace(R.id.relativePost, ssf);
                ft.commit();
            }else {
                Toast.makeText(getContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
