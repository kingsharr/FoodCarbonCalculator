package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    TextView profileEmail, profileUsername, profileAge, profileDiet, profileCarbon, profileGender;
    Button editProfile, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUserName);
        profileGender = findViewById(R.id.profileGender);
        profileAge = findViewById(R.id.profileAge);
        profileCarbon = findViewById(R.id.profileCarbon);
        profileDiet = findViewById(R.id.profileDiet);
        editProfile = findViewById(R.id.BTNEditProfile);

        showAllUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        //bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.DestHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.DestHome) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.DestCalender) {
                startActivity(new Intent(getApplicationContext(), MealPlannerActivity.class));
                return true;
            } else if (item.getItemId() == R.id.DestCommunity) {
                // Handle DestCommunity
                return true;
            } else if (item.getItemId() == R.id.DestProfile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            }
            return false;
        });

        //logout
        logoutButton = findViewById(R.id.BTNLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showAllUserData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String emailUser = document.getString("email");
                        String usernameUser = document.getString("name");
                        String gender = document.getString("gender");
                        Long age = document.getLong("age");
                        String diet = document.getString("dietaryPreference");
                        Double carbon = document.getDouble("carbon");

                        // Set retrieved data to respective TextViews
                        profileEmail.setText(emailUser);
                        profileUsername.setText(usernameUser);
                        profileAge.setText(String.valueOf(age));
                        profileGender.setText(gender);
                        profileDiet.setText(diet);
                        profileCarbon.setText(String.valueOf(carbon));
                    } else {
                        // Handle when user data doesn't exist
                        Toast.makeText(ProfileActivity.this, "No data available for this user", Toast.LENGTH_SHORT).show();
                        // You can set default values or display a message in TextViews
                        profileEmail.setText("No email available");
                        profileUsername.setText("No username available");
                    }
                } else {
                    // Handle exceptions while fetching data
                    Toast.makeText(ProfileActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void passUserData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve necessary data from documentSnapshot
                    String email = documentSnapshot.getString("email");
                    String name = documentSnapshot.getString("name");
                    String gender = documentSnapshot.getString("gender");
                    Long age = documentSnapshot.getLong("age");
                    String diet = documentSnapshot.getString("dietaryPreference");
                    Double carbon = documentSnapshot.getDouble("carbon");

                    // Pass the data to EditProfile activity
                    Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
                    intent.putExtra("email", email);
                    intent.putExtra("name", name);
                    intent.putExtra("gender", gender);
                    intent.putExtra("age", age);
                    intent.putExtra("dietaryPreference", diet);
                    intent.putExtra("carbon", carbon);

                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure to fetch user data
                Toast.makeText(ProfileActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}