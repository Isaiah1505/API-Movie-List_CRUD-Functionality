package com.example.assignment3.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment2.R;
import com.example.assignment3.Model.MovieModel;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavViewHolder> {

    List<MovieModel> favMovies;
    Context context;
    View movieView = null;

    FavMovieClickListener clickListener;

    public FavAdapter(Context context, List<MovieModel> favMovies){
        this.context = context;
        this.favMovies = favMovies;
    }

    public void setClickListener(FavMovieClickListener myClickListener){
        this.clickListener = myClickListener ;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        FavViewHolder myViewHolder = new FavViewHolder(movieView, this.clickListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {

        MovieModel movie = favMovies.get(position);

        holder.title.setText(movie.getTitle());
        Glide.with(movieView).
                load(movie.getMoviePosterURL()).
                override(holder.poster.getWidth(), holder.poster.getHeight()).
                placeholder(R.drawable.noimgfound).
                into(holder.poster);
        holder.rating.setText(movie.getRating());
    }

    @Override
    public int getItemCount() {
        return favMovies.size();
    }
}
