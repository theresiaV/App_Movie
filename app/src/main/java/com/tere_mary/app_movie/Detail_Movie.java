package com.tere_mary.app_movie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

/**
 * Created by Theresia V A Mary G on 1/29/2018.
 */

public class Detail_Movie extends AppCompatActivity{

    //untuk detail movie
    ImageView detailimage;
    RatingBar detailrating;
    TextView detailjudul,judulori,detailrelease,languageori,sinopsis;

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

        Intent intentDetail = getIntent();
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
}
