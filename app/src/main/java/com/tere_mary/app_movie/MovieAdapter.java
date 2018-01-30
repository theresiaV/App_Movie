package com.tere_mary.app_movie;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import com.tere_mary.app_movie.Model.Movie;

import java.util.List;

/**
 * Created by Theresia V A Mary G on 1/26/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    List<Movie> movies;
    int rowLayout;
    Context context;

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        //untuk list movie
        RelativeLayout moviesLayout;
        ImageButton backbg;
        TextView judul;
        RatingBar rating;

        public MovieViewHolder(final View v){
            super(v);

            //untuk list movie
            moviesLayout = (RelativeLayout) v.findViewById(R.id.RelativeLayout_ListMovie);
            judul = (TextView) v.findViewById(R.id.TextView_JudulMovie);
            rating = (RatingBar) v.findViewById(R.id.ratingBar);
            backbg = (ImageButton) v.findViewById(R.id.ImageButton_ListMovie1);
        }
    }

    public  MovieAdapter(List<Movie> movies, int rowLayout, Context context){
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder,final int position) {
        holder.judul.setText(movies.get(position).getTitle());
        double av = movies.get(position).getVoteAverage();
        final float rat = (float) (av / 10f * 5f);
        holder.rating.setRating(rat);
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movies.get(position).getBackdropPath())
                .resize(230, 220)
                .into(holder.backbg);

        holder.backbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Detail_Movie.class);
                intent.putExtra("detailjudul",movies.get(position).getTitle());
                intent.putExtra("orijudul",movies.get(position).getOriginalTitle());
                intent.putExtra("gbr",movies.get(position).getBackdropPath());
                intent.putExtra("rating",movies.get(position).getVoteAverage());
                intent.putExtra("language",movies.get(position).getOriginalLanguage());
                intent.putExtra("release",movies.get(position).getReleaseDate());
                intent.putExtra("sinopsis",movies.get(position).getOverview());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(view.getContext(), "To Detail Movie", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
