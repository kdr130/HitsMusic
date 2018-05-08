package com.kj.kevin.hitsmusic.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.KKboxService;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.activity.KKboxListActivity;


public class KKboxDetailPlayListFragment extends Fragment {
    public static final String TAG = "KKboxDetailPlayList";

    private static final String PLAYLIST_ID = "id";

    private String mPlayListId;
    private RecyclerView mDetailList;
    private KKboxService mKKboxService;


    public KKboxDetailPlayListFragment() {
        // Required empty public constructor
    }


    public static KKboxDetailPlayListFragment newInstance(String param1) {
        KKboxDetailPlayListFragment fragment = new KKboxDetailPlayListFragment();
        Bundle args = new Bundle();
        args.putString(PLAYLIST_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayListId = getArguments().getString(PLAYLIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kkbox_detail_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDetailList = view.findViewById(R.id.detail_list);
        mKKboxService = ((KKboxListActivity)getActivity()).getKkboxService();

        initView();
    }

    private void initView() {
        mKKboxService.getDetailPlayList(mPlayListId, new KKboxService.OnTaskCompletedListener() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d(TAG, "getCharts onCompleted: e: " + e.toString());
                    return;
                }

                Log.d(TAG, "onCompleted: result: " + result.toString());

                JsonArray array = result.get("tracks").getAsJsonObject().get("data").getAsJsonArray();

                for (int i = 0; i < array.size(); i++) {
                    JsonObject object = array.get(i).getAsJsonObject();
                    String name = object.get("name").getAsString();

                    Log.d(TAG, "onCompleted: i: " + (i+1));
                    Log.d(TAG, "onCompleted: title: " + name);
                }
            }
        });
    }
}
