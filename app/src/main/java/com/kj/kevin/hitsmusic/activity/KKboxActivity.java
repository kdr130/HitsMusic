package com.kj.kevin.hitsmusic.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.api.API;
import com.kj.kevin.hitsmusic.fragment.KKboxPlayListCategoryFragment;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KKboxActivity extends AppCompatActivity {
    public static final String TAG = "KKboxActivity";

    private List<PlayListInfo> mNewHitsPlaylist = new ArrayList<>();
    private List<PlayListInfo> mChartPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox);

        ApiMethods.getAccessToken(new MyObserver<JsonObject>("getAccessToken", new MyObserver.MyObserverNextListener<JsonObject>() {
            @Override
            public void onNext(JsonObject jsonObject) {
                String KKboxAccessToken = jsonObject.get("access_token").getAsString();

                Log.e(TAG, "onNext: KKboxAccessToken: " + KKboxAccessToken);
                API.setAccessToken(KKboxAccessToken);

                ApiMethods.getNewHitsPlaylist(new MyObserver<PlayListInfo>("getNewHitsPlaylist", new MyObserver.MyObserverNextListener<PlayListInfo>() {
                    @Override
                    public void onNext(PlayListInfo playListInfo) {
                        Log.e(TAG, "onNext: " + playListInfo.toString());
                        mNewHitsPlaylist.add(playListInfo);
                    }
                }));

                ApiMethods.getChart(new MyObserver<PlayListInfo>("getChart", new MyObserver.MyObserverNextListener<PlayListInfo>() {
                    @Override
                    public void onNext(PlayListInfo playListInfo) {
                        Log.e(TAG, "onNext: " + playListInfo.toString());
                        mChartPlaylist.add(playListInfo);
                    }
                }, new MyObserver.MyObserverCompleteListener() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: !!!" );

                        HashMap<Integer, List<PlayListInfo>> map = new HashMap<>();

                        map.put(R.string.chart, mChartPlaylist);
                        map.put(R.string.new_hits_playlist, mNewHitsPlaylist);

                        KKboxPlayListCategoryFragment kKboxPlayListCategoryFragment = KKboxPlayListCategoryFragment.newInstance(map);

                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.container, kKboxPlayListCategoryFragment);
                        transaction.commitAllowingStateLoss();

                    }
                }));

            }
        }));
    }
}
