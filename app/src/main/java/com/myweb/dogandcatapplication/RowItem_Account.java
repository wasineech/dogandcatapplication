package com.myweb.dogandcatapplication;

public class RowItem_Account {
    public String pet_id = "";
    public String pet_name = "";
    public String pet_profile = "";
    public String noti_count = "";


    public RowItem_Account(String pet_id, String pet_name, String pet_profile, String noti_count) {
        this.pet_id = pet_id;
        this.pet_name = pet_name;
        this.pet_profile = pet_profile;
        this.noti_count = noti_count;
    }

    public String getPet_id() {
        return pet_id;
    }
    public String getPet_name() {
        return pet_name;
    }
    public String getPet_profile() {
        return pet_profile;
    }
    public String getNoti_count() {
        return noti_count;
    }

}
