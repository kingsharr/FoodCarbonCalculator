package com.example.foodcarboncalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.DestHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.DestHome) {
                // Handle DestHome
                return true;
            } else if (item.getItemId() == R.id.DestCalender) {
                startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.DestCommunity) {
                // Handle DestCommunity
                return true;
            } else if (item.getItemId() == R.id.DestProfile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });

    }

}
