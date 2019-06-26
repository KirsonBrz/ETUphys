package com.etuspace.firebase.example.fireeats.java.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etuspace.firebase.example.fireeats.java.model.Lab;
import com.etuspace.firebase.example.fireeats.R;
import com.etuspace.firebase.example.fireeats.java.util.RestaurantUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class AllLabAdapter extends FirestoreAdapter<AllLabAdapter.ViewHolder> {

    public interface OnRestaurantSelectedListener {

        void onRestaurantSelected(DocumentSnapshot restaurant);

    }

    private OnRestaurantSelectedListener mListener;

    public AllLabAdapter(Query query, OnRestaurantSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_lab, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.restaurantItemImage)
        ImageView imageView;

        @BindView(R.id.restaurantItemName)
        TextView nameView;

        @BindView(R.id.restaurantItemRating)
        MaterialRatingBar ratingBar;

        @BindView(R.id.restaurantItemNumRatings)
        TextView numRatingsView;

        @BindView(R.id.restaurantItemPrice)
        TextView priceView;

        @BindView(R.id.restaurantItemCategory)
        TextView categoryView;

        @BindView(R.id.restaurantItemCity)
        TextView cityView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnRestaurantSelectedListener listener) {

            Lab lab = snapshot.toObject(Lab.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(imageView.getContext())
                    .load(lab.getPhoto())
                    .into(imageView);

            nameView.setText(lab.getName());
            ratingBar.setRating((float) lab.getAvgRating());
            cityView.setText(lab.getCity());
            categoryView.setText(lab.getCategory());
            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
                    lab.getNumRatings()));
            priceView.setText(RestaurantUtil.getPriceString(lab));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRestaurantSelected(snapshot);
                    }
                }
            });
        }

    }
}
