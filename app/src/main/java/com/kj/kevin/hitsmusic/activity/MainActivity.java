package com.kj.kevin.hitsmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kj.kevin.hitsmusic.R;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "MainActivity";

    /*
    GET
    https://www.googleapis.com/youtube/v3/search?key=AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w&part=snippet&q=五月天&maxResults=10
     */
    public static final String YOUTUBE_API_KEY = "AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");
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
