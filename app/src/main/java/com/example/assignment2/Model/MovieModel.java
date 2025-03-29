package com.example.assignment2.Model;

public class MovieModel {

    private String Id;
    private String title;
    private String yearReleased;
    private String runtime;
    private String genres;
    private String rating;
    private String moviePosterURL;
    private String description;

    public MovieModel(){};

    public MovieModel(String id, String title, String yearReleased, String moviePosterURL){
        Id = id;
        this.title = title;
        this.yearReleased = yearReleased;
        this.moviePosterURL = moviePosterURL;
    }

    public MovieModel(String id, String title, String yearReleased,
                      String runtime, String genres, String moviePosterURL,
                      String rating, String description) {
        Id = id;
        this.title = title;
        this.yearReleased = yearReleased;
        this.runtime = runtime;
        this.genres = genres;
        this.moviePosterURL = moviePosterURL;
        this.rating = rating;
        this.description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(String yearReleased) {
        this.yearReleased = yearReleased;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getMoviePosterURL() {
        return moviePosterURL;
    }

    public void setMoviePosterURL(String moviePosterURL) {
        this.moviePosterURL = moviePosterURL;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
