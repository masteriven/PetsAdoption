package com.petsadoption.tal.petsadoption.Objects;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

/**
 * Created by Tal on 20/11/2017.
 */

public class FireBaseInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
    }
}
