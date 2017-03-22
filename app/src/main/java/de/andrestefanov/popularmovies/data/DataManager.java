package de.andrestefanov.popularmovies.data;

import de.andrestefanov.popularmovies.data.db.DbHelper;
import de.andrestefanov.popularmovies.data.network.ApiHelper;
import de.andrestefanov.popularmovies.data.prefs.PreferencesHelper;

public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper {



}
