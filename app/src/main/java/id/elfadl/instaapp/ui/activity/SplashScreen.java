package id.elfadl.instaapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.elfadl.instaapp.application.MainApplication;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainApplication.getFromCache("auth_key") != null ? (MainApplication.getFromCache("profile") != null ? MainActivity.class : CompleteProfile.class) : SignIn.class));
            finish();
        }, 2000);
    }
}
