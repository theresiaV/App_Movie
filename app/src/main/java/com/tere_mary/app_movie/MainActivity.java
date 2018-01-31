package com.tere_mary.app_movie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tere_mary.app_movie.API.APIClient;
import com.tere_mary.app_movie.API.iAPIService;
import com.tere_mary.app_movie.Model.Movie;
import com.tere_mary.app_movie.Model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //API KEY from themoviedb.org
    private final static String API_KEY = "01ab80596cbd9b2f4aa653d7bde58c15";

    RelativeLayout rlayout;

    RecyclerView recyclerView;
    Button popular, upcoming,search;
    EditText searchtext;

    List<Movie> movies;
    MovieAdapter adapter;
    ProgressDialog pd;
    int temp;
    String judulsearch;

    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlayout = (RelativeLayout) findViewById(R.id.RelativeLayout_Awal);
        //koneksi
        if (!konekNet()) {
            snackbar.make(rlayout, "No Connection Internet", Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            snackbar.make(rlayout, "Connection Internet", Snackbar.LENGTH_SHORT)
                    .show();
        }

        popular = (Button) findViewById(R.id.Button_Popular);
        upcoming = (Button) findViewById(R.id.Button_Upcoming);
        search = (Button) findViewById(R.id.Button_Search);
        searchtext = (EditText) findViewById(R.id.EditText_Search);

        temp = 0;

        initViews();
        loadJSON();

        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = 0;

                initViews();
                loadJSON();
            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = 1;

                initViews();
                loadJSON();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judulsearch = searchtext.getText().toString();

                if (!"".equals(judulsearch)){
                    temp = 2;

                    initViews();
                    loadJSON();
                } else {
                    snackbar.make(rlayout, "Input the movie title", Snackbar.LENGTH_SHORT)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    searchtext.setCursorVisible(true);
                                }
                            })
                            .show();
                }
            }
        });
    }

    private Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof  Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews(){
        pd = new ProgressDialog(this);
        pd.setMessage("Loading . . .");
        pd.setTitle("Harap Menunggu");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_Awal);
        movies = new ArrayList<>();
        adapter = new MovieAdapter(movies, R.layout.list_movie, getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        loadJSON();
    }

    //loadJson
    private void loadJSON(){
        try {

            //API KEY-nya ada atau tidak
            if (API_KEY.isEmpty()) {
                snackbar.make(rlayout, "No API Key found", Snackbar.LENGTH_LONG)
                        .show();

                pd.dismiss();
                return;
            }

            APIClient apiClient = new APIClient();

            iAPIService apiService = APIClient.getClient().create(iAPIService.class);
            retrofit2.Call<MovieResponse> call = null;

            if (temp == 0){
                call = apiService.getPopularMovies(API_KEY);
                Log.d("","Respon temp" + temp);
            } else if (temp == 1){
                call = apiService.getUpcomingMovies(API_KEY);
                Log.d("","Respon temp" + temp);
            } else if (temp == 2){
                call = apiService.searchMovie(API_KEY,judulsearch);
                Log.d("","Respon temp" + temp);
            }

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MovieResponse> call, Response<MovieResponse> response) {
                    //cek response-nya 200 atau tidak
                    int status = response.code();
                    Log.d(TAG, "Response body : " + status);
                    if (status == 200){
                        pd.dismiss();
                    }
                    else {
                        snackbar.make(rlayout, "You respon not 200", Snackbar.LENGTH_SHORT);
                    }

                    //cek movie number yang keambil
                    List<Movie> movies = response.body().getResults();
                    if (movies.size() != 0){
                        Log.d(TAG, "Number of movies received : " + movies.size());
                        Toast.makeText(MainActivity.this, "Number of movies received : " + movies.size(), Toast.LENGTH_LONG).show();
                        recyclerView.setAdapter(new MovieAdapter(movies, R.layout.list_movie, getApplicationContext()));
                    } else {
                        snackbar.make(rlayout, "Movie not found",Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        searchtext.setText("");
                                        searchtext.setFocusable(true);

                                        temp = 0;
                                        initViews();
                                        loadJSON();
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });

        }catch (Exception e){
            Log.d( "Message Error", "Error");
            Toast.makeText(this, "Error load JSON", Toast.LENGTH_SHORT).show();
        }
    }

    //untuk tahu apa koneksi ke internet atau tidak
    public boolean konekNet(){
        ConnectivityManager konek = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = konek.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return  true;
        }
        else { return false; }
    }
}
