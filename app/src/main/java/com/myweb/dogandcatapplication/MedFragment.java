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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class MedFragment extends Fragment {
    Button btnAdd;
    String getPet_id,getUser_id,med_id,med_name,med_description,med_timestamp;
    List<RowItem_Med> rowItems;
    RecyclerView med_rv;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_med, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        btnAdd = getView().findViewById(R.id.btnAdd);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("user_id", getUser_id);
                data.putString("pet_id", getPet_id);
                FragmentTransaction ft = fm.beginTransaction();
                AddMedFragment ssf = new AddMedFragment();
                ssf.setArguments(data);
                ft.replace(R.id.med_content, ssf);
                ft.commit();
            }
        });

        med_rv = getView().findViewById(R.id.list_med);
        med_rv.setHasFixedSize(true);
        med_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        med_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();


        loadMedData();


//        popup1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(getContext(), popup1);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater()
//                        .inflate(R.menu.popup_menu_med, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if(item.getTitle().equals("แก้ไขประวัติการรักษา")){
//                            FragmentManager fm = getFragmentManager();
//                            FragmentTransaction ft = fm.beginTransaction();
//                            EditMedFragment ssf = new EditMedFragment();
//                            ft.replace(R.id.med_content, ssf);
//                            ft.commit();
//                        }
//                        Toast.makeText(
//                                getContext(),
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
//                        return true;
//                    }
//                });
//
//                popup.show(); //showing popup menu
//            }
//        });


    }

    private void loadMedData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_med.php?pet_id=" + getPet_id;
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
            Log.d("pet_home","response");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                med_id = collectData.getString("med_id");
                med_name= collectData.getString("med_name");
                med_description = collectData.getString("med_description");
                med_timestamp = collectData.getString("med_timestamp");

                Log.d("med_id",med_id);
                Log.d("med_name",med_name);

//                Picasso.get().load(pet_picture).into(pet_profile1);

                RowItem_Med item = new RowItem_Med(med_id,med_name,med_description,med_timestamp,getPet_id,getUser_id);
                rowItems.add(item);

            }

            final MedAdapter adapter = new MedAdapter(getActivity(), rowItems);
//            Log.d("act1",getActivity().toString());
            med_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new MedAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Med clickItem = rowItems.get(position);

                    new MedFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/del_med.php?med_id="+clickItem.getMed_id());
                    rowItems.remove(position);
                    adapter.notifyItemRemoved(position);

                    Log.d("med_del","http://"+ConfigIP.IP+"/dogcat/del_med.php?med_id="+clickItem.getMed_id());

                }
            });
//            adapter.notifyDataSetChanged();


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
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

//            if (result != null){
//                Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
//                intent.putExtra("user_id", getUser_id);
//                startActivity(intent);
//            }else {
//                Toast.makeText(getContext(), "ไม่สามารถถลบ้อมูลได้",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}