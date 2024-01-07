package com.example.madtest1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ForumDisplay extends AppCompatActivity {

    private TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);

        header = findViewById(R.id.textViewOnImage);
        header.setText("Forum & Discussion");
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
                startActivity(new Intent(getApplicationContext(), CommentDisplay.class));
                finish();
                return true;
            } else if (itemId == R.id.DestCommunity) {
                startActivity(new Intent(getApplicationContext(), ForumDisplay.class));
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
            ForumDesign forumDesignFragment = new ForumDesign();

            // Begin a fragment transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace the contents of the container with the new fragment
            transaction.replace(R.id.midContent, forumDesignFragment);

            // Complete the changes added above
            transaction.commit();
        }


    }
}
