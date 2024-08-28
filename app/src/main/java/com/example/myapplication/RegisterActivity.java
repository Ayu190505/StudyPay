package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView title, register_button;
    private EditText editTextname, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        title = (TextView) findViewById(R.id.main_title);
        title.setOnClickListener(this);

        register_button = (Button) findViewById(R.id.registerButton);
        register_button.setOnClickListener(this);

        editTextname = (EditText) findViewById(R.id.name);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email_register);
        editTextPassword = (EditText) findViewById(R.id.password_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_title:
                startActivity(new Intent(this, MainPage.class));
                break;
            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        final String registerName = editTextname.getText().toString().trim();
        final String registerAge = editTextAge.getText().toString().trim();
        final String registerEmail = editTextEmail.getText().toString().trim();
        final String registerPassword = editTextPassword.getText().toString().trim();
        final String profile_Image = "None";
        final String balance = "0.000 OMR";
        final String debt = "0.000 OMR";
        final String shops = "None";

        if (registerName.isEmpty()){
            editTextname.setError("Please Enter Full Name");
            editTextname.requestFocus();
            return;
        }
        if (registerAge.isEmpty()){
            editTextAge.setError("Please Enter Password");
            editTextAge.requestFocus();
            return;
        }
        if(registerEmail.isEmpty()){
            editTextEmail.setError("Please Enter Email");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()){
                editTextEmail.setError("Please provide valid Email");

        }
        if(registerPassword.isEmpty()){
            editTextPassword.setError("Please Enter Password");
            editTextPassword.requestFocus();
            return;
        }
        if(registerPassword.length() < 6){
            editTextPassword.setError("Minimum Password length should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            User user = new User(registerName, registerAge, registerEmail, profile_Image, balance, debt,shops);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "User has been successfully Registered!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            else{
                                                Toast.makeText(RegisterActivity.this, "Failed to Register. Please Try Again", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to Register: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
