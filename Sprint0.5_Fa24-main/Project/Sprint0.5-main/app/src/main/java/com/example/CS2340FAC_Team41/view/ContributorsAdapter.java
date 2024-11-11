// File: ContributorsAdapter.java
package model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ContributorViewHolder> {

    private List<Contributor> contributorsList;
    private Context context;

    /**
     * Constructs a new ContributorsAdapter with the specified context and list of contributors.
     *
     * @param context the context in which the adapter is used
     * @param contributorsList the list of contributors to display
     */
    public ContributorsAdapter(Context context, List<Contributor> contributorsList) {
        this.context = context;
        this.contributorsList = contributorsList;
    }

    @NonNull
    @Override
    public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a LinearLayout as the root layout for each item
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        // Create TextView for Contributor Email
        TextView tvContributorEmail = new TextView(context);
        tvContributorEmail.setId(View.generateViewId());
        tvContributorEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvContributorEmail.setTextColor(Color.BLACK);

        // Add TextView to LinearLayout
        layout.addView(tvContributorEmail, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        return new ContributorViewHolder(layout, tvContributorEmail);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorViewHolder holder, int position) {
        Contributor contributor = contributorsList.get(position);
        holder.tvContributorEmail.setText(contributor.getEmail());
    }

    @Override
    public int getItemCount() {
        return contributorsList.size();
    }

    static class ContributorViewHolder extends RecyclerView.ViewHolder {
        TextView tvContributorEmail;

        /**
         * Initializes a new instance of ContributorViewHolder with the specified item view and email text view.
         *
         * @param itemView          the view representing a single item in the list
         * @param tvContributorEmail the TextView displaying the contributor's email
         */
        public ContributorViewHolder(@NonNull View itemView, TextView tvContributorEmail) {
            super(itemView);
            this.tvContributorEmail = tvContributorEmail;
        }
    }

    // Helper method to convert dp to pixels
    /**
     * Converts a value in density-independent pixels (dp) to pixels (px).
     *
     * @param dp the value in dp to convert
     * @return the equivalent value in pixels
     */
    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
