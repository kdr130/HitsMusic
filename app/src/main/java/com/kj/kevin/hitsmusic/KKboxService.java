package com.kj.kevin.hitsmusic;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kevinkj_Lin on 2018/5/7.
 */

public class KKboxService extends Service {
    public static final String TAG = "KKboxService";
    public static final String KKBOX_CLIENT_ID = "5de9d6f49e555545b4847b4d14687ed2";
    public static final String KKBOX_CLIENT_SECRET= "e5b071b58ba0c1764de0244077996755";
    private String mKKboxAccessToken;
//    private Api api;

    // https://docs-zhtw.kkbox.codes/v1.1/reference#newhitsplaylists-playlist_id-tracks
    KKboxBinder kkboxBinder = new KKboxBinder();
    private AccessTokenApi accessTokenApi;

    public interface OnTaskCompletedListener {
        void onCompleted(Exception e, JsonObject result);
    }

    public class KKboxBinder extends Binder {
        public KKboxService getKKboxService () {
            return KKboxService.this;
        }
    }

    public class AuthenticationInterceptor implements Interceptor {

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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return kkboxBinder;
    }

    public KKboxService() {
        Log.d(TAG, "KKboxService() ");
    }

    public void initService(final OnTaskCompletedListener callback) {
//        Auth auth = new Auth(KKBOX_CLIENT_ID, KKBOX_CLIENT_SECRET, this);
//        auth.getClientCredentialsFlow().fetchAccessToken().setCallback(new FutureCallback<JsonObject>() {
//            @Override
//            public void onCompleted(Exception e, JsonObject result) {
//                mKKboxAccessToken = result.get("access_token").getAsString();
//
//                Log.d(TAG, "onCompleted: access_token: " + mKKboxAccessToken);
//                api = new Api(mKKboxAccessToken, "TW", KKboxService.this);
//
//                callback.onCompleted(e, result);
//            }
//        });

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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://account.kkbox.com")
                // using Gson To Convert Json from server's response
                // 否則會出現 com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
                .addConverterFactory(GsonConverterFactory.create(gson))
                // 使用 OkHttpClient 來設定 Header
                .client(httpClient)
                .build();

        accessTokenApi = retrofit.create(AccessTokenApi.class);

        accessTokenApi.getAccessToken("client_credentials").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.d(TAG, "onResponse: " + response.body());

                mKKboxAccessToken = response.body().get("access_token").getAsString();
                Log.d(TAG, "onResponse: mKKboxAccessToken: " + mKKboxAccessToken);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getHitsPlayLists(final OnTaskCompletedListener callback) {
//        api.getHitsPlaylistFetcher().fetchAllNewHitsPlaylists(10, 0).setCallback(new FutureCallback<JsonObject>() {
//            @Override
//            public void onCompleted(Exception e, JsonObject result) {
//                if (e != null) {
//                    Log.d(TAG, "onCompleted: Exception: " + e.toString());
//
//                }
//                callback.onCompleted(e, result);
//            }
//        });
    }

    public void getDetailPlayList(String id, final OnTaskCompletedListener callback) {
        Log.d(TAG, "getHitsPlayLists: id: " + id);

//        api.getHitsPlaylistFetcher().playlistId = id;
//        api.getHitsPlaylistFetcher().fetchMetadata().setCallback(new FutureCallback<JsonObject>() {
//            @Override
//            public void onCompleted(Exception e, JsonObject result) {
//                if (e != null) {
//                    Log.d(TAG, "onCompleted: Exception: " + e.toString());
//                }
//                callback.onCompleted(e, result);
//            }
//        });
    }

    public void getCharts(final OnTaskCompletedListener callback) {
        Log.d(TAG, "getChart: ");

//        api.getChartFetcher().fetchCharts(50, 0).setCallback(new FutureCallback<JsonObject>() {
//            @Override
//            public void onCompleted(Exception e, JsonObject result) {
//                if (e != null) {
//                    Log.d(TAG, "onCompleted: e: " + e.toString());
//                }
//                callback.onCompleted(e, result);
//            }
//        });
    }
}
