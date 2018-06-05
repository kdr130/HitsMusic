package com.kj.kevin.hitsmusic.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.kj.kevin.hitsmusic.BuildConfig;
import com.kj.kevin.hitsmusic.JobSchedulerService;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.KKboxBaseFragment;
import com.kj.kevin.hitsmusic.fragment.KKboxPlayListCategoryFragment;

import java.lang.ref.WeakReference;

public class KKboxActivity extends AppCompatActivity {
    public static final String TAG = "KKboxActivity";

    public static final String MESSENGER_INTENT_KEY
            = BuildConfig.APPLICATION_ID + ".MESSENGER_INTENT_KEY";

    protected Handler mJobHandler = null;

    private ProgressBar mProgressBar;
    private int mJobId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox);

        mProgressBar = findViewById(R.id.loading_progressbar);
        initJobHandler();
        placePlaylistCategoryFragment();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initJobHandler() {
        mJobHandler = new JobHandler(this);
    }

    private void placePlaylistCategoryFragment() {
        KKboxPlayListCategoryFragment kKboxPlayListCategoryFragment = KKboxPlayListCategoryFragment.newInstance();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, kKboxPlayListCategoryFragment);
        transaction.commitAllowingStateLoss();
    }

    public void scheduleJob() {
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, new ComponentName(this, JobSchedulerService.class));
        // 有網路就好，不管什麼是 wifi 或是 3G 4G
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        Log.e(TAG, "scheduleJob");
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    public void showLoadingProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoadingProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public Handler getJobHandler() {
        return mJobHandler;
    }

    private static class JobHandler extends Handler {
        private final WeakReference<KKboxActivity> mActivity;

        JobHandler(KKboxActivity activity){
            super();
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                KKboxActivity kkboxActivity = mActivity.get();
                FragmentManager manager = kkboxActivity.getSupportFragmentManager();
                KKboxBaseFragment fragment = (KKboxBaseFragment) manager.findFragmentById(R.id.container);

                if (fragment != null) {
                    fragment.getData();
                }
            }
        }
    }
}
