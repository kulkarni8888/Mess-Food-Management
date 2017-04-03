package com.example.vasuchand.messfood;

/**
 * Created by Vasu Chand on 4/3/2017.
 */

public class Users {
    String id,email;

    public Users()
    {

    }
    public Users(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
