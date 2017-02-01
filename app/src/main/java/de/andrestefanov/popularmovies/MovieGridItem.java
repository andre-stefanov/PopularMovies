package de.andrestefanov.popularmovies;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.andrestefanov.popularmovies.data.TMDBConstants;
import de.andrestefanov.popularmovies.model.Movie;

public class MovieGridItem extends AbstractItem<MovieGridItem, MovieGridItem.ViewHolder> {

    private static final String TAG = "MovieGridItem";

    // the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public Movie movie;

    public MovieGridItem(Movie movie) {
        this.movie = movie;
    }

    @Override
    public int getType() {
        return R.id.movie_grid_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.image_view_poster;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Picasso.with(holder.posterView.getContext())
                .load(TMDBConstants.TMDB_POSTER_BASE_URL + movie.getPosterPath())
                .noFade()
                .config(Bitmap.Config.RGB_565)
                .into(holder.posterView);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        Picasso.with(holder.posterView.getContext())
                .cancelRequest(holder.posterView);
        holder.posterView.clearAnimation();
        holder.posterView.setImageDrawable(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterView;

        ViewHolder(View itemView) {
            super(itemView);
            this.posterView = (ImageView) itemView;
        }
    }

    /**
     * our ItemFactory implementation which creates the ViewHolder for our adapter.
     * It is highly recommended to implement a ViewHolderFactory as it is 0-1ms faster for ViewHolder creation,
     * and it is also many many times more efficient if you define custom listeners on views within your item.
     */
    private static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    /**
     * return our ViewHolderFactory implementation here
     *
     * @return
     */
    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }

    public static List<MovieGridItem> fromMovieList(List<Movie> movies) {
        List<MovieGridItem> results = new ArrayList<>();
        for (Movie movie : movies) {
            results.add(new MovieGridItem(movie));
        }
        return results;
    }

}