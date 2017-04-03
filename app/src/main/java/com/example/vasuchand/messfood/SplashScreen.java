package com.example.vasuchand.messfood;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private Context context = SplashScreen.this;
    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(context);
        //firebaseAuth.getInstance().signOut();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if(firebaseAuth.getCurrentUser()!=null)
                {
                    finish();
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    //overridePendingTransition(R.anim.animate_left_to_right, R.anim.animate_right_to_left);
                    //overridePendingTransition( R.anim.animate_right_to_left,R.anim.animate_left_to_right);


                }
                else
                {
                    if(firebaseAuth.getCurrentUser()==null)
                    {
                        finish();
                        startActivity(new Intent(SplashScreen.this,Signin.class));



                    }
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
