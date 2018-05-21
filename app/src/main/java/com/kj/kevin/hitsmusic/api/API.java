package com.kj.kevin.hitsmusic.api;

import java.io.IOException;

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
    public static final String ACCESSS_TOKEN_BASE_URL = "https://account.kkbox.com";
    public static final String KKBOX_BASE_URL = "https://api.kkbox.com/v1.1/";

    public static final String KKBOX_CLIENT_ID = "5de9d6f49e555545b4847b4d14687ed2";
    public static final String KKBOX_CLIENT_SECRET= "e5b071b58ba0c1764de0244077996755";

    private static String mAccessToken;

    private static AccessTokenAPI accessTokenService;
    private static KKboxAPI kkboxService;

    public static AccessTokenAPI getAccessTokenService() {
        if (accessTokenService == null) {
            setAccessTokenService();
        }
        return accessTokenService;
    }

    public static class AuthenticationInterceptor implements Interceptor {
        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    private static void setAccessTokenService() {
        // use OkHttpClient to set Header
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        return chain.proceed(ongoing.build());
                    }
                })
                .addInterceptor(new AuthenticationInterceptor(Credentials.basic(KKBOX_CLIENT_ID, KKBOX_CLIENT_SECRET)))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ACCESSS_TOKEN_BASE_URL)
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

    private static void setKkboxService() {
        // need to set Header
        // Authorization: Bearer Etz0AB/jBfAxcz6bgRZLkQ==
        //                Bearer mAccessToken


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KKBOX_BASE_URL)
                // 將 response 使用 Gson 格式處理
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:converter-gson:2.3.0'
                .addConverterFactory(GsonConverterFactory.create())
                // 搭配 RxJava2.0 則要加入 RxJava2CallAdapterFactory.create()
                // 需要在 build.gradle 加入 compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用 OkHttpClient 來設定 Header
                .build();
        kkboxService = retrofit.create(KKboxAPI.class);
    }

}
