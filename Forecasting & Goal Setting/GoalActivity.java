package com.example.foodcarboncalculator;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoalActivity extends AppCompatActivity{

    TextView addGoal;
    Dialog popAdd;
    RecyclerView recyclerView;
    EditText editGoal;
    Button btnGoal;
    QuantityAdapter adapter;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        arrayList.clear();
        popAdd = new Dialog(this);
        popUp();
        recyclerView = findViewById(R.id.goalRecyclerView);
        adapter = new QuantityAdapter(this,arrayList);
        fetchGoals();
        setRecyclerView();

        addGoal = findViewById(R.id.btnSetGoal);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAdd.show();
            }
        });
    }

    private void popUp(){
        popAdd.setContentView(R.layout.popup_goal);
        popAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAdd.getWindow().getAttributes().gravity = Gravity.CENTER;

        btnGoal = popAdd.findViewById(R.id.btnGoal);
        editGoal = popAdd.findViewById(R.id.editGoal);

        //add goal
        btnGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(editGoal.getText());
                saveUserInfoToFirestore(text);
                arrayList.add(text);
                setRecyclerView();
                popAdd.dismiss();
            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new QuantityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String deleteData = arrayList.get(position).toString();
                //delete goal
                arrayList.remove(position);
                //notify
                adapter.notifyItemRemoved(position);
                removeFromFirestore(deleteData);
            }
        });

    }

    private void removeFromFirestore(String deleteData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the "user_goals" subcollection for the user
        CollectionReference userGoalsCollection = db.collection("goals").document(userId).collection("user_goals");

        userGoalsCollection
                .whereEqualTo("Goal", deleteData) // Filter documents based on the goal value
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the reference to the document and delete it
                                userGoalsCollection.document(document.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Document successfully deleted
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void saveUserInfoToFirestore(String text) {
        // Get the user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get goal
        String goal = text;

        // Create a Map to represent the user goal
        Map<String, Object> userGoal = new HashMap<>();
        userGoal.put("Goal", goal);

        // Get a reference to the Firestore collection "goals"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference goalsCollection = db.collection("goals");

        // Create a new document in the subcollection "user_goals" for the user
        DocumentReference userGoalDoc = goalsCollection.document(userId).collection("user_goals").document();

        // Add the goal data to the document
        userGoalDoc.set(userGoal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Goal successfully written to Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing goal to Firestore", e);
                    }
                });
    }

    private void fetchGoals(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the "user_goals" subcollection for the user
        CollectionReference userGoalsCollection = db.collection("goals").document(userId).collection("user_goals");

        // Query to get all documents in the "user_goals" subcollection
        userGoalsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Iterate through the documents and convert them to Goal objects
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String goalValue = document.getString("Goal");

                        if(goalValue != null){
                            arrayList.add(goalValue);
                        }
                    }

                    // Update the RecyclerView adapter with the retrieved goals
                    adapter.updateData(arrayList);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

}