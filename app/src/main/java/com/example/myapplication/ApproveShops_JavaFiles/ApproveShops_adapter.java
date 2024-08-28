package com.example.myapplication.ApproveShops_JavaFiles;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApproveShops_adapter extends FirebaseRecyclerAdapter<ApproveShops_model, ApproveShops_adapter.viewholder > {

    public  DatabaseReference approvedref = FirebaseDatabase.getInstance().getReference("Shops");
    public String uid;
    public ApproveShops_adapter(@NonNull FirebaseRecyclerOptions<ApproveShops_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull ApproveShops_model model) {
        holder.owner.setText(model.getOwner());
        holder.shopInfo.setText(model.getTitle() + " : " + model.getDescription());
        holder.teachername.setText(model.getTeacherName());

        holder.uid = model.getUid();
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_approve_singlerow, parent, false);
        return new viewholder(view);
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

            approve_Button = (Button) itemView.findViewById(R.id.approve_button);


            approve_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approvedref.child(uid).child("approved").setValue("yes");
                }
            });

            deny_Button = (Button) itemView.findViewById(R.id.deny_button);
            deny_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approvedref.child(uid).child("approved").setValue("no");
                }
            });


        }
    }
}
