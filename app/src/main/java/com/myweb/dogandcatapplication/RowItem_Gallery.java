package com.myweb.dogandcatapplication;

public class RowItem_Gallery {
    public String post_id = "";
    public String post_content = "";
    public String post_photo = "";
    public String post_time = "";
    public String pet_id = "";
    public String pet_name = "";
    public String pet_profile = "";


    public RowItem_Gallery(String post_id, String post_photo, String pet_id) {
        this.post_id = post_id;
        this.post_photo = post_photo;
        this.pet_id = pet_id;
    }

    public String getPost_id() {
        return post_id;
    }
    public String getPost_photo() {
        return post_photo;
    }
    public String getPet_id() {
        return pet_id;
    }
}
