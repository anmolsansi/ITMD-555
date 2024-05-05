package com.example.moviereview.controller;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.moviereview.R;
import com.example.moviereview.model.Genre;
import com.example.moviereview.model.Movie;
import com.example.moviereview.view.DetailActivity;

import java.util.*;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Movie> movies;
    private Context context;

    public SearchAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        // Set movie title
        holder.movieTitle.setText(movie.getTitle());

        // Load movie poster using Glide or Picasso
        Glide.with(holder.itemView)
                .load(movie.getPosterURL())
                .into(holder.moviePoster);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", movies.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
        }
    }
}

