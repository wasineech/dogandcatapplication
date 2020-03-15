package com.myweb.dogandcatapplication;

public class RowItem_Notification {
    public String pet_id ="";
    public String pet_name="";
    public String pet_gender="";
    public String pet_age_breed="";
    public String pet_profile ="";
    public String noti_message ="";
    public String noti_time ="";
    public String noti_isread ="";
    public String match_id ="";
    public String match_status ="";
    public String post_id ="";
    public String noti_id ="";


    public RowItem_Notification(String pet_id, String pet_name, String pet_gender, String pet_age_breed, String pet_profile, String noti_message, String noti_time, String noti_isread, String match_id, String match_status, String post_id, String noti_id) {
        this.pet_id = pet_id;
        this.pet_name = pet_name;
        this.pet_gender = pet_gender;
        this.pet_age_breed = pet_age_breed;
        this.pet_profile = pet_profile;
        this.noti_message = noti_message;
        this.noti_time = noti_time;
        this.noti_isread = noti_isread;
        this.match_id = match_id;
        this.match_status = match_status;
        this.post_id = post_id;
        this.noti_id = noti_id;
    }

    public String getPet_id() {
        return pet_id;
    }

    public String getPet_profile() {
        return pet_profile;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getPet_gender() {
        return pet_gender;
    }

    public String getPet_age_breed() {
        return pet_age_breed;
    }

    public String getPet_noti_message() {
        return noti_message;
    }

    public String getPet_noti_time() {
        return noti_time;
    }

    public String getPet_noti_isread() {
        return noti_isread;
    }
    public String getPet_match_id() {
        return match_id;
    }
    public String getPet_match_status() {
        return match_status;
    }
    public String getPet_post_id() {
        return post_id;
    }
    public String getNoti_id() {
        return noti_id;
    }


}
