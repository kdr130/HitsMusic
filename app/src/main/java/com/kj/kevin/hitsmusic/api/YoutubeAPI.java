package com.kj.kevin.hitsmusic.api;

import com.kj.kevin.hitsmusic.model.YoutubeSearchResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kevin on 2018/5/31.
 */

public interface YoutubeAPI {
    // https://www.googleapis.com/youtube/v3/search?key=AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w&part=snippet&type=video&q=五月天&maxResults=10
    @GET("search")
    Observable<YoutubeSearchResult> getSearchResult(@Query("key") String key, @Query("part") String part, @Query("type") String type, @Query("q") String keyWorld, @Query("maxResults") int maxResults );
}
