package com.example.madtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DashboardDisplay extends AppCompatActivity {

    private TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);

        header = findViewById(R.id.textViewOnImage);
        header.setText("Analytics");
        //header.setTextSize();

        if (savedInstanceState == null) {
            // Create an instance of the RatingDesign fragment
            DashboardDesign dashboardDesignFragment = new DashboardDesign();

            // Begin a fragment transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace the contents of the container with the new fragment
            transaction.replace(R.id.midContent, dashboardDesignFragment);

            // Complete the changes added above
            transaction.commit();
        }


    }
}
