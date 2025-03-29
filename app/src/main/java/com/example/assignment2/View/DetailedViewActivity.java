package com.example.assignment2.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.assignment2.R;
import com.example.assignment2.databinding.ActivityDetailedViewBinding;
import com.example.assignment2.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class DetailedViewActivity extends AppCompatActivity {

    ActivityDetailedViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding layout and using bind's root to set the default view
        binding = ActivityDetailedViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intReceived = getIntent();
        ArrayList<String> movieInfo = intReceived.getStringArrayListExtra("movieDetails");

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
            }
        });
    }

}