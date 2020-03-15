package com.myweb.dogandcatapplication;

public class RowItem_ShowSearch {
    public String pet_name_gender="";
    public String pet_age_breed="";
    public String pet_profile ="";
    public String pet_id ="";


    public RowItem_ShowSearch(String pet_id, String pet_name_gender, String pet_age_breed, String pet_profile) {
        this.pet_id = pet_id;
        this.pet_name_gender = pet_name_gender;
        this.pet_age_breed = pet_age_breed;
//
        this.pet_profile = pet_profile;
    }


//    public int getPet_profile() {
//        return Pet_profile;
//    }

    public String getPet_id() {
        return pet_id;
    }

    public String getPet_profile() {
        return pet_profile;
    }

    public String getPet_name_gender() {
        return pet_name_gender;
    }

    public String getPet_age_breed() {
        return pet_age_breed;
    }


}
