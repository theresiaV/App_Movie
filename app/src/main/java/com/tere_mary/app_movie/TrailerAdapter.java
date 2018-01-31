package com.tere_mary.app_movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tere_mary.app_movie.Model.Trailer;
import java.util.List;

/**
 * Created by Theresia V A Mary G on 1/31/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder> {
    List<Trailer> trailers;
    Context context;

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        //untuk trailer movie
        CardView trailercard;
        TextView titletrailer;
        ImageView imagetrailer;

        public MovieViewHolder(View itemView) {
            super(itemView);

            //untuk id trailer movie
            trailercard = (CardView) itemView.findViewById(R.id.CardView_Trailer);
            titletrailer = (TextView) itemView.findViewById(R.id.TextView_titletrailer);
            imagetrailer = (ImageView) itemView.findViewById(R.id.ImageView_Trailer);
        }
    }

    public TrailerAdapter (Context context, List<Trailer> trailers){
        this.trailers = trailers;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.structure_trailers, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.titletrailer.setText(trailers.get(position).getName());
        holder.trailercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtube.com/watch?v=" + trailers.get(position).getKey()));
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
