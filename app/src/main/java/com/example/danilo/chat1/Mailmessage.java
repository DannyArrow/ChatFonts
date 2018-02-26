package com.example.danilo.chat1;

import android.support.annotation.NonNull;

/**
 * Created by Danilo on 1/2/18.
 */

public class Mailmessage  implements Comparable<Mailmessage> {

   private String userid;
   private String lobbyid;
   private String username;
   private String time;
   private String profilepicture;
   private String message;
   private String usertalking;



    public Mailmessage(String userid, String lobbyid, String username, String time, String profilepicture, String message, String usertalking) {
        this.userid = userid;
        this.lobbyid = lobbyid;
        this.username = username;
        this.time = time;
        this.profilepicture = profilepicture;
        this.message = message;
        this.usertalking = usertalking;
    }
    public String getUsertalking() {
        return usertalking;
    }

    public void setUsertalking(String usertalking) {
        this.usertalking = usertalking;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLobbyid() {
        return lobbyid;
    }

    public void setLobbyid(String lobbyid) {
        this.lobbyid = lobbyid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int compareTo(@NonNull Mailmessage mailmessage) {
        return getTime().compareTo(mailmessage.getTime());
    }
}
