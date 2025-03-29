package com.example.assignment2.Utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment2.Model.MovieModel;
import com.example.assignment2.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<MovieModel> movies;
    Context context;
    View movieView = null;
    MovieClickListener clickListener;

    public MyAdapter(Context contextList, List<MovieModel> movies) {
        this.movies = movies;
        this.context = context;
    }

    public void setClickListener(MovieClickListener myClickListener){
        this.clickListener = myClickListener ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(movieView, this.clickListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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
