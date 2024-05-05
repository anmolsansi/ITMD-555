package com.example.moviereview.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.example.moviereview.R;
import com.example.moviereview.controller.FilmListController;
import com.example.moviereview.controller.SliderController;
import com.example.moviereview.model.Movie;
import com.example.moviereview.model.MovieApiService;
import com.example.moviereview.model.MovieResponse;
import com.example.moviereview.model.RetrofitInstanceOMDB;
import com.example.moviereview.model.RetrofitInstanceTMDB;
import com.example.moviereview.model.SliderItems;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterRecommended, adapterLatest, adapterUpcoming;
    private LinearLayout profileLayout;
    private RecyclerView rvRecommended, rvLatest, rvUpcoming;
    private ProgressBar lBar1, lBar2, lBar3;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private RequestQueue requestQueue;
    private MovieApiService movieApiServiceTMDB, movieApiServiceOMDB;
    private List<Movie> movies;
    private List<Movie> recommendedMovies;
    private List<Movie> upcomingMovies;
    private List<Movie> latestMovies;
    private EditText searchEditText;
    private SearchView searchView;

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        initView();
        movies = new ArrayList<>();
        recommendedMovies = new ArrayList<>();
        movieApiServiceTMDB = RetrofitInstanceTMDB.getRetrofitInstance().create(MovieApiService.class);
        movieApiServiceOMDB = RetrofitInstanceOMDB.getRetrofitInstance().create(MovieApiService.class);
        fetchTopMovies();
        sendRequestRecommendedMovies();
        sendRequestUpcomingMovies();
        sendRequestLatestMovies();
        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomepageActivity.this, SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    private void sendRequestRecommendedMovies() {
        lBar1.setVisibility(View.VISIBLE);

        Call<MovieResponse> callOMDB = movieApiServiceOMDB.searchMoviesOMDB("fd99a8ce13e0f7024121db73fe267e19", "query");
        Call<MovieResponse> call = movieApiServiceTMDB.getNowPlayingMovies("fd99a8ce13e0f7024121db73fe267e19");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {

                        recommendedMovies = movieResponse.getResults();
                        for (Movie m : recommendedMovies) {
                            m.setPosterURL("https://image.tmdb.org/t/p/w500" + m.getPoster_path());
                        }
                        lBar1.setVisibility(View.GONE);
                        adapterRecommended = new FilmListController(recommendedMovies);
                        rvRecommended.setAdapter(adapterRecommended);
                    }
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPagerSlider);
        searchView = findViewById(R.id.searchView);

        rvRecommended = findViewById(R.id.recyclerView1);
        rvRecommended.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvUpcoming = findViewById(R.id.recyclerView2);
        rvUpcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvLatest = findViewById(R.id.recyclerView3);
        float scale = getResources().getDisplayMetrics().density;
        int paddingBottomInPixels = (int) (10 * scale + 0.5f);
        rvLatest.setPadding(rvLatest.getPaddingLeft(), rvLatest.getPaddingTop(), rvLatest.getPaddingRight(), paddingBottomInPixels);
        rvLatest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        lBar1 = findViewById(R.id.progressBar1);
        lBar2 = findViewById(R.id.progressBar2);
        lBar3 = findViewById(R.id.progressBar3);

        profileLayout = findViewById(R.id.profile);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomepageActivity.this, Profile.class));
            }
        });

    }

    private void performSearch(String query) {
        Call<MovieResponse> call = movieApiServiceTMDB.searchMovies("fd99a8ce13e0f7024121db73fe267e19", query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        recommendedMovies = movieResponse.getResults();
                        for (Movie m : recommendedMovies) {
                            m.setPosterURL("https://image.tmdb.org/t/p/w500" + m.getPoster_path());
                        }
                        lBar1.setVisibility(View.GONE);
                        adapterRecommended = new FilmListController(recommendedMovies);
                        rvRecommended.setAdapter(adapterRecommended);
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
            }
        });
        Intent intent = new Intent(HomepageActivity.this, SearchResultActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }


    private void sendRequestUpcomingMovies() {
        lBar2.setVisibility(View.VISIBLE);

        Call<MovieResponse> call = movieApiServiceTMDB.getUpcomingMovies("fd99a8ce13e0f7024121db73fe267e19");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {

                        upcomingMovies = movieResponse.getResults();
                        for (Movie m : upcomingMovies) {
                            m.setPosterURL("https://image.tmdb.org/t/p/w500" + m.getPoster_path());
                        }
                        lBar2.setVisibility(View.GONE);
                        adapterUpcoming = new FilmListController(upcomingMovies);
                        rvUpcoming.setAdapter(adapterUpcoming);
                    }
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
            }
        });


    }

    private void sendRequestLatestMovies() {
        lBar3.setVisibility(View.VISIBLE);

        Call<MovieResponse> call = movieApiServiceTMDB.getLatestMovies("fd99a8ce13e0f7024121db73fe267e19");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {

                        latestMovies = movieResponse.getResults();
                        for (Movie m : latestMovies) {
                            m.setPosterURL("https://image.tmdb.org/t/p/w500" + m.getPoster_path());
                        }
                        lBar3.setVisibility(View.GONE);
                        adapterLatest = new FilmListController(latestMovies);
                        rvLatest.setAdapter(adapterLatest);
                    }
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
            }
        });


    }


    private void fetchTopMovies() {
        Call<MovieResponse> call = movieApiServiceTMDB.getTopRatedMovies("fd99a8ce13e0f7024121db73fe267e19");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        movies = movieResponse.getResults();
                        banners(movies);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void banners(List<Movie> movies) {
        List<SliderItems> sliderItemsList = new ArrayList<>();

        for (Movie movie : movies) {
            movie.setPosterURL("https://image.tmdb.org/t/p/w500" + movie.getPoster_path());
            SliderItems sliderItem = new SliderItems(movie.getPosterURL());
            sliderItemsList.add(sliderItem);
        }

        SliderController sliderAdapter = new SliderController(sliderItemsList, viewPager2);
        viewPager2.setAdapter(sliderAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(4);

        int padding = getResources().getDimensionPixelOffset(R.dimen.viewPager_padding);
        viewPager2.setPadding(padding, 0, padding, 0);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
            }
        });
    }
}
