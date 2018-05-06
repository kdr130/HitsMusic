package com.kj.kevin.hitsmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kkbox.openapideveloper.api.Api;
import com.kkbox.openapideveloper.auth.Auth;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String KKBOX_CLIENT_ID = "5de9d6f49e555545b4847b4d14687ed2";
    public static final String KKBOX_CLIENT_SECRET= "e5b071b58ba0c1764de0244077996755";
    /*
    GET
    https://www.googleapis.com/youtube/v3/search?key=AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w&part=snippet&q=五月天&maxResults=10
     */
    public static final String YOUTUBE_API_KEY = "AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w";

    private String kkboxAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth auth = new Auth(KKBOX_CLIENT_ID, KKBOX_CLIENT_SECRET, this);
        auth.getClientCredentialsFlow().fetchAccessToken().withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                kkboxAccessToken = result.getResult().get("access_token").getAsString();

                Log.d(TAG, "onCompleted: access_token: " + kkboxAccessToken);
//                useSearchApi();
                getHitsPlayLists();
            }
        });
        
    }

    private void getHitsPlayLists() {
        Api api = new Api(kkboxAccessToken, "TW", this);
        api.getHitsPlaylistFetcher().fetchAllNewHitsPlaylists(10, 0).setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d(TAG, "onCompleted: Exception: " + e.toString());
                } else {
                    Log.d(TAG, "onCompleted: result: " + result.toString());

                    JsonArray data = result.getAsJsonArray("data");
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            JsonObject object = data.get(i).getAsJsonObject();
                            Log.d(TAG, "onCompleted: no: " + (i+1));
                            Log.d(TAG, "onCompleted: title: " + object.get("title").getAsString());
                            Log.d(TAG, "onCompleted: id: " + object.get("id").getAsString());

                            getHitsPlayLists(object.get("id").getAsString());
                        }
                    }

                }
            }
        });
    }

    private void getHitsPlayLists(String id) {
        Log.d(TAG, "getHitsPlayLists: id: " + id);

        Api api = new Api(kkboxAccessToken, "TW", this);
        api.getHitsPlaylistFetcher().playlistId = id;
        api.getHitsPlaylistFetcher().fetchMetadata().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d(TAG, "onCompleted: Exception: " + e.toString());
                } else {
                    Log.d(TAG, "onCompleted: result: " + result.toString());
                    Log.d(TAG, "onCompleted: size: " + result.getAsJsonObject("tracks").getAsJsonArray("data").size());

                }
            }
        });
    }

//    private void useSearchApi() {
//        Api api = new Api(kkboxAccessToken, "TW", this);
//        api.getSearchFetcher().q = "五月天";
//        api.getSearchFetcher().fetchSearchResult(10, 0).withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
//             @Override
//             public void onCompleted(Exception e, Response<JsonObject> result) {
//                 Log.d(TAG, "onCompleted: result: " + result.getResult().toString());
//             }
//         });
//    }




}
