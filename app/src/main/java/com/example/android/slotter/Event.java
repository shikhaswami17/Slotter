/**
 * Created by Niraj Vadhaiya on 16-02-2018.
 */
package com.example.android.slotter;

public class Event {

    String eventName;
    String sdate;
    String edate;
    String description;
    String eCode;
    String eCreator;
    String randKey="";
    String intervalTime="";

    public Event()
    {

    }
    public Event(String eventName, String sdate, String edate, String description, String eCode, String eCreator) {
        this.eventName = eventName;
        this.sdate = sdate;
        this.edate = edate;
        this.description = description;
        this.eCode = eCode;
        this.eCreator = eCreator;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getEventName() {
        return eventName;
    }

    public String getSdate() {
        return sdate;
    }

    public String getEdate() {
        return edate;
    }

    public String getDescription() {
        return description;
    }

    public String geteCode() {
        return eCode;
    }

    public String geteCreator() {
        return eCreator;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getRandKey() {
        return randKey;
    }

    public void setRandKey(String randKey) {
        this.randKey = randKey;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public void seteCreator(String eCreator) {
        this.eCreator = eCreator;
    }
}
