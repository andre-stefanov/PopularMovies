package de.andrestefanov.popularmovies.data;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.andrestefanov.popularmovies.BuildConfig;
import de.andrestefanov.popularmovies.model.MovieDetails;
import de.andrestefanov.popularmovies.model.MoviesPage;
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
                .baseUrl(TMDBConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(apiOkHttpClient)
                .build();
        this.apiService = retrofit.create(TMDBApiService.class);

        this.picasso = new Picasso.Builder(context).build();
    }

    public void loadPopularMoviesPage(int page, final Callback<MoviesPage> callback) {
        Log.d(TAG, "loadPopularMoviesPage() called with: page = [" + page + "], callback = [" + callback + "]");
        apiService.loadMoviesPage(
                TMDBConstants.TMDB_POPULAR_MOVIES_PATH,
                BuildConfig.TMBD_API_KEY,
                page,
                Locale.getDefault().getLanguage())
                .enqueue(callback);
    }

    public void loadTopRatedMoviesPage(int page, final Callback<MoviesPage> callback) {
        Log.d(TAG, "loadTopRatedMoviesPage() called with: page = [" + page + "], callback = [" + callback + "]");
        apiService.loadMoviesPage(
                TMDBConstants.TMDB_TOP_RATED_MOVIES_PATH,
                BuildConfig.TMBD_API_KEY,
                page,
                Locale.getDefault().getLanguage())
                .enqueue(callback);
    }

    public void loadPoster(String posterPath, ImageView imageView) {
        loadPoster(posterPath, imageView, null);
    }

    public void loadPoster(String posterPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.load(TMDBConstants.TMDB_POSTER_BASE_URL + posterPath)
                .error(de.andrestefanov.popularmovies.R.drawable.ic_error)
                .into(imageView, callback);
    }

    public void loadBackdrop(String backdropPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.cancelRequest(imageView);
        picasso.load(TMDBConstants.TMDB_BACKDROP_BASE_URL + backdropPath)
                .into(imageView, callback);
    }

    public void loadMovieDetails(int movieId, Callback<MovieDetails> callback) {
        Call<MovieDetails> call = this.apiService.loadMovieDetails(
                movieId,
                BuildConfig.TMBD_API_KEY,
                Locale.getDefault().getLanguage());
        call.enqueue(callback);
    }

}
