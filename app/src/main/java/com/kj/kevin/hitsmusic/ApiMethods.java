package com.kj.kevin.hitsmusic;

import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.api.API;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
}
