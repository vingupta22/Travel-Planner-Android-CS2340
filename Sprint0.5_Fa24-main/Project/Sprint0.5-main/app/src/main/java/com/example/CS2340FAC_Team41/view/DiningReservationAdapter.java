package com.example.CS2340FAC_Team41.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.CS2340FAC_Team41.R;


import java.util.ArrayList;

public class DiningReservationAdapter extends ArrayAdapter<DiningReservation> {

    public DiningReservationAdapter(@NonNull Context context, ArrayList<DiningReservation> reservations) {
        super(context, 0, reservations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dining_reservation, parent, false);
        }

        DiningReservation reservation = getItem(position);

        TextView locationTextView = convertView.findViewById(R.id.text_view_location);
        TextView websiteTextView = convertView.findViewById(R.id.text_view_website);
        TextView timeTextView = convertView.findViewById(R.id.text_view_time);
        TextView reviewsTextView = convertView.findViewById(R.id.text_view_reviews);

        if (reservation != null) {
            locationTextView.setText("Location: " + reservation.location);
            websiteTextView.setText("Website: " + reservation.website);
            timeTextView.setText("Time: " + reservation.time);
            reviewsTextView.setText("Reviews: " + reservation.reviews);
        }

        return convertView;
    }
}

