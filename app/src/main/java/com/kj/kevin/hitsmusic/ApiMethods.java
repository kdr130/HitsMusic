package com.kj.kevin.hitsmusic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kj.kevin.hitsmusic.api.API;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

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

    public static void getNewHitsPlaylist(MyObserver <PlayListInfo> observer) {
        ApiSubscribe(API.getKKboxService().getNewHitsPlaylist("TW")
                .flatMap(new Function<JsonObject, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull JsonObject jsonObject) throws Exception {
                        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                        List<PlayListInfo> playListInfos;
                        Type listType = new TypeToken<List<PlayListInfo>>() {}.getType();
                        playListInfos = new Gson().fromJson(jsonArray, listType);

                        return Observable.fromIterable(playListInfos);
                    }
                })
                , observer);
    }

    public static void getChart(MyObserver <PlayListInfo> observer) {
        ApiSubscribe(API.getKKboxService().getCharts("TW")
                        .flatMap(new Function<JsonObject, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull JsonObject jsonObject) throws Exception {
                                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

                                List<PlayListInfo> playListInfos;
                                Type listType = new TypeToken<List<PlayListInfo>>() {}.getType();
                                playListInfos = new Gson().fromJson(jsonArray, listType);

                                return Observable.fromIterable(playListInfos);
                            }
                        })
                , observer);
    }


}
