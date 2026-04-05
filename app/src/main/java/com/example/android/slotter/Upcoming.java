package com.example.android.slotter;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

/**
 * Created by mit25 on 22/1/18.
 */

public class Upcoming extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef ;
    List<Event> list;
    List<String> l = new ArrayList<>();
    RecyclerView recycle;
    RecyclerAdapter recyclerAdapter;
    int TabIndex;
    String uname="";

    public void setUname(String uname) {
        this.uname = uname;
    }


    public void setint(int x)
    {
        TabIndex=x;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("heyyy","opps you entered");

        View rootView = inflater.inflate(R.layout.cretedevent_dashboard, container, false);
        super.onCreate(savedInstanceState);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myRef= FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);
        recycle = (RecyclerView) getView().findViewById(R.id.my_recycler_view);

        list = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(list,getActivity(),TabIndex);
        RecyclerView.LayoutManager recyce = new GridLayoutManager(this.getActivity(),1);
        //RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity());
        //recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        //RecyclerAdapter ad = new RecyclerAdapter(getActivity(),recyce)
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator( new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);

        prepareData();
    }

    public void prepareData() {
        Query getcreevent = myRef.child("user").child(uname).child("CREATEDEVENT");
        getcreevent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
                    String x=dataSnapshot1.child("eKey").getValue(String.class);
                    l.add(x);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query getregevent = myRef.child("user").child(uname).child("REGISTEREVENT");
        getregevent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
                    String x=dataSnapshot1.child("eKey").getValue(String.class);
                    l.add(x);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Event e = new Event("a","b","c","d","g","h");
        //list.add(e);
        final Query getevent = myRef.child("Event");
        getevent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //list = new ArrayList<Event>();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Event value = dataSnapshot1.getValue(Event.class);
                    Event fire = new Event();

                    String name = value.getEventName();
                    String sdate = value.getSdate();
                    String edate = value.getEdate();
                    String description = value.getDescription();
                    String ecreator = value.geteCreator();
                    String ecode = value.geteCode();

                    //Log.d("heyy event name!!!!",name);

                    fire.setEventName(name);
                    fire.setSdate(sdate);
                    fire.setEdate(edate);
                    fire.setDescription(description);
                    fire.seteCreator(ecreator);
                    fire.seteCode(ecode);

                    Date d = new Date();
                    Calendar c = Calendar.getInstance();
                    Date current = c.getTime();
                    SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        d =f.parse(sdate);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("date",d.toString() + " " + current.toString());
                    Calendar a = Calendar.getInstance();
                    a.setTime(current);
                    a.add(Calendar.DATE,-1);
                    current = a.getTime();
                    if(!l.contains(value.getRandKey()) && d.after(current))
                    {
                        list.add(value);
                    }
                    recyclerAdapter.notifyDataSetChanged();

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
