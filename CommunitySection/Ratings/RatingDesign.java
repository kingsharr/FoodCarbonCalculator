
package com.example.madtest1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RatingDesign extends Fragment {

    private RatingBar ratingBar;
    private EditText feedbackEditText;
    private EditText nameEditText; // EditText for the user's name
    private Button submitFeedbackButton;
    private Spinner categorySpinner;
    private FirebaseFirestore firestore; // Firestore database reference

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_rating_design, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        categorySpinner = view.findViewById(R.id.spinner);
        ratingBar = view.findViewById(R.id.ratingBar);
        feedbackEditText = view.findViewById(R.id.feedbackEditText);
        //nameEditText = view.findViewById(R.id.nameEditText); // Add the ID for your name EditText
        submitFeedbackButton = view.findViewById(R.id.submitFeedbackButton);

        // Example category items
        String[] categories = {"Select the option","Food Quality", "Service", "Cleanliness", "Ambiance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);

        // Set up the button click listener
        submitFeedbackButton.setOnClickListener(v -> {
            // Get the values from the user input
            // String name = nameEditText.getText().toString();
            String name = "UserID01"; // example of userID
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            float ratingValue = ratingBar.getRating();
            String feedback = feedbackEditText.getText().toString();

            // Create a Map to store the values
            Map<String, Object> review = new HashMap<>();
            review.put("Name", name); // Use the field names as in your Firestore
            review.put("Category", selectedCategory); // Add this if you want to store the category too
            review.put("RatingValue", ratingValue);
            review.put("feedback", feedback);

            // Add a new document to the 'Reviews' collection
            firestore.collection("Reviews")
                    .add(review)
                    .addOnSuccessListener(documentReference -> {
                        // Handle success
                        Toast.makeText(getActivity(), "Review submitted successfully", Toast.LENGTH_SHORT).show();
                        // Clear the form or navigate away
                        clearForm();

                        // Start ReviewsActivity
                        Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(getActivity(), "Failed to submit review: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });


        });

        return view;
    }

    // Helper method to clear the form after submission
    private void clearForm() {
        //nameEditText.setText("");
        feedbackEditText.setText("");
        ratingBar.setRating(0);
        categorySpinner.setSelection(0);
    }
}

