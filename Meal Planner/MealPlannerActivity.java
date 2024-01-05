package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class MealPlannerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private CalendarView calendarView;
    private RadioGroup rgbtn;
    private Spinner spinner1;
    private Spinner spinner2;
    private String stringDateSelected;
    private DatabaseReference databaseReference;
    private Button saveBtn;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_calendar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.bottom_calendar) {
                return true;
            } else if (id == R.id.bottom_calculator) {
                startActivity(new Intent(getApplicationContext(), FoodCalculatorActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.bottom_comment) {
                startActivity(new Intent(getApplicationContext(), CommentActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else {
                return false;
            }
        });

        calendarView = findViewById(R.id.calendarView);
        rgbtn = findViewById(R.id.RGTimeEat);
        saveBtn = findViewById(R.id.save_button);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("calendar");

        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.how_many_times, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(dayOfMonth);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = rgbtn.getCheckedRadioButtonId();
                if (selectedRadioButtonId == -1){
                    Toast.makeText(MealPlannerActivity.this, "Please select a time to eat", Toast.LENGTH_SHORT).show();
                }
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String selectedRadioButtonText = selectedRadioButton.getText().toString();
                String selectedFood = spinner1.getSelectedItem().toString();
                String selectedQuantity = spinner2.getSelectedItem().toString();

                saveDataToFirebase(selectedRadioButtonText, selectedFood, selectedQuantity);
                Toast.makeText(MealPlannerActivity.this, "value Stored Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDataToFirebase(String timeEat, String food, String quantity) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the "MealPlans" collection
        CollectionReference mealPlansRef = db.collection("MealPlans");

        // Create a new document with a unique ID
        DocumentReference newMealPlanRef = mealPlansRef.document();

        // Add data to the meal plan document
        Map<String, Object> mealPlanData = new HashMap<>();
        mealPlanData.put("UserID", userId);
        mealPlanData.put("Date", stringDateSelected);
        mealPlanData.put(timeEat, createMealData("Breakfast", food, quantity));

        // Set the data to Firestore
        newMealPlanRef.set(mealPlanData)
                .addOnSuccessListener(aVoid -> {
                    // Data has been successfully added
                    Toast.makeText(this, "Meal plan saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("Firestore", "Error adding document", e);
                });
    }

    // Method to create meal data as a map
    private Map<String, Object> createMealData(String mealType, String mealName, String quantityName) {
        Map<String, Object> mealData = new HashMap<>();
        mealData.put("meal", mealName);
        mealData.put("quantity", quantityName);
        // Add other meal details as needed
        return mealData;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

}