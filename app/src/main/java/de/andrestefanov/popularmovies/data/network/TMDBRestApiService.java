package de.andrestefanov.popularmovies.data.network;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.network.model.ReviewsPage;
import de.andrestefanov.popularmovies.data.network.model.Video;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface TMDBRestApiService {

    @GET("movie/popular")
    Call<List<Movie>> loadPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/top_rated")
    Call<List<Movie>> loadTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<List<Video>> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/{id}/reviews")
    Call<ReviewsPage> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);
}
