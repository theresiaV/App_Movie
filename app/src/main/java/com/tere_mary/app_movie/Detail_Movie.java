package com.tere_mary.app_movie;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.tere_mary.app_movie.API.APIClient;
import com.tere_mary.app_movie.API.iAPIService;
import com.tere_mary.app_movie.Model.Trailer;
import com.tere_mary.app_movie.Model.TrailerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Theresia V A Mary G on 1/29/2018.
 */

public class Detail_Movie extends AppCompatActivity{

    //API KEY from themoviedb.org
    private final static String API_KEY = "01ab80596cbd9b2f4aa653d7bde58c15";

    //untuk detail movie
    ImageView detailimage;
    RatingBar detailrating;
    TextView detailjudul,judulori,detailrelease,languageori,sinopsis;
    int id;

    //trailer
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        //untuk detail movie
        detailimage = (ImageView) findViewById(R.id.ImageView_DetailMovie);
        detailrating = (RatingBar) findViewById(R.id.RatingBar_DetailMovie);
        detailjudul = (TextView) findViewById(R.id.TextView_JudulDetailMovie);
        judulori = (TextView) findViewById(R.id.TextView_JudulOriDetailMovie);
        languageori = (TextView) findViewById(R.id.TextView_OriLanguage);
        detailrelease = (TextView) findViewById(R.id.TextView_DetailReleaseDate);
        sinopsis = (TextView) findViewById(R.id.TextView_Sinopsis);

        recyclerView = (RecyclerView) findViewById(R.id.RecylclerView_Trailer);

        Intent intentDetail = getIntent();
            id = intentDetail.getExtras().getInt("id");

            String sinop = intentDetail.getExtras().getString("sinopsis");
            sinopsis.setText(sinop);

            String judul = intentDetail.getExtras().getString("detailjudul");
            detailjudul.setText(judul);

            String judulo = intentDetail.getExtras().getString("orijudul");
            judulori.setText(judulo);

            String languageo = intentDetail.getExtras().getString("language");
            languageori.setText(languageo);

            String releaseo = intentDetail.getExtras().getString("release");
            detailrelease.setText(releaseo);

            String detailgbr = intentDetail.getExtras().getString("gbr");
            Picasso.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + detailgbr)
                    .resize(500,350)
                    .placeholder(R.drawable.detail)
                    .into(detailimage);

            Double vote = intentDetail.getExtras().getDouble("rating");
                final float rat = (float) (vote / 10f * 5f);
                detailrating.setRating(rat);

            loadTrailer(id);
    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle("Description Movie");
                    isShow = true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void loadTrailer(int id) {
        APIClient client = new APIClient();
        iAPIService apiService = client.getClient().create(iAPIService.class);
        Call<TrailerResponse> trailerResponse = apiService.getTrailers(id, API_KEY);
        trailerResponse.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.isSuccessful()) {
                    int status = response.code();
                    Log.d("Response Trailer ", "" + status);
                    Log.d("Data ", "" + response.body().getResults());
                   recyclerView.setAdapter(new TrailerAdapter(Detail_Movie.this, response.body().getResults()));
                    Log.d("Adapter ", "RecyclerView Adapter have");
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
            }
        });
    }
}
