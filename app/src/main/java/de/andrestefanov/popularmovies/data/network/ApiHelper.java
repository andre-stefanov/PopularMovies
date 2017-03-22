package de.andrestefanov.popularmovies.data.network;

import android.widget.ImageView;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import de.andrestefanov.popularmovies.data.network.model.Review;
import de.andrestefanov.popularmovies.data.network.model.ReviewsPage;
import de.andrestefanov.popularmovies.data.network.model.Video;
import retrofit2.Callback;

public interface ApiHelper {

    void loadMovie(int movieId, Callback<MovieDetails> callback);

    void loadPopularMovies(int page, Callback<List<Movie>> callback);

    void loadTopRatedMovies(int page, Callback<List<Movie>> callback);

    void loadPoster(String posterPath, ImageView imageView);

    void loadPoster(String posterPath, ImageView imageView, com.squareup.picasso.Callback callback);

    void loadBackdrop(String backdropPath, ImageView imageView);

    void loadBackdrop(String backdropPath, ImageView imageView, com.squareup.picasso.Callback callback);

    void loadMovieVideos(int movieId, Callback<List<Video>> callback);

    void loadVideoImage(String videoKey, ImageView imageView);

    void loadVideoImage(String videoKey, ImageView imageView, com.squareup.picasso.Callback callback);

    void loadMovieReviews(int movieId, int page, Callback<List<Review>> callback);

}
