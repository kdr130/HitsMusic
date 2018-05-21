package com.kj.kevin.hitsmusic.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Kevinkj_Lin on 2018/5/8.
 */

public interface AccessTokenAPI {
    @POST("/oauth2/token")
    @FormUrlEncoded
    Observable<JsonObject> getAccessToken(@Field("grant_type") String type);
}
