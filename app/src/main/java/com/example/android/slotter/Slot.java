package com.example.android.slotter;

/**
 * Created by Niraj Vadhaiya on 19-02-2018.
 */

public class Slot implements java.io.Serializable {

    int sNumber ;
    String date;
    String stime;
    String etime;
    String uid;
    boolean auth ;
    boolean viewToUser ;

    public Slot(){

    }

    public Slot(int sNumber, String date, String stime, String etime, String uid, boolean auth, boolean viewToUser) {
        this.sNumber = sNumber;
        this.date = date;
        this.stime = stime;
        this.etime = etime;
        this.uid = uid;
        this.auth = auth;
        this.viewToUser = viewToUser;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public void setViewToUser(boolean viewToUser) {
        this.viewToUser = viewToUser;
    }

    public int getsNumber() {
        return sNumber;
    }

    public String getDate() {
        return date;
    }

    public String getStime() {
        return stime;
    }

    public String getEtime() {
        return etime;
    }

    public String getUid() {
        return uid;
    }

    public boolean isAuth() {
        return auth;
    }

    public boolean isViewToUser() {
        return viewToUser;
    }
}
