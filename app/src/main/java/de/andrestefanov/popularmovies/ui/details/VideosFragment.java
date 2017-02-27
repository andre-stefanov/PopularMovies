package de.andrestefanov.popularmovies.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import de.andrestefanov.popularmovies.data.network.model.Video;

public class VideosFragment extends Fragment {

    private MovieDetails movie;

    @BindView(R.id.contentView)
    LinearLayout videosContainer;

    public static VideosFragment createInstance(MovieDetails movie) {
        VideosFragment videosFragment = new VideosFragment();
        videosFragment.movie = movie;
        return videosFragment;
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

        for (Video video : movie.getVideos()) {
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

        return rootView;
    }

}
