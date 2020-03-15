package com.myweb.dogandcatapplication;

public class RowItem_ShowChat {
    public String pet_send_id = "";
    public String pet_send_name = "";
    public String pet_send_profile = "";
    public String message = "";
    public String message_timestamp = "";
    public String message_status = "";
    public String pet_send = "";


    public RowItem_ShowChat(String pet_send_id, String pet_send_name, String pet_send_profile, String message, String message_timestamp, String message_status, String pet_send) {
        this.pet_send_id = pet_send_id;
        this.pet_send_name = pet_send_name;
        this.pet_send_profile = pet_send_profile;
        this.message = message;
        this.message_timestamp = message_timestamp;
        this.message_status = message_status;
        this.pet_send = pet_send;
    }
    public String getPet_send_id() {
        return pet_send_id;
    }
    public String getPet_send_name() {
        return pet_send_name;
    }
    public String getPet_send_profile() {
        return pet_send_profile;
    }
    public String getMessage() {
        return message;
    }
    public String getMessage_timestamp() {
        return message_timestamp;
    }
    public String getMessage_status() {
        return message_status;
    }
    public String getPet_send() {
        return pet_send;
    }
}
