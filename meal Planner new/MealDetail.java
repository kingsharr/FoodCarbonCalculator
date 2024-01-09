package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

    public class MealDetail extends AppCompatActivity {

        TextView breakfast, lunch, dinner, date;
        String selectedDate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_meal_detail);

            breakfast = findViewById(R.id.breakfast);
            lunch = findViewById(R.id.lunch);
            dinner = findViewById(R.id.dinner);
            date = findViewById(R.id.textDate);

            Intent intent = getIntent();
            if (intent != null) {
                selectedDate = intent.getStringExtra("Date");
            }

            String selectedDate = getIntent().getStringExtra("Date");
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();



// Reference the specific document containing meal details for the selected date
            DocumentReference mealPlanRef = db.collection("MealPlans")
                    .document(userId)
                    .collection("UserMeals")
                    .document(selectedDate); // Document ID is the selected date

            mealPlanRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document exists, retrieve the meal data
                        Map<String, Object> mealData = document.getData();

                        String breakfastText, lunchText, dinnerText;
                        // Access breakfast, lunch, and dinner data
                        if(mealData.get("breakfast")!=null){
                        breakfastText = mealData.get("breakfast").toString();}
                        else{
                            breakfastText = "no meal input";
                        }
                        if(mealData.get("lunch")!=null){
                            lunchText = mealData.get("lunch").toString();}
                        else{
                            lunchText = "no meal input";
                        }
                        if(mealData.get("dinner")!=null){
                            dinnerText = mealData.get("dinner").toString();}
                        else{
                            dinnerText = "no meal input";
                        }
                        // Use the retrieved data as needed (e.g., display in TextViews)
                        // For example:
                            breakfast.setText("Breakfast: " + breakfastText);
                            lunch.setText("Lunch: " + lunchText);
                            dinner.setText("Dinner: " + dinnerText);
                            date.setText(dateConversion(selectedDate));
                    } else {
                        // Document does not exist for the selected date
                        Toast.makeText(this, "No meal data available for this date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle exceptions while fetching data
                    Toast.makeText(this, "Error fetching meal data", Toast.LENGTH_SHORT).show();
                }
            });

        }

        public String dateConversion(String inputString) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMd", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);

            try {
                Date date = inputFormat.parse(inputString);
                return outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return "Invalid Date"; // Return a default value or handle the exception
            }
        }

}
