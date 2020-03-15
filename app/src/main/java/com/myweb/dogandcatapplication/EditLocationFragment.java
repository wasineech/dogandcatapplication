package com.myweb.dogandcatapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class EditLocationFragment extends Fragment {
    private FusedLocationProviderClient client;
    private static String get_user_id,get_pet_id;
    String test1;
    String pet_id,pet_name,pet_kind,lat,lng,name;
    String get_pet_name,get_pet_kind;
    Double ponit_lat,point_lng;
    Double Dlat,Dlng;
    String latitude, longtitude;
    double DoubleLatitude, DoubleLongtitude;
    public static ArrayList<RowItem_Location> row_pet_location = new ArrayList<>();
    ImageView btn_back,btn_refresh;
    String CheckChange = "0";
    SharedPreferences sp_notification;
    SharedPreferences.Editor editor;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_location, container, false);

        requestPermission();
        loadShowPet();

        return rootView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_user_id = getArguments().getString("user_id");
        get_pet_id = getArguments().getString("pet_id");

        btn_back = getView().findViewById(R.id.btn_back_edit_location);
        btn_refresh = getView().findViewById(R.id.btn_refresh);

        requestPermission();
        loadShowPet();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckChange.equals("0")) {
                    FragmentManager fm = getFragmentManager();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id", get_pet_id);
                    data.putString("user_id", get_user_id);
                    FragmentTransaction ft = fm.beginTransaction();
                    ProfileFragment ssf = new ProfileFragment();
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();
                }
                else{
                    new EditLocationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/edit_location.php?lat="+
                            ponit_lat+"&lng="+point_lng);

                    sp_notification = getActivity().getSharedPreferences("LOCATION", Context.MODE_PRIVATE);
                    editor = sp_notification.edit();
                    editor.putString("LOCATION","1");
                    editor.commit();

                    FragmentManager fm = getFragmentManager();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id", get_pet_id);
                    data.putString("user_id", get_user_id);
                    FragmentTransaction ft = fm.beginTransaction();
                    ProfileFragment ssf = new ProfileFragment();
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();
                }

            }

        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                loadShowPet();
            }

        });
    }


        private void requestPermission() {
        android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void loadShowPet(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "http://"+ConfigIP.IP+"/dogcat/get_pet_location.php?user_id=" + get_user_id;
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

            Log.d("get_url","yassssss");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                Log.d("get_url","yaaaaaaaaaaa");

//                pet_id = collectData.getString("pet_id");
//                pet_name = collectData.getString("pet_name");

                lat = collectData.getString("lat");
                lng = collectData.getString("lng");
//                pet_id = collectData.getString("pet_id");
//                pet_name = collectData.getString("pet_name");
//                pet_kind = collectData.getString("pet_kind");

                Dlat = Double.valueOf(lat);
                Dlng = Double.valueOf(lng);
//
//                Log.d("get_url","ybbbbbbbbbbbb");
//
//                Log.d("mapp","pet_id:" + pet_id);
//                Log.d("mapp","pet_name:" + pet_name);
//                Log.d("mapp","name:" + name);
//                Log.d("get_url","name");
//                Log.d("get_url","name:" + name);
                Log.d("get_url",lat);
                Log.d("get_url",lng);
//
//                RowItem_Location item = new RowItem_Location(Dlat,Dlng,pet_id,pet_name,pet_kind);
//                row_pet_location.add(item);






                Log.d("mapp", "SIZE2::" +  String.valueOf(row_pet_location.size()));
                Log.d("get_url", "SI                                                                                                                             ZE" +  String.valueOf(row_pet_location.size()));
            }
            Log.d("mapp", "SIZE3::" +  String.valueOf(row_pet_location.size()));
            loadMap();
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void loadMap() {
//        client = LocationServices.getFusedLocationProviderClient(getActivity());
//
//
//        if (android.support.v4.app.ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        }
//
//        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//
//                    DoubleLatitude = location.getLatitude();
//                    DoubleLongtitude = location.getLongitude();
//                    latitude = String.valueOf(location.getLatitude());
//                    longtitude = String.valueOf(location.getLongitude());
//                    Log.d("mapp", "onSuccess");
//                    Log.d("mapp", "latitude" + latitude);
//                    Log.d("mapp", "longtitude" + longtitude);


                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
                    mapFragment.getMapAsync(new OnMapReadyCallback(){
                        @Override
                        public void onMapReady(final GoogleMap mMap) {



//                Log.d("mapp", latitude);
//                Log.d("mapp", longtitude);
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            mMap.clear(); //clear old markers

                            CameraPosition googlePlex = CameraPosition.builder()
                                    .target(new LatLng(Dlat, Dlng))
                                    .zoom(15)
                                    .bearing(0)
                                    .tilt(25)
                                    .build();

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 100, null);


                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Dlat, Dlng))
                                    .title("It's Me!!"));

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


                                @Override
                                public void onMapClick(LatLng point) {
                                    MarkerOptions marker = new MarkerOptions()
                                            .position(new LatLng(point.latitude, point.longitude))
                                            .title("New Marker");

                                    mMap.clear(); //clear old markers
                                    mMap.addMarker(marker);
                                    ponit_lat = point.latitude;
                                    point_lng = point.longitude;
                                    CheckChange = "1";
//                                    new EditLocationFragment.InsertAsyn().execute("http://"+ConfigIP.IP+"/dogcat/edit_location.php?lat="+
//                                            point.latitude+"&lng="+point.longitude);

                                }
                            });

//

//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(37.4629101,-122.2449094))
//                                    .title("Iron Man")
//                                    .snippet("His Talent : Plenty of money"));
//
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(37.3092293,-122.1136845))
//                                    .title("Captain America"));

//                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                                @Override
//                                public void onInfoWindowClick(Marker marker) {
//                                    String Titlemak = marker.getTitle();
//                                    String Snippetmak = marker.getSnippet();
//                                    FragmentManager fm = getFragmentManager();
//                                    Bundle data = new Bundle();//create bundle instance
//                                    data.putString("pet_id", Snippetmak);
//                                    FragmentTransaction ft = fm.beginTransaction();
//                                    ProfileFragment ssf = new ProfileFragment();
//                                    ssf.setArguments(data);
//                                    ft.replace(R.id.content_fragment, ssf);
//                                    ft.commit();
//
//                                    Log.d("ccmap", "onMarkerClick");
//                                    Log.d("ccmap", Titlemak);
//                                    Log.d("ccmap", Snippetmak);
//                                    startMap();
//                                }
//                            });

//                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                @Override
//                                public boolean onMarkerClick(Marker marker) {
//                                    String Titlemak = marker.getTitle();
//                                    String Snippetmak = marker.getSnippet();
//                                    FragmentManager fm = getFragmentManager();
//                                    Bundle data = new Bundle();//create bundle instance
//                                    data.putString("pet_id", Snippetmak);
//                                    FragmentTransaction ft = fm.beginTransaction();
//                                    ProfileFragment ssf = new ProfileFragment();
//                                    ssf.setArguments(data);
//                                    ft.replace(R.id.content_fragment, ssf);
//                                    ft.commit();
//
//                                    Log.d("ccmap", "onMarkerClick");
//                                    Log.d("ccmap", Titlemak);
//                                    Log.d("ccmap", Snippetmak);
////                                    startMap();
//                                    return true;
//                                }
//
//                            });
                        }
                    });
//                }
                Log.d("mapp", "onSuccessNooo");
//
//            }
//        });
    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("user_id", get_user_id)
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
//                Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
//                intent.putExtra("user_id", get_user_id);
//                startActivity(intent);
            }else {
                Toast.makeText(getContext(), "ไม่สามารถถลบ้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
