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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    String getPet_id;
    ImageView pet_profile1;
    String post_id,post_photo,pet_id;
    List<RowItem_Gallery> rowItems;
    RecyclerView gallery_rv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");

//        Log.d("getPet_id2",getPet_id);

        Toast.makeText(getContext(), getPet_id, Toast.LENGTH_LONG).show();

        gallery_rv = getView().findViewById(R.id.list_gallery);
        gallery_rv.setHasFixedSize(true);
//        gallery_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        gallery_rv.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL));

        rowItems = new ArrayList<>();
        loadPostData();
    }
    private void loadPostData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_gallery.php?pet_id=" + getPet_id;
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

                post_id = collectData.getString("post_id");
                post_photo = "http://"+ConfigIP.IP+"/dogcat/" + collectData.getString("post_photo");
                pet_id = collectData.getString("pet_id");


                RowItem_Gallery item = new RowItem_Gallery(post_id,post_photo,pet_id);
                rowItems.add(item);

            }

            GalleryAdapter adapter = new GalleryAdapter(getActivity(), rowItems);
//            Log.d("act1",getActivity().toString());
            gallery_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener(){
                public void onItemClick(int position) {
                    RowItem_Gallery clickItem = rowItems.get(position);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ShowPostFragment ssf = new ShowPostFragment();
                        Bundle data = new Bundle();//create bundle instance
                        data.putString("post_id", clickItem.getPost_id());
                        ssf.setArguments(data);
                        ft.replace(R.id.relativeGallery, ssf);
                        ft.commit();
                }
            });
            adapter.notifyDataSetChanged();


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
