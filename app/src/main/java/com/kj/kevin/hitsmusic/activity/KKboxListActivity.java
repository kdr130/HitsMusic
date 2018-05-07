package com.kj.kevin.hitsmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.KKboxCharListAdapter;
import com.kj.kevin.hitsmusic.KKboxService;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.KKboxChartListFragment;
import com.kj.kevin.hitsmusic.model.Chart;

import java.util.ArrayList;
import java.util.List;

public class KKboxListActivity extends AppCompatActivity implements ServiceConnection {
    public static final String TAG = "KKboxListActivity";

    private KKboxService kkboxService;
    private boolean mIsBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox_list);
        mIsBound = bindService(new Intent(this, KKboxService.class), this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mIsBound) {
            unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected: ");
        kkboxService = ((KKboxService.KKboxBinder) iBinder).getKKboxService();
        kkboxService.initService(new KKboxService.OnTaskCompletedListener() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                FragmentManager manager = getSupportFragmentManager();
                KKboxChartListFragment fragment = KKboxChartListFragment.newInstance();
                manager.beginTransaction().add(R.id.container, fragment).commit();
            }
        });
    }

    public KKboxService getKkboxService() {
        return kkboxService;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mIsBound = false;
    }
}
