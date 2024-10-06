// HomeActivity.java
package com.example.CS2340FAC_Team41.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.CS2340FAC_Team41.R;

public class HomeActivity extends AppCompatActivity {

    // Define navigation items
    private LinearLayout navDestinations;
    private LinearLayout navLogistics;
    private LinearLayout navDining;
    private LinearLayout navAccommodations;
    private LinearLayout navTransportation;
    private LinearLayout navTravel;
    private TextView lblDestinations;
    private TextView lblLogistics;
    private TextView lblDining;
    private TextView lblAccommodations;
    private TextView lblTransportation;
    private TextView lblTravel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navDestinations = findViewById(R.id.nav_destinations);
        navLogistics = findViewById(R.id.nav_logistics);
        navDining = findViewById(R.id.nav_dining);
        navAccommodations = findViewById(R.id.nav_accommodations);
        navTransportation = findViewById(R.id.nav_transportation);
        navTravel = findViewById(R.id.nav_travel);

        lblDestinations = navDestinations.findViewById(R.id.nav_destinations_label);
        lblLogistics = navLogistics.findViewById(R.id.nav_logistics_label);
        lblDining = navDining.findViewById(R.id.nav_dining_label);
        lblAccommodations = navAccommodations.findViewById(R.id.nav_accommodations_label);
        lblTransportation = navTransportation.findViewById(R.id.nav_transportation_label);
        lblTravel = navTravel.findViewById(R.id.nav_travel_label);

        selectNavItem(navDestinations, lblDestinations);
        loadFragment(new DestinationsFragment());

        navDestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(navDestinations, lblDestinations);
                loadFragment(new DestinationsFragment());
            }
        });

        navLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(navLogistics, lblLogistics);
                loadFragment(new LogisticsFragment());
            }
        });

        navDining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(navDining, lblDining);
                loadFragment(new DiningFragment());
            }
        });

        navAccommodations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(navAccommodations, lblAccommodations);
                loadFragment(new AccommodationsFragment());
            }
        });

        navTransportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(navTransportation, lblTransportation);
                loadFragment(new TransportationFragment());
            }
        });

        navTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNavItem(navTravel, lblTravel);
                loadFragment(new TravelCommunityFragment());
            }
        });
    }

    /**
     * Loads the fragments
     *
     * @param fragment the fragment that is loaded
     */
    private void loadFragment(Fragment fragment) {
        // Replace the existing fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Selects the navigation item
     *
     * @param selectedNav the layoutpage selected
     * @param selectedLabel the label seleced
     */
    private void selectNavItem(LinearLayout selectedNav, TextView selectedLabel) {
        // Reset all navigation items to unselected state
        resetNavItems();

        // Highlight the selected navigation item
        selectedNav.setBackgroundColor(getResources().getColor(R.color.white));
        selectedLabel.setTextColor(getResources().getColor(R.color.black));
    }


    /**
     * Resets nav menu
     *
     */
    private void resetNavItems() {

        LinearLayout[] navItems = {navDestinations, navLogistics, navDining, navAccommodations,
                navTransportation, navTravel};
        TextView[] navLabels = {lblDestinations, lblLogistics, lblDining, lblAccommodations,
                lblTransportation, lblTravel};

        for (int i = 0; i < navItems.length; i++) {
            navItems[i].setBackgroundColor(getResources().getColor(R.color.white));
            navLabels[i].setTextColor(getResources().getColor(R.color.black));
        }
    }
}