package com.example.assignment2.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment2.Model.MovieModel;
import com.example.assignment2.Utility.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {

    String apiUrl = "https://www.omdbapi.com/?apikey=6d409e2b";

    private final MutableLiveData<List<MovieModel>> movieInfo = new MutableLiveData<>();
    private final MutableLiveData<MovieModel> movieDetails = new MutableLiveData<>();
    List<MovieModel> movieModelList = new ArrayList<>();
    MovieModel movieModelDetails = new MovieModel();

    public LiveData<List<MovieModel>> getMovieInfo(){
        return movieInfo;
    }

    public LiveData<MovieModel> getMovieDetails() {
        return movieDetails;
    }

    public void moviesSearch(String userQuery){

        String urlWithQuery = apiUrl+"&s="+userQuery;

        ApiClient.get(urlWithQuery, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("test", "Failed.");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                // needs to be response.body().string() to get the actual response from API
                String movieResponse = response.body().string();

                JSONObject jsonResponse = null;
                try{
                    jsonResponse = new JSONObject(movieResponse);
                    JSONArray jsonArr = jsonResponse.getJSONArray("Search");
                    for (int i = 0 ; i < jsonArr.length(); ++i) {
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

                }catch (JSONException e){
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
                Log.i("test", "Failed get");
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
}
