package com.petsadoption.tal.petsadoption.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.petsadoption.tal.petsadoption.Objects.User;
import com.petsadoption.tal.petsadoption.R;

public class SignInActivity extends AppCompatActivity {

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String userId;
    ProgressDialog progressBar;

    private EditText etUserName,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseApp.initializeApp(this);

        progressBar = new ProgressDialog(this,R.style.ProgressBarTheme);


        mFireBaseDatabase = FirebaseDatabase.getInstance();

        etUserName = (EditText)findViewById(R.id.etUserName);
        etPassword = (EditText)findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userAuth = firebaseAuth.getCurrentUser();

              if (userAuth != null){


              }else{

              }

            }
        };
    }

    public void register(View view) {


        Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
        startActivity(intent);
    }



    public void signIn(View view) {

        progressBar.setProgress(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        String userName = etUserName.getText().toString();
        String userPassword = etPassword.getText().toString();

        User user = new User(userName,userPassword,"","","","","");

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)){

            Toast.makeText(this,"לא מילתה את כל השדות",Toast.LENGTH_SHORT).show();

        }else{
            mAuth.signInWithEmailAndPassword(user.getUserName(),user.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignInActivity.this,"התחברת בהצלחה",Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();

                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(SignInActivity.this,"ההתחברות נכשלה",Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
