package com.myweb.dogandcatapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegUserActivity extends AppCompatActivity {
    String name,email,password,password2,province;
    Button btnRegister;
    TextView txtName, txtEmail, txtPassword, txtPassword2;
    Spinner spnProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtPassword2 = findViewById(R.id.txtPassword2);
        btnRegister = findViewById(R.id.btnRegister);
        spnProvince = findViewById(R.id.province);

        final String[] pvStr = getResources().getStringArray(R.array.province);
        ArrayAdapter<String> adapterDept = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pvStr);

        spnProvince.setAdapter(adapterDept);
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

                province = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = txtName.getText().toString();
                password = txtPassword.getText().toString();
                password2 = txtPassword2.getText().toString();
                email = txtEmail.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegUserActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    builder.setCancelable(true);
                    builder.setMessage("กรุณากรอกข้อมูลให้ครบ");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();


                } else {

                    if(!password.equals(password2)){

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegUserActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        builder.setCancelable(true);
                        builder.setMessage("กรุณากรอกรหัสผ่านให้ตรงกัน");
                        builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                    else {
                        new InsertAsyn().execute("http://" + ConfigIP.IP + "/dogcat/register.php");
                    }
                }

            }
        });
    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("name", name)
                        .add("password", password)
                        .add("email", email)
                        .add("province", province)
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
                //Toast.makeText(getApplicationContext(), result,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegUserActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
