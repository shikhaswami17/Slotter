package com.example.android.slotter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class addEvent extends AppCompatActivity {

    private TextView mDisplaystartDate,mDisplayendDate,mDisplaystatTime,mDisplayendTime,filepath;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener1;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener,mTimeSetListener1;
    DatabaseReference databaseevent;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        databaseevent = FirebaseDatabase.getInstance().getReference();

        mDisplaystartDate = (TextView) findViewById(R.id.startDate);
        mDisplayendDate = (TextView) findViewById(R.id.endDate);

        mDisplaystatTime = (TextView) findViewById(R.id.stime);
        mDisplayendTime = (TextView) findViewById(R.id.etime);
        filepath= (TextView) findViewById(R.id.filepath);

        mDisplaystartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        addEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String date = (month+1) + "/" + day + "/" + year;
                mDisplaystartDate.setText(date);
            }
        };

        mDisplayendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        addEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String date = (month+1) + "/" + day + "/" + year;
                mDisplayendDate.setText(date);
            }
        };

        mDisplaystatTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mDisplaystatTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        mDisplayendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mDisplayendTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    public void  genrateSlot(String k,String sTime,String eTime,String noSlot,String interval,String slotDu,String sDate,String eDate) throws ParseException {

        SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat t = new SimpleDateFormat("HH:mm");

        Date s= d.parse(sDate);
        Date e = d.parse(eDate);
        Date st = t.parse(sTime);
        Date st1 = t.parse(sTime);
        Date et = t.parse(eTime);

        int inter = Integer.parseInt(interval);
        int duration = Integer.parseInt(slotDu);

        int cn=1;

        Calendar c = Calendar.getInstance();
        Calendar a = Calendar.getInstance();

        while (!s.after(e))
        {
            a.setTime(s);
            st=st1;
            while(!st.after(et))
            {
                if(st.equals(et))
                    break;
                c.setTime(st);
                String stime = t.format(c.getTime());
                c.add(Calendar.MINUTE,duration);
                String etime = t.format(c.getTime());
                c.add(Calendar.MINUTE,inter);
                Slot slot = new Slot(cn,d.format(a.getTime()),stime,etime,"",false,false);
                String id = Integer.toString(cn);
                databaseevent.child("Event").child(k).child("SLOTDETAILS").child(id).setValue(slot);
                st = c.getTime();
                cn++;
            }
            a.setTime(s);
            //Log.d("sdate",d.format(a.getTime()));
            a.add(Calendar.DATE,1);
            //Log.d("datechange!!!!!!!",d.format(a.getTime()));
            s = a.getTime();
        }
    }

 /*   public boolean slotCheccker(String sTime,String eTime,String noSlot,String interval,String slotDu,String sDate,String eDate)
    {
        String stime[] = sTime.split(":");
        String etime[] = eTime.split(":");

        String s[] = sDate.split("/");
        String e[] = eDate.split("/");

        int slot = Integer.parseInt(noSlot);
        int inter = Integer.parseInt(interval);
        int duration = Integer.parseInt(slotDu);

        int h = Integer.parseInt(etime[0])-Integer.parseInt(stime[0]);
        int m = Integer.parseInt(etime[1])-Integer.parseInt(stime[1]);

        Date s1= new Date(Integer.parseInt(s[2]),Integer.parseInt(s[1]),Integer.parseInt(s[0]));
        Date s2 = new Date(Integer.parseInt(e[2]),Integer.parseInt(e[1]),Integer.parseInt(e[0]));

        if(s2.before(s1))
            return false;

        if(h==0) {
            if(m<0)
                return false;
        }
        if(h<0)
            return false;

        int total = h*60+m;
        int need = inter*(slot-1)+slot*duration;

        if(total<need)
            return false;

        return true;
    }
*/
    public void addEvent1(View v) throws ParseException {
        EditText eventName = (EditText) findViewById(R.id.ename);
        TextView sdate = (TextView) findViewById(R.id.startDate);
        TextView edate = (TextView) findViewById(R.id.endDate);
        EditText edescription = (EditText) findViewById(R.id.eventDescription);
        EditText ecode = (EditText) findViewById(R.id.ecode);
        EditText slotDu = (EditText) findViewById(R.id.slotdur);
        EditText interval = (EditText) findViewById(R.id.slotint);
        TextView sTime = (TextView) findViewById(R.id.stime);
        TextView eTime = (TextView) findViewById(R.id.etime);
        /*sdate.setPaintFlags(sdate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        edate.setPaintFlags(edate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sTime.setPaintFlags(sTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        eTime.setPaintFlags(eTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);*/

        SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
        String uname = sp.getString("value", null);

        Log.d("uname", uname);
        Boolean flag = false;
        if (eventName.getText().toString().length() == 0 || sdate.getText().toString().length() == 0 || edate.getText().toString().length() == 0 || edescription.getText().toString().length() == 0
                || ecode.getText().toString().length() == 0 ||  slotDu.getText().toString().length() == 0 || interval.getText().toString().length() == 0
                || sTime.getText().toString().length() == 0 || eTime.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Retry", Toast.LENGTH_SHORT).show();

            slotDu.getText().clear();
            interval.getText().clear();
            return;
        } else {
            flag = true;
        }


        if (flag) {
            Event e = new Event(eventName.getText().toString(), sdate.getText().toString(), edate.getText().toString(), edescription.getText().toString(), ecode.getText().toString(), uname);
            String id = databaseevent.push().getKey();
            e.setRandKey(id);
            e.setIntervalTime(slotDu.getText().toString());
            databaseevent.child("Event").child(id).setValue(e);
            String key = databaseevent.push().getKey();
            UserCreateEvent ue = new UserCreateEvent(id);
            databaseevent.child("user").child(uname).child("CREATEDEVENT").child(key).setValue(ue);
            path = filepath.getText().toString();

            if (path.compareTo("") == 0) {
                genrateSlot(id, sTime.getText().toString(), eTime.getText().toString(), "", interval.getText().toString(), slotDu.getText().toString(), sdate.getText().toString(), edate.getText().toString());
                //SystemClock.sleep(2000);
                Toast.makeText(getApplicationContext(), "Event Added Successfully", Toast.LENGTH_SHORT).show();
            }
        else {
            Log.d("opath",path);
            File file = new File(path);
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    Log.d("data",line);
                    String a[] = line.split(",");
                    Slot slot = new Slot(Integer.parseInt(a[0]), a[2], a[3], a[4], a[1], true, true);
                    databaseevent.child("Event").child(id).child("SLOTDETAILS").child(a[0]).setValue(slot);
                    String rkey = databaseevent.push().getKey();
                    databaseevent.child("user").child(a[1]).child("REGISTEREVENT").child(rkey).child("eKey").setValue(id);

                }
                br.close();
            } catch (IOException e1) {
                //You'll need to add proper error handling here
            }
        }
            Intent goToNextActivity = new Intent(getApplicationContext(), SlotSelction.class);
            Log.d("Heuu", id);
            goToNextActivity.putExtra("Event", id);
            startActivity(goToNextActivity);
    }
        //IMPLEMENT :: Event code is unique

    }

    public void filepicker(View v) {
        checkPermissionsAndOpenFilePicker();

    }

    public void filepicker1(){
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(android.content.Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, 1000);
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1000);
            }
        } else {
            filepicker1();
        }
    }

    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            android.net.Uri uri = data.getData(); String filePath = uri != null ? uri.getPath() : null;
            // Do anything with file
            Log.d("path",filePath);
            filepath.setText(filePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"Permission NOT Granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
