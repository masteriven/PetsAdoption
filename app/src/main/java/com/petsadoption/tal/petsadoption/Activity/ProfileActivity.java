package com.petsadoption.tal.petsadoption.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petsadoption.tal.petsadoption.Objects.User;
import com.petsadoption.tal.petsadoption.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    private TextView tvName,tvDate,tvPhone,tvCity;
    private ImageView profileImage;
    public String uriDownload;
    StorageReference storageReference;
    FirebaseDatabase mFireBaseDatabase;
    CropImageView cropImage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFireBaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFireBaseDatabase.getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        tvName = (TextView) findViewById(R.id.tvName);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        profileImage = (ImageView)findViewById(R.id.profileImage);
        cropImage = (CropImageView) findViewById(R.id.cropImageView);
        cropImage.setGuidelines(CropImageView.Guidelines.OFF);
        mAuth = FirebaseAuth.getInstance();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (firebaseUser != null){
                    String key = firebaseUser.getUid();
                    Map<String ,String> map = (Map<String, String>) dataSnapshot.child("user").child(key).getValue();

                    if (map == null){


                    }else{
                       String name = map.get("fullName");
                       String  date = map.get("dateOfBirth");
                       String  city = map.get("city");
                       String phone = map.get("phoneNumber");
                       String uriDownload = map.get("uriDownload");

                        tvName.setText(name);
                        tvDate.setText(date);
                        tvCity.setText(city);
                        tvPhone.setText(phone);

                        if (uriDownload != null){
                            profileImage.setVisibility(View.GONE);
                            Glide.with(getApplicationContext()).load(uriDownload).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    cropImage.setImageBitmap(resource);
                                }
                            });

                        }
                    }

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void cropImageView(View view) {
        CropImage.activity()
                .start(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final StorageReference imageCrop = storageReference.child("images/profileImage" + resultUri.toString() );
                imageCrop.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                 Task<Uri> downloadUrl = imageCrop.getDownloadUrl();
                     uriDownload = downloadUrl.toString();

                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("uriDownload",uriDownload);
                        databaseReference.child("user").child(firebaseUser.getUid()).updateChildren(childUpdates);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                cropImage.setImageUriAsync(resultUri);
                profileImage.setVisibility(View.INVISIBLE);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        Bitmap cropped = cropImage.getCroppedImage();
        cropImage.setImageBitmap(cropped);


    }


    public void petProfileButton(View view) {
        Intent intent = new Intent(this, PetProfileActivity.class);
        startActivity(intent);

    }

    public void updatepetprofile(View view) {
    }
}
