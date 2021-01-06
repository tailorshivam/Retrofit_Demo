package com.example.retrofitdemo.movie.Interface;

import com.example.retrofitdemo.movie.model.Movie;
import com.example.retrofitdemo.movie.model.MovieDetails;
import com.example.retrofitdemo.movie.model.MovieImages;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("movie/upcoming")
    Call<Movie> getMovieList(@Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path(value = "id", encoded = true) String id, @Query("api_key") String api_key);

    @GET("movie/{id}/images")
    Call<MovieImages> getMovieImages(@Path(value = "id", encoded = true) String id, @Query("api_key") String api_key);
}
