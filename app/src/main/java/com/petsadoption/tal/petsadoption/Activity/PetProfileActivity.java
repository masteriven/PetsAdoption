package com.petsadoption.tal.petsadoption.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsadoption.tal.petsadoption.Objects.Pet;
import com.petsadoption.tal.petsadoption.R;

import java.util.ArrayList;
import java.util.Map;

public class PetProfileActivity extends AppCompatActivity {

    TextView petName,petAge,petType,petMoreInfo;
    FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private ImageView petImageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile_acitvity);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        petName = (TextView) findViewById(R.id.tvPetname);
        petAge = (TextView) findViewById(R.id.tvPetage);
        petType = (TextView) findViewById(R.id.tvPetype);
        petMoreInfo = (TextView) findViewById(R.id.tvPetmoreinfo);
        petImageView = (ImageView)findViewById(R.id.petImageView);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (firebaseUser != null){

                    String key = firebaseUser.getUid();

                    Map<String ,String> map = (Map<String, String>) dataSnapshot.child("Pet").child(key).getValue();


                    if(map == null) {


                    }else{

                        String petAges = map.get("age");
                        String petNames = map.get("name");
                        String petMoreInfos = map.get("petMoreInfo");
                        String petTypes = map.get("type");
                        String uriKey = map.get("uriKey");

                        petName.setText(petNames);
                       petAge.setText(petAges);
                        petType.setText(petTypes);
                        petMoreInfo.setText(petMoreInfos);

                        Glide.with(getApplicationContext()).load(uriKey).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                petImageView.setImageBitmap(resource);
                            }
                        });


                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void updatepetprofile(View view) {


        Intent intent = new Intent(this, PetProfileCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
