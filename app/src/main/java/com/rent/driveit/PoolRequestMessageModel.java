package com.rent.driveit;

public class PoolRequestMessageModel {
    String Name;
    String ProfileUrl;
    String MessageText;

    public PoolRequestMessageModel(){

    }

    public PoolRequestMessageModel(String name, String profileUrl, String messageText) {
        Name = name;
        ProfileUrl = profileUrl;
        MessageText = messageText;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfileUrl() {
        return ProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        ProfileUrl = profileUrl;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }
}
