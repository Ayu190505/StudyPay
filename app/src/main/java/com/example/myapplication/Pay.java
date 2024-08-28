package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.internal.FlowLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Pay extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText sendTo, Amount;
    private ImageView titleImage;
    private Button Pay;

    private String person, money ,receiver_uid;


    private float send_money, receiver_balance, current_money, sender_calculated, receiver_calculated;
    private float debt, debt_calculated;



    private DatabaseReference db_sender, db_receiver;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mAuth = FirebaseAuth.getInstance();


        titleImage = (ImageView) findViewById(R.id.titlePic);
        Pay = (Button) findViewById(R.id.pay_button);

        sendTo = (EditText) findViewById(R.id.sendto);
        Amount = (EditText) findViewById(R.id.amount);

        db_sender = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db_receiver = FirebaseDatabase.getInstance().getReference("Users");


        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send_balance();

            }
        });
    }


    private void send_balance() {
        person = sendTo.getText().toString();
        money = Amount.getText().toString();

        se
                nd_money = Float.parseFloat(money);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot val : snapshot.getChildren()){
                    if(val.child("email").getValue(String.class).contains(person)){
                        receiver_uid = val.getKey();

                        receiver_balance = Float.parseFloat(val.child("balance").getValue().toString());
                        receiver_calculated = receiver_balance + send_money;
                        return;
                    }
                    else{
                        //Toast
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db_sender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String balance = snapshot.child("balance").getValue().toString();
                current_money = Float.parseFloat(balance);
                sender_calculated = current_money - send_money;

                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });



        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                db_sender.child("balance").setValue(Float.toString(sender_calculated));
                db_receiver.child(receiver_uid).child("balance").setValue(Float.toString(receiver_calculated));

            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);


    }

}
