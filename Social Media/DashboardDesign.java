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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
        View view = inflater.inflate(R.layout.activity_dashboard_design, container, false);

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

    private void setupDonutChart() {
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Reference to the user's food collection
        CollectionReference userFoodCollection = firestore.collection("Users").document("User_01").collection("Food");

        userFoodCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Prepare the data for the chart
            HashMap<String, Float> foodQuantities = new HashMap<>();

            // Aggregate quantities by food type
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                String foodType = documentSnapshot.getId();
                Number quantityNumber = documentSnapshot.getDouble("Quantity");
                float quantity = 0;
                if (quantityNumber != null) {
                    quantity = quantityNumber.floatValue();
                }
                Float currentQuantity = foodQuantities.get(foodType);
                if (currentQuantity == null) {
                    foodQuantities.put(foodType, quantity);
                } else {
                    foodQuantities.put(foodType, currentQuantity + quantity);
                }

            }

            // Convert aggregated data to PieEntries
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            for (Map.Entry<String, Float> entry : foodQuantities.entrySet()) {
                pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            // Create the data set and configure the chart
            PieDataSet dataSet = new PieDataSet(pieEntries, "");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setSliceSpace(0f);
            dataSet.setSelectionShift(5f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter(donutChart));
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.WHITE);

            // Configure the donut chart appearance
            donutChart.setData(data);
            donutChart.setDrawHoleEnabled(true);
            donutChart.setHoleRadius(10f);
            donutChart.setTransparentCircleRadius(55f);
            donutChart.setUsePercentValues(false);
            donutChart.getDescription().setEnabled(false);

            // Customize the legend
            Legend legend = donutChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setEnabled(true);
            legend.setYOffset(8f);

            // Refresh the chart to display the data
            donutChart.invalidate();
        }).addOnFailureListener(e -> Log.e("DonutChart", "Error fetching Food Quantity data", e));
    }



    private void setupPieChart() {
        // Hardcoded fixed values
        final float globally = 12f; // Replace with your actual global value
        final float nationally = 18.5f; // Replace with your actual national value
        final float regionally = 51f; // Replace with your actual regional value

        // Fetch the individual user's carbon emission value for UserID_01
        firestore.collection("Emissions").document("Users")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    float individually = 0f; // Default to 0 if not found
                    if (documentSnapshot.exists()) {
                        Double individualValue = documentSnapshot.getDouble("UserID_01");
                        if (individualValue != null) {
                            individually = individualValue.floatValue();
                        } else {
                            Log.d("DashboardDesign", "UserID_01 not found or is null.");
                        }
                    } else {
                        Log.d("DashboardDesign", "Document does not exist.");
                    }

                    // Prepare the entries for the pie chart
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(individually, "Individual"));
                    entries.add(new PieEntry(globally, "Globally"));
                    entries.add(new PieEntry(nationally, "Nationally"));
                    entries.add(new PieEntry(regionally, "Regionally"));

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                    pieChart.getDescription().setEnabled(false);
                    dataSet.setValueTextSize(8f);

                    PieData data = new PieData(dataSet);
                    pieChart.setData(data);
                    pieChart.setDrawHoleEnabled(false);

                    Legend l = pieChart.getLegend();
                    l.setTextSize(8f);
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    l.setDrawInside(false);

                    pieChart.invalidate(); // Refresh the chart
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("DashboardDesign", "Error fetching individual user emissions", e);
                });
    }

    private void setupBarChart() {
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Prepare the data for the bar chart
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> foodNames = new ArrayList<>();

        // Fetch carbon emission data for both Maggi and Tose
        firestore.collection("Users").document("User_01").collection("Food")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int index = 0;
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        String foodItem = documentSnapshot.getId();
                        Number quantityNumber = documentSnapshot.getDouble("Quantity");
                        Number carbonEmissionNumber = documentSnapshot.getDouble("CarbonEmission");
                        if (quantityNumber != null && carbonEmissionNumber != null) {
                            float quantity = quantityNumber.floatValue();
                            float carbonEmission = carbonEmissionNumber.floatValue();
                            float totalEmission = quantity * carbonEmission;
                            barEntries.add(new BarEntry(index, totalEmission));
                            foodNames.add(foodItem);
                            index++;
                        }
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Emission From Food");
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
                    xAxis.setYOffset(8f); // Add this line to move the labels further from the bars

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
                .addOnFailureListener(e -> Log.e("BarChart", "Error fetching food data", e));
    }


}

