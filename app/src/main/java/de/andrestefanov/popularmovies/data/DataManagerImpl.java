package de.andrestefanov.popularmovies.data;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import java.util.List;

import de.andrestefanov.popularmovies.data.db.DbHelper;
import de.andrestefanov.popularmovies.data.network.ApiHelper;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import de.andrestefanov.popularmovies.data.network.model.Review;
import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.data.prefs.PreferencesHelper;

public class DataManagerImpl implements DataManager {

    private final DbHelper mDbHelper;
    private final PreferencesHelper preferencesHelper;
    private final ApiHelper mApiHelper;

    public DataManagerImpl(DbHelper dbHelper,
                           PreferencesHelper preferencesHelper,
                           ApiHelper apiHelper)
    {
        mDbHelper = dbHelper;
        this.preferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }

    @Override
    public void loadMovie(int movieId, retrofit2.Callback<MovieDetails> callback) {
        mApiHelper.loadMovie(movieId, callback);
    }

    @Override
    public void loadPopularMovies(int page, retrofit2.Callback<List<Movie>> callback) {
        mApiHelper.loadPopularMovies(page, callback);
    }

    @Override
    public void loadTopRatedMovies(int page, retrofit2.Callback<List<Movie>> callback) {
        mApiHelper.loadTopRatedMovies(page, callback);
    }

    @Override
    public void loadPoster(String posterPath, ImageView imageView) {
        mApiHelper.loadPoster(posterPath, imageView);
    }

    @Override
    public void loadPoster(String posterPath, ImageView imageView, Callback callback) {
        mApiHelper.loadPoster(posterPath, imageView, callback);
    }

    @Override
    public void loadBackdrop(String backdropPath, ImageView imageView) {
        mApiHelper.loadBackdrop(backdropPath, imageView);
    }

    @Override
    public void loadBackdrop(String backdropPath, ImageView imageView, Callback callback) {
        mApiHelper.loadBackdrop(backdropPath, imageView, callback);
    }

    @Override
    public void loadMovieVideos(int movieId, retrofit2.Callback<List<Video>> callback) {
        mApiHelper.loadMovieVideos(movieId, callback);
    }

    @Override
    public void loadVideoImage(String videoKey, ImageView imageView) {
        mApiHelper.loadVideoImage(videoKey, imageView);
    }

    @Override
    public void loadVideoImage(String videoKey, ImageView imageView, Callback callback) {
        mApiHelper.loadVideoImage(videoKey, imageView, callback);
    }

    @Override
    public void loadMovieReviews(int movieId, int page, retrofit2.Callback<List<Review>> callback) {
        mApiHelper.loadMovieReviews(movieId, page, callback);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return preferencesHelper.getSharedPreferences();
    }

    @Override
    public void setMovieFilter(MoviesFilter filter) {
        preferencesHelper.setMovieFilter(filter);
    }

    @Override
    public MoviesFilter getMovieFilter() {
        return preferencesHelper.getMovieFilter();
    }

    @Override
    public Cursor getFavorites() {
        return mDbHelper.getFavorites();
    }

    @Override
    public boolean isFavorite(Movie movie) {
        return mDbHelper.isFavorite(movie);
    }

    @Override
    public boolean toggleFavorite(Movie movie) {
        return mDbHelper.toggleFavorite(movie);
    }
}
