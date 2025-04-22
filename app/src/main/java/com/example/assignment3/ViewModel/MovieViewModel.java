package com.example.assignment3.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3.Model.MovieModel;
import com.example.assignment3.Utility.ApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {

    String apiUrl = "https://www.omdbapi.com/?apikey=6d409e2b";

    private final MutableLiveData<List<MovieModel>> movieInfo = new MutableLiveData<>();
    private final MutableLiveData<MovieModel> movieDetails = new MutableLiveData<>();
    private final MutableLiveData<List<MovieModel>> favMovieList = new MutableLiveData<>();
    List<MovieModel> movieModelList = new ArrayList<>();
    MovieModel movieModelDetails = new MovieModel();
    List<MovieModel> userFavMovieList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth userAuth;
    private CollectionReference collectionReference = db.collection("FavMovies");


    public LiveData<List<MovieModel>> getMovieInfo(){
        return movieInfo;
    }

    public LiveData<MovieModel> getMovieDetails() {
        return movieDetails;
    }

    public LiveData<List<MovieModel>> getFavMovieList(){
        return favMovieList;
    }

    public void moviesSearch(String userQuery){

        String urlWithQuery = apiUrl+"&s="+userQuery;

        ApiClient.get(urlWithQuery, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("test", "Failed. Err: "+e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    Log.i("test","Search started");

                    JSONObject jsonResponse = null;
                    try {
                        String movieResponse = "";
                        if (response.body() != null) {
                            // needs to be response.body().string() to get the actual response from API
                            movieResponse = response.body().string();
                        }

                        if(new JSONObject(movieResponse).getString("Response").equals("True")) {

                            movieModelList.clear();
                            jsonResponse = new JSONObject(movieResponse);
                            JSONArray jsonArr = jsonResponse.getJSONArray("Search");
                            for (int i = 0; i < jsonArr.length(); ++i) {
                                MovieModel movieModel = new MovieModel();
                                JSONObject objElement = jsonArr.getJSONObject(i);
                                movieModel.setTitle(objElement.getString("Title"));
                                movieModel.setYearReleased(objElement.getString("Year"));
                                movieModel.setMoviePosterURL(objElement.getString("Poster"));
                                movieModel.setId(objElement.getString("imdbID"));
                                movieModelList.add(movieModel);

                            }

                            // posts movieModel list into LiveData obj to persist
                            movieInfo.postValue(movieModelList);
                        }else{
                            // checking if there's an error from get request and posts to MainActivity
                            String errorTitle = new JSONObject(movieResponse).getString("Error");
                            MovieModel err = new MovieModel();
                            err.setTitle("Error");
                            err.setDescription(errorTitle);
                            movieModelList.clear();
                            movieModelList.add(err);
                            movieInfo.postValue(movieModelList);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


            }
        });
    }

    public void specificMovieSearch(String movieId){

        String idQueryUrl = apiUrl+"&i="+movieId;

        ApiClient.get(idQueryUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("test", "Failed get: "+e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String movieFullDetails = response.body().string();
                JSONObject specificJsonResponse = null;
                try{

                    specificJsonResponse = new JSONObject(movieFullDetails);

                    movieModelDetails.setId(specificJsonResponse.getString("imdbID"));
                    movieModelDetails.setTitle(specificJsonResponse.getString("Title"));
                    movieModelDetails.setYearReleased(specificJsonResponse.getString("Year"));
                    movieModelDetails.setRuntime(specificJsonResponse.getString("Runtime"));
                    movieModelDetails.setGenres(specificJsonResponse.getString("Genre"));
                    movieModelDetails.setRating(specificJsonResponse.getString("imdbRating"));
                    movieModelDetails.setMoviePosterURL(specificJsonResponse.getString("Poster"));
                    movieModelDetails.setDescription(specificJsonResponse.getString("Plot"));

                    movieDetails.postValue(movieModelDetails);

                } catch (JSONException e) {
                    Log.i("test", "Error: \n"+e);
                    throw new RuntimeException(e);
                }

            }
        });

    }
    // attempts to get the favourite movie list using the UserUID to search for it
    public void getFavMovieList(String docName){

        collectionReference.document(docName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        try {
                            // parsing a normal Object using JSON, then putting each into a MovieModel ArrayList
                            List<Object> moviesCollection = Arrays.asList(documentSnapshot.getData().values().toArray());
                            JSONArray moviesArray = new JSONArray(moviesCollection);
                            for (int i = 0; i < moviesArray.length(); ++i){
                                MovieModel favMovieItem = new MovieModel();
                                JSONObject movieItem = moviesArray.getJSONObject(i);

                                favMovieItem.setId(movieItem.getString("id"));
                                favMovieItem.setTitle(movieItem.getString("title"));
                                favMovieItem.setYearReleased(movieItem.getString("yearReleased"));
                                favMovieItem.setRuntime(movieItem.getString("runtime"));
                                favMovieItem.setGenres(movieItem.getString("genres"));
                                favMovieItem.setMoviePosterURL(movieItem.getString("moviePosterURL"));
                                favMovieItem.setRating(movieItem.getString("rating"));
                                favMovieItem.setDescription(movieItem.getString("description"));

                                userFavMovieList.add(favMovieItem);
                                Log.i("test","Title: "+favMovieItem.getTitle()+"\n");
                            }

                            favMovieList.postValue(userFavMovieList);

                        }catch (NullPointerException e){
                            Log.i("test", "Null field in movies list.\n Er: "+e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("test", "Couldn't get list of movies.\nEr: "+e);
                    }
                });
    }

}
