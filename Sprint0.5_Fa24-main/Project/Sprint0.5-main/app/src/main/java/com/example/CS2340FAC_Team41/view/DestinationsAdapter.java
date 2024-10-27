package com.example.CS2340FAC_Team41.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.CS2340FAC_Team41.R;
import java.util.List;
import java.util.Map;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.DestinationViewHolder> {

    private final List<Map<String, String>> travelLogs;

    public DestinationsAdapter(List<Map<String, String>> travelLogs) {
        this.travelLogs = travelLogs;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Map<String, String> log = travelLogs.get(position);
        holder.locationTextView.setText(log.get("location"));
        holder.daysTextView.setText(log.get("days") + " days planned");
    }

    @Override
    public int getItemCount() {
        return travelLogs.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView, daysTextView;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.location_text);
            daysTextView = itemView.findViewById(R.id.days_text);
        }
    }
}
