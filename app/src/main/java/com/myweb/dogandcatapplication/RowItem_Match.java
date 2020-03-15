package com.myweb.dogandcatapplication;

public class RowItem_Match {
    public String pet_id = "";
    public String pet_name = "";
    public String pet_profile = "";
    public String match_time = "";

    public RowItem_Match(String pet_id, String pet_name, String pet_profile, String match_time) {
        this.pet_id = pet_id;
        this.pet_name = pet_name;
        this.pet_profile = pet_profile;
        this.match_time = match_time;

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
    public String getMatch_time() {
        return match_time;
    }

}
