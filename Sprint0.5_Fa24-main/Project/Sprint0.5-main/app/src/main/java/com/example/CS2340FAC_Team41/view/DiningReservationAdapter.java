package com.example.CS2340FAC_Team41.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import com.example.CS2340FAC_Team41.R;

public class DiningReservationAdapter extends RecyclerView.Adapter<DiningReservationAdapter.ViewHolder> {
    private ArrayList<DiningReservation> diningList;
    private Context context;

    /**
     * Constructs a new DiningReservationAdapter with the specified list of dining reservations.
     *
     * @param diningList the list of dining reservations to be managed by the adapter
     */
    public DiningReservationAdapter(ArrayList<DiningReservation> diningList) {
        this.diningList = diningList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_dining_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiningReservation reservation = diningList.get(position);
        holder.bind(reservation);

        // Check if the reservation is upcoming or expired based on the current date
        Date currentDate = new Date();
        if (reservation.getDateTime().after(currentDate)) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.upcoming_reservation));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.expired_reservation));
        }
    }

    @Override
    public int getItemCount() {
        return diningList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationView;
        private TextView dateView;
        private TextView timeView;
        private TextView websiteView;
        private TextView reviewsView;

        /**
         * Initializes the ViewHolder with the specified item view and its UI components.
         *
         * @param itemView the view representing a single dining reservation item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationView = itemView.findViewById(R.id.locationView);
            dateView = itemView.findViewById(R.id.dateView);
            timeView = itemView.findViewById(R.id.timeView);
            websiteView = itemView.findViewById(R.id.websiteView);
            reviewsView = itemView.findViewById(R.id.reviewsView);
        }

        /**
         * Binds the dining reservation data to the ViewHolder's UI components.
         *
         * @param reservation the DiningReservation to display
         */
        public void bind(DiningReservation reservation) {
            locationView.setText(reservation.getLocation());
            dateView.setText(reservation.getDate());
            timeView.setText(reservation.getTime());
            websiteView.setText(reservation.getWebsite());
            reviewsView.setText(reservation.getReviews());
        }
    }
}