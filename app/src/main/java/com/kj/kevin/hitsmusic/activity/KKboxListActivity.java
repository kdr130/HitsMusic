package com.kj.kevin.hitsmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.kj.kevin.hitsmusic.model.Chart;

import java.util.ArrayList;
import java.util.List;

public class KKboxListActivity extends AppCompatActivity implements ServiceConnection {
    public static final String TAG = "KKboxListActivity";

    private KKboxService kkboxService;
    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kkbox_list);

        mList = (RecyclerView) findViewById(R.id.list);

        bindService(new Intent(this, KKboxService.class), this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected: ");
        kkboxService = ((KKboxService.KKboxBinder) iBinder).getKKboxService();
        kkboxService.initService(new KKboxService.OnTaskCompletedListener() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                initView();
            }
        });
    }

    private void initView() {
        Log.d(TAG, "initView: ");
        kkboxService.getCharts(new KKboxService.OnTaskCompletedListener() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d(TAG, "onCompleted: result: " + result.toString());
                JsonArray data = result.getAsJsonArray("data");
                if (data != null) {
                    JsonObject object11 = data.get(0).getAsJsonObject();
                    Log.d(TAG, "onCompleted: [0]: " + object11.toString());
                    List<Chart> list = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        JsonObject object = data.get(i).getAsJsonObject();
//                        Log.d(TAG, "onCompleted: no: " + (i+1));
//                        Log.d(TAG, "onCompleted: title: " + object.get("title").getAsString());
//                        Log.d(TAG, "onCompleted: id: " + object.get("id").getAsString());
                        list.add(new Chart(object.get("id").getAsString(), object.get("title").getAsString(),
                                object.get("description").getAsString(), object.get("images").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString(),
                                object.get("url").getAsString()));
                    }

                    mList.setAdapter(new KKboxCharListAdapter(list));
                    // 需要設定 layoutManager
                    mList.setLayoutManager(new LinearLayoutManager(KKboxListActivity.this));
                    // 使用 Support Library 內建給 RecyclerView 的項目間隔線
                    mList.addItemDecoration(new DividerItemDecoration(KKboxListActivity.this, DividerItemDecoration.VERTICAL));
                }
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
