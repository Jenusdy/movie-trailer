package com.example.movietrailer.retrofit;

import com.example.movietrailer.model.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
    Call<MovieModel> getPopularMovie(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") String page
    );

}
