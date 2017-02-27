package de.andrestefanov.popularmovies.ui.favorites;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesPresenter extends MvpBasePresenter<FavoritesFragment> {

    void loadFavorites() {
        Integer[] ids = new Integer[]{328111, 328112, 328113, 328114, 328115};

        for (int id : ids)
            PopularMoviesApp.dataManager().loadMovie(id, new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
//                    if(getView() != null)
//                        getView().addData(response.body().toMovie());
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {

                }
            });
    }

}
