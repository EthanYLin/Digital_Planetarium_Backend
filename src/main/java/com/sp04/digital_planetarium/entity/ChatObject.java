package com.sp04.digital_planetarium.entity;

public class ChatObject {

    public enum Type {
        PRIVATE, ROOM, BROADCAST
    }

    private String fromUserName;
    private Type type;
    private String to;
    private String message;

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatObject() {
    }

    public ChatObject(String fromUserName, Type type, String to, String message) {
        this.fromUserName = fromUserName;
        this.type = type;
        this.to = to;
        this.message = message;
    }

    public boolean isSomeNull(){
        return fromUserName == null || type == null || to == null || message == null;
    }
}
