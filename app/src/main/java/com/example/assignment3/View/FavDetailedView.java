package com.example.assignment3.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.assignment2.R;
import com.example.assignment2.databinding.ActivityFavDetailedViewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class FavDetailedView extends AppCompatActivity {

    ActivityFavDetailedViewBinding binding;
    ArrayList<String> itemDetails = new ArrayList<>();
    //FirebaseAuth userAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("FavMovies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavDetailedViewBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Intent favItemDetails = getIntent();
        itemDetails = favItemDetails.getStringArrayListExtra("favItemDetails");
        String currUserId = favItemDetails.getStringExtra("UsernameUID");

        if(itemDetails != null) {
            binding.title.setText(itemDetails.get(1));
            binding.year.setText(itemDetails.get(2));
            binding.runtime.setText(itemDetails.get(3));
            binding.genres.setText(itemDetails.get(4));
            Glide.with(this)
                    .load(itemDetails.get(5))
                    .override(binding.fullPoster.getWidth(), binding.fullPoster.getHeight()).
                    placeholder(R.drawable.noimgfound)
                    .into(binding.fullPoster);
            binding.rating.setText(itemDetails.get(6));
            binding.plot.setText(itemDetails.get(7));
        }

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "Update Btn");
                updateItemDescription(currUserId);
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "Delete Btn");
                deleteMovieItem(currUserId);
                Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });

        binding.backToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "Back Btn");
                Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });

    }

    private void updateItemDescription(String docName){

        collectionReference.document(docName)
                .update(
                        itemDetails.get(0)+".description",String.valueOf(binding.plot.getText())
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("test", "Description Updated.");
                        Toast.makeText(getApplicationContext(), itemDetails.get(1)+"'s Description Has Been Updated.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("test", "Couldn't Update Description.\nEr: "+e);
                    }
                });
    }

    private void deleteMovieItem(String docName){

        collectionReference.document(docName)
                .update(itemDetails.get(0), FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("test", "Item Has Been Deleted.");
                        Toast.makeText(getApplicationContext(), itemDetails.get(1)+" Has Been Deleted.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("test", "Couldn't Delete Item.\nEr: "+e);
                    }
                });
    }
}