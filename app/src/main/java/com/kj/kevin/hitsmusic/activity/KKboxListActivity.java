package com.kj.kevin.hitsmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.api.API;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.ArrayList;
import java.util.List;

public class KKboxListActivity extends AppCompatActivity {
    public static final String TAG = "KKboxListActivity";

    private List<PlayListInfo> mNewHitsPlaylist = new ArrayList<>();
    private List<PlayListInfo> mChartPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox_list);

        ApiMethods.getAccessToken(new MyObserver<JsonObject>("getAccessToken", new MyObserver.MyObserverNextListener<JsonObject>() {
            @Override
            public void onNext(JsonObject jsonObject) {
                String KKboxAccessToken = jsonObject.get("access_token").getAsString();

                Log.e(TAG, "onNext: KKboxAccessToken: " + KKboxAccessToken);
                API.setAccessToken(KKboxAccessToken);
                API.setKKboxService();

                ApiMethods.getNewHitsPlaylist(new MyObserver<PlayListInfo>("getNewHitsPlaylist", new MyObserver.MyObserverNextListener<PlayListInfo>() {
                    @Override
                    public void onNext(PlayListInfo playListInfo) {
                        Log.e(TAG, "onNext: " + playListInfo.getTitle());
                        mNewHitsPlaylist.add(playListInfo);
                    }
                }));

                ApiMethods.getChart(new MyObserver<PlayListInfo>("getChart", new MyObserver.MyObserverNextListener<PlayListInfo>() {
                    @Override
                    public void onNext(PlayListInfo playListInfo) {
                        Log.e(TAG, "onNext: " + playListInfo.getTitle());
                        mChartPlaylist.add(playListInfo);
                    }
                }, new MyObserver.MyObserverCompleteListener() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: !!!" );
                    }
                }));

            }
        }));
    }
}
