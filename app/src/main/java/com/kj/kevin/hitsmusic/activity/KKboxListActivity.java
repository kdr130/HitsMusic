package com.kj.kevin.hitsmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.api.API;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class KKboxListActivity extends AppCompatActivity {
    public static final String TAG = "KKboxListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox_list);

        ApiMethods.getAccessToken(new Observer<JsonObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                String KKboxAccessToken = jsonObject.get("access_token").getAsString();

                Log.e(TAG, "onNext: KKboxAccessToken: " + KKboxAccessToken);
                API.setAccessToken(KKboxAccessToken);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
