package com.example.assignment3.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment2.databinding.ActivityDetailedViewBinding;
import com.example.assignment2.R;
import com.example.assignment3.Model.MovieModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailedViewActivity extends AppCompatActivity {

    ActivityDetailedViewBinding binding;
    ArrayList<String> movieInfo = new ArrayList<>();
    FirebaseAuth userAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("FavMovies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding layout and using bind's root to set the default view
        binding = ActivityDetailedViewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        userAuth = FirebaseAuth.getInstance();
        String currUserId = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();

        Intent intReceived = getIntent();
        movieInfo = intReceived.getStringArrayListExtra("movieDetails");

        if(movieInfo != null) {
            binding.title.setText(movieInfo.get(0));
            binding.plot.setText(movieInfo.get(1));
            Glide.with(this)
                    .load(movieInfo.get(2))
                    .override(binding.fullPoster.getWidth(), binding.fullPoster.getHeight()).
                    placeholder(R.drawable.noimgfound)
                            .into(binding.fullPoster);
            binding.year.setText(movieInfo.get(3));
            binding.genres.setText(movieInfo.get(4));
            binding.runtime.setText(movieInfo.get(5));
            binding.rating.setText(movieInfo.get(6));
        }


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intReturn = new Intent(DetailedViewActivity.this, MainActivity.class);
                startActivity(intReturn);
                finish();
            }
        });

        binding.addFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "Added to favourites");
                postMovieToFavList(currUserId);
            }
        });
    }

    private void postMovieToFavList(String userId){

        Map<String, MovieModel> movieMap = new HashMap<>();
        MovieModel chosenMovie = new MovieModel(movieInfo.get(7), movieInfo.get(0)
                ,movieInfo.get(3),movieInfo.get(5),movieInfo.get(4)
                ,movieInfo.get(2),movieInfo.get(6),movieInfo.get(1));

        movieMap.put(chosenMovie.getId(), chosenMovie);

        collectionReference.document(userId).set(movieMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailedViewActivity.this, chosenMovie.getTitle()+" has been added to User's Movie List!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("test", "Couldn't add Movie to User's List. Er: "+e);
                    }
                });
    }

}