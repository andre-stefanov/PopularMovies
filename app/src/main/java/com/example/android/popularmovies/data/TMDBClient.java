package com.example.android.popularmovies.data;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.model.MoviesPage;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.popularmovies.data.TMDBConstants.TMDB_BACKDROP_BASE_URL;
import static com.example.android.popularmovies.data.TMDBConstants.TMDB_POSTER_BASE_URL;

public class TMDBClient {

    private static final String TAG = "TMDBClient";

    private final boolean VERBOSE_HTTP_LOGGING = false;

    private final TMDBApiService apiService;

    private final Picasso picasso;

    public TMDBClient(Context context) {

        OkHttpClient.Builder apiClientBuilder = new OkHttpClient.Builder();

        if (VERBOSE_HTTP_LOGGING) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            apiClientBuilder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient apiOkHttpClient = apiClientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDBConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(apiOkHttpClient)
                .build();
        this.apiService = retrofit.create(TMDBApiService.class);

        this.picasso = new Picasso.Builder(context).build();
    }

    public void loadPopularMoviesPage(int page, Callback<MoviesPage> callback, String language) {
        Log.d(TAG, "loadPopularMoviesPage: " + page);
        Call<MoviesPage> call = this.apiService.loadPopularMoviesPage(
                BuildConfig.TMBD_API_KEY,
                page,
                language);
        call.enqueue(callback);
    }

    public void loadTopRatedMoviesPage(int page, Callback<MoviesPage> callback, String language) {
        Log.d(TAG, "loadTopRatedMoviesPage: " + page);
        Call<MoviesPage> call = this.apiService.loadTopRatedMoviesPage(
                BuildConfig.TMBD_API_KEY,
                page,
                language);
        call.enqueue(callback);
    }

    public void loadPoster(String posterPath, ImageView imageView) {
        loadPoster(posterPath, imageView, null);
    }

    public void loadPoster(String posterPath, ImageView imageView, com.squareup.picasso.Callback callback) {
        picasso.cancelRequest(imageView);
        imageView.clearAnimation();
        imageView.setImageDrawable(null);
        picasso.load(TMDB_POSTER_BASE_URL + posterPath)
                .error(R.drawable.ic_error)
                .noFade()
                .into(imageView, callback);
    }

    public void loadBackdrop(String backdropPath, ImageView imageView) {
        picasso.cancelRequest(imageView);
        picasso.load(TMDB_BACKDROP_BASE_URL + backdropPath)
                .into(imageView);
    }

    public void loadMovieDetails(int movieId, Callback<MovieDetails> callback) {
        Call<MovieDetails> call = this.apiService.loadMovieDetails(
                movieId,
                BuildConfig.TMBD_API_KEY,
                Locale.getDefault().getLanguage());
        call.enqueue(callback);
    }

}
