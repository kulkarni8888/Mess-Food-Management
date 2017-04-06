package com.example.vasuchand.messfood;

import android.content.Context;

/**
 * Created by Vasu Chand on 4/4/2017.
 */

public class offlinecredentials {
    Context context;
    String email;
    public offlinecredentials(Context context,String email) {
        this.context = context;
        this.email = email;
    }
    Session session = new Session(context);



}
