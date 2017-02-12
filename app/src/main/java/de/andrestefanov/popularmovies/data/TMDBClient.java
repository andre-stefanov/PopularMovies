package de.andrestefanov.popularmovies.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.andrestefanov.popularmovies.BuildConfig;
import de.andrestefanov.popularmovies.Constants;
import de.andrestefanov.popularmovies.model.MovieDetails;
import de.andrestefanov.popularmovies.model.MoviesPage;
import de.andrestefanov.popularmovies.model.Video;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBClient {

    private static final String TAG = "TMDBClient";

    private final TMDBApiService apiService;

    private final Picasso picasso;

    public TMDBClient(Context context) {
        OkHttpClient apiOkHttpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(apiOkHttpClient)
                .build();
        this.apiService = retrofit.create(TMDBApiService.class);

        this.picasso = new Picasso.Builder(context)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void loadPopularMoviesPage(int page, final Callback<MoviesPage> callback) {
        Log.d(TAG, "loadPopularMoviesPage() called with: page = [" + page + "], callback = [" + callback + "]");
        apiService.loadMoviesPage(
                Constants.TMDB_POPULAR_MOVIES_PATH,
                BuildConfig.TMBD_API_KEY,
                page,
                Locale.getDefault().getLanguage())
                .enqueue(callback);
    }

    public void loadTopRatedMoviesPage(int page, final Callback<MoviesPage> callback) {
        Log.d(TAG, "loadTopRatedMoviesPage() called with: page = [" + page + "], callback = [" + callback + "]");
        apiService.loadMoviesPage(
                Constants.TMDB_TOP_RATED_MOVIES_PATH,
                BuildConfig.TMBD_API_KEY,
                page,
                Locale.getDefault().getLanguage())
                .enqueue(callback);
    }

    public void loadPoster(String posterPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.load(Constants.TMDB_POSTER_BASE_URL + posterPath)
                .error(de.andrestefanov.popularmovies.R.drawable.ic_error)
                .into(imageView, callback);
    }

    public void loadBackdrop(String backdropPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.cancelRequest(imageView);
        picasso.load(Constants.TMDB_BACKDROP_BASE_URL + backdropPath)
                .into(imageView, callback);
    }

    public void loadMovieDetails(int movieId, Callback<MovieDetails> callback) {
        Call<MovieDetails> call = this.apiService.loadMovieDetails(
                movieId,
                BuildConfig.TMBD_API_KEY,
                Locale.getDefault().getLanguage());
        call.enqueue(callback);
    }

    public void loadMovieVideos(int movieId, Callback<Video.Page> callback) {
        Call<Video.Page> call = this.apiService.loadMovieVideos(
                movieId,
                BuildConfig.TMBD_API_KEY,
                Locale.getDefault().getLanguage());
        call.enqueue(callback);
    }

}
