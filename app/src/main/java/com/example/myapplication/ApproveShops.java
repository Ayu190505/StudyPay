package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.myapplication.ApproveShops_JavaFiles.ApproveShops_adapter;
import com.example.myapplication.ApproveShops_JavaFiles.ApproveShops_model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class ApproveShops extends AppCompatActivity {
    RecyclerView recyclerView;
    ApproveShops_adapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_shops);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_blue_light)));




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.Pay_navigation:
                        Intent payAct = new Intent(getApplicationContext(), Pay.class);
                        finishAffinity();
                        startActivity(payAct);
                        break;

                    case R.id.Home:
                        Intent mainAct = new Intent(getApplicationContext(), Teacher_MainPage.class);
                        finishAffinity();
                        startActivity(mainAct);
                        break;


                }
                return false;
            }
        });











        recyclerView = (RecyclerView) findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ApproveShops_model> options =
                new FirebaseRecyclerOptions.Builder<ApproveShops_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Shops"), ApproveShops_model.class)
                        .build();
        adapter = new ApproveShops_adapter(options);
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
        FirebaseRecyclerOptions<ApproveShops_model> options =
                new FirebaseRecyclerOptions.Builder<ApproveShops_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Shops").orderByChild("owner").startAt(query).endAt(query+"\uf8ff"), ApproveShops_model.class)
                        .build();

        adapter = new ApproveShops_adapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
