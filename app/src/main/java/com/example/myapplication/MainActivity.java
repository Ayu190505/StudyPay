package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, maint;
    private EditText Email, Password;
    private Button signIn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.Signup);
        register.setOnClickListener(this);


        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);

        signIn = (Button) findViewById(R.id.loginButton);
        signIn.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.Login_progressBar);

        mAuth = FirebaseAuth.getInstance();

        maint = (TextView) findViewById(R.id.main_title);
        maint.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Signup:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.loginButton:
                userLoginHandler();
                break;
            case R.id.main_title:
                startActivity(new Intent(this, Lend.class));
                break;

        }
    }

    private void userLoginHandler() {
        final String email_string = Email.getText().toString().trim();
        final String password_string = Password.getText().toString().trim();

        if (email_string.isEmpty()) {
            Email.setError("Please Enter Email");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            Email.setError("Please provide valid Email");
            Email.requestFocus();
            return;
        }
        if (!email_string.endsWith("@aba.com")){
            Email.setError("Please Enter aba Email");
            Email.requestFocus();
            return;
        }
        if (password_string.isEmpty()) {
            Password.setError("Please Enter Password");
            Password.requestFocus();
            return;
        }
        if (password_string.length() < 6) {
            Password.setError("Minimum Password length should be 6 characters");
            Password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email_string, password_string)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(!Pattern.compile( "[1-9]" ).matcher(email_string).find()) {
                                startActivity(new Intent(MainActivity.this, Teacher_MainPage.class));
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                           else{ //Student Main page
                                startActivity(new Intent(MainActivity.this, MainPage.class));
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Failed To Login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}