package com.example.madtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RatingDisplay extends AppCompatActivity {

    private TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);

        header = findViewById(R.id.textViewOnImage);
        header.setText("Rating and Reviews");
        //header.setTextSize();


        if (savedInstanceState == null) {
            // Create an instance of the RatingDesign fragment
            RatingDesign ratingDesignFragment = new RatingDesign();

            // Begin a fragment transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace the contents of the container with the new fragment
            transaction.replace(R.id.midContent, ratingDesignFragment);

            // Complete the changes added above
            transaction.commit();
        }

        // Inflate the design you want to add
        //LayoutInflater inflater = LayoutInflater.from(this);
        //View viewDesign = inflater.inflate(R.layout.activity_rating_design, null);

        // Find the container in the activity_ratings layout
        //ViewGroup fragmentContainer = findViewById(R.id.midContent);

//        if (fragmentContainer != null && savedInstanceState == null) {
//            // Clear any existing views in the container
//            fragmentContainer.removeAllViews();
//
//            // Add viewDesign to the container
//            fragmentContainer.addView(viewDesign);
//        }

    }
}
