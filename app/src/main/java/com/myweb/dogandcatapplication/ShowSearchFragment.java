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


public class ShowSearchFragment extends Fragment {
    String get_pet_kind,get_pet_gender,get_pet_breed,get_pet_age,get_province,pet_id,pet_kind,pet_gender,pet_breed,pet_picture,pet_rp,province,pet_name,pet_ng,pet_b,pet_age,pet_a;
    String getPet_id,getUser_id,getPet_id2;
    List<RowItem_ShowSearch> rowItems;
    RecyclerView showsearch_rv;
    ImageView btn_back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_search, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");
        get_pet_kind = getArguments().getString("pet_kind");
        get_pet_gender = getArguments().getString("pet_gender");
        get_pet_breed = getArguments().getString("pet_breed");
        get_pet_age = getArguments().getString("age");
        get_province = getArguments().getString("province");

        btn_back = getView().findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                data.putString("user_id", getUser_id);
                FragmentTransaction ft = fm.beginTransaction();
                SearchFragment ssf = new SearchFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }
        });


        Log.d("get_province","k:"+get_pet_kind+" |g:"+get_pet_gender+" |b:"+get_pet_breed+" |p:"+get_province);

        showsearch_rv = getView().findViewById(R.id.list_show_search);
        showsearch_rv.setHasFixedSize(true);
        showsearch_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        showsearch_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));


        Toast.makeText(getContext(), get_pet_kind + get_pet_gender + get_pet_breed + get_province,Toast.LENGTH_SHORT).show();
        rowItems = new ArrayList<>();
        loadShowPet();

    }

    private void loadShowPet(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_showpet.php?pet1_id="+getPet_id+"&pet_kind=" + get_pet_kind + "&pet_gender=" + get_pet_gender + "&pet_breed=" + get_pet_breed + "&pet_age=" + get_pet_age +"&province=" + get_province+"&user_id=" + getUser_id;
        Log.d("get_url",url);
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
                pet_name = collectData.getString("pet_name");
                pet_kind = collectData.getString("pet_kind");
                pet_gender = collectData.getString("pet_gender");
                pet_age = collectData.getString("pet_age");
                pet_breed = collectData.getString("pet_breed");
                pet_picture = collectData.getString("pet_picture");
                pet_rp = "http://"+ConfigIP.IP+"/dogcat/"+pet_picture;
                pet_ng = "ชื่อ: " + pet_name + " " + "( เพศ: " + pet_gender + ")";
                pet_b = "อายุ: " +  pet_age + " สายพันธ์: " + pet_breed;

                Log.d("petnameSS",pet_ng);
                Log.d("pet_pictureSS",pet_b);
                Log.d("pet_ageSS",pet_rp);
//                province = collectData.getString("province");


                RowItem_ShowSearch item = new RowItem_ShowSearch(pet_id,pet_ng,pet_b,pet_rp);
                rowItems.add(item);

                //Log.d("item",item);

                // showsearch_listview.setOnItemClickListener(this);

            }


            ShowSearchAdapter adapter = new ShowSearchAdapter(getActivity(), rowItems);
            showsearch_rv.setAdapter(adapter);

            adapter.setOnItemClickListener(new ShowSearchAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_ShowSearch clickItem = rowItems.get(position);
                    FragmentManager fm = getFragmentManager();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id2", clickItem.getPet_id());
                    data.putString("pet_id", getPet_id);
                    FragmentTransaction ft = fm.beginTransaction();
                    ShowProfileFragment ssf = new ShowProfileFragment();
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();

                }

                public void onMatchClick(int position) {
                    RowItem_ShowSearch clickItem = rowItems.get(position);
                    getPet_id2 = clickItem.getPet_id();
                    new ShowSearchFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/add_match.php?pet_id2=" + getPet_id2);
//                    FragmentManager fm = getFragmentManager();
//                    Bundle data = new Bundle();//create bundle instance
//                    data.putString("pet_id", clickItem.getPet_id());
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ShowProfileFragment ssf = new ShowProfileFragment();
//                    ssf.setArguments(data);
//                    ft.replace(R.id.content_fragment, ssf);
//                    ft.commit();

                }
            });

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
                        .add("pet_id1", getPet_id)
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
//        RowItem_ShowSearch clickItem = rowItems.get(position);
//        FragmentManager fm = getFragmentManager();
//        Bundle data = new Bundle();//create bundle instance
//        data.putString("pet_id", clickItem.getPet_id());
//        FragmentTransaction ft = fm.beginTransaction();
//        ShowProfileFragment ssf = new ShowProfileFragment();
//        ssf.setArguments(data);
//        ft.replace(R.id.content_fragment, ssf);
//        ft.commit();
////        Intent intent = new Intent(SelectAccountActivity.this, HomeActivity.class);
////        intent.putExtra("pet_id", clickItem.getPet_id());
////        startActivity(intent);
//    }

    /*public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, @NonNull int position,
                            long id) {

        String member_name = rowItems.get(position).getPet_kind();
    }*/

}
