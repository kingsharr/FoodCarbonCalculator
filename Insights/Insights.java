package com.example.foodcarboncalculator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Insights extends AppCompatActivity {

    TextView insight1;
    TextView insight2;
    TextView insight3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        insight1 = findViewById(R.id.txtInsight1);
        insight2 = findViewById(R.id.txtInsight2);
        insight3 = findViewById(R.id.txtInsight3);

        setInsightText();
    }

    private void setInsightText() {
        double emission = 0;
        int tree = 0;
        int energy = 0;
        insight1.setText(emission + "kg C02");
        insight2.setText("Saving" + tree + " trees, equivalent to their annual CO2 absorption");
        insight3.setText("Saving" + energy + "kWh of energy");
    }
}