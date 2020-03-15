package com.myweb.dogandcatapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class AddMedFragment extends Fragment {
    String getPet_id,getUser_id,med_name,med_description,med_time;
    TextView txtName, txtDescription, txtDate;
    int mYear,mMonth,mDay,mHour,mMinute;
    Button btnSave;
    ImageView btn_back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_med,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        txtName = getView().findViewById(R.id.txtName);
        txtDescription = getView().findViewById(R.id.txtDescription);
        txtDate = getView().findViewById(R.id.txtDate);
        btnSave = getView().findViewById(R.id.btnSave);

        btn_back = getView().findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                data.putString("user_id", getUser_id);
                FragmentTransaction ft = fm.beginTransaction();
                MedFragment ssf = new MedFragment();
                ssf.setArguments(data);
                ft.replace(R.id.med_content, ssf);
                ft.commit();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                med_time = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity().getApplicationContext(),
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                med_name = txtName.getText().toString();
                med_description = txtDescription.getText().toString();
//                med_time = mYear+"-"+mMonth+"-"+mDay;
                new AddMedFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_med.php");
//                Log.d("add_med", "http://"+ConfigIP.IP+"/dogcat/add_med.php?med_name="+med_name+"&med_description="+med_description+
//                        "&med_time="+med_time+"&=pet_id="+getPet_id);
//                FragmentManager fm = getFragmentManager();
//                Bundle data = new Bundle();//create bundle instance
//                data.putString("pet_id", getPet_id);
//                FragmentTransaction ft = fm.beginTransaction();
//                AddMedFragment ssf = new AddMedFragment();
//                ssf.setArguments(data);
//                ft.replace(R.id.med_content, ssf);
//                ft.commit();
            }
        });

    }
    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pet_id", getPet_id)
                        .add("med_name", med_name)
                        .add("med_description", med_description)
                        .add("med_time", med_time)
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
                MedFragment ssf = new MedFragment();
                ssf.setArguments(data);
                ft.replace(R.id.med_content, ssf);
                ft.commit();
//                Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
//                intent.putExtra("user_id", getUser_id);
//                startActivity(intent);
            }else {
//                Toast.makeText(getContext(), "ไม่สามารถถลบ้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
}