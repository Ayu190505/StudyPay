package com.example.myapplication.MainPage_JavaFiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainPage_JavaFiles.MainPage_adapter;
import com.example.myapplication.MainPage_JavaFiles.MainPage_model;
import com.example.myapplication.MainPage;
import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage_adapter extends FirebaseRecyclerAdapter<MainPage_model, MainPage_adapter.viewholder> {
    public DatabaseReference approvedref = FirebaseDatabase.getInstance().getReference("Shops");
    public String uid;
    public MainPage_adapter(@NonNull FirebaseRecyclerOptions<MainPage_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainPage_adapter.viewholder holder, int position, @NonNull MainPage_model model) {
        holder.owner.setText(model.getOwner());
        holder.shopInfo.setText(model.getTitle() + " : " + model.getDescription());
        holder.teachername.setText(model.getTeacherName());

        holder.uid = model.getUid();
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);

    }

    @NonNull
    @Override
    public MainPage_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mainpage_singlerow, parent, false);
        return new MainPage_adapter.viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView owner, shopInfo, teachername;
        Button approve_Button, deny_Button;
        String uid;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.shopImg);
            owner = (TextView) itemView.findViewById(R.id.ownertext);
            shopInfo = (TextView) itemView.findViewById(R.id.descriptiontext);
            teachername = (TextView) itemView.findViewById(R.id.assignedto_text);

        }
    }

}
