package com.tere_mary.app_movie.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Theresia V A Mary G on 1/31/2018.
 */

public class TrailerResponse {
    @SerializedName("results")
    private ArrayList<Trailer> results;

    public TrailerResponse(ArrayList<Trailer> results) {
        this.results = results;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }
}
