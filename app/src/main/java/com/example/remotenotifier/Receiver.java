package com.example.remotenotifier;

public class Receiver {

    public String name;
    public String email;
    public String token;
    public int optionNo;
    public String responseVal;
    public String uploadDate;
    public String uploadTime;

    public Receiver(){

    }

    public Receiver(String name, String email){
        this.name = name;
        this.email = email;
    }

}
