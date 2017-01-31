package com.example.android.popularmovies.data;

import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.model.MoviesPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface TMDBApiService {

    @GET("movie/{path}")
    Call<MoviesPage> loadMoviesPage(@Path("path") String path, @Query("api_key") String apiKey, @Query("page") int page, @Query("language") String language);

    @GET("movie/{id}")
    Call<MovieDetails> loadMovieDetails(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);

}
