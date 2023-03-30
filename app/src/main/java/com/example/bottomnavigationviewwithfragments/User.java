package com.example.bottomnavigationviewwithfragments;

public class User {
    public String username;
    public String userid;
    public String time;
    public String room;
    public String device;
    public String reason;
    public String key;


    public User(String username,String userid,String time,String room,String device,
                String reason,String key) {
        this.username = username;
        this.userid = userid;
        this.time = time;
        this.room = room;
        this.device = device;
        this.reason = reason;
        this.key = key;
    }

    public String getUsername(){
        return username;
    }
    public String getUserid(){
        return userid;
    }
    public String getTime(){
        return time;
    }
    public String getReason(){
        return reason;
    }

    public String getRoom(){
        return room;
    }

    public String getDevice(){
        return device;
    }
    public String getKey(){
        return key;
    }
}
