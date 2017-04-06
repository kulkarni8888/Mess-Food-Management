package com.example.vasuchand.messfood;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vasu Chand on 3/30/2017.
 */

public class Session {
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;




    private String Module_Pref= "SharedPreference";

    public Session(Context context) {
        this._context = context;

    }

    public void setBreakFastT(Context context,String IS_FIRST_TIME_LAUNCH ,boolean isFirstTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setLunchT(Context context,String IS_FIRST_TIME_LAUNCH ,boolean isFirstTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setSnackT(Context context,String IS_FIRST_TIME_LAUNCH ,boolean isFirstTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setDinnerT(Context context,String IS_FIRST_TIME_LAUNCH ,boolean isFirstTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setBreakFast(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setLunch(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setSnack(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setDinner(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }

    // accepted or rejected

    public void setaccBreakFast(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setaccLunch(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setaccSnack(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setaccDinner(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }

    // set date of acceptance or rejection

    public void setdateBreakFast(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setdateLunch(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setdateSnack(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setdateDinner(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }

    public void setuserid(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }

    public void settoken(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }
    public void setemail(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(Module_Pref, Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.commit();

    }





    public  String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(Module_Pref,	Context.MODE_PRIVATE);
        String position = prefs.getString(key, "...");
        return position;
    }

    public boolean isFirstTimeLaunch(Context context,String IS_FIRST_TIME_LAUNCH ) {
        SharedPreferences prefs = context.getSharedPreferences(Module_Pref,	Context.MODE_PRIVATE);
        return prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
