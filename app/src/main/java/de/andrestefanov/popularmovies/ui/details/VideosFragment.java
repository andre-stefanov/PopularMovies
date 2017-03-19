package de.andrestefanov.popularmovies.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import de.andrestefanov.popularmovies.data.network.model.Video;

public class VideosFragment extends MvpLceViewStateFragment<RelativeLayout, List<Video>, MvpLceView<List<Video>>, VideosPresenter> {

    private int movieId;

    private List<Video> data = new ArrayList<>();

    @BindView(R.id.contentView)
    LinearLayout videosContainer;

    public static VideosFragment createInstance(int movieId) {
        VideosFragment videosFragment = new VideosFragment();
        videosFragment.movieId = movieId;
        return videosFragment;
    }

    @NonNull
    @Override
    public VideosPresenter createPresenter() {
        return new VideosPresenter(movieId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_videos, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @NonNull
    @Override
    public LceViewState<List<Video>, MvpLceView<List<Video>>> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Video> getData() {
        return data;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(List<Video> data) {
        this.data = data;

        videosContainer.removeAllViews();
        for (Video video : data) {
            View videoView = getActivity().getLayoutInflater().inflate(R.layout.view_movie_video, videosContainer, false);
            ((TextView) videoView.findViewById(R.id.video_title)).setText(video.getName());
            PopularMoviesApp.dataManager().loadVideoImage(video.getKey(), (ImageView) videoView.findViewById(R.id.video_thumbnail));
            videoView.setTag("http://www.youtube.com/watch?v=" + video.getKey());

            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag())));
                }
            });

            videosContainer.addView(videoView);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData(pullToRefresh);
    }
}
