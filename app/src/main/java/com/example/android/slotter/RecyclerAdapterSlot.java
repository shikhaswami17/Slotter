package com.example.android.slotter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Niraj Vadhaiya on 22-02-2018.
 */

public class RecyclerAdapterSlot extends RecyclerView.Adapter<RecyclerAdapterSlot.MyHoder>{
    List<Slot> list;
    Context context;
    FirebaseDatabase database;
    DatabaseReference myRef ;
    String eventkey;

    public RecyclerAdapterSlot(List<Slot> list, Context context,String key) {
        this.list = list;
        this.context = context;
        this.eventkey=key;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        myRef= FirebaseDatabase.getInstance().getReference().child("Event").child(eventkey).child("SLOTDETAILS");
        myRef.keepSynced(true);

        View view = LayoutInflater.from(context).inflate(R.layout.event_card_view,parent,false);
        MyHoder m = new MyHoder(view,context,list,myRef);
        //m = myHoder;


        return m;
    }
    @Override
    public void onBindViewHolder(MyHoder holder, final int position) {
        Slot mylist = list.get(position);
        holder.name.setText(mylist.getsNumber()+" "+mylist.getDate());
        holder.email.setText(mylist.getStime());
        holder.address.setText(mylist.getEtime());

    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try{
            if(list.size()==0){
                arr = 0;
            }
            else{
                arr=list.size();
            }
        }catch (Exception e){
        }
        return arr;
    }


    public static class MyHoder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,email,address;
        List<Slot> l;
        FirebaseDatabase database;
        DatabaseReference myRef ;
        LinearLayout layout;
        Context ctx;
        String slotnum;

        public MyHoder(View itemView,Context ctx,List<Slot> list,DatabaseReference ref) {
            super(itemView);
            this.ctx = ctx;
            this.l = list;
            this.myRef = ref;
            itemView.setOnClickListener(this);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            name = (TextView) itemView.findViewById(R.id.eName);
            email= (TextView) itemView.findViewById(R.id.sDate);
            address= (TextView) itemView.findViewById(R.id.eDate);

        }
        @Override
        public void onClick(View view) {

            int positon = getAdapterPosition();
            Slot e = this.l.get(positon);
            slotnum = Integer.toString(e.getsNumber());



            final Query color = myRef.child(slotnum);
            color.addListenerForSingleValueEvent((new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String x = dataSnapshot.child("viewToUser").getValue().toString();
                    Log.d("boolean",x);
                    if(x=="true")
                    {
                        myRef.child(slotnum).child("viewToUser").setValue(false);
                        layout.setBackgroundColor(Color.parseColor("#ffffff"));


                    }
                    else
                    {
                        myRef.child(slotnum).child("viewToUser").setValue(true);

                        layout.setBackgroundColor(Color.parseColor("#567845"));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }));
            //view.(Color.parseColor("#ADE07B"));
            //change to false and true
        }


    }

}