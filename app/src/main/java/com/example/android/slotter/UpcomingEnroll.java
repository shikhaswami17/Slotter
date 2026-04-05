package com.example.android.slotter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpcomingEnroll extends AppCompatActivity {

    TextView ename,ecreator,sdate,edate,edes,ecode;
    FirebaseDatabase database;

    DatabaseReference myRef ;
    String randkey;
    ArrayList<Slot> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_enroll);

        myRef= FirebaseDatabase.getInstance().getReference().child("Event");
        myRef.keepSynced(true);

        ename = (TextView) findViewById(R.id.enameenrl);
        ecreator = (TextView) findViewById(R.id.ecreatorenrl);
        sdate = (TextView) findViewById(R.id.startDateenrl);
        edate = (TextView) findViewById(R.id.endDateenrl);
        ecode = (TextView) findViewById(R.id.ecodeenrl);
        edes = (TextView) findViewById(R.id.eventDescriptionenrl);
        randkey = getIntent().getStringExtra("Event Key");

       // Log.d("ekey !!!",randkey);
        ename.setText("Event Name : " + getIntent().getStringExtra("Event Name"));
        ecreator.setText("Event Creator : " + getIntent().getStringExtra("Event Creator"));
        edes.setText("Event Description : " + getIntent().getStringExtra("Event Description"));
        sdate.setText("Event Start Date : " + getIntent().getStringExtra("Start Date"));
        edate.setText("Event End Date : " + getIntent().getStringExtra("End Date"));
        ecode.setText("Event Code : " + getIntent().getStringExtra("Event Code"));

        prepareData();


    }

    public void prepareData() {

        Log.d("heyy event key!!!!",randkey);

        final Query getPass = myRef.child(randkey).child("SLOTDETAILS");

        Log.d("heyy event key!!!!",randkey);
        //Event e = new Event("a","b","c","d","g","h");
        //list.add(e);
        getPass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //list = new ArrayList<Event>();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    //Slot value = dataSnapshot1.getValue(Slot.class);
                    Slot fire = new Slot();

                    int sNumber= dataSnapshot1.child("sNumber").getValue(Integer.class);
                    String date = dataSnapshot1.child("date").getValue(String.class);
                    String stime= dataSnapshot1.child("stime").getValue(String.class);
                    String etime= dataSnapshot1.child("etime").getValue(String.class);
                    String uid = dataSnapshot1.child("uid").getValue(String.class);
                    Boolean isView = dataSnapshot1.child("viewToUser").getValue(Boolean.class);
                    boolean isAuth= dataSnapshot1.child("auth").getValue(Boolean.class);

                    Log.d("heyy event name!!!!",date);

                    fire.setsNumber(sNumber);
                    fire.setDate(date);
                    fire.setStime(stime);
                    fire.setEtime(etime);
                    fire.setUid(uid);
                    fire.setViewToUser(isView);
                    fire.setAuth(isAuth);


                    if(uid.compareTo("")==0 && isView) {
                        list.add(fire);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

    }


    public void enrl(View view)
    {
        SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
        String uname = sp.getString("value" , null);
        if(list.size()==0)
        {
            Toast.makeText(getApplicationContext(),"No more Slot Available",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(i);
        }
        else {
            myRef= FirebaseDatabase.getInstance().getReference().child("user").child(uname).child("REGISTEREVENT");
            String key = myRef.push().getKey();
            myRef.child(key).child("eKey").setValue(randkey);
            myRef.keepSynced(true);
            Intent i =new Intent(getApplicationContext(),EnrollinSlot.class);
            i.putExtra("uname",uname);
            i.putExtra("Event Key",randkey);
            startActivity(i);
        }
    }
}

