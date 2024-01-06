package com.example.madtest1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.madtest1.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class DashboardDesign extends Fragment {

    private PieChart donutChart;
    private BarChart barChart;
    private PieChart pieChart;
    private FirebaseFirestore firestore;

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

        return view;
    }

//    private void setupDonutChart() {
//        // Your data for the chart
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(40f, "A"));
//        entries.add(new PieEntry(30f, "B"));
//        entries.add(new PieEntry(30f, "C"));
//
//        PieDataSet dataSet = new PieDataSet(entries, "A");
//        PieData data = new PieData(dataSet);
//        donutChart.setData(data);
//
//        // Styling
//        donutChart.setDrawHoleEnabled(true);
//        donutChart.setHoleRadius(20f); // Adjust the hole radius for the donut effect
//        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Add your colors
//
//        Legend l = donutChart.getLegend();
//        l.setTextSize(8f);
//        // Adjust legend position
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//
//        // Remove the description label
//        donutChart.getDescription().setEnabled(false);
//
//        donutChart.invalidate(); // Refresh chart
//    }

//    private void setupDonutChart() {
//        firestore.collection("Consumption").document("Food")
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        ArrayList<PieEntry> entries = new ArrayList<>();
//
//                        // Iterate over each field and add it as an entry
//                        Map<String, Object> foods = documentSnapshot.getData();
//                        if (foods != null) {
//                            for (Map.Entry<String, Object> entry : foods.entrySet()) {
//                                String foodType = entry.getKey();
//                                Number foodValue = (Number) entry.getValue();
//                                if (foodValue != null) {
//                                    entries.add(new PieEntry(foodValue.floatValue(), foodType));
//                                }
//                            }
//                        }
//
//                        // Update the chart with the food entries
//                        PieDataSet dataSet = new PieDataSet(entries, "Food Intake");
//                        dataSet.setColors(ColorTemplate.JOYFUL_COLORS); // Use joyful colors for the chart
//                        dataSet.setSliceSpace(3f); // space between slices
//                        dataSet.setSelectionShift(5f); // the "pop-out" effect on tap
//
//                        PieData data = new PieData(dataSet);
//                        data.setValueTextSize(10f);
//                        data.setValueTextColor(Color.WHITE);
//
//                        donutChart.setData(data);
//                        donutChart.setDrawHoleEnabled(true);
//                        donutChart.setHoleRadius(50f); // Hole radius for the donut effect
//                        donutChart.setTransparentCircleRadius(55f); // Outer transparent circle radius
//
//                        Legend l = donutChart.getLegend();
//                        l.setTextSize(8f);
//                        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//                        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                        l.setDrawInside(false);
//
//                        donutChart.getDescription().setEnabled(false);
//                        donutChart.invalidate(); // Refresh chart
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // Handle the error
//                    Log.e("DashboardDesign", "Error fetching food consumption data", e);
//                });
//    }

    private void setupDonutChart() {
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Reference to the user's food collection
        CollectionReference userFoodCollection = firestore.collection("Users").document("User_01").collection("Food");

        userFoodCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Prepare the data for the chart
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String[] foodNames = new String[queryDocumentSnapshots.size()];
            int index = 0;

            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                // Retrieve the Quantity value
                Number quantityNumber = documentSnapshot.getDouble("Quantity");
                float quantity = 0;
                if (quantityNumber != null) {
                    quantity = quantityNumber.floatValue();
                }

                // Use the quantity value for the donut chart
                pieEntries.add(new PieEntry(quantity, documentSnapshot.getId()));
                index++;
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Set the colors for the entries
            dataSet.setSliceSpace(0f); // Space between slices
            dataSet.setSelectionShift(5f); // Amount to offset a selected pie slice

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter(donutChart)); // Formatter to display values as percentages
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.WHITE);

            donutChart.setData(data);
            donutChart.setDrawHoleEnabled(true);
            donutChart.setHoleRadius(10f); // Adjust the hole radius for the donut effect
            donutChart.setTransparentCircleRadius(55f); // Transparent circle radius
            donutChart.setUsePercentValues(true); // Show percentages instead of actual values
            donutChart.getDescription().setEnabled(false);

            // Customizing the legend
            Legend l = donutChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setEnabled(true);
            l.setYOffset(8f); // Adjust this value as needed for spacing

            // Refresh the chart
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


//    private void setupBarChart() {
//        // Your data for the chart
//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        barEntries.add(new BarEntry(0f, 40f));
//        barEntries.add(new BarEntry(1f, 30f));
//        barEntries.add(new BarEntry(2f, 20f));
//
//        BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Footprint");
//        BarData barData = new BarData(barDataSet);
//        barChart.setData(barData);
//
//        // Set the text size for the value labels above the bars
//        barDataSet.setValueTextSize(10f); // Adjusted text size
//
//        // Set the text size for the labels on the X-axis and Y-axis
//        barChart.getXAxis().setTextSize(10f); // Adjusted text size
//        barChart.getAxisLeft().setTextSize(10f); // Adjusted text size
//        barChart.getAxisRight().setTextSize(10f); // Adjusted text size
//
//        // Define the labels for each bar
//        String[] labels = new String[]{"A", "B", "C"};
//
//        // Add extra offset to the chart
//        barChart.setExtraOffsets(0, 0, 0, 20); // left, top, right, bottom
//
//        Legend l = barChart.getLegend();
//        l.setTextSize(8f);
//        // Adjust legend position
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//
//        // Set the labels to the XAxis
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//        xAxis.setGranularity(1f); // Only show integer values on the XAxis
//        xAxis.setGranularityEnabled(true); // Enable granularity
//        xAxis.setPosition(XAxis.XAxisPosition.TOP); // Labels should appear at the bottom
//        // Styling
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Add your colors
//        barChart.getDescription().setEnabled(false);
//        barChart.invalidate(); // Refresh chart
//    }

//    private void setupBarChart() {
//        // Initialize Firestore
//        firestore = FirebaseFirestore.getInstance();
//
//        // Reference to the user's food document
//        DocumentReference userFoodRef = firestore.collection("Users").document("User_01").collection("Food").document("Maggi");
//
//        userFoodRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                // Retrieve the CarbonEmission value
//                Number carbonEmission = documentSnapshot.getDouble("CarbonEmission");
//                float emissionValue = 0;
//                if (carbonEmission != null) {
//                    emissionValue = carbonEmission.floatValue();
//                }
//
//                // Create a single entry for the bar chart with the fetched carbon emission value
//                ArrayList<BarEntry> barEntries = new ArrayList<>();
//                barEntries.add(new BarEntry(0f, emissionValue)); // Use the emission value for the bar chart
//
//                BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Emission");
//                BarData barData = new BarData(barDataSet);
//                barChart.setData(barData);
//
//                // Set the text size for the value labels above the bars
//                barDataSet.setValueTextSize(10f);
//
//                // Rest of your chart styling code
//                // Set the text size for the labels on the X-axis and Y-axis
//                barChart.getXAxis().setTextSize(10f);
//                barChart.getAxisLeft().setTextSize(10f);
//                barChart.getAxisRight().setTextSize(10f);
//
//                // Define the labels for each bar
//                String[] labels = new String[]{"Maggi"};
//
//                // Add extra offset to the chart
//                barChart.setExtraOffsets(0, 0, 0, 20);
//
//                Legend l = barChart.getLegend();
//                l.setTextSize(8f);
//                // Adjust legend position
//                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                l.setDrawInside(false);
//
//                // Set the labels to the XAxis
//                XAxis xAxis = barChart.getXAxis();
//                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//                xAxis.setGranularity(1f);
//                xAxis.setGranularityEnabled(true);
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                // Styling
//                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//                barChart.getDescription().setEnabled(false);
//                barChart.invalidate(); // Refresh chart
//
//            } else {
//                Log.d("BarChart", "No CarbonEmission data found for User_01");
//            }
//        }).addOnFailureListener(e -> Log.e("BarChart", "Error fetching CarbonEmission data", e));
//    }

//    private void setupBarChart() {
//        // Initialize Firestore
//        firestore = FirebaseFirestore.getInstance();
//
//        // Prepare the data for the bar chart
//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        String[] labels = new String[]{"Maggi", "Tose"};
//
//        // Fetch carbon emission for Maggi
//        DocumentReference maggiRef = firestore.collection("Users").document("User_01").collection("Food").document("Maggi");
//        maggiRef.get().addOnSuccessListener(maggiSnapshot -> {
//            if (maggiSnapshot.exists()) {
//                // Retrieve the CarbonEmission value for Maggi
//                Number maggiEmission = maggiSnapshot.getDouble("CarbonEmission");
//                if (maggiEmission != null) {
//                    // Add Maggi emission value as the first entry
//                    barEntries.add(new BarEntry(0f, maggiEmission.floatValue()));
//                }
//                // Fetch carbon emission for Tose
//                DocumentReference toseRef = firestore.collection("Users").document("User_01").collection("Food").document("Tose");
//                toseRef.get().addOnSuccessListener(toseSnapshot -> {
//                    if (toseSnapshot.exists()) {
//                        // Retrieve the CarbonEmission value for Tose
//                        Number toseEmission = toseSnapshot.getDouble("CarbonEmission");
//                        if (toseEmission != null) {
//                            // Add Tose emission value as the second entry
//                            barEntries.add(new BarEntry(1f, toseEmission.floatValue()));
//                        }
//                        // Continue with setting up the bar chart using the entries
//                        BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Emission");
//                        BarData barData = new BarData(barDataSet);
//                        barChart.setData(barData);
//                        barDataSet.setColors(new int[]{Color.GREEN, Color.YELLOW}); // Set specific colors for each bar
//
//                        // Set the text size for the value labels above the bars
//                        barDataSet.setValueTextSize(10f);
//
//                        // Rest of your chart styling code
//                        // Set the text size for the labels on the X-axis and Y-axis
//                        barChart.getXAxis().setTextSize(10f);
//                        barChart.getAxisLeft().setTextSize(10f);
//                        barChart.getAxisRight().setTextSize(10f);
//
//                        // Add extra offset to the chart
//                        barChart.setExtraOffsets(0, 0, 0, 20);
//
//                        Legend l = barChart.getLegend();
//                        l.setTextSize(8f);
//                        // Adjust legend position
//                        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//                        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                        l.setDrawInside(false);
//
//                        // Set the labels to the XAxis
//                        XAxis xAxis = barChart.getXAxis();
//                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//                        xAxis.setGranularity(1f);
//                        xAxis.setGranularityEnabled(true);
//                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//
//                        // Styling
//                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//                        barChart.getDescription().setEnabled(false);
//                        barChart.invalidate(); // Refresh chart
//
//                    } else {
//                        Log.d("BarChart", "No CarbonEmission data found for Tose");
//                    }
//                }).addOnFailureListener(e -> Log.e("BarChart", "Error fetching CarbonEmission data for Tose", e));
//            } else {
//                Log.d("BarChart", "No CarbonEmission data found for Maggi");
//            }
//        }).addOnFailureListener(e -> Log.e("BarChart", "Error fetching CarbonEmission data for Maggi", e));
//    }

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

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Emission");
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
