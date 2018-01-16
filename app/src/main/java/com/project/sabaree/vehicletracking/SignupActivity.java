package com.project.sabaree.vehicletracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;

public class SignupActivity extends AppCompatActivity {

    EditText sName,sRegisterNumber,sEmail,sPassword,sRePassword;
    Button signUp ;
    TextView existing_user;
    ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getReference();
    DatabaseReference userRef = rootRef.child("Users");
    DatabaseReference detailRef = rootRef.child("Details");

    String sName_str = "";
    String sRegisterNumber_str = "";
    String sEmail_str = "";
    String sPassword_str = "";
    String sRePassword_str = "";
    String gender_str ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUp = findViewById(R.id.signup_btn);
        sName = findViewById(R.id.user_name);
        sRegisterNumber = findViewById(R.id.user_reg_no);
        sEmail = findViewById(R.id.user_email);
        sPassword = findViewById(R.id.user_pass);
        sRePassword = findViewById(R.id.user_re_pass);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        existing_user = findViewById(R.id.existing_user);

        existing_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()){
            case R.id.user_male:
                if(checked)
                    gender_str = "male";
                break;
            case R.id.user_female:
                if(checked)
                    gender_str = "female";
                break;
        }
    }

    private void createNewUser(){

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        sName_str = sName.getText().toString().trim();
        sRegisterNumber_str = sRegisterNumber.getText().toString().trim();
        sEmail_str = sEmail.getText().toString().trim();
        sPassword_str = sPassword.getText().toString().trim();
        sRePassword_str = sRePassword.getText().toString().trim();

        if(sRegisterNumber_str.length() == 7){
            if(sPassword_str.length() > 5){
                if (sName_str.isEmpty()||sRegisterNumber_str.isEmpty()||sEmail_str.isEmpty()||sPassword_str.isEmpty()||sRePassword_str.isEmpty()||gender_str.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(sPassword_str.equals(sRePassword_str)){
                        if(sEmail_str.contains("@")){

                            userRef.child(sRegisterNumber_str).setValue(sPassword_str);

                            DatabaseReference userDataFolder = detailRef.child(sRegisterNumber_str);


                            LinkedHashMap<String,String> userMap = new LinkedHashMap<>();
                            userMap.put("Name",sName_str);
                            userMap.put("RegisterNumber",sRegisterNumber_str);
                            userMap.put("Email",sEmail_str);
                            userMap.put("Password",sPassword_str);


                            userDataFolder.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }else{
                                        Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid email ID", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                        sPassword.setText("");
                        sRePassword.setText("");
                    }
                }

            }else{
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, "Password must be more than 5 letters", Toast.LENGTH_SHORT).show();
            }
        }else{
            progressDialog.dismiss();
            Toast.makeText(SignupActivity.this, "Invalid Register Number (must be 7 digits)", Toast.LENGTH_SHORT).show();
        }
    }
}
