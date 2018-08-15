package com.petsadoption.tal.petsadoption.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.petsadoption.tal.petsadoption.Objects.Pet;
import com.petsadoption.tal.petsadoption.Objects.User;
import com.petsadoption.tal.petsadoption.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PetProfileCreationActivity extends AppCompatActivity {

    private EditText petName,petMoreInfo;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private ImageView pictureLogo;
    CropImageView cropImageView;
    public String uriDownload;
    private TextView petTvAge;
    private TextView petTvChoice;
    private TextView tvPetCity;
    ProgressDialog progressDialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_pet_profile_creation);

        mAuth = FirebaseAuth.getInstance();

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        petTvChoice = (TextView)findViewById(R.id.petTvChoice);
        petName = (EditText) findViewById(R.id.etPetname);
        petTvAge = (TextView)findViewById(R.id.petTvAge);
        petMoreInfo = (EditText) findViewById(R.id.etMoreinfo);
        pictureLogo = (ImageView)findViewById(R.id.pictureLogo);
        tvPetCity = (TextView)findViewById(R.id.tvPetCity);



    }

    public void confirm(View view) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser != null){

            String petNames = petName.getText().toString();
            String petAges = petTvAge.getText().toString();
            final String petTypes = petTvChoice.getText().toString();
            String petMoreDetail = petMoreInfo.getText().toString();
            final String petCity = tvPetCity.getText().toString();

            final Pet pet = new Pet(petNames,petAges,petTypes,petMoreDetail,petCity,uriDownload);

            mDatabase.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String key = firebaseUser.getUid();

                    Map<String,String> maps = (Map<String,String> )dataSnapshot.child("user").child(key).getValue();

                    if (maps == null){

                    }else{
                        String phoneNumber = maps.get("phoneNumber");
                        String fullName = maps.get("fullName");

                        Map<String,Object> childupdate = new HashMap<>();
                        childupdate.put("phoneNumber",phoneNumber);
                        childupdate.put("fullName",fullName);
                        mDatabase.child("Pet").child(firebaseUser.getUid()).updateChildren(childupdate);

                    }

                    Intent intent = new Intent(PetProfileCreationActivity.this,PetProfileActivity.class);
                    startActivity(intent);

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                    mDatabase.child("Pet").child(firebaseUser.getUid()).setValue(pet);
                    mDatabase.child(petTypes).child(firebaseUser.getUid()).setValue(pet);
                    mDatabase.child(petCity).child(firebaseUser.getUid()).setValue(pet);



        }



        Toast.makeText(this, "המידע עודכן בהצלחה", Toast.LENGTH_SHORT).show();


    }

    public void petImageIdCreate(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final StorageReference imageCrop = storageReference.child("images/" + resultUri.toString() );
                imageCrop.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                       Task<Uri> downloadUrl = imageCrop.getDownloadUrl();
                        uriDownload = downloadUrl.toString();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                cropImageView.setImageUriAsync(resultUri);
                pictureLogo.setVisibility(View.INVISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        Bitmap cropped = cropImageView.getCroppedImage();
        cropImageView.setImageBitmap(cropped);

    }

    public void choosePet(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר בעל חיים");
        final String[] petChoice = getResources().getStringArray(R.array.petArray);
        builder.setItems(petChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                petTvChoice.setText(petChoice[i++]);

            }
        });

        builder.create();
        builder.show();
    }

    public void chooseAge(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר גיל(בשנים)");
        final String[] petAgeArray = getResources().getStringArray(R.array.petAge);
        builder.setItems(petAgeArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                petTvAge.setText(petAgeArray[i++]);



            }
        });

        builder.create();
        builder.show();
    }


    public void btnCity(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר עיר");
        final String[] petCity = getResources().getStringArray(R.array.city);
        builder.setItems(petCity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                tvPetCity.setText(petCity[i++]);
            }
        });

        builder.create();
        builder.show();

    }

}









