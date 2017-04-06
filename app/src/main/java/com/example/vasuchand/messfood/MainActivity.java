package com.example.vasuchand.messfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    LinearLayout l1, l2, l3, l4,l11,l22,l33,l44;
    private int mHour,mMinute;
    private int year, month, mDay;
    private String dayOfTheWeek;
    TextView h1, h2, h3, h4;
    TextView v1,v2,v3,v4,t1,t2,t3,t4,t11,t22,t33,t44;
    Session session;
    Context context = MainActivity.this;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        imageview = (ImageView)findViewById(R.id.user);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("updating...");
        l1 = (LinearLayout) findViewById(R.id.c1l1);
        l2 = (LinearLayout) findViewById(R.id.c2l1);
        l3 = (LinearLayout) findViewById(R.id.c3l1);
        l4 = (LinearLayout) findViewById(R.id.c4l1);

        l11 = (LinearLayout) findViewById(R.id.c1l2);
        l22 = (LinearLayout) findViewById(R.id.c2l2);
        l33 = (LinearLayout) findViewById(R.id.c3l2);
        l44 = (LinearLayout) findViewById(R.id.c4l2);

        h1 = (TextView) findViewById(R.id.c1t1);
        h2 = (TextView) findViewById(R.id.c2t1);
        h3 = (TextView) findViewById(R.id.c3t1);
        h4 = (TextView) findViewById(R.id.c4t1);

        v1 = (TextView) findViewById(R.id.c1v1);
        v2 = (TextView) findViewById(R.id.c2v1);
        v3 = (TextView) findViewById(R.id.c3v1);
        v4 = (TextView) findViewById(R.id.c4v1);

        t1 = (TextView)findViewById(R.id.t1);
        t2 =(TextView)findViewById(R.id.t2);
        t3 = (TextView)findViewById(R.id.t3);
        t4 = (TextView)findViewById(R.id.t4);

        t11 = (TextView)findViewById(R.id.t11);
        t22 = (TextView)findViewById(R.id.t22);
        t33 = (TextView)findViewById(R.id.t33);
        t44 = (TextView)findViewById(R.id.t44);

        Calendar cc = Calendar.getInstance();
        year = cc.get(Calendar.YEAR);
        month = cc.get(Calendar.MONTH) + 1;
        mDay = cc.get(Calendar.DAY_OF_MONTH);

        session = new Session(MainActivity.this);
        System.out.println("Date" + year + ":" + month + ":" + mDay + " ad  " + dayOfTheWeek);
        mHour = cc.get(Calendar.HOUR_OF_DAY);
        mMinute = cc.get(Calendar.MINUTE);
        System.out.println("time_format" + String.format("%02d:%02d", mHour, mMinute));

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();


        //System.out.println("vasuuuuu "+ date + "... ... " +getdate());
        loadmenu();
        System.out.println("************************* breakfast **********");
        System.out.println(session.isFirstTimeLaunch(context,config.btrue));
        System.out.println(session.getPreferences(context,config.bday));
        System.out.println(session.getPreferences(context,config.bdate));
        System.out.println(session.getPreferences(context,config.bacc));

        System.out.println("************************* lunch **********");
        System.out.println(session.isFirstTimeLaunch(context,config.ltrue));
        System.out.println(session.getPreferences(context,config.lday));
        System.out.println(session.getPreferences(context,config.ldate));
        System.out.println(session.getPreferences(context,config.lacc));

        System.out.println("************************* Snack **********");
        System.out.println(session.isFirstTimeLaunch(context,config.strue));
        System.out.println(session.getPreferences(context,config.sday));
        System.out.println(session.getPreferences(context,config.sdate));
        System.out.println(session.getPreferences(context,config.sacc));

        System.out.println("************************* Dinner **********");
        System.out.println(session.isFirstTimeLaunch(context,config.dtrue));
        System.out.println(session.getPreferences(context,config.dday));
        System.out.println(session.getPreferences(context,config.ddate));
        System.out.println(session.getPreferences(context,config.dacc));

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Profile.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        /*
         Token
         */



    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
    }


    public void loadmenu() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        //case 1 morning , need count before 6 a.m.
        if (mHour < 9 || mHour >= 21 ) {
            //breakfast - > lunch - > snack - > dinner all same day
            //1.
            String tommorow="";
            String coupon_date="";
            h1.setText(config.breakfast);
            if(mHour >= 21 && mHour<24) {
                 tommorow = getDayOfWeek();
                 coupon_date = getTommorowdate();
            }else
            {
                 tommorow = dayOfTheWeek;
                 coupon_date = getdate();
            }

            //System.out.println("vasu"+tommorow + " date "+ coupon_date);
            v1.setText( tommorow +" " + "6:00 A.M.");
            t1.setText(coupon_date);
            t11.setText(tommorow);
            List menu = getMenu(tommorow, "breakfast");

            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l1.addView(textView);
            }
            /* Accept reject Show coupon */
            int flag=0;

            if(session.isFirstTimeLaunch(context,config.btrue) )
            {
                  String bday = session.getPreferences(context,config.bday);// breakfast day
                  String bdate = session.getPreferences(context,config.bdate); // breakfast date
                  String bacc = session.getPreferences(context,config.bacc); // breakfast accepted or nt


                  if(bday.equals(tommorow) && coupon_date.equals(bdate) )
                  {
                    // coupon accepted or rejected

                      flag =1;

                      setlayout_accept_reject(bacc,l11,config.breakfast,1);

                  }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.breakfast,l11,1);
            }



            //2.
            h2.setText(config.lunch);
            v2.setText(tommorow +" " + "11.00 A.M.");
            menu = getMenu(tommorow, "lunch");
            t2.setText(coupon_date);
            t22.setText(tommorow);
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l2.addView(textView);
            }
            /*
              Lunch
             */
             flag=0;


            if(session.isFirstTimeLaunch(context,config.ltrue) )
            {
                String lday = session.getPreferences(context,config.lday);// breakfast day
                String ldate = session.getPreferences(context,config.ldate); // breakfast date
                String lacc = session.getPreferences(context,config.lacc); // breakfast accepted or nt

                if(lday.equals(tommorow) && coupon_date.equals(ldate) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(lacc,l22,config.lunch,2);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.lunch,l22,2);
            }

            /*
               Snack
             */

            h3.setText(config.snack);
            v3.setText(tommorow +" " + "4:00 P.M.");
            t3.setText(coupon_date);
            t33.setText(tommorow);
            menu = getMenu(tommorow, "snack");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l3.addView(textView);
            }

             flag=0;



            if(session.isFirstTimeLaunch(context,config.snack) )
            {
                String sday = session.getPreferences(context,config.sday);// breakfast day
                String sdate = session.getPreferences(context,config.sdate); // breakfast date
                String sacc = session.getPreferences(context,config.sacc); // breakfast accepted or nt

                if(sday.equals(tommorow) && coupon_date.equals(sdate) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(sacc,l33,config.snack,3);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.snack,l33,3);
            }


            h4.setText(config.dinner);
            v4.setText(tommorow +" " + "7.00 P.M.");
            t4.setText(coupon_date);
            t44.setText(tommorow);
            menu = getMenu(tommorow, "dinner");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l4.addView(textView);
            }

             flag=0;


            if(session.isFirstTimeLaunch(context,config.dtrue) )
            {
                String dday = session.getPreferences(context,config.dday);// breakfast day
                String ddate = session.getPreferences(context,config.ddate); // breakfast date
                String dacc = session.getPreferences(context,config.dacc); // breakfast accepted or nt

                if(dday.equals(tommorow) && coupon_date.equals(ddate) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(dacc,l44,config.dinner,4);


                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.dinner,l44,4);
            }

        }
        //case2 start
        else if (mHour > 9 && mHour < 14) {

            h1.setText(config.lunch);
            v1.setText(dayOfTheWeek +" " + "11.00 A.M.");
            String tomorrow = getDayOfWeek();
            String coupon_date =getdate();
            t1.setText(coupon_date);
            t11.setText(tomorrow);
            System.out.println("case 2 Tomorrows date is " + tomorrow);

            List menu = getMenu(dayOfTheWeek, "lunch");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l1.addView(textView);
            }

            int flag=0;
            if(session.isFirstTimeLaunch(context,config.ltrue) )
            {
                String lday = session.getPreferences(context,config.lday);// breakfast day
                String ldate = session.getPreferences(context,config.ldate); // breakfast date
                String lacc = session.getPreferences(context,config.lacc); // breakfast accepted or nt

                if(lday.equals(dayOfTheWeek) && coupon_date.equals(ldate) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(lacc,l11,config.lunch,1);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.lunch,l11,1);
            }

            /*
               Snack case 2

             */


            h2.setText(config.snack);
            v2.setText(dayOfTheWeek +" " + "4.00 P.M.");
            menu = getMenu(dayOfTheWeek, "snack");
            t22.setText(dayOfTheWeek);
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l2.addView(textView);
            }
            t2.setText(coupon_date);

            flag=0;
            if(session.isFirstTimeLaunch(context,config.strue) )
            {
                String sday = session.getPreferences(context,config.sday);// breakfast day
                String sdate = session.getPreferences(context,config.sdate); // breakfast date
                String sacc = session.getPreferences(context,config.sacc); // breakfast accepted or nt

                if(sday.equals(dayOfTheWeek) && sdate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(sacc,l22,config.snack,2);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.snack,l22,2);
            }

            /*
               Dinner Case 2
             */

            h3.setText(config.dinner);
            v3.setText(dayOfTheWeek +" " + "7.00 P.M.");
            t33.setText(dayOfTheWeek);
            menu = getMenu(dayOfTheWeek, "dinner");
            t3.setText(coupon_date);
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l3.addView(textView);
            }

            flag=0;
            if(session.isFirstTimeLaunch(context,config.dtrue) )
            {
                String dday = session.getPreferences(context,config.dday);// breakfast day
                String ddate = session.getPreferences(context,config.ddate); // breakfast date
                String dacc = session.getPreferences(context,config.dacc); // breakfast accepted or nt

                if(dday.equals(dayOfTheWeek) && ddate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(dacc,l33,config.dinner,3);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.dinner,l33,3);
            }

            /*
              BreakFast case 2
             */
            h4.setText(config.breakfast);
            v4.setText(tomorrow +" " + "6.00 A.M.");
            t44.setText(tomorrow);
            menu = getMenu(tomorrow, "breakfast");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l4.addView(textView);
            }

            flag=0;
            coupon_date = getTommorowdate();
            t4.setText(coupon_date);
            if(session.isFirstTimeLaunch(context,config.btrue) )
            {
                String bday = session.getPreferences(context,config.bday);// breakfast day
                String bdate = session.getPreferences(context,config.bdate); // breakfast date
                String bacc = session.getPreferences(context,config.bacc); // breakfast accepted or nt

                if(bday.equals(tomorrow) && bdate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(bacc,l44,config.breakfast,4);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.breakfast,l44,4);
            }


        }
        //case3 snack
        else if(mHour>=14 && mHour<17)
        {
            h1.setText(config.snack);
            String tomorrow = getDayOfWeek();
            String coupon_date = getdate();
            System.out.println("case 3 is working ");
            v1.setText(dayOfTheWeek +" " + "4.00 P.M.");
            t1.setText(coupon_date);
            t11.setText(dayOfTheWeek);
            List menu = getMenu(dayOfTheWeek, "snack");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l1.addView(textView);
            }

            int flag=0;

            if(session.isFirstTimeLaunch(context,config.strue) )
            {
                String sday = session.getPreferences(context,config.sday);// snack day
                String sdate = session.getPreferences(context,config.sdate); // Snack date
                String sacc = session.getPreferences(context,config.sacc); // Snack accepted or nt

                System.out.println( "check ing"  +sday + " " +dayOfTheWeek + " " +sdate + " " +coupon_date );
                if(sday.equals(dayOfTheWeek) && sdate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(sacc,l11,config.snack,1);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.snack,l11,1);
            }

            /*
            case 3 dinner same day
             */
            h2.setText(config.dinner);
            v2.setText(dayOfTheWeek +" " + "7.00 P.M.");
            menu = getMenu(dayOfTheWeek, "dinner");
            t2.setText(coupon_date);
            t22.setText(dayOfTheWeek);
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l2.addView(textView);
            }
            flag=0;
            if(session.isFirstTimeLaunch(context,config.dtrue) )
            {
                String dday = session.getPreferences(context,config.dday);// breakfast day
                String ddate = session.getPreferences(context,config.ddate); // breakfast date
                String dacc = session.getPreferences(context,config.dacc); // breakfast accepted or nt

                if(dday.equals(dayOfTheWeek) && ddate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(dacc,l22,config.dinner,2);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.dinner,l22,2);
            }

           /*
               BreakFast day next ..
             */

            coupon_date = getTommorowdate();
            h3.setText(config.breakfast);
            v3.setText(tomorrow+" " + "7.00 A.M.");
            menu = getMenu(tomorrow, "breakfast");
            t3.setText(coupon_date);
            t33.setText(tomorrow);
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l3.addView(textView);
            }

            flag=0;

            if(session.isFirstTimeLaunch(context,config.btrue) )
            {
                String bday = session.getPreferences(context,config.bday);// breakfast day
                String bdate = session.getPreferences(context,config.bdate); // breakfast date
                String bacc = session.getPreferences(context,config.bacc); // breakfast accepted or nt

                if(bday.equals(tomorrow) && bdate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(bacc,l33,config.breakfast,3);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.breakfast,l33,3);
            }

            /*
               Case4 lunch next day
             */

            h4.setText(config.lunch);
            v4.setText(tomorrow +" " + "2.00 P.M.");
            t4.setText(coupon_date);
            t44.setText(tomorrow);
            menu = getMenu(tomorrow, "lunch");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l4.addView(textView);
            }

            flag=0;
            if(session.isFirstTimeLaunch(context,config.ltrue) )
            {
                String lday = session.getPreferences(context,config.lday);// breakfast day
                String ldate = session.getPreferences(context,config.ldate); // breakfast date
                String lacc = session.getPreferences(context,config.lacc); // breakfast accepted or nt

                if(lday.equals(tomorrow) && ldate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(lacc,l44,config.lunch,4);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.lunch,l44,4);
            }

        }
        else if(mHour>=17 && mHour<21){
            //Dinner
            h1.setText(config.dinner);
            v1.setText(dayOfTheWeek +" " + "7.00 P.M.");
            String tomorrow = dayOfTheWeek;
            String coupon_date = getdate();
            List menu = getMenu(dayOfTheWeek, "dinner");
            t1.setText(coupon_date);
            t11.setText(dayOfTheWeek);
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l1.addView(textView);
            }

            /*
              Dinner same day
             */
            int flag=0;


            //System.out.println("Dinnner" + " " + session.isFirstTimeLaunch(context,config.dtrue)+" "+tomorrow + " "+ coupon_date);
            if(session.isFirstTimeLaunch(context,config.dtrue) )
            {
                String dday = session.getPreferences(context,config.dday);// breakfast day
                String ddate = session.getPreferences(context,config.ddate); // breakfast date
                String dacc = session.getPreferences(context,config.dacc); // breakfast accepted or nt

                System.out.println("Dinnner" + dday + " " +ddate + " " +dacc + " " +tomorrow + " "+ coupon_date);
                if(dday.equals(tomorrow) && ddate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(dacc,l11,config.dinner,1);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.dinner,l11,1);
            }

            /*
               breakfast next day
             */
            tomorrow = getDayOfWeek();
            coupon_date = getTommorowdate();

            h2.setText(config.breakfast);
            v2.setText(tomorrow +" " + "6.00 A.M.");
            t2.setText(coupon_date);
            t22.setText(tomorrow);
            menu = getMenu(tomorrow, "breakfast");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l2.addView(textView);
            }
            flag=0;
            if(session.isFirstTimeLaunch(context,config.btrue) )
            {
                String bday = session.getPreferences(context,config.bday);// breakfast day
                String bdate = session.getPreferences(context,config.bdate); // breakfast date
                String bacc = session.getPreferences(context,config.bacc); // breakfast accepted or nt

                if(bday.equals(tomorrow) && bdate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(bacc,l22,config.breakfast,2);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.breakfast,l22,2);
            }

            /*
              Lunch next day
             */

            h3.setText(config.lunch);
            v3.setText(tomorrow +" " + "2.00 P.M.");
            t3.setText(coupon_date);
            t33.setText(tomorrow);
            menu = getMenu(tomorrow, "lunch");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l3.addView(textView);
            }
            flag=0;
            if(session.isFirstTimeLaunch(context,config.ltrue) )
            {
                String lday = session.getPreferences(context,config.lday);// breakfast day
                String ldate = session.getPreferences(context,config.ldate); // breakfast date
                String lacc = session.getPreferences(context,config.lacc); // breakfast accepted or nt

                if(lday.equals(tomorrow) && ldate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(lacc,l33,config.lunch,3);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.lunch,l33,3);
            }

            /*
             Snack next day
             */
            h4.setText(config.snack);
            v4.setText(tomorrow +" " + "4.00 P.M.");
            t4.setText(coupon_date);
            t44.setText(tomorrow);
            menu = getMenu(tomorrow, "snack");
            for (int i = 0; i < menu.size(); i++) {
                String s = (String) menu.get(i);
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                textView.setText(s);
                l4.addView(textView);
            }

            flag=0;
            if(session.isFirstTimeLaunch(context,config.strue) )
            {
                String sday = session.getPreferences(context,config.sday);// breakfast day
                String sdate = session.getPreferences(context,config.sdate); // breakfast date
                String sacc = session.getPreferences(context,config.sacc); // breakfast accepted or nt

                if(sday.equals(tomorrow) && sdate.equals(coupon_date) )
                {
                    // coupon accepted or rejected

                    flag =1;
                    setlayout_accept_reject(sacc,l44,config.snack,4);

                }
            }
            if(flag==0)//not pressed the button
            {
                setlayout_not_selected(config.snack,l44,4);
            }

        }


    }


    /*******
     *     Layout accept reject **********START**********************
     *****************************************************************
     *****************************************************************
     *****************************************************************
     */

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public  void setlayout_not_selected(final String meal, final LinearLayout l,int id)
    {
        int flags = 0;
        final Button b = new Button(context);

        if(meal.equals(config.breakfast) && breakfastdeadline())
        {
            flags =1;
        }
        else if(meal.equals(config.lunch) && lunchdeadline())
        {
            flags=1;
        }
        else if(meal.equals(config.snack) && snackdeadline())
        {
            flags = 1;
        }
        else if(meal.equals(config.dinner) && dinnerdeadline())
        {
            flags =1;
        }

        if(flags==0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 1f);

            b.setTextSize(28);
            b.setAllCaps(false);
            b.setBackgroundResource(R.drawable.button);
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.show_coupon));
            b.setText("Accept");
            b.setId(id);
            b.setLayoutParams(params);

            final Button b1 = new Button(context);

            b1.setTextSize(28);
            b1.setAllCaps(false);
            b1.setBackgroundResource(R.drawable.button);
            b1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.show_coupon));
            b1.setText("Reject");
            b1.setId(id);
            b1.setLayoutParams(params);

            l.addView(b);
            l.addView(b1);
            //accept the proposal
            if(isNetworkAvailable())
            {
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        Toast.makeText(getApplicationContext(), "You have Accepted the " + meal + " meal !", Toast.LENGTH_SHORT).show();
                        l.removeView(b);
                        l.removeView(b1);
                        int ids = arg0.getId();


                        System.out.println("ajda " + arg0.getId());
                        String date = "";
                        String day = "";
                        if (ids == 1) {

                            date = t1.getText().toString();
                            day = t11.getText().toString();
                        } else if (ids == 2) {
                            date = t2.getText().toString();
                            day = t22.getText().toString();
                        } else if (ids == 3) {
                            date = t3.getText().toString();
                            day = t33.getText().toString();
                        } else {
                            date = t4.getText().toString();
                            day = t44.getText().toString();
                        }
                        System.out.println("************" + ids + "" + meal + " " + config.breakfast);


                        if (meal.equals(config.breakfast)) {
                            if (mHour >= config.breakfasttime && mHour < 9) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();

                            } else {
                                session.setBreakFastT(context, config.btrue, true);
                                session.setBreakFast(context, config.bday, day);
                                session.setdateBreakFast(context, config.bdate, date);
                                session.setaccBreakFast(context, config.bacc, "Accepted");
                                setlayout_accept_reject("Accepted", l, meal, ids);

                            }
                        }
                        if (meal.equals(config.lunch)) {
                            if (mHour >= config.lunchtime && mHour < 14) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();

                            } else {
                                session.setLunchT(context, config.ltrue, true);

                                System.out.println("accepted  brekfast" + date + " " + day);
                                session.setLunch(context, config.lday, day);
                                session.setdateLunch(context, config.ldate, date);
                                session.setaccLunch(context, config.lacc, "Accepted");
                                setlayout_accept_reject("Accepted", l, meal, ids);
                            }

                            //  System.out.println(session.getPreferences(context,config.lday) +" " +session.getPreferences(context,config.ldate));
                        }
                        if (meal.equals(config.snack)) {
                            if (mHour >= config.snacktime && mHour < 17) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();
                            } else {
                                session.setSnackT(context, config.strue, true);
                                System.out.println("accepted  brekfast" + date + " " + day);
                                session.setSnack(context, config.sday, day);
                                session.setdateSnack(context, config.sdate, date);
                                session.setaccSnack(context, config.sacc, "Accepted");
                                setlayout_accept_reject("Accepted", l, meal, ids);
                            }

                        }
                        if (meal.equals(config.dinner)) {
                            if (mHour >= config.dinnertime && mHour < 21) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();
                            } else {

                                session.setDinnerT(context, config.dtrue, true);
                                session.setDinner(context, config.dday, day);
                                session.setdateDinner(context, config.ddate, date);
                                session.setaccDinner(context, config.dacc, "Accepted");
                                setlayout_accept_reject("Accepted", l, meal, ids);
                            }

                        }
                        countpeoples(meal, date);


                    }
                });
                // reject
                b1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "You have Rejected the " + meal + " meal !", Toast.LENGTH_SHORT).show();
                        l.removeView(b);
                        l.removeView(b1);

                        String date = "";
                        String day = "";
                        int id = arg0.getId();
                        if (id == 1) {

                            date = t1.getText().toString();
                            day = t11.getText().toString();
                        } else if (id == 2) {
                            date = t2.getText().toString();
                            day = t22.getText().toString();
                        } else if (id == 3) {
                            date = t3.getText().toString();
                            day = t33.getText().toString();
                        } else {
                            date = t4.getText().toString();
                            day = t44.getText().toString();
                        }
                        System.out.println("Reject today" + date + " " + day);

                        if (meal.equals(config.breakfast)) {
                            if (mHour >= config.breakfasttime && mHour < 9) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();

                            } else {
                                session.setBreakFastT(context, config.btrue, false);
                                session.setBreakFast(context, config.bday, day);
                                session.setdateBreakFast(context, config.bdate, date);
                                session.setaccBreakFast(context, config.bacc, "Rejected");
                                setlayout_accept_reject("Rejected", l, meal, id);

                            }
                        }
                        if (meal.equals(config.lunch)) {
                            if (mHour >= config.lunchtime && mHour < 14) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();

                            } else {
                                session.setLunchT(context, config.ltrue, false);
                                session.setLunch(context, config.lday, day);
                                session.setdateLunch(context, config.ldate, date);
                                session.setaccLunch(context, config.lacc, "Rejected");
                                setlayout_accept_reject("Rejected", l, meal, id);
                            }

                            //  System.out.println(session.getPreferences(context,config.lday) +" " +session.getPreferences(context,config.ldate));
                        }
                        if (meal.equals(config.snack)) {
                            if (mHour >= config.snacktime && mHour < 17) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();
                            } else {
                                session.setSnackT(context, config.strue, false);
                                session.setSnack(context, config.sday, day);
                                session.setdateSnack(context, config.sdate, date);
                                session.setaccSnack(context, config.sacc, "Rejected");
                                setlayout_accept_reject("Rejected", l, meal, id);
                            }

                        }
                        if (meal.equals(config.dinner)) {
                            if (mHour >= config.dinnertime && mHour < 21) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();
                            } else {

                                session.setDinnerT(context, config.dtrue, true);
                                session.setDinner(context, config.dday, day);
                                session.setdateDinner(context, config.ddate, date);
                                session.setaccDinner(context, config.dacc, "Rejected");
                                setlayout_accept_reject("Rejected", l, meal, id);
                            }

                        }


                        //setlayout_accept_reject("Rejected",l,meal,id);

                    }
                });
            }
            else
            {
                Toast.makeText(context, "Check Your Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }//
        /*
          case when deadline is passed and user do not select any option
         */
        else
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 1f);

            b.setTextSize(28);
            b.setAllCaps(false);
            b.setBackgroundResource(R.drawable.button);
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.show_coupon));
            b.setText("Deadline Passed !");
            b.setId(id);
            b.setLayoutParams(params);
            l.addView(b);
        }
    }
     /*
        Redo of the options if deadline allows
      */
    public  void setlayout_accept_reject(final String a, final LinearLayout l, final String meal, final int id)
    {
        int flags = 0;

        if(meal.equals(config.breakfast) && breakfastdeadline())
        {
            flags =1;
        }
        else if(meal.equals(config.lunch) && lunchdeadline())
        {
            flags=1;
        }
        else if(meal.equals(config.snack) && snackdeadline())
        {
            flags = 1;
        }
        else if(meal.equals(config.dinner) && dinnerdeadline())
        {
            flags =1;
        }



        final String s = a;
        if(flags==0) {
            final Button b = new Button(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 1f);

            params.gravity = Gravity.CENTER;

            b.setTextSize(28);
            b.setAllCaps(false);
            b.setBackgroundResource(R.drawable.button);
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.show_coupon));
            b.setText(s);
            b.setId(id);
            b.setLayoutParams(params);

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.undo);
            imageView.setLayoutParams(params);

            imageView.setId(id);
            l.addView(b);
            l.addView(imageView);
            if(isNetworkAvailable()) {
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {


                        String date = "";
                        String day = "";
                        int id = arg0.getId();
                        if (id == 1) {

                            date = t1.getText().toString();

                        } else if (id == 2) {
                            date = t2.getText().toString();

                        } else if (id == 3) {
                            date = t3.getText().toString();

                        } else {
                            date = t4.getText().toString();

                        }

                        System.out.println("date of the coupon selected" + date);
                        if (a.equals("Accepted")) {
                            decreasecountpeoples(meal, date);

                        }


                        if (meal.equals(config.breakfast)) {
                            if (mHour >= config.breakfasttime && mHour < 9) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();

                            } else {

                                session.setBreakFastT(context, config.btrue, false);
                                l.removeAllViews();
                                setlayout_not_selected(meal, l, id);
                                session.setBreakFast(context, config.bday, config.junk);
                                session.setdateBreakFast(context, config.bdate, config.junk);
                                session.setaccBreakFast(context, config.bacc, config.junk);

                            }
                        }
                        if (meal.equals(config.lunch)) {
                            if (mHour >= config.lunchtime && mHour < 14) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();

                            } else {
                                session.setLunchT(context, config.ltrue, false);
                                l.removeAllViews();
                                setlayout_not_selected(meal, l, id);
                                session.setLunch(context, config.lday, config.junk);
                                session.setdateLunch(context, config.ldate, config.junk);
                                session.setaccLunch(context, config.lacc, config.junk);
                            }

                            //  System.out.println(session.getPreferences(context,config.lday) +" " +session.getPreferences(context,config.ldate));
                        }
                        if (meal.equals(config.snack)) {
                            if (mHour >= config.snacktime && mHour < 17) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();
                            } else {
                                session.setSnackT(context, config.strue, false);
                                l.removeAllViews();
                                setlayout_not_selected(meal, l, id);
                                session.setSnack(context, config.sday, config.junk);
                                session.setdateSnack(context, config.sdate, config.junk);
                                session.setaccSnack(context, config.sacc, config.junk);
                            }

                        }
                        if (meal.equals(config.dinner)) {
                            if (mHour >= config.dinnertime && mHour < 21) {
                                Toast.makeText(context, "You cannot edit , Deadline already passed !", Toast.LENGTH_SHORT).show();
                            } else {

                                session.setDinnerT(context, config.dtrue, false);
                                l.removeAllViews();
                                setlayout_not_selected(meal, l, id);
                                session.setDinner(context, config.dday, config.junk);
                                session.setdateDinner(context, config.bdate, config.junk);
                                session.setaccDinner(context, config.dacc, config.junk);
                            }

                        }


                    }
                });
            }
            else
            {
                Toast.makeText(context, "Check Your Ineternet Connection !", Toast.LENGTH_SHORT).show();
            }
        }
        /*
          ***********8 Case when Deadline is passed and user accept it or not ? ***********
          * *******************************************************************************
         */

        else
        {
            final Button b = new Button(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 1f);

            params.gravity = Gravity.CENTER;

            b.setTextSize(28);
            b.setAllCaps(false);
            b.setBackgroundResource(R.drawable.button);
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.show_coupon));
            b.setText("Show Coupon !");
            b.setId(id);
            b.setLayoutParams(params);
            l.addView(b);
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if(s.equals("Accepted"))
                    {
                        //Intent to new show activty
                    }
                    else
                    {
                     Toast.makeText(context,"You have Rejected the Coupon",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }


    public void countpeoples(final String menu, final String date)
    {
      databaseReference = FirebaseDatabase.getInstance().getReference(menu);



        DatabaseReference ref = databaseReference.child(date);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null ||(Long) currentData.getValue()==0) {
                    //System.out.println( "countpeople " +currentData.getValue());
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                    //System.out.println("*************DONE************************");

                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                decreasecoupons(menu);
                System.out.println("*************DONE************************");

            }


        });

    }

    public void decreasecoupons(String menu)
    {
        if(menu.equals(config.breakfast))
        {
            menu ="breakFast";
        }
        else if(menu.equals(config.lunch))
        {
           menu = "lunch";
        }
        else if(menu.equals(config.snack))
        {
            menu ="snack";
        }
        else
        {
            menu = "dinner";
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(config.coupon);

        String id = session.getPreferences(context,config.userid);

        DatabaseReference ref = databaseReference.child(id).child(menu);

        //System.out.println("*****called******");
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null ||(Long) currentData.getValue()==0) {
                   // System.out.println("************Done************");
                    currentData.setValue(1);
                } else {
                    //System.out.println("************Done2************");
                    currentData.setValue((Long) currentData.getValue() - 1);

                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {


            }


        });

    }

    // when user select redo option after accepting the meal
    public void decreasecountpeoples(final String menu, final String date)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(menu);

        DatabaseReference ref = databaseReference.child(date);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null ||(Long) currentData.getValue()==0) {
                     //System.out.println( "countpeople " +currentData.getValue());
                    currentData.setValue(0);
                } else {
                    currentData.setValue((Long) currentData.getValue() - 1);
                    System.out.println( "****decreased count ****** " +currentData.getValue());

                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                //Toast.makeText(context, " !", Toast.LENGTH_SHORT).show();
                increasedcoupons(menu);

            }


        });

    }

    public void increasedcoupons(String menu)
    {
        if(menu.equals(config.breakfast))
        {
            menu ="breakFast";
        }
        else if(menu.equals(config.lunch))
        {
            menu = "lunch";
        }
        else if(menu.equals(config.snack))
        {
            menu ="snack";
        }
        else
        {
            menu = "dinner";
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(config.coupon);

        String id = session.getPreferences(context,config.userid);

        DatabaseReference ref = databaseReference.child(id).child(menu);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null ||(Long) currentData.getValue()==0) {
                    System.out.println( "Increased count  " +currentData.getValue());
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                    System.out.println("update count here also");
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Toast.makeText(context, "Changes Succesfully made !", Toast.LENGTH_SHORT).show();

            }


        });

    }
    public boolean breakfastdeadline()
    {
        if(mHour>=config.breakfasttime && mHour <9)
        {
            //Toast.makeText(context,"You cannot edit , Deadline already passed !",Toast.LENGTH_SHORT).show();

            return true;
        }

        return false;
    }

    public boolean lunchdeadline()
    {
        if(mHour>=config.lunchtime && mHour<14)
        {
            //Toast.makeText(context,"You cannot edit , Deadline already passed !",Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public boolean snackdeadline()
    {
        if(mHour>=config.snacktime && mHour<17)
        {
           // Toast.makeText(context,"You cannot edit , Deadline already passed !",Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public boolean dinnerdeadline()
    {
        if(mHour>=config.dinnertime &&mHour<21)
        {
           // Toast.makeText(context,"You cannot edit , Deadline already passed !",Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
    public static String getdate()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return date;
    }

    public static String getTommorowdate()
    {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        String date = new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
        return date;
    }

    public List getMenu(String day, String time) {
        String str = "";
        List<String> menuList = null;
        try {


            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray(time);

            JSONObject jsonObject;

            jsonObject = m_jArry.getJSONObject(0);
            str = jsonObject.getString(day);
            menuList = Arrays.asList(str.split(","));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return menuList;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("menu.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getDayOfWeek() {
        String dayOfWeek;

        Date date = null;


        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DATE, 1);  // number of days to add
        String tomorrow = (String) (date_format.format(calender.getTime()));
        System.out.println("Tomorrows date is 22 " + tomorrow);

        try {
            date = date_format.parse(tomorrow);
        } catch (ParseException e) {
            e.printStackTrace();
            dayOfWeek = null;
        }

        calender.setTime(date);

        switch (calender.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;

            default:
                dayOfWeek = null;
                break;
        }

        return dayOfWeek;
    }
}
