package de.andrestefanov.popularmovies.data;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.andrestefanov.popularmovies.Constants;

public class YouTubeClient {

    private static final String TAG = "YouTubeClient";

    private final Picasso picasso;

    private Context context;

    public YouTubeClient(Context context) {
        this.context = context;
        this.picasso = new Picasso.Builder(context).build();
    }

    public void loadVideoImage(String id, ImageView imageView, Callback callback) {
        String url = String.format(Constants.YT_IMAGE_URL_TMPL, id);
        Log.d(TAG, "loadVideoImage: " + url);
        picasso.load(url)
                .into(imageView, callback);
    }
}
