package de.andrestefanov.popularmovies.ui.main;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;

public interface MoviesView extends MvpLceView<List<Movie>> {

    void addData(List<Movie> movies);

    void clear();

}
