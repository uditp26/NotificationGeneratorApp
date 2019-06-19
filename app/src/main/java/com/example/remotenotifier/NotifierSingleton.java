package com.example.remotenotifier;

public class NotifierSingleton {

    private static NotifierSingleton object = null;

    protected NotifierSingleton(){}

    public String userToken;

    public static synchronized NotifierSingleton getObject() {
        if(null == object){
            object = new NotifierSingleton();
        }
        return object;
    }
}
