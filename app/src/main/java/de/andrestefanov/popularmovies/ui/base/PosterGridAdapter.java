package de.andrestefanov.popularmovies.ui.base;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;

public class PosterGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PosterGridAdapter";

    private List<Movie> data;

    private OnMovieSelectedListener onMovieSelectedListener;

    protected PosterGridAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PosterViewHolder)holder).setMovie(data.get(position));
    }

    @Override
    public final int getItemCount() {
        return data.size();
    }

    public final void setData(List<Movie> data) {
        this.data = data;
        notifyDataSetChanged();
        onDataChanged();
    }

    public final List<Movie> getData() {
        return data;
    }

    void setOnMovieSelectedListener(OnMovieSelectedListener onMovieSelectedListener) {
        this.onMovieSelectedListener = onMovieSelectedListener;
    }

    protected void onDataChanged() {

    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements Callback {

        @BindView(R.id.imageview_poster)
        ImageView posterView;

        @BindView(R.id.imageview_poster_progress)
        ProgressBar progress;

        private PosterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            posterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMovieSelectedListener != null)
                        onMovieSelectedListener.onMovieSelected(data.get(getAdapterPosition()));
                }
            });
        }

        public void setMovie(final Movie movie) {
            this.progress.setVisibility(View.VISIBLE);
            PopularMoviesApp.dataManager().loadPoster(movie.getPosterPath(), posterView, this);
        }

        @Override
        public void onSuccess() {
            this.progress.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError() {
            Log.e(TAG, "onError() called");
        }
    }

}
