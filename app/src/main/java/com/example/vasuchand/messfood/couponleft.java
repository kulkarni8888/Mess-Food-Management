package com.example.vasuchand.messfood;

/**
 * Created by Vasu Chand on 4/2/2017.
 */

public class couponleft {
    long BreakFast,Lunch,Snack,Dinner;


    String Email;

    public couponleft() {

    }

    public couponleft(String email, int breakFast, int lunch, int snack, int dinner) {
        BreakFast = breakFast;
        Lunch = lunch;
        Snack = snack;
        Dinner = dinner;
        Email = email;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public long getBreakFast() {
        return BreakFast;
    }

    public void setBreakFast(long breakFast) {
        BreakFast = breakFast;
    }

    public long getLunch() {
        return Lunch;
    }

    public void setLunch(long lunch) {
        Lunch = lunch;
    }

    public long getSnack() {
        return Snack;
    }

    public void setSnack(int snack) {
        Snack = snack;
    }

    public long getDinner() {
        return Dinner;
    }

    public void setDinner(long dinner) {
        Dinner = dinner;
    }
}
