
package com.example.madtest1;

import static com.example.madtest1.R.id.reviewsRecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    private TextView header;
    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout); // Replace with your actual layout file

        header = findViewById(R.id.textViewOnImage);
        header.setText("Rating and Reviews");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.DestCalender);
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


        // Inflate the reviews layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View reviewsLayout = inflater.inflate(R.layout.activity_reviews, null);

        /// Set up the RecyclerView and the adapter within the reviews layout
        RecyclerView reviewsRecyclerView = reviewsLayout.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty list
        adapter = new ReviewsAdapter(new ArrayList<>());
        reviewsRecyclerView.setAdapter(adapter);

        // Find the container in the page_layout
        ViewGroup container = findViewById(R.id.midContent); // Replace with the actual ID of your container
        // Add the reviews layout to the container
        container.addView(reviewsLayout);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        fetchReviews(); // Fetch reviews from Firestore


        // Find and set up the "Write a Review" button
        Button writeReviewButton = reviewsLayout.findViewById(R.id.writeReviewButton); // Make sure this ID exists in your layout
        writeReviewButton.setOnClickListener(v -> {
            // Create an Intent to start the activity where users can write a review
            Intent intent = new Intent(ReviewsActivity.this, RatingDisplay.class); // Replace with your actual class
            startActivity(intent);
        });
    }

    private void fetchReviews() {
        firestore.collection("Reviews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Review> fetchedReviews = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Review review = document.toObject(Review.class);
                            fetchedReviews.add(review);
                            // Log the fetched review
                            Log.d("ReviewFetch", "Fetched review: " + review.getName() + ", " +
                                    review.getFeedback() + ", Rating: " + review.getRatingValue());
                        }
                        // Update the adapter's data
                        adapter.updateReviews(fetchedReviews);
                    } else {
                        // Log the error
                        Log.w("ReviewFetch", "Error getting documents.", task.getException());
                    }
                });
    }

}


