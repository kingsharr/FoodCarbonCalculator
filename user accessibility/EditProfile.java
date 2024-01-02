package com.example.foodcarboncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    EditText editName, editEmail, editAge, editCarbon;
    Spinner spinnerGender, spinnerDiet;
    Button saveButton;
    String nameUser, emailUser, usernameUser, dietUser, genderUser;
    Long ageUser;
    Double carbonUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editName);
        spinnerGender = findViewById(R.id.spinner_gender);
        spinnerDiet = findViewById(R.id.spinner_diet);
        editCarbon = findViewById(R.id.editCarbon);
        saveButton = findViewById(R.id.saveButton);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged()){
                    Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfile.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNameChanged() {
        if (!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!emailUser.equals(editEmail.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else {
            return false;
        }
    }


    public void showData() {
        Intent intent = getIntent();
        if (intent != null) {
            nameUser = intent.getStringExtra("name");
            emailUser = intent.getStringExtra("email");
            ageUser = intent.getLongExtra("age", 0);
            carbonUser = intent.getDoubleExtra("carbon", 0);
            genderUser = intent.getStringExtra("gender");
            dietUser = intent.getStringExtra("dietaryPreference");

            editName.setText(nameUser);
            editEmail.setText(emailUser);
            editAge.setText(String.valueOf(ageUser)); // Display age in EditText
            editCarbon.setText(String.valueOf(carbonUser)); // Display carbon in EditText

            // Set selection for spinners (assuming you have populated the spinners with data)
            setSpinnerSelection(spinnerGender, genderUser);
            setSpinnerSelection(spinnerDiet, dietUser);
        }
    }

    // Method to set the spinner selection based on a given value
    private void setSpinnerSelection(Spinner spinner, String value) {
        if(value.equals("dietUser")) {
            // Create a string array with dietary preferences, including the default value
            String[] dietaryPreferences = {"Vegetarian", "Vegan", "Pescatarian", "Omnivore"};

            // Create an ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, dietaryPreferences);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            // Set the default selection to "Preference"
            spinner.setSelection(0);}
        else{
            // Create a string array with dietary preferences, including the default value
            String[] dietaryPreferences = {"Female", "Male"};

            // Create an ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, dietaryPreferences);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            // Set the default selection to "Preference"
            spinner.setSelection(0);
        }
    }

}