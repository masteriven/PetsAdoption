package com.petsadoption.tal.petsadoption.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsadoption.tal.petsadoption.Adapter.NewPetsAdapter;
import com.petsadoption.tal.petsadoption.Fragment.NewPetsFragment;
import com.petsadoption.tal.petsadoption.Objects.Pet;
import com.petsadoption.tal.petsadoption.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetContentActivity extends AppCompatActivity {

    private TextView petType;
    private TextView petName;
    private TextView petAge;
    private TextView petCity;
    private DatabaseReference mDatabase;
    private TextView petInfo;
    private TextView userName;
    private ImageView petImage;
    String phoneNumbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_content);

        petType = (TextView)findViewById(R.id.petType);
        petName = (TextView)findViewById(R.id.petName);
        petAge = (TextView)findViewById(R.id.petAge);
        petCity = (TextView)findViewById(R.id.petCity);
        petInfo = (TextView)findViewById(R.id.petInfo);
        petImage = (ImageView)findViewById(R.id.petImage);
        userName = (TextView) findViewById(R.id.userName);



        Intent intent = getIntent();

        String type = intent.getStringExtra("type");
        String name = intent.getStringExtra("name");
        String age = intent.getStringExtra("age");
        String city = intent.getStringExtra("city");
        String info = intent.getStringExtra("info");
        String image = intent.getStringExtra("image");
        String userNames = intent.getStringExtra("fullName");
       phoneNumbers = intent.getStringExtra("phoneNumber");




        petType.setText(type);
        petName.setText(name);
        petAge.setText(age);
         petCity.setText(city);
        petInfo.setText(info);
        userName.setText(userNames);

        Glide.with(getApplicationContext()).load(image).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                petImage.setImageBitmap(resource);
            }
        });



    }
    public void btnPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumbers));
        startActivity(intent);
    }

    public void whatsappButton(View view) {
        Uri uri = Uri.parse("smsto:" + phoneNumbers);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }
}
