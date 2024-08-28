package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ApproveShops_JavaFiles.ApproveShops_adapter;
import com.example.myapplication.ApproveShops_JavaFiles.ApproveShops_model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class CreateShops extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;
    private EditText editShop_Title, editShop_Description, editShop_TeacherName;
    private ImageView profileImage;
    private Button browse, upload;
    private Uri filepath;
    private Bitmap bitmap;
    private DatabaseReference userInfo_Fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shops);


        mAuth = FirebaseAuth.getInstance();

        profileImage = (ImageView) findViewById(R.id.profilePic);
        browse = (Button) findViewById(R.id.browse_profilePic);
        upload = (Button) findViewById(R.id.upload_profilePic);

        editShop_Title = (EditText) findViewById(R.id.ShopTitle);
        editShop_Description = (EditText) findViewById(R.id.createShop_description);
        editShop_TeacherName = (EditText) findViewById(R.id.teacherName);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
                        Intent mainAct = new Intent(getApplicationContext(), MainPage.class);
                        finishAffinity();
                        startActivity(mainAct);
                        break;
                }
                return false;
            }
        });





        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Dexter.withActivity(CreateShops.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/");
                                    startActivityForResult(Intent.createChooser(intent, "Please Select an Image"), 1 );

                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                }
                            }).check();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase();
            }
        });
    }

    private void uploadToFirebase() {
        final String title = editShop_Title.getText().toString().trim();
        final String description = editShop_Description.getText().toString().trim();
        final String teacherName = editShop_TeacherName.getText().toString().trim();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth Image_auth = FirebaseAuth.getInstance();



        final StorageReference uploader = storage.getReference(Image_auth.getCurrentUser().getUid());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Shops").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                String uid = Image_auth.getCurrentUser().getUid();


                                userInfo_Fetch = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userInfo_Fetch.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String owner = snapshot.child("name").getValue().toString();
                                        rootRef.child("uid").setValue(uid);
                                        rootRef.child("owner").setValue(owner);
                                        rootRef.child("image").setValue(uri.toString());
                                        rootRef.child("title").setValue(title);
                                        rootRef.child("Description").setValue(description);
                                        rootRef.child("teacherName").setValue(teacherName);
                                        rootRef.child("approved").setValue("no");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });






                            }
                        });

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully", Toast.LENGTH_LONG);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percentUploaded = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        dialog.setMessage("Creating Shop: " +(int)percentUploaded+ " %");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK);
        {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);

            }catch (Exception ex){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
