package de.andrestefanov.popularmovies.data.network;

import android.content.Context;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.andrestefanov.popularmovies.BuildConfig;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.network.model.Review;
import de.andrestefanov.popularmovies.data.network.model.ReviewsPage;
import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.utils.Constants;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelperImpl implements ApiHelper {

    private TMDBRestApiService tmdbRestApiService;

    private Picasso picasso;

    public ApiHelperImpl(Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Movie>>(){}.getType(), new MovieListDeserializer())
                .registerTypeAdapter(new TypeToken<List<Video>>(){}.getType(), new VideoListDeserializer())
                .create();
        tmdbRestApiService = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.TMDB_BASE_URL)
                .build()
                .create(TMDBRestApiService.class);
        picasso = Picasso.with(context);
    }

    @Override
    public void loadPopularMovies(int page, Callback<List<Movie>> callback) {
        tmdbRestApiService
                .loadPopularMovies(
                    BuildConfig.TMBD_API_KEY,
                    Locale.getDefault().getLanguage(),
                    page)
                .enqueue(callback);
    }

    @Override
    public void loadTopRatedMovies(int page, Callback<List<Movie>> callback) {
        tmdbRestApiService.loadTopRatedMovies(
                BuildConfig.TMBD_API_KEY,
                Locale.getDefault().getLanguage(),
                page).enqueue(callback);
    }

    @Override
    public void loadPoster(String posterPath, ImageView imageView) {
        loadPoster(posterPath, imageView, null);
    }

    @Override
    public void loadPoster(String posterPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.load(Constants.TMDB_POSTER_BASE_URL + posterPath).into(imageView, callback);
    }

    @Override
    public void loadBackdrop(String backdropPath, ImageView imageView) {
        loadBackdrop(backdropPath, imageView, null);
    }

    @Override
    public void loadBackdrop(String backdropPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.load(Constants.TMDB_BACKDROP_BASE_URL + backdropPath).into(imageView, callback);
    }

    @Override
    public void loadMovieVideos(int movieId, Callback<List<Video>> callback) {
        tmdbRestApiService.getMovieVideos(movieId, BuildConfig.TMBD_API_KEY, Locale.getDefault().getLanguage()).enqueue(callback);
    }

    @Override
    public void loadVideoImage(String videoKey, ImageView imageView) {
        loadVideoImage(videoKey, imageView, null);
    }

    @Override
    public void loadVideoImage(String videoKey, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.load(String.format(Constants.YT_IMAGE_URL_TMPL, videoKey)).into(imageView, callback);
    }

    @Override
    public void loadMovieReviews(int movieId, int page, Callback<ReviewsPage> callback) {
        tmdbRestApiService.getMovieReviews(movieId, BuildConfig.TMBD_API_KEY, "en-US", page).enqueue(callback);
    }
}
