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

import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.KKboxPlayListAdapter;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.ArrayList;
import java.util.List;


public class KKboxPlayListFragmentKKbox extends KKboxBaseFragment {
    public static final String TAG = "KKboxPlayListFragment";
    private static final String ARG_CATEGORY_RESID = "category_resId";

    private RecyclerView mPlayListRecyclerView;
    private int mCategoryResId;
    private List<PlayListInfo> mData;
    private OnPlayListClickedListener mPlayListClickedListener = new OnPlayListClickedListener() {
        @Override
        public void onPlayListClicked(int position) {
            Log.e(TAG, "onPlayListClicked: position: " + position + ", id: " + mData.get(position).getId() );

            KKboxDetailPlayListFragmentKKbox kkboxDetailPlayListFragment = KKboxDetailPlayListFragmentKKbox.newInstance(mData.get(position).getId());
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, kkboxDetailPlayListFragment);
            transaction.commitAllowingStateLoss();
        }
    };

    public interface OnPlayListClickedListener {
        void onPlayListClicked(int position);
    }

    public KKboxPlayListFragmentKKbox() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static KKboxPlayListFragmentKKbox newInstance(int categoryResId) {
        KKboxPlayListFragmentKKbox fragment = new KKboxPlayListFragmentKKbox();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_RESID, categoryResId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kkbox_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");

        showLoadingProgressBar();

        if ( getArguments().getSerializable(ARG_CATEGORY_RESID) instanceof Integer ) {
            mCategoryResId = getArguments().getInt(ARG_CATEGORY_RESID);
        }

        mPlayListRecyclerView = view.findViewById(R.id.list);
    }

    public void getData() {
        Log.e(TAG, "getData: ");

        if (mData != null) {
            initView();

            return;
        }

        mData = new ArrayList<>();

        switch (mCategoryResId) {
            case KKboxPlayListCategoryFragmentKKbox.PlaylistCategoryResId.CHART:
                getChartPlaylist();
                break;
            case KKboxPlayListCategoryFragmentKKbox.PlaylistCategoryResId.NEW_HITS:
                getNewHitsPlaylist();
                break;
            default:
                Log.e(TAG, "getData: mCategoryResId: " + mCategoryResId );
        }
    }

    private void getChartPlaylist() {
        Log.e(TAG, "getChartPlaylist: ");
        ApiMethods.getNewHitsPlaylist(new MyObserver<PlayListInfo>("getNewHitsPlaylist", new MyObserver.MyObserverNextListener<PlayListInfo>() {
            @Override
            public void onNext(PlayListInfo playListInfo) {
                Log.e(TAG, "onNext: " + playListInfo.toString());
                mData.add(playListInfo);
            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete:");
                initView();
            }
        }));
    }

    private void getNewHitsPlaylist() {
        Log.e(TAG, "getNewHitsPlaylist: ");
        ApiMethods.getChart(new MyObserver<PlayListInfo>("getChart", new MyObserver.MyObserverNextListener<PlayListInfo>() {
            @Override
            public void onNext(PlayListInfo playListInfo) {
                Log.e(TAG, "onNext: " + playListInfo.toString());
                mData.add(playListInfo);
            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete:");
                initView();
            }
        }));
    }

    private void initView() {
        if (!isAdded()) {
            return;
        }
        mPlayListRecyclerView.setAdapter(new KKboxPlayListAdapter(mData, mPlayListClickedListener));
        // 需要設定 layoutManager
        mPlayListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 使用 Support Library 內建給 RecyclerView 的項目間隔線
        mPlayListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        hideLoadingProgressBar();
    }
}
