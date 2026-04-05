package com.example.android.slotter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCas;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private DatabaseReference databaseuser;
    private DataSnapshot dsU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseuser = FirebaseDatabase.getInstance().getReference();

    }


    public boolean validateCredentials(final String uname, final String pwd) {
        if (uname.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pwd.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pwd.length() < 4 || pwd.length() > 10) {
            Toast.makeText(getApplicationContext(), "Enter password between 4 and 10 alphanumeric characters", Toast.LENGTH_SHORT).show();
            return false;
        }


       final Query getPass = databaseuser.child("user").child(uname);

       getPass.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               Object pass1 = dataSnapshot.child("pwd").getValue();

               //Log.d("YOU LOG IN!!!!", pass);
               if(pass1==null)
               {}
               else
               {
                   String pass = pass1.toString();
                   if(pass.equals(pwd)) {
                       Log.d("YOU LOG IN!!!!", pass);

                       SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
                       SharedPreferences.Editor ed = sp.edit();
                       ed.putString("value", uname);
                       ed.commit();

                       Intent goToNextActivity = new Intent(getApplicationContext(), DashboardActivity.class);
                       goToNextActivity.putExtra("uname",uname);
                       startActivity(goToNextActivity);
                   }
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
        //Retrive password from database given username
        //SELECT password FROM User WHERE usernams=uname;
        return false;
    }

    public void loginFailed(Button login){
        login.setEnabled(true);
    }

    public void showProgressRing(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging...");
        progressDialog.setTitle("ProgressDialog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    public void Login(View v){
        Button login=(Button) findViewById(R.id.btn_login);
        EditText uname=(EditText) findViewById(R.id.etxt_uname);
        EditText pwd=(EditText) findViewById(R.id.etxt_pwd);

        login.setEnabled(false);

        if(!validateCredentials(uname.getText().toString(),pwd.getText().toString())){
            loginFailed(login);
            return;
        }

        //show progressbar
        //redirect to dashboard activity
    }

    public void signup(View v){
        Intent goToNextActivity = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(goToNextActivity);
    }
}
