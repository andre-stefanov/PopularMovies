package de.andrestefanov.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Video {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("site")
    @Expose
    private String site;

    @SerializedName("size")
    @Expose
    private Integer size;

    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class Page {

        @SerializedName("id")
        @Expose
        private Integer movieId;

        @SerializedName("results")
        @Expose
        private List<Video> results = null;

        public Integer getMovieId() {
            return movieId;
        }

        public void setMovieId(Integer movieId) {
            this.movieId = movieId;
        }

        public List<Video> getResults() {
            return results;
        }

        public void setResults(List<Video> results) {
            this.results = results;
        }

    }

}