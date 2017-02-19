package de.andrestefanov.popularmovies.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Video;

public class VideosFragment extends MvpLceFragment<LinearLayout, List<Video>, MvpLceView<List<Video>>, VideosPresenter> {

    private int movieID;

    @BindView(R.id.contentView)
    LinearLayout videosContainer;

    public static VideosFragment createInstance(int movieID) {
        VideosFragment fragment = new VideosFragment();
        fragment.movieID = movieID;
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_videos, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData(false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public VideosPresenter createPresenter() {
        return new VideosPresenter(movieID);
    }

    @Override
    public void setData(List<Video> data) {
        for (Video video : data) {
            View videoView = getActivity().getLayoutInflater().inflate(R.layout.view_movie_video, videosContainer, false);
            ((TextView) videoView.findViewById(R.id.video_title)).setText(video.getName());
            getPresenter().loadVideoImage(video.getKey(), (ImageView) videoView.findViewById(R.id.video_thumbnail));
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
        getPresenter().loadVideos(pullToRefresh);
    }
}
