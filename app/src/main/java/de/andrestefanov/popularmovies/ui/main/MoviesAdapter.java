package de.andrestefanov.popularmovies.ui.main;

import android.support.v7.widget.RecyclerView;
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
import butterknife.OnClick;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.utils.Constants;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    @SuppressWarnings("FieldCanBeLocal")
    private static int THRESHOLD = Constants.TMDB_API_MOVIES_PER_PAGE;

    private List<Movie> data;

    private boolean loading = false;

    private OnMovieClickListener clickListener;

    private OnMoreDataRequestListener dataRequestListener;

    MoviesAdapter(OnMovieClickListener clickListener, OnMoreDataRequestListener moreDataRequestListener) {
        this.data = new ArrayList<>();
        this.clickListener = clickListener;
        this.dataRequestListener = moreDataRequestListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.setPosterPath(data.get(holder.getAdapterPosition()).getPosterPath());

        if (!loading && position > data.size() - THRESHOLD) {
            loadModeData();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadModeData() {
        loading = true;
        dataRequestListener.onRequestMoreData();
    }

    void setData(List<Movie> movies) {
        data = movies;
        notifyDataSetChanged();

        loading = false;
    }

    void addData(List<Movie> movies) {
        int oldSize = data.size();
        data.addAll(movies);
        notifyItemRangeInserted(oldSize, movies.size());

        loading = false;
    }

    void setClickListener(OnMovieClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    interface OnMoreDataRequestListener {

        void onRequestMoreData();

    }

    interface OnMovieClickListener {
        void onClick(ImageView imageView, Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview_poster)
        ImageView poster;

        @BindView(R.id.imageview_poster_progress)
        ProgressBar progress;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.imageview_poster)
        void onPosterClick() {
            clickListener.onClick(poster, data.get(getAdapterPosition()));
        }

        void setPosterPath(String posterPath) {
            progress.setVisibility(View.VISIBLE);
            PopularMoviesApp.dataManager().loadPoster(posterPath, poster, new Callback() {
                @Override
                public void onSuccess() {
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
