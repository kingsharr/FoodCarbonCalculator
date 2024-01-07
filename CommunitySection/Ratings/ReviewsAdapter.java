package com.example.madtest1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.usernameTextView.setText(review.getName());
        holder.reviewTextView.setText(review.getFeedback());
        holder.ratingBar.setRating(review.getRatingValue());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    // Method to update the review list
    public void updateReviews(List<Review> newReviews) {
        reviewList.clear();
        reviewList.addAll(newReviews);
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView reviewTextView;
        RatingBar ratingBar;

        ReviewViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView); // Replace with your actual TextView ID for username
            reviewTextView = itemView.findViewById(R.id.reviewTextView); // Replace with your actual TextView ID for review text
            ratingBar = itemView.findViewById(R.id.ratingBar); // Replace with your actual RatingBar ID
        }
    }
}
