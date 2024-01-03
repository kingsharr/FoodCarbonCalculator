package com.example.madtest1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout); // This should be your activity's main layout

        header = findViewById(R.id.textViewOnImage);
        header.setText("Rating and Reviews");

        // Inflate the reviews layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View reviewsLayout = inflater.inflate(R.layout.activity_reviews, null);

        // Set up the RecyclerView and the adapter within the reviews layout
        RecyclerView reviewsRecyclerView = reviewsLayout.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Assuming you have a ReviewsAdapter and dummy review data
        List<Review> dummyReviews = getDummyReviews();
        ReviewsAdapter adapter = new ReviewsAdapter(dummyReviews);
        reviewsRecyclerView.setAdapter(adapter);

        // Find the container in the page_layout
        ViewGroup container = findViewById(R.id.midContent); // Replace with the actual ID of your container

        // Add the reviews layout to the container
        container.addView(reviewsLayout);

        // Find and set up the "Write a Review" button
        Button writeReviewButton = reviewsLayout.findViewById(R.id.writeReviewButton);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start RatingDisplay Activity
                Intent intent = new Intent(ReviewsActivity.this, RatingDisplay.class);
                startActivity(intent);
            }
        });
    }

    private List<Review> getDummyReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Alice", "Loved the atmosphere!", 4.5f));
        reviews.add(new Review("Bob", "Great service, will come again.", 5.0f));
        reviews.add(new Review("Charlie", "Food was a bit bland.", 3.0f));
        // Add more dummy data as needed
        return reviews;
    }
}
