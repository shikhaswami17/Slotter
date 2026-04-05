package com.example.android.slotter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class SlotAuthorise extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef ;
    List<Slot> list;
    RecyclerView recycle;
    RecyclerAdapterAuth recyclerAdapter;
    String eventKey;
    List<Integer> sno = new ArrayList<>();

    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_authorise);
        eventKey= getIntent().getStringExtra("Event Key");

        myRef= FirebaseDatabase.getInstance().getReference().child("Event");
        myRef.keepSynced(true);
        recycle = (RecyclerView) findViewById(R.id.my_recycler_view_Slot_auth);

        list = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapterAuth(list,getApplicationContext(),eventKey);
        RecyclerView.LayoutManager recyce = new GridLayoutManager(getApplicationContext(),1);
        //RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity());
        //recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        //RecyclerAdapter ad = new RecyclerAdapter(getActivity(),recyce)
        recycle.setLayoutManager(recyce);
        recycle.setItemAnimator( new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);

        prepareData();

    }

    public void prepareData() {
        Log.d("heyy event key!!!!",eventKey);

        final Query getPass = myRef.child(eventKey).child("SLOTDETAILS");

        Log.d("heyy event key!!!!",eventKey);
        //Event e = new Event("a","b","c","d","g","h");
        //list.add(e);
        getPass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //list = new ArrayList<Event>();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    //  Slot value = dataSnapshot1.getValue(Slot.class);
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

                    if(isView && uid.compareTo("")!=0 && !isAuth && !sno.contains(sNumber)) {
                        list.add(fire);
                        sno.add(sNumber);
                    }
                    String x = Integer.toString(list.size());
                    Log.d("List size",x);
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

    public void confirm(View view){
        Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(i);
    }
}
