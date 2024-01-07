package com.example.foodcarboncalculator;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainPageActivity extends AppCompatActivity {
    TextView insight1;
    TextView insight2;
    TextView insight3;

    ImageView BTNdashboard;
    CardView cardShopping, cardCarbon, checkGoal;
    FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile); // Set profile as selected
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                item.setTitle("Home");
                return true;
            } else if (item.getItemId() == R.id.bottom_calendar) {
                startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                item.setTitle("Calender");
                return true;
            } else if (item.getItemId() == R.id.bottom_calculator) {
                startActivity(new Intent(getApplicationContext(), FoodCalculatorActivity.class));
                item.setTitle("Calculator");
                return true;
            } else if (item.getItemId() == R.id.bottom_comment) {
                startActivity(new Intent(getApplicationContext(), CommentActivity.class));
                item.setTitle("Profile");
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                item.setTitle("Profile");
                return true;
            }
            return false;
        });

            /*BTNdashboard = findViewById(R.id.dashboardImage);
            BTNdashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainPageActivity.this, AnalyticActivity.class);
                    startActivity(intent);
                }
            });*/

            cardShopping = findViewById(R.id.button_discover1);
            cardShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainPageActivity.this, ShoppingListActivity.class);
                    startActivity(intent);
                }
            });

            cardCarbon = findViewById(R.id.button_discover2);
            cardCarbon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainPageActivity.this, CarbonTipsActivity.class);
                    startActivity(intent);
                }
            });

            checkGoal = findViewById(R.id.addGoal);
            checkGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainPageActivity.this, GoalActivity.class);
                    startActivity(intent);
                }
            });

            insight1 = findViewById(R.id.txtInsight1);
            insight2 = findViewById(R.id.txtInsight2);
            insight3 = findViewById(R.id.txtInsight3);
            setInsightText();
    }

    private void setInsightText() {

        //get food analysis data from database
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the emission collection
        CollectionReference foodPlansRef = db.collection("valueCarbon");

        // Query to get all documents in the "user_goals" subcollection
        foodPlansRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    double emission = 0;
                    int tree = 0;
                    int energy = 0;
                    // Iterate through the documents
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        double val = document.getDouble("CarbonValue");
                        tree += (int) val/100;
                        Log.w(TAG, "emission after tree: " + emission, task.getException());
                        energy += (int) val/150;
                        Log.w(TAG, "emission after carbon: " + emission, task.getException());
                        emission += val;
                        Log.w(TAG, "emission: " + emission, task.getException());

                    }
                    insight1.setText(emission + "g C02");
                    insight2.setText("Saving " + tree + " trees, equivalent to their annual CO2 absorption");
                    insight3.setText("Saving " + energy + "Wh of energy");

                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

    }
}