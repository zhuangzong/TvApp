package org.tvapp.db.bean;


import java.util.List;

public class SearchResult {
    List<VideoJoin> movies;

    public List<VideoJoin> getMovies() {
        return movies;
    }

    public void setMovies(List<VideoJoin> movies) {
        this.movies = movies;
    }
}
