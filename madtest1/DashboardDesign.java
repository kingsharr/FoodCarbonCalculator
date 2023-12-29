package com.example.madtest1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DashboardDesign extends Fragment {

    private PieChart donutChart;
    private BarChart barChart;
    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboarddesign, container, false);

        donutChart = view.findViewById(R.id.donutChart);
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);

        setupDonutChart();
        setupBarChart();
        setupPieChart();

        return view;
    }

    private void setupDonutChart() {
        // Your data for the chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "Category 1"));
        entries.add(new PieEntry(30f, "Category 2"));
        entries.add(new PieEntry(30f, "Category 3"));

        PieDataSet dataSet = new PieDataSet(entries, "Your Label");
        PieData data = new PieData(dataSet);
        donutChart.setData(data);

        // Styling
        donutChart.setDrawHoleEnabled(true);
        donutChart.setHoleRadius(50f); // Adjust the hole radius for the donut effect
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Add your colors
        donutChart.invalidate(); // Refresh chart
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Category A"));
        entries.add(new PieEntry(40f, "Category B"));
        entries.add(new PieEntry(30f, "Category C"));

        PieDataSet dataSet = new PieDataSet(entries, "Category Distribution");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // or any other color set

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false); // This makes it a regular pie chart
        pieChart.invalidate(); // refresh the chart
    }

    private void setupBarChart() {
        // Your data for the chart
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 40f));
        barEntries.add(new BarEntry(2f, 30f));
        barEntries.add(new BarEntry(3f, 20f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Your Bar Label");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Styling
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Add your colors
        barChart.getDescription().setEnabled(false);
        barChart.invalidate(); // Refresh chart
    }
}
