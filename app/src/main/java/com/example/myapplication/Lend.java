package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Lend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText sendTo, Amount, debt_Sendto, debt_Amount;
    private ImageView titleImage;
    private Button Pay, PayDebt, lend;

    private String person, money ,receiver_uid;


    private float send_money, receiver_balance, current_money, sender_calculated, receiver_calculated;
    private float debt, debt_calculated;



    private DatabaseReference db_sender, db_receiver;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);

        mAuth = FirebaseAuth.getInstance();


        titleImage = (ImageView) findViewById(R.id.lend_titlePic);
        PayDebt = (Button) findViewById(R.id.lend_paydebt_button);
        lend = (Button) findViewById(R.id.lend_button);

        sendTo = (EditText) findViewById(R.id.lend_sendto);
        Amount = (EditText) findViewById(R.id.lend_amount);
        debt_Sendto = (EditText) findViewById(R.id.lend_debt_sendto);
        debt_Amount = (EditText) findViewById(R.id.lend_debtamount);


        db_sender = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db_receiver = FirebaseDatabase.getInstance().getReference("Users");

        lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process_lend();
            }
        });

        PayDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_debt();
            }
        });

    }

    private void process_lend() {
        person = sendTo.getText().toString();
        money = Amount.getText().toString();

        send_money = Float.parseFloat(money);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot val : snapshot.getChildren()){
                    if(val.child("email").getValue(String.class).contains(person)){
                        receiver_uid = val.getKey();
                        debt = Float.parseFloat(val.child("debt").getValue().toString());

                        receiver_balance = Float.parseFloat(val.child("balance").getValue().toString());
                        receiver_calculated = receiver_balance + send_money;

                        debt_calculated = debt + send_money;

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
                db_receiver.child(receiver_uid).child("debt").setValue(Float.toString(debt_calculated));
                db_receiver.child(receiver_uid).child("balance").setValue(Float.toString(receiver_calculated));


            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);
    }


    private void send_debt() {
        person = debt_Sendto.getText().toString();
        money = debt_Amount.getText().toString();

        send_money = Float.parseFloat(money);

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
                debt = Float.parseFloat(snapshot.child("debt").getValue().toString());
                current_money = Float.parseFloat(balance);
                sender_calculated = current_money - send_money;
                debt_calculated = debt - send_money;


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
                db_sender.child("debt").setValue(Float.toString(debt_calculated));
                db_receiver.child(receiver_uid).child("balance").setValue(Float.toString(receiver_calculated));



            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);

    }

}
