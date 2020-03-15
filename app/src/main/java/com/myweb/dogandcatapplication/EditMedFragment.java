package com.myweb.dogandcatapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EditMedFragment extends Fragment {
    String getPet_id,getUser_id,get_med_id,get_med_name,get_med_med_description,get_med_med_time;
    String med_name, med_description, med_time;
    TextView edit_med_name, edit_med_description, edit_med_date;
    ImageView btn_back;
    Button btnEdit;
    String output;
    String check = "get";
    int mYear, mMonth, mDay;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_med,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        get_med_id = getArguments().getString("med_id");
        get_med_name  = getArguments().getString("med_name");
        get_med_med_description  = getArguments().getString("med_description");
        get_med_med_time  = getArguments().getString("med_time");

        edit_med_name = getView().findViewById(R.id.edit_med_name);
        edit_med_description = getView().findViewById(R.id.edit_med_description);
        edit_med_date = getView().findViewById(R.id.edit_med_date);
        btnEdit = getView().findViewById(R.id.btnEdit);
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

        edit_med_name.setText(get_med_name);
        edit_med_description.setText(get_med_med_description);
        edit_med_date.setText(get_med_med_time);

        edit_med_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "date_onclick";
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edit_med_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                                rYear = dayOfMonth ;
//                                rMonth = (monthOfYear + 1);
//                                rDay = year ;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pet_image = getStringImage(bitmap);
                med_name = edit_med_name.getText().toString();
                med_description = edit_med_description.getText().toString();
                med_time = edit_med_date.getText().toString();

                if(check.equals("date_onclick")) {
                    DateFormat before = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat after = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;

                    try {
                        date = before.parse(med_time);
                        output = after.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.d("pet_birthday", med_time);
                    Log.d("med_date", "med: " + med_time);
                    Log.d("output_date", "op" + output);
                }
                else{
                    output = med_time;
                }

                if (med_name.isEmpty() || med_description.isEmpty()|| med_time.isEmpty()) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                    new EditMedFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/edit_med.php?pet_id=" + getPet_id);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    MedFragment ssf = new MedFragment();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("user_id", getUser_id);
                    data.putString("pet_id", getPet_id);
                    ssf.setArguments(data);
                    ft.replace(R.id.med_content, ssf);
                    ft.commit();
                }

            }
        });
    }


    public class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("med_id", get_med_id)
                        .add("med_name", med_name)
                        .add("med_description", med_description)
                        .add("med_time", output)
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
        public void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null){
                // Toast.makeText(EditPetFragment.this, "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                //Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }




}
