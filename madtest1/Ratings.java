package com.example.madtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Ratings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);

//        // Inflate the design you want to add
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View viewDesign = inflater.inflate(R.layout.activity_rating_design, null);
//
////        Intent intent = new Intent(this, RatingDesign.class);
////        startActivity(intent);
//
//        // Find the container in the activity_ratings layout
//        ViewGroup fragmentContainer = findViewById(R.id.midContent);
//
//        if (fragmentContainer != null && savedInstanceState == null) {
//            // Clear any existing views in the container
//            fragmentContainer.removeAllViews();
//
//            // Add viewDesign to the container
//            fragmentContainer.addView(viewDesign);
//        }

        if (savedInstanceState == null) {
            RatingDesign ratingDesignFragment = new RatingDesign();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.midContent, ratingDesignFragment);
            transaction.commit();
        }

    }
}
