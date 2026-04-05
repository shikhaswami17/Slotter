package com.example.android.slotter;

/**
 * Created by Niraj Vadhaiya on 19-02-2018.
 */

public class UserCreateEvent {

    String eKey;

    public UserCreateEvent(String eKey) {

        this.eKey = eKey;
    }


    public String geteKey() {
        return eKey;
    }
}
