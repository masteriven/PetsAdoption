package com.petsadoption.tal.petsadoption.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petsadoption.tal.petsadoption.Objects.User;
import com.petsadoption.tal.petsadoption.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "AddToDataBase";
    private EditText mMail,mPassword,fullName,dateOfBirth,phoneNumber;
    private Button btnCity;
    FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String uriDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFireBaseDatabase.getReference().child("user");
        final FirebaseUser user = mAuth.getCurrentUser();

        mMail = (EditText) findViewById(R.id.etName);
        mPassword = (EditText) findViewById(R.id.etPassword);
        fullName = (EditText)findViewById(R.id.etFullName);
        dateOfBirth = (EditText)findViewById(R.id.etDateOfBirth);
        phoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        btnCity = (Button)findViewById(R.id.btCity);

        final AlertDialog.Builder cityDialog = new AlertDialog.Builder(this);


        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityDialog.setTitle("בחר עיר");
                final String[] city = getResources().getStringArray(R.array.city);
                cityDialog.setItems(city, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        btnCity.setText(city[i++]);
                    }
                });

                cityDialog.create();
                cityDialog.show();
            }
        });
    }

    public void register(View view) {

        String userName = mMail.getText().toString().trim();
        String userPassword = mPassword.getText().toString().trim();
        String fullNames = fullName.getText().toString().trim();
        String city = btnCity.getText().toString();
        String dateOfBirths = dateOfBirth.getText().toString().trim();
        String phoneNumbers = phoneNumber.getText().toString().trim();



        final User user = new User(userName,userPassword,fullNames,city,dateOfBirths,phoneNumbers,uriDownload);


        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPassword)&&
                !TextUtils.isEmpty(fullNames)&& !TextUtils.isEmpty(city)&& !TextUtils.isEmpty(dateOfBirths)&&!TextUtils.isEmpty(phoneNumbers)){


            mAuth.createUserWithEmailAndPassword(user.getUserName(),user.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String userId = mAuth.getCurrentUser().getUid();

                        DatabaseReference databaseReference = mRef.child(userId);

                        databaseReference.child("userName").setValue(user.getUserName());
                        databaseReference.child("password").setValue(user.getUserPassword());
                        databaseReference.child("fullName").setValue(user.getFullName());
                        databaseReference.child("city").setValue(user.getCity());
                        databaseReference.child("dateOfBirth").setValue(user.getDateOfBirth());
                        databaseReference.child("phoneNumber").setValue(user.getPhoneNumber());
                        databaseReference.child("uriDownload").setValue(user.getProfileImageKey());

                        Toast.makeText(RegisterActivity.this,"Singnup secesfuly",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(RegisterActivity.this,"Singnup is not secesfuly",Toast.LENGTH_LONG).show();

                    }
                }
            });
            mMail.setText("");
            mPassword.setText("");
            fullName.setText("");
            dateOfBirth.setText("");
            phoneNumber.setText("");



        }else{
            Toast.makeText(this,"fill out all fields",Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

}
