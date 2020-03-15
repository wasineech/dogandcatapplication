package com.myweb.dogandcatapplication;

import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


public class SearchFragment extends Fragment {
    String pet_kind,pet_gender,pet_age,pet_breed,province,age,url;
    String getPet_id,getUser_id;
    Button btnSearch,btnNoti;
    RadioGroup kindGroup,genderGroup;
    RadioButton kindButton,genderButton;
    TextView txtBreed;
    Spinner spnBreed,spnProvince,spnAge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPet_id = getArguments().getString("pet_id");
        getUser_id = getArguments().getString("user_id");

        spnProvince = (Spinner) getView().findViewById(R.id.province);
        spnAge = (Spinner) getView().findViewById(R.id.age);
        spnBreed = (Spinner) getView().findViewById(R.id.breed);
        txtBreed = getView().findViewById(R.id.txtBreed);
        btnSearch = getView().findViewById(R.id.btnSearch);
        kindGroup = (RadioGroup) getView().findViewById(R.id.pet_kind);
//        btnNoti = view.findViewById(R.id.btnNoti);
//
//        btnNoti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getFragmentManager();
//                Bundle data = new Bundle();//create bundle instance
//                data.putString("pet_id", getPet_id);
//                FragmentTransaction ft = fm.beginTransaction();
//                NotificationFragment ssf = new NotificationFragment();
//                ssf.setArguments(data);
//                ft.replace(R.id.content_fragment, ssf);
//                ft.commit();
//            }
//
//        });

        kindGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb =(RadioButton)getView().findViewById(checkedId);
                String textViewChoice = rb.getText().toString();
                if(textViewChoice.equals("C")){
            final String[] breedStr = getResources().getStringArray(R.array.cat_breed);
            ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_dropdown_item_1line, breedStr);

            spnBreed.setAdapter(adapterBreed);
            spnBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    pet_breed = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        else{
            final String[] breedStr = getResources().getStringArray(R.array.dog_breed);
            ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_dropdown_item_1line, breedStr);

            spnBreed.setAdapter(adapterBreed);
            spnBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    pet_breed = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }
            }
        });
//        int selectedId = kindGroup.getCheckedRadioButtonId();
//        Log.d("selectid1","id1" + selectedId);
//        kindButton = (RadioButton) getView().findViewById(selectedId);
//
//        if(kindButton != null) {
//
//            String st_pet_kind = kindButton.getText().toString();
//            Log.d("st_pet_kind","id1" + st_pet_kind);
//        }
//        if(st_pet_kind.equals("C")){
//            final String[] breedStr = getResources().getStringArray(R.array.cat_breed);
//            ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, breedStr);
//
//            spnBreed.setAdapter(adapterBreed);
//            spnBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                    pet_breed = parent.getItemAtPosition(position).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//        }
//        else{
//            final String[] breedStr = getResources().getStringArray(R.array.dog_breed);
//            ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, breedStr);
//
//            spnBreed.setAdapter(adapterBreed);
//            spnBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                    pet_breed = parent.getItemAtPosition(position).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//
//        }

        final String[] pvStr = getResources().getStringArray(R.array.province);
        ArrayAdapter<String> adapterDeptProvince = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, pvStr);

        spnProvince.setAdapter(adapterDeptProvince);
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                province = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final String[] ageStr = getResources().getStringArray(R.array.age);
        ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, ageStr);

        spnAge.setAdapter(adapterAge);
        spnAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindGroup = (RadioGroup) getView().findViewById(R.id.pet_kind);
                if (kindGroup.getCheckedRadioButtonId() == -1)
                {
                    pet_kind = "ว่าง";
                }
                else {
                    int selectedId = kindGroup.getCheckedRadioButtonId();
                    Log.d("selectid", "id" + selectedId);
                    kindButton = (RadioButton) getView().findViewById(selectedId);


                    String s_pet_kind = kindButton.getText().toString();
                    if (s_pet_kind.equals("C")) {
                        pet_kind = "แมว";
                    } else {
                        pet_kind = "สุนัข";
                    }
                }

                genderGroup = (RadioGroup) getView().findViewById(R.id.gender);

                if (genderGroup.getCheckedRadioButtonId() == -1)
                {
                    pet_gender = "ว่าง";
                }
                else
                {
                    int selectedId2 = genderGroup.getCheckedRadioButtonId();
                    genderButton = (RadioButton) getView().findViewById(selectedId2);

//                    Log.d("pet2","selectedId2"+String.valueOf(selectedId2));

                    String s_pet_gender = genderButton.getText().toString();

                    if (s_pet_gender.equals("เพศผู้")) {
                        pet_gender = "ผู้";
                    } else {
                        pet_gender = "เมีย";
                    }
                }




//                pet_breed = txtBreed.getText().toString();

                if (pet_kind.equals("ว่าง") || pet_gender.equals("ว่าง")|| province.equals("เลือกจังหวัด")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
                    builder.setCancelable(true);
                    Log.d("petname",pet_kind+pet_gender+pet_breed+province);
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
//                    Toast.makeText(getContext(), pet_kind + pet_kind + pet_gender + pet_breed + province,Toast.LENGTH_SHORT).show();
                    //search(pet_kind,pet_gender,pet_breed,province);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ShowSearchFragment ssf = new ShowSearchFragment();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("pet_id", getPet_id);
                    data.putString("user_id", getUser_id);
                    data.putString("pet_kind", pet_kind);
                    data.putString("pet_gender", pet_gender);
                    data.putString("pet_breed", pet_breed);
                    data.putString("province", province);
                    data.putString("age", age);
                    ssf.setArguments(data);
                    ft.replace(R.id.content_fragment, ssf);
                    ft.commit();

                    Log.d("pet2",pet_breed);
                    Log.d("pet2",age);
                    Log.d("pet2",pet_gender);

                }

            }
        });

//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ShowSearchFragment ssf = new ShowSearchFragment();
//                    ft.replace(R.id.content_fragment, ssf);
//                    ft.commit();

    }
}
