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

import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.KKboxPlayListCategoryAdapter;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KKboxPlayListCategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private static final String ARG_MAP = "map";

    private HashMap<Integer, List<PlayListInfo>> mDataHashMap;
    private RecyclerView mCategoryRecycleView;
    private OnCategoryClickedListener mCategoryClickedListener = new OnCategoryClickedListener() {
        @Override
        public void onCategoryClicked(int position) {
            Log.e(TAG, "onCategoryClicked: position: " + position);
        }
    };


    public interface OnCategoryClickedListener {
        void onCategoryClicked(int position);
    }

    public KKboxPlayListCategoryFragment() {
        // Required empty public constructor
    }

    public static KKboxPlayListCategoryFragment newInstance(HashMap<Integer, List<PlayListInfo>> map) {
        KKboxPlayListCategoryFragment fragment = new KKboxPlayListCategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MAP, map);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getSerializable(ARG_MAP) instanceof HashMap ) {
            mDataHashMap = (HashMap<Integer, List<PlayListInfo>>)getArguments().getSerializable(ARG_MAP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kkbox_play_list_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Integer> categoryResIdList = new ArrayList<>();
        categoryResIdList.addAll(mDataHashMap.keySet());

        mCategoryRecycleView = view.findViewById(R.id.category_list);
        mCategoryRecycleView.setAdapter(new KKboxPlayListCategoryAdapter(categoryResIdList, mCategoryClickedListener));
        mCategoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 使用 Support Library 內建給 RecyclerView 的項目間隔線
        mCategoryRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
}
