package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

public class TMDBMoviesGridAdapter extends RecyclerView.Adapter<TMDBMoviesGridAdapter.MovieImageViewHolder> {

    public interface OnDataEventsListener {

        void onRequestMoreData();

        void onRequestMoviePoster(Movie movie, MovieImageViewHolder imageView);

    }

    public static class MovieImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;

        public MovieImageViewHolder(ImageView v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.imageview_poster);
        }
    }

    private OnDataEventsListener onDataEventsListener;

    private ArrayList<Movie> movies;

    public TMDBMoviesGridAdapter(ArrayList<Movie> data) {
        this.movies = data;
    }

    @Override
    public MovieImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view_movie, parent, false);
        
        return new MovieImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieImageViewHolder holder, final int position) {
        final Movie movie = movies.get(position);
        holder.mImageView.setImageDrawable(null);
        onDataEventsListener.onRequestMoviePoster(movie, holder);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mImageView.getContext(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.MOVIE_ID_EXTRA, movie.getId());
                intent.putExtra(MovieDetailsActivity.MOVIE_TITLE_EXTRA, movie.getTitle());
                holder.mImageView.getContext().startActivity(intent);
            }
        });

        if (position == (movies.size() - 1) && onDataEventsListener != null)
            onDataEventsListener.onRequestMoreData();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnDataEventsListener(OnDataEventsListener onDataEventsListener) {
        this.onDataEventsListener = onDataEventsListener;
    }

}
