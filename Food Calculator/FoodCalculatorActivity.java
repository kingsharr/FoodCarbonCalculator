package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;

public class FoodCalculatorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;
    private TextView carbonValueTextView;
    Button findOutBtn;
    Spinner spinner1, spinner2;

    private Map<String, Double> foodCarbonMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_calculator);

        if (FirebaseApp.getApps(this).isEmpty()){
            FirebaseApp.initializeApp(this);
        }

        carbonValueTextView = findViewById(R.id.carbonValueTextView);
        findOutBtn = findViewById(R.id.find_out_button);
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_calculator);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.bottom_calendar) {
                startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.bottom_calculator) {
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

        foodCarbonMap.put("Nasi Lemak", 40.0);
        foodCarbonMap.put("Nasi Briyani", 54.0);
        foodCarbonMap.put("Nasi Lemak", 500.0);
        foodCarbonMap.put("Nasi Briyani", 600.0);
        foodCarbonMap.put("Nasi Kerabu", 400.0);
        foodCarbonMap.put("Chicken Rice", 700.0);
        foodCarbonMap.put("Banana Leaf Rice", 600.0);
        foodCarbonMap.put("Nasi Goreng", 400.0);
        foodCarbonMap.put("Mee goreng", 400.0);
        foodCarbonMap.put("Char Kway Teow", 500.0);
        foodCarbonMap.put("Maggi", 100.0);
        foodCarbonMap.put("Roti Canai", 300.0);
        foodCarbonMap.put("Roti Jala", 300.0);
        foodCarbonMap.put("Tose", 100.0);
        foodCarbonMap.put("Chapati", 150.0);
        foodCarbonMap.put("Kaya Toast", 150.0);
        foodCarbonMap.put("Curry Laksa", 500.0);
        foodCarbonMap.put("Yong Tau Foo", 200.0);
        foodCarbonMap.put("Chicken Chop", 700.0);
        foodCarbonMap.put("Pizza", 800.0);
        foodCarbonMap.put("Ramly Burger", 400.0);
        foodCarbonMap.put("Murtabak", 400.0);
        foodCarbonMap.put("Satay", 300.0);
        foodCarbonMap.put("Apam Balik", 200.0);
        foodCarbonMap.put("Rojak", 300.0);
        foodCarbonMap.put("Pisang Goreng", 200.0);
        foodCarbonMap.put("Ikan Bakar", 600.0);

        findOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFood = spinner1.getSelectedItem().toString();
                String selectedConsumption = spinner2.getSelectedItem().toString();

                if (selectedFood.equals("Select Food") || selectedConsumption.equals("Select Frequency")) {
                    Toast.makeText(FoodCalculatorActivity.this, "Please select both food type and frequency", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform calculation based on selected values and display the result
                    double carbonValue = calculateCarbonFootprint(selectedFood, selectedConsumption);
                    updateCarbonValueTextView(carbonValue);
                }
            }
        });
    }

    private double calculateCarbonFootprint(String foodType, String frequency) {
        // Retrieve the carbon value for the selected food type
        double baseCarbonValue = foodCarbonMap.get(foodType);

        // Add your logic to adjust the base carbon value based on frequency
        double frequencyMultiplier;
        switch (frequency) {
            case "less quantity":
                frequencyMultiplier = 0.5;
                break;
            case "medium quantity":
                frequencyMultiplier = 1.0;
                break;
            case "more quantity":
                frequencyMultiplier = 2.0;
                break;
            default:
                frequencyMultiplier = 1.0;
        }

        return baseCarbonValue * frequencyMultiplier;
    }

    private void updateCarbonValueTextView(double carbonValue){
        carbonValueTextView.setText("Carbon Value: " + carbonValue + "CO2e");
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