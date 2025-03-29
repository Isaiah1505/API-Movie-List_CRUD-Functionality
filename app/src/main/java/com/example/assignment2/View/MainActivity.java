package com.example.assignment2.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment2.Model.MovieModel;
import com.example.assignment2.Utility.MovieClickListener;
import com.example.assignment2.Utility.MyAdapter;
import com.example.assignment2.ViewModel.MovieViewModel;
import com.example.assignment2.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements MovieClickListener {

    ActivityMainBinding binding;
    MovieViewModel movieViewModel;
    MyAdapter myAdapter;
    List<MovieModel> movieList = new ArrayList<>();
    MovieModel movieDetails = new MovieModel();
    ArrayList<String> movieDetailsArrList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binds to the layout associated
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // using the root of the layout
        setContentView(binding.getRoot());

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        movieViewModel.getMovieInfo().observe(this, movieModel -> {

            movieList = movieModel;
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.searchResultsRecycler.setLayoutManager(layoutManager);

            myAdapter = new MyAdapter(getApplicationContext(), movieList);
            binding.searchResultsRecycler.setAdapter(myAdapter);

            myAdapter.setClickListener(this);

        });


        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userQuery = String.valueOf(binding.searchBar.getText());
                if(!userQuery.isBlank()) {
                    movieViewModel.moviesSearch(userQuery);
                }else{
                    Toast.makeText(MainActivity.this, "Input field can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v, int pos) {
        Toast.makeText(this, "You chose: "+movieList.get(pos).getTitle(), Toast.LENGTH_SHORT).show();
        movieViewModel.specificMovieSearch(movieList.get(pos).getId());
        movieDetailsArrList.clear();
        movieViewModel.getMovieDetails().observe(this, movieModelDetails -> {
            movieDetails = movieModelDetails;
            movieDetailsArrList.add(movieDetails.getTitle());
            movieDetailsArrList.add(movieDetails.getDescription());
            movieDetailsArrList.add(movieDetails.getMoviePosterURL());
            movieDetailsArrList.add(movieDetails.getYearReleased());
            movieDetailsArrList.add(movieDetails.getGenres());
            movieDetailsArrList.add(movieDetails.getRuntime());
            movieDetailsArrList.add(movieDetails.getRating());

            Intent intObj = new Intent(MainActivity.this, DetailedViewActivity.class);
            intObj.putExtra("movieDetails", movieDetailsArrList);
            startActivity(intObj);
        });

    }
}