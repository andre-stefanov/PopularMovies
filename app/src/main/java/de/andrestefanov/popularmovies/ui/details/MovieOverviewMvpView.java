package de.andrestefanov.popularmovies.ui.details;

import android.widget.ImageView;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.MvpView;

public interface MovieOverviewMvpView extends MvpView {

    ImageView getPosterImageView();

    void showMovie(Movie movie);

}
