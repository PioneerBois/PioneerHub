package com.pandinu.PioneerHub;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_CREATEDAT="createdAt";
    public static final String KEY_OBJECTID = "objectId";
    public static final String KEY_USERID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DESCRIPTION="description";
    public static final String KEY_LIKESCOUNT = "likesCount";
    public static final String KEY_COMMENTSCOUNT = "commentsCount";
    public static final String KEY_CHANNEL ="channel";
    public static final String KEY_LIKESARRAY = "likesArray";


    public JSONArray getLikesArray(){ return getJSONArray(KEY_LIKESARRAY);}



    public Date getPostCreatedAt(){
        return getCreatedAt();
    }

    public String getPostObjectId(){return getObjectId();}

    public ParseUser getUserId(){
        return getParseUser(KEY_USERID);
    }
    public void setUserId(ParseUser userId){
        put(KEY_USERID, userId);
    }

    public ParseFile getImg(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImg(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){ put(KEY_DESCRIPTION, description); }

    public Number getLikesCount(){
        return getNumber(KEY_LIKESCOUNT);
    }
    public void setLikesCount(Number likesCount){ put(KEY_LIKESCOUNT, likesCount); }

    public Number getCommentsCount(){
        return getNumber(KEY_COMMENTSCOUNT);
    }
    public void setCommentsCount(Number commentsCount){ put(KEY_COMMENTSCOUNT, commentsCount); }

    public String getChannel(){
        return getString(KEY_CHANNEL);
    }
    public void setChannel(String channel){ put(KEY_CHANNEL, channel); }


}
