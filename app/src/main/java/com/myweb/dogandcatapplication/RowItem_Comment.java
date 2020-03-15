package com.myweb.dogandcatapplication;

public class RowItem_Comment {
    public String post_id = "";
    public String post_comment_content = "";
    public String pet_id = "";
    public String pet_name = "";
    public String pet_profile = "";


    public RowItem_Comment(String post_id, String post_comment_content, String pet_id, String pet_name, String pet_profile) {
        this.post_id = post_id;
        this.post_comment_content = post_comment_content;
        this.pet_id = pet_id;
        this.pet_name = pet_name;
        this.pet_profile = pet_profile;
    }

    public String getPost_id() {
        return post_id;
    }
    public String getPost_comment_content() {
        return post_comment_content;
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

}
