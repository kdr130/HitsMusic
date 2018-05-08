package com.kj.kevin.hitsmusic;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Kevinkj_Lin on 2018/5/8.
 */

public interface AccessTokenApi {
    @POST("/oauth2/token")
    @FormUrlEncoded
    Call<JsonObject> getAccessToken(@Field("grant_type") String type);
}
