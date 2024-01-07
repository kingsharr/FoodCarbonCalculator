package com.example.madtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsDisplay extends AppCompatActivity {

    private TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);

        header = findViewById(R.id.textViewOnImage);
        header.setText("Article & Resources");
        //header.setTextSize();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.DestCommunity);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.DestHome) {

                return true;
            } else if (itemId == R.id.DestCalender) {
                startActivity(new Intent(getApplicationContext(), ReviewsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.DestCalculator) {
                // Handle DestCalculator
                return true;
            } else if (itemId == R.id.DestCommunity) {
                startActivity(new Intent(getApplicationContext(), CommentDisplay.class));
                finish();
                return true;
            } else if (itemId == R.id.DestProfile) {
                startActivity(new Intent(getApplicationContext(), DashboardDisplay.class));
                finish();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            // Create an instance of the RatingDesign fragment
            NewsDesign newsDesignFragment = new NewsDesign();

            // Begin a fragment transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace the contents of the container with the new fragment
            transaction.replace(R.id.midContent, newsDesignFragment);

            // Complete the changes added above
            transaction.commit();
        }


    }
}
