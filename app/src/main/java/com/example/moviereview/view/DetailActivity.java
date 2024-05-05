package com.example.moviereview.view;

import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviereview.R;
import com.example.moviereview.controller.CategoryEachFilmListController;
import com.example.moviereview.model.Genre;
import com.example.moviereview.model.MovieApiService;
import com.example.moviereview.model.MovieDetail;
import com.example.moviereview.model.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView titleTxt, movieRateTxt, movieTimeTxt, movieSummaryInfo, releaseDate;
    private int idFilm;
    private ImageView pic2, backImg;
    private RecyclerView.Adapter adapterActorList, adapterCategory;
    private RecyclerView recyclerViewActors, recyclerViewCategory;
    private NestedScrollView scrollView;
    private MovieApiService movieApiService;
    private RecyclerView recyclerViewTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idFilm = getIntent().getIntExtra("id", 0);
        movieApiService = RetrofitInstance.getRetrofitInstance().create(MovieApiService.class);
        initView();
        sendRequest();
    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        Call<MovieDetail> call = movieApiService.searchMovie(idFilm,"fd99a8ce13e0f7024121db73fe267e19");
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail movieDetail = response.body();
                    if (movieDetail != null) {
                        System.out.println();

                        movieDetail.setPosterURL("https://image.tmdb.org/t/p/w500" + movieDetail.getPosterPath());

                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);

                        titleTxt.setText(movieDetail.getTitle());
                        movieRateTxt.setText(valueOf(movieDetail.getVoteAverage()));
                        movieTimeTxt.setText(format("%d minutes", movieDetail.getRuntime()));
                        releaseDate.setText(movieDetail.getReleaseDate());
                        movieSummaryInfo.setText(movieDetail.getOverview());
                        Glide.with(DetailActivity.this).load(movieDetail.getPosterURL()).into(pic2);

                        if(movieDetail.getGenres().size() > 0){
                            adapterCategory = new CategoryEachFilmListController((ArrayList<Genre>) movieDetail.getGenres());
                            recyclerViewCategory.setAdapter(adapterCategory);
                        }

                    }
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void initView() {
        titleTxt = findViewById(R.id.movieNameText);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView2);
        pic2 = findViewById(R.id.imageView7);
        movieRateTxt = findViewById(R.id.movieStar);
        movieTimeTxt = findViewById(R.id.movieTime);
        movieSummaryInfo = findViewById(R.id.movieSummary);
        backImg = findViewById(R.id.imageView);
        recyclerViewCategory = findViewById(R.id.genresList);
        releaseDate = findViewById(R.id.textView8);

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        backImg.setOnClickListener(v -> finish());
    }
}