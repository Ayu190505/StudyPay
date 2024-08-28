package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.myapplication.MainPage_JavaFiles.MainPage_adapter;
import com.example.myapplication.MainPage_JavaFiles.MainPage_model;
import com.example.myapplication.Teacher_MainPage_Javafiles.Teacher_MainPage_adapter;
import com.example.myapplication.Teacher_MainPage_Javafiles.Teacher_MainPage_model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_MainPage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationview;
    private TextView Balance;
    private DatabaseReference balanceFetch;
    private Button approve_ShopsButton, create_ShopsButton;
    private ActionBar toolbar;
    RecyclerView recyclerView;
    Teacher_MainPage_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__main_page);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_light)));

        Balance = (TextView) findViewById(R.id.balance);

        //approve_ShopsButton = (Button) findViewById(R.id.approveShops_button);
        //create_ShopsButton = (Button) findViewById(R.id.createShops_button);
        //approve_ShopsButton.setOnClickListener(this);
        //create_ShopsButton.setOnClickListener(this);

        balanceFetch = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.approveShops_navigation:
                        Intent approveAct = new Intent(getApplicationContext(), ApproveShops.class);
                        finishAffinity();
                        startActivity(approveAct);
                        break;


                    case R.id.Pay_navigation:
                        Intent payAct = new Intent(getApplicationContext(), Pay.class);
                        finishAffinity();
                        startActivity(payAct);
                        break;


                }
                return false;
            }
        });

        balanceFetch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String stringBalance = snapshot.child("balance").getValue().toString();
                Balance.setText(stringBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.teacher_recview_mainpage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Teacher_MainPage_model> options =
                new FirebaseRecyclerOptions.Builder<Teacher_MainPage_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Shops").orderByChild("approved").startAt("yes").endAt("yes"+"\uf8ff"), Teacher_MainPage_model.class)
                        .build();
        adapter = new Teacher_MainPage_adapter(options);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchview =(SearchView) item.getActionView();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String query) {
        FirebaseRecyclerOptions<Teacher_MainPage_model> options =
                new FirebaseRecyclerOptions.Builder<Teacher_MainPage_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Shops").orderByChild("owner").startAt(query).endAt(query+"\uf8ff"), Teacher_MainPage_model.class)
                        .build();

        adapter = new Teacher_MainPage_adapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



}

