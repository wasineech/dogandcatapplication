package com.myweb.dogandcatapplication;

public class RowItem_Med {
    public String med_id = "";
    public String med_name = "";
    public String med_description = "";
    public String med_timestamp = "";
    public String pet_id = "";
    public String user_id = "";


    public RowItem_Med(String med_id, String med_name, String med_description, String med_timestamp, String pet_id, String user_id) {
        this.med_id = med_id;
        this.med_name = med_name;
        this.med_description = med_description;
        this.med_timestamp = med_timestamp;
        this.pet_id = pet_id;
        this.user_id = user_id;
    }

    public String getMed_id() {
        return med_id;
    }
    public String getMed_name() {
        return med_name;
    }
    public String getMed_description() {
        return med_description;
    }
    public String getMed_timestamp() {
        return med_timestamp;
    }
    public String getPet_id() {
        return pet_id;
    }
    public String getUser_id() {
        return user_id;
    }
}
