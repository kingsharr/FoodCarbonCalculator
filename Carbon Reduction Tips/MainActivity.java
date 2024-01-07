//package com.example.madtest1;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//public class MainActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new FragmentTest())
//                    .commit();
//        }
//
//    }
//}

package com.example.madtest1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        View view = LayoutInflater.from(this).inflate(R.layout.page_layout, null);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.DestHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.DestHome) {
                return true;
            } else if (itemId == R.id.DestCalender) {
//                startActivity(new Intent(getApplicationContext(), ReviewsActivity.class));
//                finish();
                return true;
            } else if (itemId == R.id.DestCalculator) {
                // Handle DestCalculator
                return true;
            } else if (itemId == R.id.DestCommunity) {
//                startActivity(new Intent(getApplicationContext(), CommentDisplay.class));
//                finish();
                return true;
            } else if (itemId == R.id.DestProfile) {
//                startActivity(new Intent(getApplicationContext(), DashboardDisplay.class));
//                finish();
                return true;
            }
            return false;
        });

        // Check if the container is already created
        if (savedInstanceState == null) {
            // Perform the fragment transaction to add the Slider fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.midContent, new Slider())
                    .commit();
        }

        // Set the content view to the inflated view
        setContentView(view);
    }
}
