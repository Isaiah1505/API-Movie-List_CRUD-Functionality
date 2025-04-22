package com.example.assignment3.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment2.R;
import com.example.assignment2.databinding.ActivityMainBinding;
import com.example.assignment3.Model.MovieModel;
import com.example.assignment3.Utility.FavAdapter;
import com.example.assignment3.Utility.FavMovieClickListener;
import com.example.assignment3.Utility.SearchMovieClickListener;
import com.example.assignment3.Utility.SearchAdapter;
import com.example.assignment3.ViewModel.MovieViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SearchMovieClickListener, FavMovieClickListener {

    ActivityMainBinding binding;
    MovieViewModel movieViewModel;
    SearchAdapter searchAdapter;
    FavAdapter favAdapter;
    List<MovieModel> movieList = new ArrayList<>();
    MovieModel movieDetails = new MovieModel();
    ArrayList<String> movieDetailsArrList = new ArrayList<>();
    List<MovieModel> favMovieList = new ArrayList<>();
    ArrayList<String> favItem = new ArrayList<>();
    String currUserId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth userAuth;

    private CollectionReference collectionReference = db.collection("FavMovies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binds to the layout associated
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // using the root of the layout
        setContentView(binding.getRoot());
        // getting currentUsers UID to display their favourites list
        userAuth = FirebaseAuth.getInstance();
        currUserId = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        // when movieViewModel.moviesSearch(userQuery); is called, this is observed
        // for the data posted to the liveData object, then append to recyclerView
        movieViewModel.getMovieInfo().observe(this, movieModel -> {

            if(!movieModel.get(0).getTitle().equals("Error")) {

                binding.errText.setText("");
                movieList = movieModel;
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                binding.searchResultsRecycler.setLayoutManager(layoutManager);

                searchAdapter = new SearchAdapter(getApplicationContext(), movieList);
                binding.searchResultsRecycler.setAdapter(searchAdapter);

                searchAdapter.setClickListener(this);
            }else{
                binding.errText.setText(movieModel.get(0).getDescription());
            }

        });
        // observed so when movieViewModel.moviesSearch(userQuery); is called, the needed
        // data posted to the LiveData object can be append to RecyclerView
        movieViewModel.getFavMovieList().observe(this, userFavMovies ->{

            favMovieList = userFavMovies;
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.searchResultsRecycler.setLayoutManager(layoutManager);
            favAdapter = new FavAdapter(getApplicationContext(), userFavMovies);
            binding.searchResultsRecycler.setAdapter(favAdapter);

            favAdapter.setClickListener(this);

        });


        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // grabs from searchbar, does blank check then queries API for results
                String userQuery = String.valueOf(binding.searchBar.getText());
                if(!userQuery.isBlank()) {
                    movieList.clear();
                    movieViewModel.moviesSearch(userQuery);
                }else{
                    Toast.makeText(MainActivity.this, "Input field can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // signs out user and sends them back to the Login Page
                userAuth.signOut();
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutIntent);
                finish();
            }
        });

        binding.toggleFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // toggles favourites page to show favourites list and show what's needed
                Log.i("test", "Favourite Movies Displayed");
                if(binding.searchBar.getVisibility() != View.INVISIBLE) {
                    binding.searchResultsRecycler.setAdapter(null);
                    binding.searchBar.setVisibility(View.INVISIBLE);
                    binding.searchBtn.setVisibility(View.INVISIBLE);
                    binding.mainPageTitle.setText(R.string.favListTitle);
                    binding.toggleFavList.setText(R.string.showSearchListBtn);
                    movieViewModel.getFavMovieList(currUserId);
                }else{
                    binding.searchBar.setVisibility(View.VISIBLE);
                    binding.searchBtn.setVisibility(View.VISIBLE);
                    binding.mainPageTitle.setText(R.string.searchPageTitle);
                    binding.toggleFavList.setText(R.string.favListToggleBtn);
                    binding.searchResultsRecycler.setAdapter(null);
                }
            }
        });
    }
    // incase app is closed without logging out
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(userAuth.getCurrentUser() != null) {
            userAuth.signOut();
        }
    }
    //onClick for the Search List
    @Override
    public void onClick(View v, int pos) {
        Toast.makeText(this, "You chose: "+movieList.get(pos).getTitle(), Toast.LENGTH_SHORT).show();
        movieViewModel.specificMovieSearch(movieList.get(pos).getId());
        movieDetailsArrList.clear();
        // observes call made to get data posted to LiveData obj
        // then passes it to detailed view as an ArrayList
        movieViewModel.getMovieDetails().observe(this, movieModelDetails -> {
            movieDetails = movieModelDetails;
            movieDetailsArrList.add(movieDetails.getTitle());
            movieDetailsArrList.add(movieDetails.getDescription());
            movieDetailsArrList.add(movieDetails.getMoviePosterURL());
            movieDetailsArrList.add(movieDetails.getYearReleased());
            movieDetailsArrList.add(movieDetails.getGenres());
            movieDetailsArrList.add(movieDetails.getRuntime());
            movieDetailsArrList.add(movieDetails.getRating());
            movieDetailsArrList.add(movieDetails.getId());

            Intent intObj = new Intent(MainActivity.this, DetailedViewActivity.class);
            intObj.putExtra("movieDetails", movieDetailsArrList);
            startActivity(intObj);
        });

    }
    // onClick for the Favourite List
    @Override
    public void onClick(View v, int position, boolean inUse) {
        Toast.makeText(this, "You Clicked: "+favMovieList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        // Passes an ArrayList and UserUID to detailedFavItem to display clicked movie
        favItem.add(favMovieList.get(position).getId());
        favItem.add(favMovieList.get(position).getTitle());
        favItem.add(favMovieList.get(position).getYearReleased());
        favItem.add(favMovieList.get(position).getRuntime());
        favItem.add(favMovieList.get(position).getGenres());
        favItem.add(favMovieList.get(position).getMoviePosterURL());
        favItem.add(favMovieList.get(position).getRating());
        favItem.add(favMovieList.get(position).getDescription());

        Intent favItemInt = new Intent(getApplicationContext(), FavDetailedView.class);
        favItemInt.putExtra("favItemDetails",favItem);
        favItemInt.putExtra("UsernameUID",currUserId);
        startActivity(favItemInt);
    }

}