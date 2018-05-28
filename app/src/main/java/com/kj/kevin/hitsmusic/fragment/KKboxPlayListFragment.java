package com.kj.kevin.hitsmusic.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.kj.kevin.hitsmusic.model.SongInfo;

import java.util.ArrayList;
import java.util.List;


public class KKboxPlayListFragment extends Fragment {
    public static final String TAG = "KKboxPlayListFragment";
    private static final String ARG_LIST = "list";

    private RecyclerView mPlayListRecyclerView;
    private List<PlayListInfo> mData;
    private OnPlayListClickedListener mPlayListClickedListener = new OnPlayListClickedListener() {
        @Override
        public void onPlayListClicked(int position) {
            Log.e(TAG, "onPlayListClicked: clicked position: " + position + ", id: " + mData.get(position).getId() );

            final List<SongInfo> songList = new ArrayList<>();

            ApiMethods.getDetailPlayList(mData.get(position).getId(), new MyObserver<List<SongInfo>>("getDetailPlayList", new MyObserver.MyObserverNextListener<List<SongInfo>>() {
                @Override
                public void onNext(List<SongInfo> songInfos) {
                    Log.e(TAG, "onNext: size: " + songInfos.size() );
                    songList.addAll(songInfos);
                }
            }, new MyObserver.MyObserverCompleteListener() {
                @Override
                public void onComplete() {
                    KKboxDetailPlayListFragment kkboxDetailPlayListFragment = KKboxDetailPlayListFragment.newInstance(songList);
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, kkboxDetailPlayListFragment);
                    transaction.commit();
                }
            }));

        }
    };

    public interface OnPlayListClickedListener {
        void onPlayListClicked(int position);
    }

    public KKboxPlayListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static KKboxPlayListFragment newInstance(List<PlayListInfo> data) {
        KKboxPlayListFragment fragment = new KKboxPlayListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST, (ArrayList)data);
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

        if ( getArguments().getSerializable(ARG_LIST) instanceof ArrayList ) {
            mData = (List<PlayListInfo>)getArguments().getSerializable(ARG_LIST);
        }

        mPlayListRecyclerView = view.findViewById(R.id.list);

        initView();
    }

    private void initView() {
        mPlayListRecyclerView.setAdapter(new KKboxPlayListAdapter(mData, mPlayListClickedListener));
        // 需要設定 layoutManager
        mPlayListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 使用 Support Library 內建給 RecyclerView 的項目間隔線
        mPlayListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
}
