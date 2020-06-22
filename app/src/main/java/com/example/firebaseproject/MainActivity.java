package com.example.firebaseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, NotesRecyclerAdapter.NoteListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    NotesRecyclerAdapter notesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.mainRecycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
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
    private void showAlertDialog() {
        final EditText noteEditText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(noteEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: " + noteEditText.getText());
                        addNote(noteEditText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void addNote(String text) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Note note = new Note(text, false, new Timestamp(new Date()), userId);

        FirebaseFirestore.getInstance()
                .collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Succesfully added the note...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
        if (notesRecyclerAdapter != null) {
            notesRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startLoginActivity();
            return;
        }
        initRecyclerView(firebaseAuth.getCurrentUser());
    }

    private void initRecyclerView(FirebaseUser user) {

        Query query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userId", user.getUid())
                .orderBy("completed", Query.Direction.ASCENDING)
                .orderBy("created", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        notesRecyclerAdapter = new NotesRecyclerAdapter(options, this);
        recyclerView.setAdapter(notesRecyclerAdapter);
        notesRecyclerAdapter.startListening();

 //       ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
 //       itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public void handleCheckChanged(boolean isChecked, DocumentSnapshot snapshot) {

    }

    @Override
    public void handleEditNote(DocumentSnapshot snapshot) {

    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {

    }
}