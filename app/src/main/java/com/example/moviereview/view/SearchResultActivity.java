package com.example.moviereview.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereview.R;
import com.example.moviereview.controller.SearchAdapter;
import com.example.moviereview.model.Movie;
import com.example.moviereview.model.MovieApiService;
import com.example.moviereview.model.MovieResponse;
import com.example.moviereview.model.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    private TextView searchResultTextView;
    private MovieApiService movieApiService;
    private List<Movie> searchResults;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter searchAdapter;
    private ProgressBar progressBar;
    private ImageView imageView;
    private LinearLayout explorerLayout, profileLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        String queryText = getIntent().getStringExtra("query");
        explorerLayout = findViewById(R.id.explorerSearch);
        profileLayout = findViewById(R.id.profileSearch);

        imageView = findViewById(R.id.imageViewSearch);
        progressBar = findViewById(R.id.progressBarSearch);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint(queryText);

        imageView.setOnClickListener(v -> finish());


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchResults = new ArrayList<>();
        movieApiService = RetrofitInstance.getRetrofitInstance().create(MovieApiService.class);
        searchMovies(queryText);


        explorerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResultActivity.this, HomepageActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResultActivity.this, Profile.class));
            }
        });



    }

    private void searchMovies(String query) {
        Call<MovieResponse> call = movieApiService.searchMovies("fd99a8ce13e0f7024121db73fe267e19", query = query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        searchResults = movieResponse.getResults();

                        for (Movie m : searchResults) {
                            m.setPosterURL("https://image.tmdb.org/t/p/w500" + m.getPoster_path());
                        }

                        progressBar.setVisibility(View.GONE);
                        searchAdapter = new SearchAdapter(searchResults);
                        recyclerView.setAdapter(searchAdapter);
                    }
                } else {
                    Log.e("API Error", "Failed to fetch search results");
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch search results: " + t.getMessage());
            }
        });
    }
}
