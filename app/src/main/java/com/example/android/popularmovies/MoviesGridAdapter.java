package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MoviesPage;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.popularmovies.Preferences.KEY_MOVIES_FILTER_KEY;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_POPULAR;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_TOP_RATED;

class MoviesGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Callback<MoviesPage>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MoviesGridAdapter";

    private SharedPreferences preferences;

    private final Context context;

    private final TMDBClient client;

    static final int VIEW_TYPE_MOVIE = 1;

    static final int VIEW_TYPE_PROGRESS = 2;

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class MovieImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final ImageView mImageView;

        MovieImageViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v;
        }
    }

    private final View.OnClickListener clickListener;

    private ArrayList<Movie> movies;

    MoviesGridAdapter(final Context context, TMDBClient client, View.OnClickListener clickListener) {
        this.context = context;
        this.client = client;
        this.movies = new ArrayList<>();
        this.clickListener = clickListener;
        this.preferences = context.getSharedPreferences(Preferences.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        this.preferences.registerOnSharedPreferenceChangeListener(this);

        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_MOVIE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_view_poster, parent, false);
            v.setOnClickListener(clickListener);
            return new MovieImageViewHolder(v);
        } else {
            return new ProgressViewHolder(inflater.inflate(R.layout.loading_indicator, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position < movies.size()) {
            final Movie movie = movies.get(position);
            MovieImageViewHolder viewHolder = (MovieImageViewHolder) holder;
            viewHolder.mImageView.setTag(movie);
            viewHolder.mImageView.setImageResource(android.R.color.transparent);

            client.loadPoster(movie.getPosterPath(), viewHolder.mImageView);

            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewHolder.mImageView.startAnimation(animation);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < movies.size()) {
            return VIEW_TYPE_MOVIE;
        } else {
            return VIEW_TYPE_PROGRESS;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size() + 1;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof MovieImageViewHolder) {
            ((MovieImageViewHolder)holder).mImageView.clearAnimation();
        }
    }

    public void loadMoreMovies(int page) {
        String currentFilter = preferences.getString(KEY_MOVIES_FILTER_KEY, MOVIES_FILTER_POPULAR);
        if (currentFilter.equals(MOVIES_FILTER_POPULAR)) {
            client.loadPopularMoviesPage(page, this, Locale.getDefault().getLanguage());
        } else if (currentFilter.equals(MOVIES_FILTER_TOP_RATED)) {
            client.loadTopRatedMoviesPage(page, this, Locale.getDefault().getLanguage());
        }
    }

    @Override
    public void onResponse(Call<MoviesPage> call, Response<MoviesPage> response) {
        movies.addAll(response.body().getResults());
        notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<MoviesPage> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_MOVIES_FILTER_KEY)) {
            Log.d(TAG, "onSharedPreferenceChanged: clear data");
            movies.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < movies.size())
            return movies.get(position).getId();
        else
            return 0;
    }
}
