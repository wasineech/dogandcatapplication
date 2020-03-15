package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectAccountActivity extends AppCompatActivity implements SelectAccountAdapter.OnItemClickListener {

    String pet_picture,pet_name,user_id,pet_id,noti_count_str;
    List<RowItem_Account> rowItems;
    RecyclerView showsearch_rv;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);

        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            user_id = _bundle.getString("user_id");
        }

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAccountActivity.this, AddPetActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        loadPetData();

        showsearch_rv = findViewById(R.id.list_account);
        showsearch_rv.setHasFixedSize(true);
        showsearch_rv.setLayoutManager(new LinearLayoutManager(SelectAccountActivity.this));
        showsearch_rv.addItemDecoration(new DividerItemDecoration(SelectAccountActivity.this,
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();
    }
    private void loadPetData(){
        final ProgressDialog progressDialog = new ProgressDialog(SelectAccountActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get.php?user_id=" + user_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectAccountActivity.this.getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(SelectAccountActivity.this.getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData.getString("pet_picture");
                noti_count_str = collectData.getString("noti_count");
//                Toast.makeText(SelectAccountActivity.this.getApplicationContext(), pet_picture , Toast.LENGTH_LONG).show();
                RowItem_Account item = new RowItem_Account(pet_id, pet_name, pet_picture,noti_count_str);
                rowItems.add(item);
            }
            SelectAccountAdapter adapter = new SelectAccountAdapter(SelectAccountActivity.this, rowItems);
            showsearch_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(SelectAccountActivity.this);

        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    public void onItemClick(int position) {
        RowItem_Account clickItem = rowItems.get(position);
        Intent intent = new Intent(SelectAccountActivity.this, HomeActivity.class);
        intent.putExtra("pet_id", clickItem.getPet_id());
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}