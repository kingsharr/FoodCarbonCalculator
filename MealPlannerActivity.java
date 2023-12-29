package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MealPlannerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private CalendarView calendarView;
    private RadioGroup rgbtn;
    private Spinner spinner1;
    private Spinner spinner2;
    private String stringDateSelected;
    private DatabaseReference databaseReference;
    //private FirebaseDatabase database;
    private Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);

        calendarView = findViewById(R.id.calendarView);
        rgbtn = findViewById(R.id.RGTimeEat);
        saveBtn = findViewById(R.id.save_button);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("calendar");
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //databaseReference = database.getReference().child("calendar");

        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.drink, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                stringDateSelected = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(dayOfMonth);
                //calendarClicked();
            }
        });

        //databaseReference = FirebaseDatabase.getInstance().getReference("calendar");
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
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
                String selectedDrink = spinner2.getSelectedItem().toString();

                saveDataToFirebase(selectedRadioButtonText, selectedFood, selectedDrink);
                //Toast.makeText(MealPlannerActivity.this, "value Stored Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDataToFirebase(String timeEat, String food, String drink){
        DatabaseReference dataRef = databaseReference.child(stringDateSelected).push();

        MealPlan mealPlan = new MealPlan(dataRef.getKey(), timeEat, food, drink);

        dataRef.setValue(mealPlan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MealPlannerActivity.this, "Meal Plan saved successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MealPlannerActivity.this, "Failed to save meal plan. Please try again", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseDebug", "Error saving data", e);
            }
        });
        /*
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Time Eat", timeEat);
        dataMap.put("Food", food);
        dataMap.put("Drink", drink);

        dataRef.setValue(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FirebaseDebug", "Data saved successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseDebug", "Error saving data", e);
            }
        });

        //dataRef.child("Time Eat").setValue(timeEat);
        //dataRef.child("Food").setValue(food);
        //dataRef.child("Drink").setValue(drink);

    */
    }

    /*public void buttonSaveEvent(View view){
        databaseReference.child(stringDateSelected).setValue(rgbtn.toString());
        databaseReference.child(stringDateSelected).setValue(spinner1.toString());
        databaseReference.child(stringDateSelected).setValue(spinner2.toString());
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

}