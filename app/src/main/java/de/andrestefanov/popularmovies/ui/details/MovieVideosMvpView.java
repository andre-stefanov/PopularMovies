package de.andrestefanov.popularmovies.ui.details;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.ui.base.MvpView;


public interface MovieVideosMvpView extends MvpView {

    void showVideos(List<Video> videos);

}
