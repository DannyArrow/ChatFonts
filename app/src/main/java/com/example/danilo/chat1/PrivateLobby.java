package com.example.danilo.chat1;

/**
 * Created by Danilo on 12/20/17.
 */

public class PrivateLobby {
    String user1 = null;
    String user2 = null;

    public PrivateLobby(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}

