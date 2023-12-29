// Review.java
package com.example.madtest1;

public class Review {
    private final String username;
    private final String reviewText;
    private final float rating;

    public Review(String username, String reviewText, float rating) {
        this.username = username;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getRating() {
        return rating;
    }
}
