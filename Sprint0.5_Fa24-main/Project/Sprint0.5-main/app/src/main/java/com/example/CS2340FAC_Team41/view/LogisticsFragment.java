package com.example.CS2340FAC_Team41.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.CS2340FAC_Team41.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogisticsFragment extends Fragment {

    private PieChart pieChart;
    private TextView tvTotalDays;
    private Button btnVisualize;
    private DatabaseReference travelLogsRef;
    private DatabaseReference vacationLogsRef;
    private String userId;
    private int allottedDays = 0;
    private int totalPlannedDays = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_logistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        travelLogsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("travelLogs");
        vacationLogsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("vacationLogs");

        pieChart = view.findViewById(R.id.pie_chart);
        tvTotalDays = view.findViewById(R.id.tv_total_days);
        btnVisualize = view.findViewById(R.id.btn_visualize);

        // Load allotted and planned vacation days
        loadAllottedDays();
        loadPlannedVacationDays();

        // Set up button listener for the pie chart visualization
        btnVisualize.setOnClickListener(v -> visualizeTripDays());
    }

    /**
     * This loads the allotted days
     */
    private void loadAllottedDays() {
        vacationLogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot log : snapshot.getChildren()) {
                    String durationStr = log.child("duration").getValue(String.class);
                    if (durationStr != null && !durationStr.isEmpty()) {
                        allottedDays += Integer.parseInt(durationStr);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load allotted days", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Load the panned vacation days as requested
     */
    private void loadPlannedVacationDays() {
        travelLogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                for (DataSnapshot trip : snapshot.getChildren()) {
                    String startDateStr = trip.child("startTime").getValue(String.class);
                    String endDateStr = trip.child("endTime").getValue(String.class);

                    if (startDateStr == null || endDateStr == null) {
                        continue;
                    }

                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);

                        long difference = endDate.getTime() - startDate.getTime();
                        int tripDays = (int) (difference / (1000 * 60 * 60 * 24)) + 1;

                        totalPlannedDays += tripDays;

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing trip dates", Toast.LENGTH_SHORT).show();
                    }
                }
                tvTotalDays.setText("Planned Vacation Days: " + totalPlannedDays);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load travel logs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Visualizes the trip days
     */
    private void visualizeTripDays() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(totalPlannedDays, "Planned Days"));
        entries.add(new PieEntry(Math.max(allottedDays - totalPlannedDays, 0), "Remaining Days"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        List<Integer> colors = new ArrayList<>();
        colors.add(requireContext().getColor(R.color.blue));
        colors.add(requireContext().getColor(R.color.orange));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(requireContext().getColor(R.color.white));

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f%%", value);
            }
        });

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Trip Overview");
        pieChart.setCenterTextSize(18f);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.getDescription().setEnabled(false);

        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12f);
        legend.setFormSize(10f);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        pieChart.invalidate();
    }
}