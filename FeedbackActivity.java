package com.example.foodcarboncalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Connect with UI Widgets: RateBarFeedback, TVRating, ETFeedback, BtnFeedback
        RatingBar rateBarFeedback = findViewById(R.id.RateBarFeedback);
        TextView tvRating = findViewById(R.id.TVRating);
        EditText etFeedback = findViewById(R.id.ETFeedback);
        Button btnFeedback = findViewById(R.id.BtnFeedback);

        // Find the Spinner view in your layout
        Spinner spinnerReview = findViewById(R.id.SPReview);

        // Create a string array with review options, including the default value
        String[] reviewOptions = {"Content Type (Select an option)", "Food Items", "Recipes", "Meal Plans"};

        // Create an ArrayAdapter
        ArrayAdapter<String> reviewAdapter = new ArrayAdapter<>(
                FeedbackActivity.this, android.R.layout.simple_spinner_item, reviewOptions);

        // Specify the layout to use when the list of choices appears
        reviewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerReview.setAdapter(reviewAdapter);

        // Set the default selection to "Review Type"
        spinnerReview.setSelection(0);

        // The button OnClickListener to save feedback to Firestore
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFeedbackToFirestore(spinnerReview.getSelectedItem().toString(),
                        rateBarFeedback.getRating(), etFeedback.getText().toString());
            }
        });

        // The rating bar OnRatingBarChangeListener to change the rating whenever it is used by the user.
        rateBarFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRating.setText("You have rated " + rating);
            }
        });
    }

    private void saveFeedbackToFirestore(String contentType, float rating, String review) {
        // Get the user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a Map to represent the feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("contentType", contentType);
        feedbackData.put("rating", rating);
        feedbackData.put("review", review);

        // Get a reference to the Firestore collection "feedback"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference feedbackRef = db.collection("feedback").document(userId);

        // Set the feedback data in the Firestore document
        feedbackRef.set(feedbackData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Successfully saved feedback
                            Toast.makeText(FeedbackActivity.this, "Feedback saved to Firestore.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to save feedback
                            Toast.makeText(FeedbackActivity.this, "Error saving feedback.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
