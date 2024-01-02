package com.example.foodcarboncalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class SetupActivity extends AppCompatActivity {

    private EditText ageInput;
    private EditText nameInput;
    private RadioGroup radioGroup;
    private Spinner spinnerGoals;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Initialize views
        ageInput = findViewById(R.id.age_input);
        nameInput = findViewById(R.id.names_input);
        radioGroup = findViewById(R.id.radioGroup);
        spinnerGoals = findViewById(R.id.spinner_goals);
        submitButton = findViewById(R.id.submit_button);

        // Set up the spinner
        setUpSpinner();

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to save user information to Firestore
                saveUserInfoToFirestore();
            }
        });
    }

    private void setUpSpinner() {
        // Create a string array with dietary preferences, including the default value
        String[] dietaryPreferences = {"Preference (Select an option)", "Vegetarian", "Vegan", "Pescatarian", "Omnivore"};

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, dietaryPreferences);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerGoals.setAdapter(adapter);

        // Set the default selection to "Preference"
        spinnerGoals.setSelection(0);
    }

    private void saveUserInfoToFirestore() {
        // Get the user's UID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get user inputs
        String name = nameInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        int age = Integer.parseInt(ageStr); // assuming the age is entered as an integer

        // Get selected gender
        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        String gender = selectedRadioButton.getText().toString();

        // Get selected dietary preference
        String dietaryPreference = spinnerGoals.getSelectedItem().toString();

        // Create a Map to represent the user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("age", age);
        userData.put("gender", gender);
        userData.put("dietaryPreference", dietaryPreference);

        // Get a reference to the Firestore collection "users"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        // Set the user data in the Firestore document
        userRef.set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Successfully saved user information
                            Toast.makeText(SetupActivity.this, "User information saved.", Toast.LENGTH_SHORT).show();

                            // Navigate to the SuccessActivity
                            startActivity(new Intent(SetupActivity.this, SuccessActivity.class));
                            finish(); // Finish the current activity to prevent going back to it
                        } else {
                            // Failed to save user information
                            Toast.makeText(SetupActivity.this, "Error saving user information.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
