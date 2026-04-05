package com.example.android.slotter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
/**
 * Created by Niraj Vadhaiya on 22-02-2018.
 */

public class RecyclerAdapterEnroll extends RecyclerView.Adapter<RecyclerAdapterEnroll.MyHoder>{

    List<Slot> list;
    Context context;
    FirebaseDatabase database;
    DatabaseReference myRef ;
    String eventkey;
    String uname;
    public RecyclerAdapterEnroll(List<Slot> list, Context context,String key,String uname) {
        this.list = list;
        this.context = context;
        this.eventkey=key;
        this.uname= uname;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        myRef= FirebaseDatabase.getInstance().getReference().child("Event").child(eventkey).child("SLOTDETAILS");
        myRef.keepSynced(true);


        View view = LayoutInflater.from(context).inflate(R.layout.event_card_view,parent,false);
        MyHoder myHoder = new MyHoder(view,context,list,myRef,uname);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
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
        String uname;
        Context ctx;

        public MyHoder(View itemView,Context ctx,List<Slot> list,DatabaseReference ref,String uname) {
            super(itemView);
            this.ctx = ctx;
            this.l = list;
            this.myRef = ref;
            this.uname = uname;
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.eName);
            email= (TextView) itemView.findViewById(R.id.sDate);
            address= (TextView) itemView.findViewById(R.id.eDate);

        }

        @Override
        public void onClick(View view) {
            int positon = getAdapterPosition();
            Slot e = this.l.get(positon);
            final String slot = Integer.toString(e.getsNumber());
            Log.d("Slot NO. ", slot);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(itemView.getContext());
            builder1.setMessage("Confirm This Slot!!!!");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            myRef.child(slot).child("uid").setValue(uname);
                            Intent i =new Intent(ctx,DashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(i);

                        }
                    });

            builder1.setNegativeButton(
                    "Cancle",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
    Log.d("dbsjd","dfsdf");
            //view.(Color.parseColor("#ADE07B"));
            //change to false and true
        }


    }

}