package com.example.vasuchand.messfood.fcmnotification;

import android.content.Intent;

import com.example.vasuchand.messfood.MainActivity;
import com.example.vasuchand.messfood.Session;
import com.example.vasuchand.messfood.Users;
import com.example.vasuchand.messfood.config;
import com.example.vasuchand.messfood.loginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Vasu Chand on 4/4/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    Session session;
    DatabaseReference databaseReference;
    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        session = new Session(this);
        session.settoken(this, config.token,token);
       // registerToken(token);
    }
    public void registerToken(String token)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(config.tokencolum);

        // couponleft couponleft = new couponleft(email,30, 30, 30, 30);
        String id = session.getPreferences(this,config.userid);
        String email = session.getPreferences(this,config.email);
        Token tkn= new Token(id,token,email);
        //System.out.println(id + " "  +email);
        databaseReference.child(id).setValue(tkn);

    }

}
