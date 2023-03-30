package com.example.bottomnavigationviewwithfragments;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class post {
    public String id;
    public String name;
    public int starCount=0;
    public Map<String,Boolean> stars = new HashMap<>();
    public post(String id,String name){
        this.id=id;
        this.name=name;
    }
    public post(){

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id",id);
        result.put("name",name);
        result.put("stars",stars);
        return result;
    }
}
