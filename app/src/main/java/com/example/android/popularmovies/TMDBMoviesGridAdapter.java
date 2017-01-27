package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

class TMDBMoviesGridAdapter extends RecyclerView.Adapter<TMDBMoviesGridAdapter.MovieImageViewHolder> {

    private final Context context;

    private int lastPosition = -1;

    interface OnDataEventsListener {

        void onRequestMoreData();

        void onRequestMoviePoster(Movie movie, MovieImageViewHolder imageView);

    }

    static class MovieImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final ImageView mImageView;

        MovieImageViewHolder(ImageView v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.imageview_poster);
        }
    }

    private OnDataEventsListener onDataEventsListener;

    private final View.OnClickListener clickListener;

    private ArrayList<Movie> movies;

    TMDBMoviesGridAdapter(Context context, ArrayList<Movie> data, View.OnClickListener clickListener) {
        this.context = context;
        this.movies = data;
        this.clickListener = clickListener;
    }

    @Override
    public MovieImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view_movie, parent, false);
        v.setOnClickListener(clickListener);
        
        return new MovieImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieImageViewHolder holder, final int position) {
        final Movie movie = movies.get(position);
        holder.mImageView.setTag(movie);
        holder.mImageView.setImageDrawable(null);

        onDataEventsListener.onRequestMoviePoster(movie, holder);

        setAnimation(holder.mImageView, position);

        if (position == (movies.size() - 1) && onDataEventsListener != null)
            onDataEventsListener.onRequestMoreData();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    void setOnDataEventsListener(OnDataEventsListener onDataEventsListener) {
        this.onDataEventsListener = onDataEventsListener;
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public void onViewDetachedFromWindow(MovieImageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mImageView.clearAnimation();
    }
}
