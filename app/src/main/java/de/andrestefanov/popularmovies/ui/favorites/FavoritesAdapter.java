package de.andrestefanov.popularmovies.ui.favorites;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.db.FavoriteColumns;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesAdapter extends CursorAdapter {

    public FavoritesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    public static final String[] PROJECTION = new String[]{
            FavoriteColumns._ID
    };

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_poster, parent, false);
        v.setTag(new ViewHolder(v));
        return v;
    }

    @Override
    public void bindView(final View view, Context context, Cursor cursor) {

        final ViewHolder vh = (ViewHolder) view.getTag();

        vh.progress.setVisibility(View.VISIBLE);
        PopularMoviesApp.dataManager().loadMovie(
                cursor.getInt(cursor.getColumnIndex(FavoriteColumns._ID)),
                new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                        PopularMoviesApp.dataManager().loadPoster(
                                response.body().getPosterPath(),
                                vh.imageView,
                                new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        vh.progress.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<MovieDetails> call, Throwable t) {

                    }
                });
    }

    static class ViewHolder {

        @BindView(R.id.imageview_poster)
        ImageView imageView;

        @BindView(R.id.imageview_poster_progress)
        ProgressBar progress;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }
}
