package com.kj.kevin.hitsmusic.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kevinkj_Lin on 2018/5/21.
 */

public class API {
    public static final String TAG = "API";
    private static final String ACCESS_TOKEN_BASE_URL = "https://account.kkbox.com/";
    private static final String KKBOX_BASE_URL = "https://api.kkbox.com/v1.1/";
    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/";

    private static final String KKBOX_CLIENT_ID = "5de9d6f49e555545b4847b4d14687ed2";
    private static final String KKBOX_CLIENT_SECRET= "e5b071b58ba0c1764de0244077996755";

    private static String mAccessToken;

    private static AccessTokenAPI accessTokenService;
    private static KKboxAPI kkboxService;
    private static YoutubeAPI youtubeService;

    public static AccessTokenAPI getAccessTokenService() {
        if (accessTokenService == null) {
            setAccessTokenService();
        }
        return accessTokenService;
    }

    private static class HeaderInterceptor implements Interceptor {

        private HashMap<String, String> map;

        HeaderInterceptor(HashMap<String, String> map) {
            this.map = map;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request.Builder originalBuilder = chain.request().newBuilder();

            for ( String key : map.keySet() ) {

                Log.e(TAG, "intercept: key: " + key + ", value: " + map.get(key) );

                originalBuilder.addHeader(key, map.get(key));
            }

            Request original =
                    originalBuilder.removeHeader("User-Agent")
                                    .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                                    .build();

            return chain.proceed(original);
        }
    }

    private static void setAccessTokenService() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", Credentials.basic(KKBOX_CLIENT_ID, KKBOX_CLIENT_SECRET));


        // use OkHttpClient to set Header
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(headerMap))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ACCESS_TOKEN_BASE_URL)
                // 將 response 使用 Gson 格式處理
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:converter-gson:2.3.0'
                .addConverterFactory(GsonConverterFactory.create())
                // 搭配 RxJava2.0 則要加入 RxJava2CallAdapterFactory.create()
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用 OkHttpClient 來設定 Header
                .client(httpClient)
                .build();
        accessTokenService = retrofit.create(AccessTokenAPI.class);
    }

    public static void setAccessToken(String token) {
        mAccessToken = token;
    }

    public static void setKKboxService() {

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + mAccessToken);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(headerMap))
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KKBOX_BASE_URL)
                // 將 response 使用 Gson 格式處理
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:converter-gson:2.3.0'
                .addConverterFactory(GsonConverterFactory.create(gson))
                // 搭配 RxJava2.0 則要加入 RxJava2CallAdapterFactory.create()
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用 OkHttpClient 來設定 Header
                .client(httpClient)
                .build();
        kkboxService = retrofit.create(KKboxAPI.class);
    }

    public static KKboxAPI getKKboxService() {
        if (kkboxService == null) {
            setKKboxService();
        }
        return kkboxService;
    }

    public static void setYouTubeService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                // 將 response 使用 Gson 格式處理
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:converter-gson:2.3.0'
                .addConverterFactory(GsonConverterFactory.create(gson))
                // 搭配 RxJava2.0 則要加入 RxJava2CallAdapterFactory.create()
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用 OkHttpClient 來設定 Header
                .build();
        youtubeService = retrofit.create(YoutubeAPI.class);
    }

    public static YoutubeAPI getYoutubeService() {
        if ( youtubeService == null ) {
            setYouTubeService();
        }
        return youtubeService;
    }

}
