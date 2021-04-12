package com.pandinu.PioneerHub;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {


    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";



    /*public String getUserName(){
        return getString(KEY_USERNAME);
    }*/

    public void setUserName(String userName){
        put(KEY_USERNAME, userName);
    }

    //Only set password
    public void setPassword(String password){
        put(KEY_PASSWORD, password);
    }

}
