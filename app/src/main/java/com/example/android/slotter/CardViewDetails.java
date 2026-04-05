package com.example.android.slotter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CardViewDetails extends AppCompatActivity {

    TextView ename,ecreator,sdate,edate,edes,ecode,sno,stime1,etime1,slotdate,auth;
    FirebaseDatabase database;
    DatabaseReference myRef ;
    String eventKey;
    String uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        eventKey =getIntent().getStringExtra("Event Key");
        myRef= FirebaseDatabase.getInstance().getReference().child("Event");
        myRef.keepSynced(true);

        SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
        uname = sp.getString("value", null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_details);
        ename = (TextView) findViewById(R.id.ename1);
        ecreator = (TextView) findViewById(R.id.ecreator1);
        sdate = (TextView) findViewById(R.id.startDate1);
        edate = (TextView) findViewById(R.id.endDate1);
        ecode = (TextView) findViewById(R.id.ecode1);
        edes = (TextView) findViewById(R.id.eventDescription1);
        sno = (TextView) findViewById(R.id.sno);
        stime1 = (TextView) findViewById(R.id.stime);
        etime1 = (TextView) findViewById(R.id.etime);
        slotdate = (TextView) findViewById(R.id.sldate);
        auth = (TextView) findViewById(R.id.auth);

        ename.setText("Event Name : " + getIntent().getStringExtra("Event Name"));
        ecreator.setText("Event Creator : " + getIntent().getStringExtra("Event Creator"));
        edes.setText("Event Description : " + getIntent().getStringExtra("Event Description"));
        sdate.setText("Event Start Date : " + getIntent().getStringExtra("Start Date"));
        edate.setText("Event End Date : " + getIntent().getStringExtra("End Date"));
        ecode.setText("Event Code : " + getIntent().getStringExtra("Event Code"));

        final Query getPass = myRef.child(eventKey).child("SLOTDETAILS");

        Log.d("heyy event key!!!!",eventKey);
        getPass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    int sNumber= dataSnapshot1.child("sNumber").getValue(Integer.class);
                    String date = dataSnapshot1.child("date").getValue(String.class);
                    String stime= dataSnapshot1.child("stime").getValue(String.class);
                    String etime= dataSnapshot1.child("etime").getValue(String.class);
                    String uid = dataSnapshot1.child("uid").getValue(String.class);
                    Boolean isView = dataSnapshot1.child("viewToUser").getValue(Boolean.class);
                    boolean isAuth= dataSnapshot1.child("auth").getValue(Boolean.class);

                    Log.d("heyy event name!!!!",date);

                    if(uid.equals(uname))
                    {
                        sno.setText("Slot Number : " + sNumber);
                        stime1.setText("Start Time : " + stime);
                        etime1.setText("End Time : " + etime);
                        slotdate.setText("Slot Date : " + date);
                        auth.setText("Slot Authentication Status : " + isAuth);
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
}
