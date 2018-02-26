package com.example.danilo.chat1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Danilo on 12/6/17.
 */


    public class ChatMessage {

        private String messageText;
        private String messageUser;
        private String messageTime;



    private String userid;


    private String profilepicture;

        public ChatMessage(String messageText, String messageUser, String profilepicture,String userid) {
            this.messageText = messageText;
            this.messageUser = messageUser;
            this.profilepicture = profilepicture;
            this.userid = userid;

            // Initialize to current time
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            messageTime = dateFormat.format(date);
        }

        public ChatMessage(){

        }

        public String getUserid() {
        return userid;
    }

        public void setUserid(String userid) {
        this.userid = userid;
    }

        public String getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }

        public String getMessageUser() {
            return messageUser;
        }

        public void setMessageUser(String messageUser) {
            this.messageUser = messageUser;
        }

        public String getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(String messageTime) {
            this.messageTime = messageTime;
        }
    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

}

