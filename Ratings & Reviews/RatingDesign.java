package com.example.madtest1;

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

import java.util.ArrayList;
import java.util.List;

public class RatingDesign extends Fragment {

    private RatingBar ratingBar;
    private EditText feedbackEditText;
    private Button submitFeedbackButton;
    private Spinner categorySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_rating_design, container, false);

        categorySpinner = view.findViewById(R.id.spinner);
        ratingBar = view.findViewById(R.id.ratingBar);
        feedbackEditText = view.findViewById(R.id.feedbackEditText);
        submitFeedbackButton = view.findViewById(R.id.submitFeedbackButton);


        // Example category items
        String[] categories = {"Select the option","Food Quality", "Service", "Cleanliness", "Ambiance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);

//        List<String> categories = new ArrayList<>();
//        categories.add("Select the option"); // Default option
//        categories.add("Option 1");
//        categories.add("Option 2");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(dataAdapter);

        submitFeedbackButton.setOnClickListener(v -> {
            // Logic to handle review submission
            float ratingValue = ratingBar.getRating();
            String feedback = feedbackEditText.getText().toString();
            // Handle the submission
        });

        return view;
    }
}
