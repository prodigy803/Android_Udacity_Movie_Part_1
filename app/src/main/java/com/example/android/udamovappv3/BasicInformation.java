package com.example.android.udamovappv3;

/**
 * Created by pc on 6/27/2017.
 */

public class BasicInformation {
    private String image_url;
    private String id;

    public void set_ImageUrl(String image_url){
        this.image_url = image_url;
    }
    public String get_ImageUrl(){
        return image_url;
    }
    public void set_ItemId(String id){
        this.id = id;
    }
    public String get_ItemId(){
        return id;
    }
}
