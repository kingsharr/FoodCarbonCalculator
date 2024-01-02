package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView profileEmail, profileUsername, profileAge, profileDiet, profileCarbon, profileGender;
    Button editProfile;

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

    }

    public void showAllUserData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String emailUser = snapshot.child("email").getValue(String.class);
                    String usernameUser = snapshot.child("name").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    Long age = snapshot.child("age").getValue(Long.class);
                    String diet = snapshot.child("dietaryPreference").getValue(String.class);
                    Double carbon = snapshot.child("carbon").getValue(Double.class);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }



    public void passUserData(){
        String userUsername = profileUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);

                    Intent intent = new Intent(ProfileActivity.this, EditProfile.class);

                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("name", usernameFromDB);

                    startActivity(intent);

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}