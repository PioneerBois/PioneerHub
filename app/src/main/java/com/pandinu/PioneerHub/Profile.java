package com.pandinu.PioneerHub;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Profile")
public class Profile extends ParseObject {


    public static final String KEY_USERID = "userId";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_PROFILEIMG = "profileImg";
    public static final String KEY_PROFILEDESC = "profileDesc";
    public static final String KEY_MAJOR = "major";
    public static final String KEY_GRADUATIONMONTH = "graduationMonth";
    public static final String KEY_GRADUATIONYEAR = "graduationYear";





    public ParseUser getUserId(){
        return getParseUser(KEY_USERID);
    }

    public void setUserId(ParseUser userId){
        put(KEY_USERID, userId);
    }

    public String getFirstName(){
        return getString(KEY_FIRSTNAME);
    }

    public void setFirstName(String firstName){
        put(KEY_FIRSTNAME, firstName);
    }

    public String getLastName(){
        return getString(KEY_LASTNAME);
    }

    public void setLastName(String lastName){
        put(KEY_LASTNAME, lastName);
    }

    public ParseFile getProfileImg(){
        return getParseFile(KEY_PROFILEIMG);
    }

    public void setProfileImg(ParseFile parseFile){
        put(KEY_PROFILEIMG, parseFile);
    }

    public String getProfileDesc(){
        return getString(KEY_PROFILEDESC);
    }

    public void setProfileDesc(String profileDesc){
        put(KEY_PROFILEDESC, profileDesc);
    }

    public String getMajor(){
        return getString(KEY_MAJOR);
    }

    public void setMajor(String major){
        put(KEY_MAJOR, major);
    }

    public Number getGraduationMonth(){
        return getNumber(KEY_GRADUATIONMONTH);
    }

    public void setGraduationMonth(Number graduationMonth){
        put(KEY_GRADUATIONMONTH, graduationMonth);
    }

    public Number getGraduationYear(){
        return getNumber(KEY_GRADUATIONYEAR);
    }

    public void setGraduationYear(Number graduationYear){
        put(KEY_GRADUATIONYEAR, graduationYear);
    }









}
