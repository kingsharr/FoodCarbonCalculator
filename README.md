## Food Carbon Calculator

- Sharran's branch
- I have added User Registration, User Login and User Review and Rating.
- I have also added 11 drawable files which will be used by the above Activities.
- There is also 2 values files here which is colors.xml and strings.xml
- Lastly, I will add the dependencies that I used for my project here. Feel free to add them to your project as well:
- dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    //implementation("com.google.firebase:firebase-database:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")

    //Facebook
    implementation ("com.facebook.android:facebook-login:latest.release")

    //Google
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.android.gms:play-services-auth:20.7.0")

}
