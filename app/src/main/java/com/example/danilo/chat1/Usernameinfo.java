package com.example.danilo.chat1;

/**
 * Created by Danilo on 12/8/17.
 */



public class Usernameinfo {
    private  String username;
    private String profilepicture;

    public Usernameinfo() {
    }

    public Usernameinfo(String username, String profilepicture) {
        this.username = username;
        this.profilepicture = profilepicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepicture() {

        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }
}
