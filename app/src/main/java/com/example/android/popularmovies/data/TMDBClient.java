package com.example.android.popularmovies.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieDetails;
import com.example.android.popularmovies.model.MoviesPage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public void loadPopularMoviesPage(int page, final Callback<MoviesPage> callback) {
        asyncLoadMoviesPageAndPosters(TMDBConstants.TMDB_POPULAR_MOVIES_PATH, page, callback);
    }

    public void loadTopRatedMoviesPage(int page, final Callback<MoviesPage> callback) {
        asyncLoadMoviesPageAndPosters(TMDBConstants.TMDB_TOP_RATED_MOVIES_PATH, page, callback);
    }

    public void loadPoster(String posterPath, ImageView imageView) {
        loadPoster(posterPath, imageView, null);
    }

    public void loadPoster(String posterPath, ImageView imageView, com.squareup.picasso.Callback callback) {
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

    private void asyncLoadMoviesPageAndPosters(String path, int page, final Callback<MoviesPage> callback) {
        Call<MoviesPage> call = apiService.loadMoviesPage(
                path,
                BuildConfig.TMBD_API_KEY,
                page,
                Locale.getDefault().getLanguage());
        Callback<MoviesPage> tmp = new Callback<MoviesPage>() {
            @Override
            public void onResponse(Call<MoviesPage> call, Response<MoviesPage> response) {
                new LoadPostersTask(call, response, callback).execute();
            }

            @Override
            public void onFailure(Call<MoviesPage> call, Throwable t) {
                callback.onFailure(call, t);
            }
        };
        call.enqueue(tmp);
    }

    private class LoadPostersTask extends AsyncTask<Void, Void, Boolean> {

        private Call<MoviesPage> call;

        private Response<MoviesPage> response;

        private Callback<MoviesPage> callback;

        LoadPostersTask(Call<MoviesPage> call, Response<MoviesPage> response, Callback<MoviesPage> callback) {
            this.call = call;
            this.response = response;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                for (Movie movie : response.body().getResults())
                    picasso.load(TMDB_POSTER_BASE_URL + movie.getPosterPath())
                            .config(Bitmap.Config.RGB_565)
                            .noFade()
                            .get();
            } catch (IOException e) {
                callback.onFailure(call, e);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean succeeded) {
            super.onPostExecute(succeeded);
            if (succeeded)
                callback.onResponse(call, response);
        }
    }

}
