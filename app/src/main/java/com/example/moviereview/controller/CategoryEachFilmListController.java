package com.example.moviereview.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereview.R;
import com.example.moviereview.model.Genre;

import java.util.ArrayList;

public class CategoryEachFilmListController extends RecyclerView.Adapter<CategoryEachFilmListController.ViewHolder> {
        ArrayList<Genre> items;
        Context context;

        public CategoryEachFilmListController(ArrayList<Genre> items) {
                this.items = items;
        }

        @NonNull
        @Override
        public CategoryEachFilmListController.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                context = parent.getContext();
                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
                return new ViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryEachFilmListController.ViewHolder holder, int position) {
                System.out.println(items.get(position).getName().toString());
                holder.titleTxt.setText(items.get(position).getName().toString());
                System.out.println();

        }

        @Override
        public int getItemCount() {
                return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
                TextView titleTxt;
//                ImageView pic;

                public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        titleTxt = itemView.findViewById(R.id.titleText);
//                        pic = itemView.findViewById(R.id.imageViewFilm);
                }
        }
}
