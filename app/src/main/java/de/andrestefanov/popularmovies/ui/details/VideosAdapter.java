package de.andrestefanov.popularmovies.ui.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Video;

public class VideosAdapter extends BaseAdapter {

    private final Context context;

    private final List<Video> videos;

    public VideosAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(videos.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.view_movie_video, parent, false);

        Video video = videos.get(position);

        TextView videoTitle = (TextView) convertView.findViewById(R.id.video_title);
        videoTitle.setText(video.getName());

        ImageView videoThumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
        PopularMoviesApp.dataManager().loadVideoImage(video.getKey(), videoThumbnail);

        convertView.setTag("http://www.youtube.com/watch?v=" + video.getKey());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag())));
            }
        });

        return convertView;
    }
}
