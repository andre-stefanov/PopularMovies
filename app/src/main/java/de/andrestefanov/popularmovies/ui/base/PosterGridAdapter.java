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

    private List<Movie> movies;

    private OnPosterClickListener clickListener;

    public PosterGridAdapter() {
        this.movies = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).setMovie(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setData(List<Movie> data) {
        movies = data;
        notifyDataSetChanged();
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void addData(Movie movie) {
        movies.add(movie);
        notifyItemInserted(movies.size() - 1);
    }

    public void addData(List<Movie> data) {
        int oldSize = movies.size();
        movies.addAll(data);
        notifyItemRangeInserted(oldSize, data.size());
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.clickListener = onPosterClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements Callback {

        @BindView(R.id.imageview_poster)
        ImageView posterView;

        @BindView(R.id.imageview_poster_progress)
        ProgressBar progress;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            posterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onPosterClick(posterView, movies.get(getAdapterPosition()));
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

    public interface OnPosterClickListener {

        void onPosterClick(ImageView poster, Movie movie);

    }

}
