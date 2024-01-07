

package com.example.madtest1;
import com.google.firebase.firestore.PropertyName;

public class Review {
    private String Name;
    private String Category;
    private float RatingValue;
    private String feedback;

    // No-argument constructor
    public Review() {}

    // Getters and setters
    @PropertyName("Name")
    public String getName() {
        return Name;
    }

    @PropertyName("Name")
    public void setName(String name) {
        this.Name = name;
    }

    @PropertyName("Category")
    public String getCategory() {
        return Category;
    }

    @PropertyName("Category")
    public void setCategory(String category) {
        this.Category = category;
    }

    @PropertyName("RatingValue")
    public float getRatingValue() {
        return RatingValue;
    }

    @PropertyName("RatingValue")
    public void setRatingValue(float ratingValue) {
        this.RatingValue = ratingValue;
    }

    @PropertyName("feedback")
    public String getFeedback() {
        return feedback;
    }

    @PropertyName("feedback")
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

