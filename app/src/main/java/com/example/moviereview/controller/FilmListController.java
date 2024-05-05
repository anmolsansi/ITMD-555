package com.example.moviereview.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviereview.R;
import com.example.moviereview.model.Movie;
import com.example.moviereview.view.DetailActivity;

import java.util.List;

public class FilmListController extends RecyclerView.Adapter<FilmListController.ViewHolder> {
    List<Movie> movies;
    Context context;

    public FilmListController(List<Movie> movie) {
        this.movies = movie;
    }

    @NonNull
    @Override
    public FilmListController.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewholder_film, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListController.ViewHolder holder, int position) {
        holder.titleTxt.setText(movies.get(position).getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(10));

        Glide.with(context).load(movies.get(position).getPosterURL()).apply(requestOptions).into(holder.pic);

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
        TextView titleTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.imageViewFilm);
        }
    }
}
