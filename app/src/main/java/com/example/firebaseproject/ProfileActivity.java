package com.example.firebaseproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextInputEditText displayNameET;
    Button updateProfileBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circleImageView = findViewById(R.id.circleImageView);
        displayNameET = findViewById(R.id.displayName_EdT);
        updateProfileBtn = findViewById(R.id.updateProfile_Btn);
        progressBar = findViewById(R.id.progressBar);
    }

    public void updateProfile_Btn(View view) {
    }

    public void imageClick_Img(View view) {
    }
}