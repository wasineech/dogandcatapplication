package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;


public class ShowMatchFragment extends Fragment {
    String pet_picture,pet_name,match_time,user_id,pet_id;
    String getPet_id,getUser_id;
    List<RowItem_Match> rowItems;
    RecyclerView match_rv;
    ImageView btn_back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_match, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        btn_back = getView().findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Bundle data = new Bundle();//create bundle instance
                data.putString("pet_id", getPet_id);
                data.putString("user_id", getUser_id);
                FragmentTransaction ft = fm.beginTransaction();
                ProfileFragment ssf = new ProfileFragment();
                ssf.setArguments(data);
                ft.replace(R.id.content_fragment, ssf);
                ft.commit();
            }
        });

        match_rv = getView().findViewById(R.id.list_match);
        match_rv.setHasFixedSize(true);
        match_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        match_rv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();
        loadMatchData();
    }

    private void loadMatchData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_show_match.php?pet_id=" + getPet_id;
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
            Log.d("puii3","show");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pet_id = collectData.getString("pet_id");
                pet_name = collectData.getString("pet_name");
                match_time = collectData.getString("match_time");
                pet_picture = "http://"+ConfigIP.IP+"/dogcat/"+collectData.getString("pet_picture");
//                Toast.makeText(SelectAccountActivity.this.getApplicationContext(), pet_picture , Toast.LENGTH_LONG).show();
                RowItem_Match item = new RowItem_Match(pet_id, pet_name, pet_picture, match_time);
                rowItems.add(item);

            }
            ShowMatchAdapter adapter = new ShowMatchAdapter(getActivity(), rowItems);
            match_rv.setAdapter(adapter);
            Log.d("puii3",getActivity().toString());

            adapter.setOnItemClickListener(new ShowMatchAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Match clickItem = rowItems.get(position);
                    FragmentManager fm = getFragmentManager();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id2", clickItem.getPet_id());
//                    data.putString("user_id", getUser_id);
                    FragmentTransaction ft = fm.beginTransaction();
                    ShowProfileFragment ssf = new ShowProfileFragment();
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();

                }
            });


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
