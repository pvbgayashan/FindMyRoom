package com.fmr.findmyroom;

public class ChatModel {

    private String chatMessage;
    private boolean isSend;

    public ChatModel(String chatMessage, boolean isSend) {
        this.chatMessage = chatMessage;
        this.isSend = isSend;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public boolean isSend() {
        return isSend;
    }
}
