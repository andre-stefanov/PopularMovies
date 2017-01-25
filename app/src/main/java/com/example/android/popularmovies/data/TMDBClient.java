package com.example.android.popularmovies.data;

import android.content.Context;
import android.widget.ImageView;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.model.MoviesPage;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.popularmovies.data.TMDBConstants.TMDB_BACKDROP_BASE_URL;
import static com.example.android.popularmovies.data.TMDBConstants.TMDB_POSTER_BASE_URL;

public class TMDBClient {

    private final TMDBApiService apiService;

    private final Picasso picasso;

    public TMDBClient(Context context) {

        OkHttpClient.Builder apiClientBuilder = new OkHttpClient.Builder();

        int apiCacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache apiCache = new Cache(new File(context.getCacheDir(), "apiCache"), apiCacheSize);
        apiClientBuilder.cache(apiCache);

        OkHttpClient.Builder imagesClientBuilder = new OkHttpClient.Builder();

        long imagesCacheSize = 50 * 1024 * 1024; // 50 MiB
        Cache imagesCache = new Cache(new File(context.getCacheDir(), "imagesCache"), imagesCacheSize);
        imagesClientBuilder.cache(imagesCache);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            apiClientBuilder.addInterceptor(loggingInterceptor);
            imagesClientBuilder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient apiOkHttpClient = apiClientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDBConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(apiOkHttpClient)
                .build();
        this.apiService = retrofit.create(TMDBApiService.class);

        OkHttpClient imagesOkHttpClient = imagesClientBuilder.build();
        OkHttp3Downloader okHttpDownloader = new OkHttp3Downloader(imagesOkHttpClient);
        this.picasso = new Picasso.Builder(context).downloader(okHttpDownloader).build();
    }

    public void loadPopularMoviesPage(int page, Callback<MoviesPage> callback, String language) {
        Call<MoviesPage> call = this.apiService.loadPopularMoviesPage(
                BuildConfig.TMBD_API_KEY,
                page,
                language);
        call.enqueue(callback);
    }

    public void loadTopRatedMoviesPage(int page, Callback<MoviesPage> callback, String language) {
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
        picasso.load(TMDB_POSTER_BASE_URL + posterPath)
                .error(R.drawable.ic_error)
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
