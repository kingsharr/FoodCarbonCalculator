package com.example.madtest1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class CommentDesign extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.comment_design, container, false);

        // Find and set up the selection button in comment page
        ImageButton ratingButton = view.findViewById(R.id.ratingButton); // Ensure this ID matches your layout
        ratingButton.setOnClickListener(v -> {
            // Create an Intent to start the ReviewsActivity
            Intent intent = new Intent(getActivity(), ReviewsActivity.class); // Use getActivity() for the context
            startActivity(intent);
        });

//        ImageButton forumButton = view.findViewById(R.id.forumButton); // Ensure this ID matches your layout
//        forumButton.setOnClickListener(v -> {
//            // Create an Intent to start the ReviewsActivity
//            Intent intent = new Intent(getActivity(), ReviewsActivity.class); // Use getActivity() for the context
//            startActivity(intent);
//        });
//
        ImageButton articleButton = view.findViewById(R.id.articleButton); // Ensure this ID matches your layout
        articleButton.setOnClickListener(v -> {
            // Create an Intent to start the ReviewsActivity
            Intent intent = new Intent(getActivity(), NewsDisplay.class); // Use getActivity() for the context
            startActivity(intent);
        });

        return view;
    }
}
