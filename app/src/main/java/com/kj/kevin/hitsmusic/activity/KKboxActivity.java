package com.kj.kevin.hitsmusic.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.HitsMusic;
import com.kj.kevin.hitsmusic.JobSchedulerService;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.api.API;
import com.kj.kevin.hitsmusic.fragment.KKboxPlayListCategoryFragmentKKbox;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KKboxActivity extends AppCompatActivity {
    public static final String TAG = "KKboxActivity";

    private ProgressBar mProgressBar;
    private int mJobId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox);

        mProgressBar = findViewById(R.id.loading_progressbar);
        showLoadingProgressBar();

        ApiMethods.getAccessToken(new MyObserver<JsonObject>("getAccessToken", new MyObserver.MyObserverNextListener<JsonObject>() {
            @Override
            public void onNext(JsonObject jsonObject) {
                String KKboxAccessToken = jsonObject.get("access_token").getAsString();

                Log.e(TAG, "onNext: KKboxAccessToken: " + KKboxAccessToken);
                API.setAccessToken(KKboxAccessToken);


            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                KKboxPlayListCategoryFragmentKKbox kKboxPlayListCategoryFragment = KKboxPlayListCategoryFragmentKKbox.newInstance();

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.container, kKboxPlayListCategoryFragment);
                transaction.commitAllowingStateLoss();
            }
        }));

        scheduleJob();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent startJobServiceIntent = new Intent(this, JobSchedulerService.class);
        startService(startJobServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (HitsMusic.isMobileNetworkAvailable(this)) {
            Log.e(TAG, "onResume: Mobile Network Is Available" );
        } else {
            Log.e(TAG, "onResume: Mobile Network Is Not Available" );
            HitsMusic.showNetworkNotAvailableDialog(this);
        }
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, JobSchedulerService.class));

        super.onStop();
    }

    private void scheduleJob() {
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, new ComponentName(this, JobSchedulerService.class));
        // 有網路就好，不管什麼是 wifi 或是 3G 4G
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);

        Log.e(TAG, "Scheduling job");
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    public void showLoadingProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoadingProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
