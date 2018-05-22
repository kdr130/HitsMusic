package com.kj.kevin.hitsmusic.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kevinkj_Lin on 2018/5/21.
 */

public interface KKboxAPI {

    // https://api.kkbox.com/v1.1/featured-playlists?territory=TW
    // 取得最新主題歌單列表
    @GET("featured-playlists")
    Observable<JsonObject> getNewHitsPlaylist(@Query("territory") String territory);

    // 歌曲排行榜
    @GET("charts")
    Observable<JsonObject> getCharts(@Query("territory") String territory);
}
