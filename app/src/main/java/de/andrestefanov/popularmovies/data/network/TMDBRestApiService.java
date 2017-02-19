package de.andrestefanov.popularmovies.data.network;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import de.andrestefanov.popularmovies.data.network.model.ReviewsPage;
import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface TMDBRestApiService {

    @GET("movie/{id}")
    Call<MovieDetails> loadMovie(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/" + Constants.TMDB_POPULAR_MOVIES_PATH)
    Call<List<Movie>> loadPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/" + Constants.TMDB_TOP_RATED_MOVIES_PATH)
    Call<List<Movie>> loadTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<List<Video>> loadMovieVideos(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/{id}/reviews")
    Call<ReviewsPage> loadMovieReviews(@Path("id") int id, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);
}
