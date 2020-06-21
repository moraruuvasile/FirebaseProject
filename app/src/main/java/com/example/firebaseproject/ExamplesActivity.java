package com.example.firebaseproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamplesActivity extends AppCompatActivity {
    private static final String TAG = "ExamplesActivity";
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void createDocument(View view) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "a");
        map.put("price", 1);
        map.put("isAvailable", true);
        map.put("time", new Timestamp(new Date()));

        firestoreDB.collection("note")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: task was succesfull");
                        Log.d(TAG, "onSuccess: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess: task was NOT succesfull");
                    }
                });

    }

    public void readDocument(View view) {
        Task</*QuerySnapshot*/DocumentSnapshot > note = firestoreDB.collection("note")
                               .document("NqH8OQOdefXq0pcvraqm")
 //               .whereLessThan("price", 6)
                .get()
//               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                   @Override
//                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                       Log.d(TAG, "onSuccess: QuerySnapshot");
//                       List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
//                       for (DocumentSnapshot s : snapshots) {
//                           Log.d(TAG, "onSuccess: " + s.getData());
//                       }
//                   }
//               })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess: " + documentSnapshot.getData());
                        Log.d(TAG, "onSuccess: " + documentSnapshot.getBoolean("isAvailable"));
 //                       Product product = documentSnapshot.toObject(Product.class);
 //                       Log.d(TAG, "onSuccess: " + product);
                    }
                });


    }

    public void getAllDocumentsWithRealtimeUpdates(View view) {
                FirebaseFirestore.getInstance()
               .collection("note")
               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                       if (e != null) {
                           Log.e(TAG, "onEvent: ", e);
                           return;
                       }
                       if (queryDocumentSnapshots != null) {
                           Log.d(TAG, "onEvent: ---------------------------");
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                Log.d(TAG, "onEvent: " + snapshot.getData());
                            }

                           List<DocumentChange> documentChangeList = queryDocumentSnapshots.getDocumentChanges();
                           for (DocumentChange documentChange: documentChangeList) {
                               Log.d(TAG, "onEvent: onEvent: " + documentChange.getDocument().getData());
                           }
                       } else {
                           Log.e(TAG, "onEvent: query snapshot was null");
                       }
                   }
               });

    }

    public void updateDocument(View view) {
    }

    public void getAllDocuments(View view) {
    }

    public void deleteDocument(View view) {
    }
}