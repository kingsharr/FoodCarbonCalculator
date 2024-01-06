package com.example.foodcarboncalculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    static RequestManager glide;
    private ArrayList<Comment> comments;

    private static boolean isLiked = false;

    public CommentAdapter(Context context, ArrayList<Comment> comments, RequestManager glide) {
        this.context = context;
        this.comments = comments;
        this.glide = glide;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your comment item layout
        View view = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (position >= 0 && position < comments.size()) {
            Comment currentComment = comments.get(position);

            // Log statements for debugging
            Log.d("CommentAdapter", "Position: " + position);
            Log.d("CommentAdapter", "Current Comment: " + currentComment);

            // Check if the currentComment is not null
            if (currentComment != null) {
                String comment = currentComment.getCommentText();
                String userId = currentComment.getUserId();

                if (comment != null && userId != null) {
                    Log.d("Comment", "comment: " + comment);
                    holder.tvComment.setText(comment);
                    holder.userName.setText(userId);
                }
            }
        }
    }

   /* @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if(!comments.isEmpty()){
            // Bind the comment data to the ViewHolder
            if(comments.get(position).getCommentText() != null){
                String comment = comments.get(position).getCommentText();
                String userId = comments.get(position).getUserId();
                holder.tvComment.setText(comment);
                holder.userName.setText(userId);
            }
        }
    }*/

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // ViewHolder class
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        // Add views for comment item (e.g., TextView)
        TextView tvComment;
        TextView userName;
        ImageView imgLike;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views for comment item
            tvComment = itemView.findViewById(R.id.forumComment);
            userName = itemView.findViewById(R.id.TxtUsernameC);
            imgLike = itemView.findViewById(R.id.imgLike);

            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isLiked = !isLiked;

                    // Load the appropriate image based on the like state
                    int imageResource = isLiked ? R.drawable.filled_heart : R.drawable.unfilled_heart;
                    glide.load(imageResource).into(imgLike);
                }
            });
        }
    }
}

