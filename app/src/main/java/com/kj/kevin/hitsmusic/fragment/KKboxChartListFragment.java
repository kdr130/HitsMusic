package com.kj.kevin.hitsmusic.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.KKboxCharListAdapter;
import com.kj.kevin.hitsmusic.KKboxService;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.activity.KKboxListActivity;
import com.kj.kevin.hitsmusic.model.Chart;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KKboxChartListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KKboxChartListFragment extends Fragment {
    public static final String TAG = "KKboxChartListFragment";

    private RecyclerView mList;
    private KKboxService mKKboxService;

    public KKboxChartListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static KKboxChartListFragment newInstance() {
        KKboxChartListFragment fragment = new KKboxChartListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kkbox_chart_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.list);
        mKKboxService = ((KKboxListActivity)getActivity()).getKkboxService();
        initView();
    }

    private void initView() {
        mKKboxService.getCharts(new KKboxService.OnTaskCompletedListener() {
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
                    mList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    // 使用 Support Library 內建給 RecyclerView 的項目間隔線
                    mList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                }
            }
        });
    }
}
