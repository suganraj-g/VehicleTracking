package com.project.sabaree.vehicletracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getReference();
    DatabaseReference userRef = rootRef.child("Users");



    EditText mRegisterNumber ;
    EditText mPassword;
    Button mLogin;
    TextView mNewUser;

    String mRegisterNumber_str = "";
    String mPassword_str = "";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRegisterNumber = findViewById(R.id.register_number);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login_btn);
        mNewUser = findViewById(R.id.new_user);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinExistingUser();
            }
        });

        mNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signinExistingUser(){
        mRegisterNumber_str = mRegisterNumber.getText().toString();
        mPassword_str = mPassword.getText().toString();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Authenticating");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue().toString();
                String child = dataSnapshot.getKey().toString();
                //Log.i("TAG","data: "+value);
                //Log.i("TAG","data: "+child);
                if(child.contains(mRegisterNumber_str)&&value.contains(mPassword_str)){
                    /*try {
                        JSONObject object = new JSONObject(value);
                        String pass = object.getString("1414226");
                        Log.i("TAG","tag"+pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Check Your RegNo and Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
