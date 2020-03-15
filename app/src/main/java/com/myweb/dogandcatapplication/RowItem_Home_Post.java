package com.myweb.dogandcatapplication;

public class RowItem_Home_Post {
    public String post_id = "";
    public String post_content = "";
    public String post_photo = "";
    public String post_time = "";
    public String pet_id = "";
    public String pet_name = "";
    public String pet_profile = "";
    public String count_comment = "";
    public String count_like = "";
    public String like_status = "";


    public RowItem_Home_Post(String post_id, String post_content, String post_photo, String post_time, String pet_id, String pet_name, String pet_profile, String count_comment, String count_like, String like_status) {
        this.post_id = post_id;
        this.post_content = post_content;
        this.post_photo = post_photo;
        this.post_time = post_time;
        this.pet_id = pet_id;
        this.pet_name = pet_name;
        this.pet_profile = pet_profile;
        this.count_comment = count_comment;
        this.count_like = count_like;
        this.like_status = like_status;
    }

    public String getPost_id() {
        return post_id;
    }
    public String getPost_content() {
        return post_content;
    }
    public String getPost_photo() {
        return post_photo;
    }
    public String getPost_time() {
        return post_time;
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
    public String getCount_comment() {
        return count_comment;
    }
    public String getCount_like() {
        return count_like;
    }
    public String getLike_status() {
        return like_status;
    }
}
