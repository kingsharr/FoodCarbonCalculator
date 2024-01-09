package com.example.foodcarboncalculator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardDesign extends Fragment {

    private PieChart donutChart;
    private BarChart barChart;
    private PieChart pieChart;
    private FirebaseFirestore firestore;
    private RelativeLayout relativeLayout;
    private ImageButton shareButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboarddesign, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        donutChart = view.findViewById(R.id.donutChart);
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);

        setupDonutChart();
        setupBarChart();
        setupPieChart();

        relativeLayout = view.findViewById(R.id.relativeLayout);
        shareButton = view.findViewById(R.id.shareButton);

        // Change the image resource
        ImageView mainImageView = view.findViewById(R.id.image);
        mainImageView.setImageResource(R.drawable.new_image);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a bitmap of the layout
                Bitmap bitmap = createBitmapFromView(relativeLayout);

                // Share the generated image
                if (bitmap != null) {
                    shareImage(bitmap);
                } else {
                    Toast.makeText(requireContext(), "Failed to generate image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to create a bitmap from a view (layout)
    private Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void shareImage(Bitmap bitmap) {
        // Share the image
        Uri imageUri = getImageUri(bitmap);
        if (imageUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share image via"));
        } else {
            Toast.makeText(requireContext(), "Failed to get image Uri", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        File imageFile = new File(requireContext().getCacheDir(), "shared_image.png");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return FileProvider.getUriForFile(requireContext(), "com.example.foodcarboncalculator", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setupDonutChart() {
        // Get the current FirebaseUser
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is not null
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Initialize Firestore
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            // Reference to the valueCarbon collection
            CollectionReference valueCarbonCollection = firestore.collection("valueCarbon");

            // Query to get documents for the specific user
            valueCarbonCollection.whereEqualTo("UserID", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                // Prepare the data for the chart
                HashMap<String, Float> portionQuantities = new HashMap<>();

                // Aggregate portions by food type for the specific user
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    String foodType = documentSnapshot.getString("Food");
                    String portionString = documentSnapshot.getString("Portion");
                    float quantity = getPortionValue(portionString); // Convert portion string to numerical value

                    Float currentQuantity = portionQuantities.get(foodType);
                    if (currentQuantity == null) {
                        portionQuantities.put(foodType, quantity);
                    } else {
                        portionQuantities.put(foodType, currentQuantity + quantity);
                    }
                }

                // Convert aggregated data to PieEntries
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                for (Map.Entry<String, Float> entry : portionQuantities.entrySet()) {
                    pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
                }

                // Create the data set and configure the chart
                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                dataSet.setSliceSpace(0f);
                dataSet.setSelectionShift(5f);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.WHITE);

                // Configure the donut chart appearance
                donutChart.setData(data);
                donutChart.setDrawHoleEnabled(true);
                donutChart.setHoleRadius(10f);
                donutChart.setTransparentCircleRadius(61f);
                donutChart.setUsePercentValues(true);
                donutChart.getDescription().setEnabled(false);

                // Customize the legend
                Legend legend = donutChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setDrawInside(false);
                legend.setEnabled(true);

                // Refresh the chart to display the data
                donutChart.invalidate();
            }).addOnFailureListener(e -> Log.e("DonutChart", "Error fetching user-specific Portion data", e));
        } else {
            // Handle the case when no user is logged in
            Log.e("DonutChart", "No user is currently logged in.");
            // You could also update your UI to reflect this status
        }
    }

    // Helper method to convert portion strings to numerical values
    private float getPortionValue(String portionString) {
        switch (portionString) {
            case "less quantity":
                return 1;
            case "medium":
                return 2;
            case "more quantity":
                return 3;
            default:
                return 0; // Default case if the portionString does not match
        }
    }

    // Call this method to initialize the donut chart for the current user
    public void initializeChart() {
        setupDonutChart();
    }

    private void setupPieChart() {
        // Initialize Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Hardcoded fixed values
        final float globally = 12f; // Replace with your actual global value
        final float nationally = 18.5f; // Replace with your actual national value
        final float regionally = 51f; // Replace with your actual regional value

        // Get the current FirebaseUser
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is not null
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the valueCarbon collection
            CollectionReference valueCarbonCollection = firestore.collection("valueCarbon");

            // Query to get documents for the specific user
            valueCarbonCollection.whereEqualTo("UserID", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                float totalIndividualCarbonValue = 0f; // Accumulator for the individual carbon value

                // Aggregate carbon values for the specific user
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    String portionString = documentSnapshot.getString("Portion");
                    Number carbonValueNumber = documentSnapshot.getLong("CarbonValue");

                    if (portionString != null && carbonValueNumber != null) {
                        float portionValue = getPortionValue(portionString); // Convert portion string to numerical value
                        float carbonValue = carbonValueNumber.floatValue();
                        totalIndividualCarbonValue += portionValue * carbonValue;
                    }
                }

                // Prepare the entries for the pie chart
                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(totalIndividualCarbonValue, "Individually"));
                entries.add(new PieEntry(globally, "Globally"));
                entries.add(new PieEntry(nationally, "Nationally"));
                entries.add(new PieEntry(regionally, "Regionally"));

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData data = new PieData(dataSet);
                pieChart.setData(data);

                // Configure chart aesthetics
                pieChart.getDescription().setEnabled(false);
                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(Color.WHITE);

                pieChart.setDrawHoleEnabled(false);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setHoleRadius(0f);

                Legend l = pieChart.getLegend();
                l.setTextSize(8f);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);

                // Refresh the chart
                pieChart.invalidate();
            }).addOnFailureListener(e -> {
                // Handle the error
                Log.e("PieChart", "Error fetching individual user carbon values", e);
            });
        } else {
            // Handle the case when no user is logged in
            Log.e("PieChart", "No user is currently logged in.");
            // You could also update your UI to reflect this status
        }
    }

    private void setupBarChart() {
        // Initialize Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Get the current FirebaseUser
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is not null
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Prepare the data for the bar chart
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            ArrayList<String> foodNames = new ArrayList<>();

            // Reference to the valueCarbon collection and query for the specific user
            firestore.collection("valueCarbon")
                    .whereEqualTo("UserID", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int index = 0;
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String foodItem = documentSnapshot.getString("Food");
                            Number carbonValueNumber = documentSnapshot.getLong("CarbonValue");
                            if (carbonValueNumber != null) {
                                float carbonValue = carbonValueNumber.floatValue();
                                barEntries.add(new BarEntry(index, carbonValue));
                                foodNames.add(foodItem);
                                index++;
                            }
                        }

                        BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Value by Food");
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);

                        // Set the text size for the value labels above the bars
                        barDataSet.setValueTextSize(10f);

                        // Set the colors for each bar
                        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                        // Define the labels for each bar
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(foodNames));
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setYOffset(8f); // To move the labels further from the bars

                        Legend l = barChart.getLegend();
                        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        l.setDrawInside(false);
                        l.setEnabled(true);

                        // Refresh the chart
                        barChart.getDescription().setEnabled(false);
                        barChart.invalidate();
                    })
                    .addOnFailureListener(e -> Log.e("BarChart", "Error fetching carbon value data", e));
        } else {
            // Handle the case when no user is logged in
            Log.e("BarChart", "No user is currently logged in.");
            // You could also update your UI to reflect this status
        }
    }

}

