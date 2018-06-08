package com.kj.kevin.hitsmusic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kj.kevin.hitsmusic.api.API;
import com.kj.kevin.hitsmusic.model.PlayListInfo;
import com.kj.kevin.hitsmusic.model.SongInfo;
import com.kj.kevin.hitsmusic.model.YoutubeSearchResult;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kevinkj_Lin on 2018/5/21.
 */

public class ApiMethods {
    // 封裝 subscribe 和 observer 的 thread
    private static void ApiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void getAccessToken(Observer<JsonObject> observer) {
        ApiSubscribe(API.getAccessTokenService().getAccessToken("client_credentials"), observer);
    }

    // 取得最新主題歌單列表
    public static void getNewHitsPlaylist(MyObserver <List<PlayListInfo>> observer) {
        ApiSubscribe(API.getKKboxService().getNewHitsPlaylist("TW")
                .flatMap(new Function<JsonObject, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull JsonObject jsonObject) throws Exception {
                        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                        List<PlayListInfo> playListInfos;
                        Type listType = new TypeToken<List<PlayListInfo>>() {}.getType();
                        playListInfos = new Gson().fromJson(jsonArray, listType);

                        return Observable.just(playListInfos);
                    }
                })
                , observer);
    }

    // 取得歌曲排行榜列表
    public static void getChartPlaylist(MyObserver <List<PlayListInfo>> observer) {
        ApiSubscribe(API.getKKboxService().getCharts("TW")
                        .flatMap(new Function<JsonObject, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull JsonObject jsonObject) throws Exception {
                                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                                List<PlayListInfo> playListInfos;
                                Type listType = new TypeToken<List<PlayListInfo>>() {}.getType();
                                playListInfos = new Gson().fromJson(jsonArray, listType);

                                return Observable.just(playListInfos);
                            }
                        })
                , observer);
    }

    // 根據 playListId 取得詳細的歌單資訊
    public static void getDetailPlayList(String playListId, MyObserver <List<SongInfo>> observer) {
        ApiSubscribe(API.getKKboxService().getDetailPlayList(playListId, "TW")
                .flatMap(new Function<JsonObject, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull JsonObject jsonObject) throws Exception {
                        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                        List<SongInfo> songInfos;
                        Type listType = new TypeToken<List<SongInfo>>() {}.getType();
                        songInfos = new Gson().fromJson(jsonArray, listType);

                        return Observable.just(songInfos);
                    }
                }), observer);
    }

    public static void getYoutubeSearchResult(String developerKey, String searchKeyword, MyObserver<YoutubeSearchResult> observer) {
        ApiSubscribe(API.getYoutubeService().getSearchResult(developerKey, "snippet", "video", searchKeyword, 10), observer);
    }
}
