package de.andrestefanov.popularmovies.data.prefs;

public enum MoviesFilter {

    POPULAR("popular"),
    TOP_RATED("top_rated");

    private static final String MOVIES_FILTER_POPULAR = "popular";
    private static final String MOVIES_FILTER_TOP_RATED = "top_rated";

    private final String value;

    MoviesFilter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MoviesFilter fromString(String s) {
        switch (s) {
            case MOVIES_FILTER_POPULAR:
                return POPULAR;
            case MOVIES_FILTER_TOP_RATED:
                return TOP_RATED;
        }
        return null;
    }
}
