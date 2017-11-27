package com.mjlf.entity;

public class User {
    private String uid;
    private String uName;
    private String uPass;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPass() {
        return uPass;
    }

    public void setuPass(String uPass) {
        this.uPass = uPass;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", uName='" + uName + '\'' +
                ", uPass='" + uPass + '\'' +
                '}';
    }
}
