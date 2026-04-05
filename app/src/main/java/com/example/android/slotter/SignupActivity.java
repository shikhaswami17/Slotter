package com.example.android.slotter;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatabaseReference databaseuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        databaseuser = FirebaseDatabase.getInstance().getReference();
    }

    public void signupFailed(Button signup){
        Toast.makeText(getApplicationContext(),"Retry",Toast.LENGTH_SHORT).show();
        signup.setEnabled(true);
    }

    private void registerUser(String uname, String name, String email, String cno, String pwd){

        //String id = databaseuser.push().getKey();
        User u = new User(uname,name,email,cno,pwd);

        databaseuser.child("user").child(uname).setValue(u);

    }

    public boolean validateInfo(String uname, String name, String email, String cno, String pwd, String cpwd){
        if(uname.length()==0){
            Toast.makeText(getApplicationContext(),"Enter username",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(name.length()==0){
            Toast.makeText(getApplicationContext(),"Enter name",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(email.length()==0){
            Toast.makeText(getApplicationContext(),"Enter email",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(cno.length()==0){
            Toast.makeText(getApplicationContext(),"Enter cno",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(pwd.length()==0){
            Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if ( pwd.length() < 4 || pwd.length() > 10) {
            Toast.makeText(getApplicationContext(),"Enter password between 4 and 10 alphanumeric characters",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(cpwd.length()==0){
            Toast.makeText(getApplicationContext(),"Enter conformation password",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!pwd.equals((cpwd))){
            Toast.makeText(getApplicationContext(),"Password and confirmation password does not match",Toast.LENGTH_SHORT).show();
            return false;
        }

        registerUser(uname, name, email, cno,pwd);
        //Insert data into database
    ///IMPLEMENT :::  check if uname is not already inserted
        return true;
    }

    public void showProgressRing(){
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Logging...");
        progressDialog.setTitle("ProgressDialog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        return;
    }


    public void Signup(View v){
        EditText uname=(EditText) findViewById(R.id.etxt_uname);
        EditText name=(EditText) findViewById(R.id.etxt_name);
        EditText email=(EditText) findViewById(R.id.etxt_email);
        EditText cno=(EditText) findViewById(R.id.etxt_cno);
        EditText pwd=(EditText) findViewById(R.id.etxt_pwd);
        EditText cpwd=(EditText) findViewById(R.id.etxt_cpwd);
        Button signup=(Button) findViewById(R.id.btn_signup);

        signup.setEnabled(false);
        if(!validateInfo(uname.getText().toString(),name.getText().toString(),email.getText().toString(),cno.getText().toString(),pwd.getText().toString(),cpwd.getText().toString())){
            signupFailed(signup);
            return;
        }

        //show progressbar
        showProgressRing();

        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    public void login(View v){
        Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(goToNextActivity);
    }
}
