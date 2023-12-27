package com.example.foodcarboncalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // Find the "NEXT" button in your layout
        Button nextSuccessButton = findViewById(R.id.nextsuccess_button);

        // Set the OnClickListener for the "NEXT" button
        nextSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the FeedbackActivity when the button is clicked
                Intent intent = new Intent(SuccessActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }
}
