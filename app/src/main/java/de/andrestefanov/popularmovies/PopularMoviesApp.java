package de.andrestefanov.popularmovies;

import android.app.Application;

import de.andrestefanov.popularmovies.data.DataManager;
import de.andrestefanov.popularmovies.data.DataManagerImpl;
import de.andrestefanov.popularmovies.data.db.DbHelper;
import de.andrestefanov.popularmovies.data.db.DbHelperImpl;
import de.andrestefanov.popularmovies.data.network.ApiHelper;
import de.andrestefanov.popularmovies.data.network.ApiHelperImpl;
import de.andrestefanov.popularmovies.data.prefs.PrefConstants;
import de.andrestefanov.popularmovies.data.prefs.PreferencesHelper;
import de.andrestefanov.popularmovies.data.prefs.PreferencesHelperImpl;

public class PopularMoviesApp extends Application {

    private static final String TAG = "PopularMoviesApp";

    private static DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        DbHelper dbHelper = new DbHelperImpl(getContentResolver());
        ApiHelper apiHelper = new ApiHelperImpl(this);
        PreferencesHelper preferencesHelper = new PreferencesHelperImpl(getSharedPreferences(PrefConstants.PREFS_FILE_NAME, MODE_PRIVATE));

        dataManager = new DataManagerImpl(dbHelper, preferencesHelper, apiHelper);
    }

    public static DataManager dataManager() {
        return dataManager;
    }
}
