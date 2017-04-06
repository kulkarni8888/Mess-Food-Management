package com.example.vasuchand.messfood.fcmnotification;

/**
 * Created by Vasu Chand on 4/4/2017.
 */

public class Token {
    String id,token,email;

    public Token()
    {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Token(String id, String token, String email) {

        this.id = id;
        this.token = token;
        this.email = email;

    }
}
