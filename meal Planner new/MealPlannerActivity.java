package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MealPlannerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private CalendarView calendarView;
    private RadioGroup rgbtn;
    private Spinner spinnerFood;
    private Spinner spinnerDrink;
    private String stringDateSelected;
    private DatabaseReference databaseReference;
    //private FirebaseDatabase database;
    private Button saveBtn, detailBtn;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);

        calendarView = findViewById(R.id.calendarView);
        rgbtn = findViewById(R.id.RGTimeEat);
        saveBtn = findViewById(R.id.save_button);
        detailBtn = findViewById(R.id.button4);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("calendar");
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //databaseReference = database.getReference().child("calendar");

        spinnerFood = findViewById(R.id.spinnerFood);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFood.setAdapter(adapter1);
        spinnerFood.setOnItemSelectedListener(this);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(dayOfMonth);
                //calendarClicked();
            }
        });

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealPlannerActivity.this, MealDetail.class);
                intent.putExtra("Date", stringDateSelected);
                startActivity(intent);
                finish();
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
                String selectedFood = spinnerFood.getSelectedItem().toString();

                saveDataToFirebase(selectedRadioButtonText, selectedFood);
                //Toast.makeText(MealPlannerActivity.this, "value Stored Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDataToFirebase(String timeEat, String food) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Reference the specific document containing meal details for the selected date
        DocumentReference mealPlanRef = db.collection("MealPlans")
                .document(userId)
                .collection("UserMeals") // Subcollection for each user
                .document(stringDateSelected); // Document ID is the selected date

// Add data to the meal plan document without overwriting existing content
        Map<String, Object> mealPlanData = new HashMap<>();
        if (timeEat.equals("Breakfast")) {
            mealPlanData.put("breakfast", food);
        } else if (timeEat.equals("Lunch")) {
            mealPlanData.put("lunch", food);
        } else {
            mealPlanData.put("dinner", food);
        }

// Update the document in Firestore with the new meal data
        mealPlanRef.update(mealPlanData)
                .addOnSuccessListener(aVoid -> {
                    // Data has been successfully added
                    Toast.makeText(this, "Meal plan saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("Firestore", "Error updating document", e);
                });

    }

    // Method to create meal data as a map

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

}