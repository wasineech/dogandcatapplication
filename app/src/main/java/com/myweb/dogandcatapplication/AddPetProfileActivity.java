package com.myweb.dogandcatapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddPetProfileActivity extends AppCompatActivity {
    String pet_image,user_id,pet_kind,pet_name,pet_gender,pet_birthday,pet_breed;
    ImageView pet_profile;
    Button btnAddProfile,btnRegister;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_profile);

        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            user_id = _bundle.getString("user_id");
            pet_name = _bundle.getString("pet_name");
            pet_kind = _bundle.getString("pet_kind");
            pet_breed = _bundle.getString("pet_breed");
            pet_birthday = _bundle.getString("pet_birthday");
            pet_gender = _bundle.getString("pet_gender");

        }

        pet_profile = findViewById(R.id.pet_profile);
        btnAddProfile = findViewById(R.id.btnAddProfile);
        btnRegister = findViewById(R.id.btnRegister);

        btnAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AddPetProfileActivity.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_pet.php");
                btnRegister.setEnabled(false);
            }
        });

    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                pet_image = getStringImage(bitmap);
                RequestBody _requestBody = new FormBody.Builder()
                        .add("user_id", user_id)
                        .add("pet_name", pet_name)
                        .add("pet_kind", pet_kind)
                        .add("pet_breed", pet_breed)
                        .add("pet_birthday", pet_birthday)
                        .add("pet_gender", pet_gender)
                        .add("pet_picture", pet_image)
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
                Intent intent = new Intent(AddPetProfileActivity.this, SelectAccountActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pet_profile.setImageBitmap(bitmap);
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
}
