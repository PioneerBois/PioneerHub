package com.pandinu.PioneerHub.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String info;
    private String description;
    private String profileUrl;
    private String coverUrl;

    public User(){
        this.firstName = "";
        this.lastName = "";
        this.info = "";
        this.description = "";
        this.profileUrl = "";
        this.coverUrl = "";
    }

    public User(String fName, String lName, String usrInfo, String usrDesc, String usrProfileUrl, String usrCoverUrl){
        this.firstName = fName;
        this.lastName = lName;
        this.info = usrInfo;
        this.description = usrDesc;
        this.profileUrl = usrProfileUrl;
        this.coverUrl = usrCoverUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getFullName(){
        return (firstName + " " + lastName);
    }
}
