package com.instanitro.instanitro;

public class InstaAccount {
    public String username,password,uid;

    public InstaAccount(String Username, String Password,String Uid) {
        this.username = Username;
        this.password = Password;
        this.uid = Uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public InstaAccount() {
    }
}
