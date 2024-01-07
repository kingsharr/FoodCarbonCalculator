dependencies {

    //this includes login & register dependencies as well
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    //Firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")

    //Facebook
    implementation ("com.facebook.android:facebook-login:latest.release")

    //Google
    implementation("com.google.android.gms:play-services-auth:20.7.0")


}


private void setInsightText() {

        //get food analysis data from database
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the emission collection
        CollectionReference foodPlansRef = db.collection("valueCarbon");

        // Query to get all documents in the "user_goals" subcollection
        foodPlansRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    double emission = 0;
                    int tree = 0;
                    int energy = 0;
                    // Iterate through the documents
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        double val = document.getDouble("carbonValue");
                        tree += (int) val/100;
                        Log.w(TAG, "emission after tree: " + emission, task.getException());
                        energy += (int) val/20;
                        Log.w(TAG, "emission after carbon: " + emission, task.getException());
                        emission += val;
                        Log.w(TAG, "emission: " + emission, task.getException());

                    }
                    insight1.setText(emission + "kg C02");
                    insight2.setText("Saving " + tree + " trees, equivalent to their annual CO2 absorption");
                    insight3.setText("Saving " + energy + "kWh of energy");

                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

    }
