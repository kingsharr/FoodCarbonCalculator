package com.example.foodcarboncalculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    EditText editName, editEmail, editAge, editCarbon;
    Spinner spinnerGender, spinnerDiet;
    Button saveButton, logoutButton, cancelButton;
    CircleImageView ProfileImage;
    Long ageUser;
    Double  carbonUser;
    String nameUser, emailUser, usernameUser, dietUser, genderUser;
    DocumentReference reference;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        reference = db.collection("users").document(userId);
        storageReference = FirebaseStorage.getInstance().getReference();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        ProfileImage = findViewById(R.id.imageView2);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editAge);
        spinnerGender = findViewById(R.id.spinner_gender);
        spinnerDiet = findViewById(R.id.spinner_diet);
        editCarbon = findViewById(R.id.editCarbon);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.BtnCancel);

        showData();

        StorageReference profileRef = storageReference.child("users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ProfileImage);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isAgeChanged() || isCarbonChanged() || isDietChanged() || isGenderChanged()){
                    Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                        startActivity(intent);
                } else {
                    Toast.makeText(EditProfile.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changesMade()) {
                    showDiscardChangesDialog();
                } else {
                    // If no changes made, go back to activity_profile
                    Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                    startActivity(intent);
                    finish(); // Optional: Close the current activity
                }
            }

            private boolean changesMade() {
                if(!nameUser.equals(editName.getText().toString())||
                        !emailUser.equals(editEmail.getText().toString())||
                        !genderUser.equals(spinnerGender.getSelectedItem().toString())||
                        ageUser!=Long.parseLong(editAge.getText().toString())||
                        !dietUser.equals(spinnerDiet.getSelectedItem().toString())||
                        carbonUser!=Double.parseDouble(editCarbon.getText().toString())
                ){ return true;
                }else return false;
            }
        });

    }

    private void showDiscardChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard changes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If the user clicks "Yes", go back to activity_profile
                Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If the user clicks "No", stay in the EditProfileActivity
                dialogInterface.dismiss(); // Dismiss the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private boolean isNameChanged() {
        if (!nameUser.equals(editName.getText().toString())) {
            // Update the 'gender' field in the Firestore document
            reference.update("name", editName.getText().toString())
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        nameUser = editName.getText().toString();
                    })
                    .addOnFailureListener(e -> {
                        //
                    });

            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!emailUser.equals(editEmail.getText().toString())) {
            // Update the 'gender' field in the Firestore document
            reference.update("email", editEmail.getText().toString())
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        emailUser = editEmail.getText().toString();
                    })
                    .addOnFailureListener(e -> {
                        //
                    });

            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        if (!genderUser.equals(spinnerGender.getSelectedItem().toString())) {
            // Update the 'gender' field in the Firestore document
            reference.update("gender", spinnerGender.getSelectedItem().toString())
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        genderUser = spinnerGender.getSelectedItem().toString();
                    })
                    .addOnFailureListener(e -> {
                        //
                    });

            return true;
        } else {
            return false;
        }
    }

    private boolean isAgeChanged() {
        if (ageUser!=Long.parseLong(editAge.getText().toString())) {
            // Update the 'gender' field in the Firestore document
            reference.update("age", Long.parseLong(editAge.getText().toString()))
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        ageUser = Long.parseLong(editAge.getText().toString());
                    })
                    .addOnFailureListener(e -> {
                        //
                    });

            return true;
        } else {
            return false;
        }
    }

    private boolean isDietChanged() {
        if (!dietUser.equals(spinnerDiet.getSelectedItem().toString())) {
            // Update the 'gender' field in the Firestore document
            reference.update("dietaryPreference", spinnerDiet.getSelectedItem().toString())
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        dietUser = spinnerDiet.getSelectedItem().toString();
                    })
                    .addOnFailureListener(e -> {
                        //
                    });

            return true;
        } else {
            return false;
        }
    }

    private boolean isCarbonChanged() {
        if (carbonUser!=Double.parseDouble(editCarbon.getText().toString())) {
            // Update the 'gender' field in the Firestore document
            reference.update("carbon", Double.parseDouble(editCarbon.getText().toString()))
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        carbonUser=Double.parseDouble(editCarbon.getText().toString());
                    })
                    .addOnFailureListener(e -> {
                        //
                    });

            return true;
        } else {
            return false;
        }
    }
    private boolean isProfileChanged(Uri imageUri) {
        if (imageUri != null) {
            // If a new image is selected, it means the profile picture is changed
            return true;
        } else {
            // If the imageUri is null, it means the profile picture remains unchanged
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
        if(value.equals("Male") || value.equals("Female")) {
            // Create a string array with dietary preferences, including the default value
            String[] gender = {"Female", "Male"};

            // Create an ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, gender);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            // Set the default selection to "Preference"
            spinner.setSelection(0);
        }
        else{
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }

    }
    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(ProfileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
