package com.example.android.slotter;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
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

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class live extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef ;
    ArrayList<Slot> list = new ArrayList<Slot>();
    ArrayList<String> emaillist = new ArrayList<>();
    String eventKey;
    String dtime,interval;
    int cn=0, est;
    TextView uname,sno,sdate,stime;
    String email = "";

    public void setemail(String x)
    {
        this.email = x;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        eventKey= getIntent().getStringExtra("Event Key");
        interval = getIntent().getStringExtra("interval");
        list = (ArrayList<Slot>) getIntent().getSerializableExtra("List");
        emaillist = (ArrayList<String>) getIntent().getSerializableExtra(("emailList"));

        Log.d("heyy event key!!!!",eventKey);

        myRef= FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);

        uname = (TextView) findViewById(R.id.lname);
        sno = (TextView) findViewById(R.id.lsno);
        sdate = (TextView) findViewById(R.id.ldate);
        stime = (TextView) findViewById(R.id.lstime);

        Log.d("check",String.valueOf(emaillist.size()));

        setdata();

    }

    void setdata()
    {
        Log.d("cn",String.valueOf(cn));
        if(list.size()==0)
        {
            Toast.makeText(getApplicationContext(),"No User Enrolled",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(i);
        }
        else
        {
            Slot s = list.get(cn);
            if(cn==0)
            {
                dtime = s.getStime();
                String x= s.getEtime();
                String t[] = dtime.split(":");
                String t1[] = x.split(":");
                int  min1= Integer.parseInt(t[0])*60+Integer.parseInt(t[1]);
                int  min2= Integer.parseInt(t1[0])*60+Integer.parseInt(t1[1]);
                est = min2-min1 + Integer.parseInt(interval);

            }
            //Log.d("snloo",s.getsNumber());
            uname.setText("User Name: "+s.getUid());
            sdate.setText("Date: "+s.getDate());
            stime.setText("Start Time: "+s.getStime());
            sno.setText("Slot number: "+s.getsNumber()+"");
        }
    }

    public void start(View v)
    {

    }

    public void stop(View v) {

        myRef.child("Event").child(eventKey).child("SLOTDETAILS").child(list.get(cn).getsNumber()+"").child("auth").setValue(false);

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        DateFormat df = new SimpleDateFormat("HH:mm");
        if(cn+1< list.size()) {
            Slot s1 = list.get(cn + 1);
            dtime = s1.getEtime();
        }
        String time = df.format(d);
        String t[] = time.split(":");
        String t1[] = dtime.split(":");

        int min1 = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);
        int min2 = Integer.parseInt(t1[0]) * 60 + Integer.parseInt(t1[1]);
        int esti = min2 - min1;

        est = (int) ((esti * 0.9) + (0.1 * est));

        // email = myRef.child("user").child(uname.getText().toString()).child("email").getKey();
        String username = "";
        if (cn < list.size() - 2)
        {
            Slot s = list.get(cn + 2);
            String etime[] = s.getEtime().split(":");
            int min11 = Integer.parseInt(etime[0]) * 60 + Integer.parseInt(etime[1]);
            String stime[] = s.getStime().split(":");
            int min12 = Integer.parseInt(stime[0]) * 60 + Integer.parseInt(stime[1]);
            int finaltime = est + Integer.parseInt(interval);
            finaltime = min11-min1;
            int du =min11-min12;
            if( min11 - min12 >= 2*du )
            {
                finaltime = min12-min1;
                est = du;
            }
            email = emaillist.get(cn+2);
            username = s.getUid();

            Log.d("useranmem",username);

            Log.d("your email!!", this.email);
            String[] recipients = {email};
            SendEmailAsyncTask emailAsyncTask = new SendEmailAsyncTask();
            //emailAsyncTask.activity = this;
            String id = "slotter2018@gmail.com";
            String pass = "Abcd1234.";
            emailAsyncTask.m = new Mail(id, pass);
            emailAsyncTask.m.set_from(id);
            emailAsyncTask.m.setBody("Hey " + username + " Your Slot will be start within " + finaltime + " minutes.\n So Please be on Time.\n Thanks." );
            emailAsyncTask.m.set_to(recipients);
            emailAsyncTask.m.set_subject("Slotter notification");
            emailAsyncTask.execute();
        }
        //notificatiin set to user:::
        cn++;
        Log.d("cn",String.valueOf(cn));
        if(cn == list.size())
        {
            Toast.makeText(getApplicationContext(),"Slot Completed",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(i);
        }
        else {
            setdata();
        }
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;
        //register_new activity;

        public SendEmailAsyncTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
                    Log.d("****EMAIL CHECK***", "Email sent.");

                } else {
                    Log.d("****EMAIL CHECK***", "Email failed to send.");
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                Log.d("****EMAIL CHECK***", "Authentication failed.");
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                e.printStackTrace();
                Log.d("****EMAIL CHECK***", "Email failed to send.");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("****EMAIL CHECK***", "Unexpected error occured.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
           // activity.progressDialog.dismiss();
            Log.d("****EMAIL CHECK***", "Progress Dialog dismissed.");
            //activity.successDialog();
            //activity.clearDetails();
        }
    }


}

