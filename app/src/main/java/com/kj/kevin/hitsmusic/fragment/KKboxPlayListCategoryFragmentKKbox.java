package com.kj.kevin.hitsmusic.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.KKboxPlayListCategoryAdapter;
import com.kj.kevin.hitsmusic.api.API;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KKboxPlayListCategoryFragmentKKbox extends KKboxBaseFragment {
    private static final String TAG = "CategoryFragment";

    private List<Integer> mCategoryResIdList;
    private RecyclerView mCategoryRecycleView;

    public interface PlaylistCategoryResId {
        int CHART = R.string.chart;
        int NEW_HITS = R.string.new_hits;
    }

    public static final int[] PLAYLIST_CATEGORY = {R.string.chart, R.string.new_hits};

    private OnCategoryClickedListener mCategoryClickedListener = new OnCategoryClickedListener() {
        @Override
        public void onCategoryClicked(int position) {
            Log.e(TAG, "onCategoryClicked: position: " + position);

            KKboxPlayListFragmentKKbox kKboxPlayListFragment = KKboxPlayListFragmentKKbox.newInstance(PLAYLIST_CATEGORY[position]);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, kKboxPlayListFragment);
            transaction.commitAllowingStateLoss();
        }
    };


    public interface OnCategoryClickedListener {
        void onCategoryClicked(int position);
    }

    public KKboxPlayListCategoryFragmentKKbox() {
        // Required empty public constructor
    }

    public static KKboxPlayListCategoryFragmentKKbox newInstance() {
        Log.e(TAG, "newInstance: ");
        KKboxPlayListCategoryFragmentKKbox fragment = new KKboxPlayListCategoryFragmentKKbox();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        showLoadingProgressBar();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kkbox_play_list_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e(TAG, "onViewCreated: ");

        mCategoryRecycleView = view.findViewById(R.id.category_list);

        if (mCategoryResIdList == null) {
            mCategoryResIdList = new ArrayList<>();
            mCategoryResIdList.add(R.string.chart);
            mCategoryResIdList.add(R.string.new_hits);
        }
    }

    @Override
    public void getData() {

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
                initView();
            }
        }));
    }

    private void initView() {
        if (!isAdded()) {
            return;
        }

        mCategoryRecycleView.setAdapter(new KKboxPlayListCategoryAdapter(mCategoryResIdList, mCategoryClickedListener));
        mCategoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 使用 Support Library 內建給 RecyclerView 的項目間隔線
        mCategoryRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        hideLoadingProgressBar();
    }
}
