package com.example.foodcarboncalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ImageView BTNdashboard;
    CardView cardShopping, cardCarbon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                item.setTitle("Home");
                return true;
            } else if (item.getItemId() == R.id.bottom_calendar) {
                startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                item.setTitle("calender");
                return true;
            } else if (item.getItemId() == R.id.bottom_comment) {
                // Handle DestCommunity
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                item.setTitle("Profile");
                return true;
            }
            return false;
        }); BTNdashboard = findViewById(R.id.dashboardImage);
        /*BTNdashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.foodcarboncalculator.MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });*/

        cardShopping = findViewById(R.id.button_discover1);
        cardShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });

        cardCarbon = findViewById(R.id.button_discover2);
        /*cardCarbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CarbonTipsActivity.class);
                startActivity(intent);
            }
        });*/
        // Find your HorizontalScrollView by its ID
        LinearLayout linearLayout = findViewById(R.id.insightLayout);

        // Set OnClickListener to the HorizontalScrollView
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_insights.class);
                startActivity(intent);
            }
        });
    }
}
