package com.example.moviereview.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("trending/movie/day")
    Call<MovieResponse> getLatestMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetail> searchMovie(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> searchMovies(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("/")
    Call<MovieResponse> searchMoviesOMDB(@Query("api_key") String apiKey, @Query("t") String query);

}

