package de.andrestefanov.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.data.TMDBClient;
import de.andrestefanov.popularmovies.model.Movie;
import de.andrestefanov.popularmovies.model.MoviesPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static de.andrestefanov.popularmovies.Preferences.KEY_MOVIES_FILTER_KEY;
import static de.andrestefanov.popularmovies.Preferences.MOVIES_FILTER_POPULAR;
import static de.andrestefanov.popularmovies.Preferences.MOVIES_FILTER_TOP_RATED;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> implements Callback<MoviesPage>, SharedPreferences.OnSharedPreferenceChangeListener {

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview_poster)
        ImageView poster;

        @BindView(R.id.backdrop_progress)
        ContentLoadingProgressBar progress;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnMovieClickListener {
        void onClick(ImageView imageView, Movie movie);
    }

    private static final String TAG = "MoviesAdapter";

    private static int TRESHHOLD = 20;

    private Context context;

    private SharedPreferences preferences;

    private TMDBClient client;

    private List<Movie> data;

    private boolean loading = false;

    private OnMovieClickListener clickListener;

    MoviesAdapter(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(Preferences.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        this.client = new TMDBClient(context);
        this.data = new ArrayList<>();

        this.preferences.registerOnSharedPreferenceChangeListener(this);

        loadModeData();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.view_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.progress.show();

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(holder.poster, data.get(holder.getAdapterPosition()));
                }
            }
        });

        client.loadPoster(data.get(position).getPosterPath(), holder.poster, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.progress.hide();
            }

            @Override
            public void onError() {

            }
        });

        if (!loading && position > data.size() - TRESHHOLD) {
            loadModeData();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadModeData() {
        loading = true;
        switch (preferences.getString(KEY_MOVIES_FILTER_KEY, MOVIES_FILTER_POPULAR)) {
            case MOVIES_FILTER_POPULAR:
                client.loadPopularMoviesPage(getNextPage(), this);
                break;
            case MOVIES_FILTER_TOP_RATED:
                client.loadTopRatedMoviesPage(getNextPage(), this);
                break;
        }
    }

    private int getNextPage() {
        return data.size() / 20 + 1;
    }

    @Override
    public void onResponse(Call<MoviesPage> call, Response<MoviesPage> response) {
        int oldSize = data.size();
        data.addAll(response.body().getResults());
        notifyItemRangeInserted(oldSize, response.body().getResults().size());
        loading = false;
    }

    @Override
    public void onFailure(Call<MoviesPage> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_MOVIES_FILTER_KEY)) {
            reset();
        }
    }

    private void reset() {
        data.clear();
        notifyDataSetChanged();
        loadModeData();
    }

    void setClickListener(OnMovieClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
