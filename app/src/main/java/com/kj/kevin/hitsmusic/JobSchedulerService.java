package com.kj.kevin.hitsmusic;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.kj.kevin.hitsmusic.activity.KKboxActivity.MESSENGER_INTENT_KEY;

/**
 * Created by Kevin on 2018/6/3.
 */

public class JobSchedulerService extends JobService {
    public static final String TAG = "JobSchedule";

    private Messenger mActivityMessenger;
    private boolean mIsRan = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " + this.toString());
        mActivityMessenger = intent.getParcelableExtra(MESSENGER_INTENT_KEY);

        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob: " + this.toString());

        if(!mIsRan) {
            mIsRan = true;
            sendMessage();
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "onStopJob: ");

        return false;
    }

    private void sendMessage() {
        Log.e(TAG, "sendMessage: " );
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            Log.e(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }
        Message m = Message.obtain();
        try {
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }
}
