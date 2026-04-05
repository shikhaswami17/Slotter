package com.example.android.slotter;

/**
 * Created by Niraj Vadhaiya on 13-02-2018.
 */

public class User {

    String uname;
    String name;
    String email;
    String cno;
    String pwd;

    public User(String uname, String name, String email, String cno, String pwd) {
        this.uname = uname;
        this.name = name;
        this.email = email;
        this.cno = cno;
        this.pwd = pwd;
    }

    public User()

    {

    }

    public String getUname() {
        return uname;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCno() {
        return cno;
    }

    public String getPwd() {
        return pwd;
    }


}
