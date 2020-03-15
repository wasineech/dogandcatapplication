package com.myweb.dogandcatapplication;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class AddPetActivity extends AppCompatActivity {
    String pet_kind,pet_name,pet_gender,pet_birthday,pet_breed,pet_breed_check,user_id;
    Button btnRegister;
    int mYear,mMonth,mDay,mHour,mMinute;
    RadioGroup kindGroup,genderGroup;
    RadioButton kindButton,genderButton;
    TextView txtName,txtBirthday,txtBreed;
    Spinner spnBreed;
    TableRow rowBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            user_id = _bundle.getString("user_id");
        }

        txtName = findViewById(R.id.txtName);
        txtBirthday = findViewById(R.id.txtBirthday);
        spnBreed = (Spinner) findViewById(R.id.breed);
        txtBreed = findViewById(R.id.txtBreed);
        rowBreed = findViewById(R.id.rowBreed);
        btnRegister = findViewById(R.id.btnRegister);
        kindGroup = (RadioGroup)findViewById(R.id.pet_kind);

        txtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        kindGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb =(RadioButton)findViewById(checkedId);
                String textViewChoice = rb.getText().toString();
                if(textViewChoice.equals("C")){
                    pet_kind = "แมว";
                    final String[] breedStr = getResources().getStringArray(R.array.cat_breed);
                    ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, breedStr);
                    spnBreed.setAdapter(adapterBreed);
                    spnBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            pet_breed_check = parent.getItemAtPosition(position).toString();
                            if(pet_breed_check.equals("อื่นๆ")) {
                                rowBreed.setVisibility(View.VISIBLE);
                            }
                            else{
                                rowBreed.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
                else{
                    pet_kind = "สุนัข";
                    final String[] breedStr = getResources().getStringArray(R.array.dog_breed);
                    ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, breedStr);

                    spnBreed.setAdapter(adapterBreed);
                    spnBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            pet_breed_check = parent.getItemAtPosition(position).toString();
                            if(pet_breed_check.equals("อื่นๆ")) {
                                rowBreed.setVisibility(View.VISIBLE);
                            }
                            else{
                                rowBreed.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pet_breed_check.equals("อื่นๆ")) {
                    pet_breed = "other"+txtBreed.getText().toString();
                }
                else{
                    pet_breed = pet_breed_check;
                }

                kindGroup = findViewById(R.id.pet_kind);
                int selectedId = kindGroup.getCheckedRadioButtonId();
                kindButton = findViewById(selectedId);
//                String s_pet_kind = kindButton.getText().toString();
//                if (s_pet_kind.equals("C")) {
//                    pet_gender = "แมว";
//                } else {
//                    pet_gender = "สุนัข";
//                }
//
//                Log.d("add_pet1",pet_gender);

                genderGroup = findViewById(R.id.gender);
                int selectedId2 = genderGroup.getCheckedRadioButtonId();
                genderButton = findViewById(selectedId2);
                String s_pet_gender = genderButton.getText().toString();
                if (s_pet_gender.equals("เพศผู้")) {
                    pet_gender = "ผู้";
                } else {
                    pet_gender = "เมีย";
                }

                pet_kind = kindButton.getText().toString();
                pet_name = txtName.getText().toString();
                pet_birthday = mYear+"-"+mMonth+"-"+mDay;

                if (pet_kind.isEmpty() || pet_name.isEmpty() || pet_gender.isEmpty()|| pet_birthday.isEmpty() ||pet_breed.isEmpty() ) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddPetActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                    Intent intent = new Intent(AddPetActivity.this, AddPetProfileActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("pet_name", pet_name);
                    intent.putExtra("pet_kind", pet_kind);
                    intent.putExtra("pet_breed", pet_breed);
                    intent.putExtra("pet_birthday", pet_birthday);
                    intent.putExtra("pet_gender", pet_gender);
                    startActivity(intent);
                }

            }
        });

    }
}
