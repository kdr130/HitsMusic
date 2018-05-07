package com.kj.kevin.hitsmusic;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kkbox.openapideveloper.api.Api;
import com.kkbox.openapideveloper.auth.Auth;
import com.koushikdutta.async.future.FutureCallback;

/**
 * Created by Kevinkj_Lin on 2018/5/7.
 */

public class KKboxService extends Service {
    public static final String TAG = "KKboxService";
    public static final String KKBOX_CLIENT_ID = "5de9d6f49e555545b4847b4d14687ed2";
    public static final String KKBOX_CLIENT_SECRET= "e5b071b58ba0c1764de0244077996755";
    private String mKKboxAccessToken;
    private Api api;

    KKboxBinder kkboxBinder = new KKboxBinder();

    public interface OnTaskCompletedListener {
        void onCompleted(Exception e, JsonObject result);
    }

    public class KKboxBinder extends Binder {
        public KKboxService getKKboxService () {
            return KKboxService.this;
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
        Auth auth = new Auth(KKBOX_CLIENT_ID, KKBOX_CLIENT_SECRET, this);
        auth.getClientCredentialsFlow().fetchAccessToken().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                mKKboxAccessToken = result.get("access_token").getAsString();

                Log.d(TAG, "onCompleted: access_token: " + mKKboxAccessToken);
                api = new Api(mKKboxAccessToken, "TW", KKboxService.this);

                callback.onCompleted(e, result);
            }
        });
    }

    private void getHitsPlayLists(final OnTaskCompletedListener callback) {
        api.getHitsPlaylistFetcher().fetchAllNewHitsPlaylists(10, 0).setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d(TAG, "onCompleted: Exception: " + e.toString());

                }
                callback.onCompleted(e, result);
            }
        });
    }

    private void getHitsPlayLists(String id, final OnTaskCompletedListener callback) {
        Log.d(TAG, "getHitsPlayLists: id: " + id);

        api.getHitsPlaylistFetcher().playlistId = id;
        api.getHitsPlaylistFetcher().fetchMetadata().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d(TAG, "onCompleted: Exception: " + e.toString());
                }
                callback.onCompleted(e, result);
            }
        });
    }

    public void getCharts(final OnTaskCompletedListener callback) {
        Log.d(TAG, "getChart: ");

        api.getChartFetcher().fetchCharts(50, 0).setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d(TAG, "onCompleted: e: " + e.toString());
                }
                callback.onCompleted(e, result);
            }
        });
    }
}
