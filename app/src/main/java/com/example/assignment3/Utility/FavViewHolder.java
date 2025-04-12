package com.example.assignment3.Utility;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;

public class FavViewHolder extends RecyclerView.ViewHolder {

    ImageView poster;
    TextView title;
    TextView rating;
    FavMovieClickListener clickListener;
    public FavViewHolder(@NonNull View favMovieItem, FavMovieClickListener clickListener) {
        super(favMovieItem);

        poster = favMovieItem.findViewById(R.id.moviePoster);
        title = favMovieItem.findViewById(R.id.movieTitle);
        rating = favMovieItem.findViewById(R.id.movieRelease);

        this.clickListener = clickListener;

        favMovieItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, getAdapterPosition(), true);
            }
        });
    }
}
