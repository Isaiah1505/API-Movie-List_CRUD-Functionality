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

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    List<MovieModel> movies;
    Context context;
    View movieView = null;
    SearchMovieClickListener clickListener;

    public SearchAdapter(Context contextList, List<MovieModel> movies) {
        this.movies = movies;
        this.context = contextList;
    }

    public void setClickListener(SearchMovieClickListener myClickListener){
        this.clickListener = myClickListener ;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        SearchViewHolder myViewHolder = new SearchViewHolder(movieView, this.clickListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        MovieModel movie = movies.get(position);

        holder.title.setText(movie.getTitle());
        Glide.with(movieView).
                load(movie.getMoviePosterURL()).
                override(holder.poster.getWidth(), holder.poster.getHeight()).
                placeholder(R.drawable.noimgfound).
                into(holder.poster);
        holder.year.setText(movie.getYearReleased());


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
