package com.example.firebaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.mainRecycler);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startExamplesActivity();
            }
        });

//       if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//           startLoginActivity();
//       } else{
//           FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
//                   .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() { // a fost inlocuit cu onAuthStateChanged
//               @Override
//               public void onSuccess(GetTokenResult getTokenResult) {
//                   Log.d(TAG, "onSuccess: " +getTokenResult.getToken());
//               }
//           });
//       }
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginRegisterActivity.class));
        finish();
    }

    private void startExamplesActivity() {
        Intent intent = new Intent(this, ExamplesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logOut:
                Toast.makeText(this, "Log-out", Toast.LENGTH_SHORT).show();
                AuthUI.getInstance().signOut(this);
 //                       .addOnCompleteListener(new OnCompleteListener<Void>() {
 //                           @Override
 //                           public void onComplete(@NonNull Task<Void> task) {
 //                               if (task.isSuccessful()) {
 //                                   starLoginActivity();
 //                               } else {
 //                                   Log.e(TAG, "onComplete: ", task.getException());
 //                               }
 //                           }
 //                       });
  // ori asta             FirebaseAuth.getInstance().signOut();
                return true;

            case R.id.action_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startLoginActivity();
            return;
        }
    }
}