package com.kj.kevin.hitsmusic.fragment;


import android.content.Intent;
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
import com.kj.kevin.hitsmusic.activity.YoutubePlayerActivity;
import com.kj.kevin.hitsmusic.adapter.KKboxDetailPlayListAdapter;
import com.kj.kevin.hitsmusic.model.SongInfo;

import java.util.ArrayList;
import java.util.List;


public class KKboxDetailPlayListFragment extends Fragment {
    public static final String TAG = "KKboxDetailPlayList";
    public static final String SONG_DATA = "song_data";
    private static final String ARG_LIST = "list";

    private List<SongInfo> mSongList;
    private RecyclerView mDetailRecycleView;
    private OnSongClickedListener mOnSongClickedListener = new OnSongClickedListener() {
        @Override
        public void onSongClicked(int position) {
            Log.e(TAG, "onSongClicked: position: " + position );

            Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
            intent.putExtra(SONG_DATA, mSongList.get(position));
            startActivity(intent);
        }
    };

    public interface OnSongClickedListener {
        void onSongClicked(int position);
    }

    public KKboxDetailPlayListFragment() {
        // Required empty public constructor
    }


    public static KKboxDetailPlayListFragment newInstance(List<SongInfo> data) {
        KKboxDetailPlayListFragment fragment = new KKboxDetailPlayListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST, (ArrayList)data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSongList = (List<SongInfo>) getArguments().getSerializable(ARG_LIST);
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

        mDetailRecycleView = view.findViewById(R.id.detail_list);
        initView();
    }

    private void initView() {
        mDetailRecycleView.setAdapter(new KKboxDetailPlayListAdapter(mSongList, mOnSongClickedListener));
        mDetailRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDetailRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
}
