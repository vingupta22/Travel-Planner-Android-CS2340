package com.example.CS2340FAC_Team41.view;


import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.CS2340FAC_Team41.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;

import model.Accommodation;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

    private List<Accommodation> accommodations = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accommodation, parent, false);
        return new AccommodationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.locationTextView.setText(accommodation.getLocation());
        holder.roomTypeTextView.setText(accommodation.getRoomType());
        holder.checkInDateTextView.setText(accommodation.getCheckInDate());
        holder.checkOutDateTextView.setText(accommodation.getCheckOutDate());
        holder.numberOfRoomsTextView.setText(String.valueOf(accommodation.getNumberOfRooms()));

        // Check if the reservation is upcoming or expired and set background color accordingly
        if (isExpired(accommodation.getCheckOutDate())) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2")); // Light red for expired
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#C8E6C9")); // Light green for upcoming
        }
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    private boolean isExpired(String checkOutDate) {
        try {
            Date today = new Date();
            Date checkOut = dateFormat.parse(checkOutDate);
            return checkOut != null && checkOut.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView roomTypeTextView;
        private final TextView checkInDateTextView;
        private final TextView checkOutDateTextView;
        private final TextView numberOfRoomsTextView;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            roomTypeTextView = itemView.findViewById(R.id.roomTypeTextView);
            checkInDateTextView = itemView.findViewById(R.id.checkInDateTextView);
            checkOutDateTextView = itemView.findViewById(R.id.checkOutDateTextView);
            numberOfRoomsTextView = itemView.findViewById(R.id.numberOfRoomsTextView);
        }
    }
}