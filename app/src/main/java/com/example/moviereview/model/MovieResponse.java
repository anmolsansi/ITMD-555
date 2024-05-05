package com.example.moviereview.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieResponse {
    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}
