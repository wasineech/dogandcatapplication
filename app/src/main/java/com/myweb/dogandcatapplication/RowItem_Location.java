package com.myweb.dogandcatapplication;

import java.util.List;

public class RowItem_Location {
    public Double lat, lng;
    public String pet_id,pet_name,pet_kind;

    public RowItem_Location(Double lat, Double lng,String pet_id,String pet_name,String pet_kind){
        this.lat = lat;
        this.lng = lng;
        this.pet_id = pet_id;
        this.pet_name = pet_name;
        this.pet_kind = pet_kind;
    }

    public Double getPet_lat() {
        return lat;
    }

    public Double getPet_lng() {
        return lng;
    }

    public String getPet_id() {
        return pet_id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getPet_kind() {
        return pet_kind;
    }

}
