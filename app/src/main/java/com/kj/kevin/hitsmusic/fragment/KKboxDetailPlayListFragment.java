package com.kj.kevin.hitsmusic.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.KKboxDetailPlayListAdapter;
import com.kj.kevin.hitsmusic.model.SongInfo;

import java.util.ArrayList;
import java.util.List;


public class KKboxDetailPlayListFragment extends BaseFragment {
    public static final String TAG = "KKboxDetailPlayList";
    private static final String ARG_PLAYLIST_ID = "playlist_id";
    private static final String ARG_PLAYLIST_NAME = "playlist_name";

    private String mPlaylistId, mPlaylistName;
    private List<SongInfo> mSongList;
    private RecyclerView mDetailRecycleView;
    private TextView mPlaylistNameView;
    private ImageView mPlayAll;
    private OnSongClickedListener mOnSongClickedListener = new OnSongClickedListener() {
        @Override
        public void onSongClicked(int position) {
            Log.e(TAG, "onSongClicked: data: " + mSongList.get(position) );
            List<SongInfo> songList = new ArrayList<>();
            songList.add(mSongList.get(position));

            YoutubeRelatedSongPlayerFragment fragment = YoutubeRelatedSongPlayerFragment.newInstance((ArrayList<SongInfo>)songList);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, fragment).commitAllowingStateLoss();

        }
    };

    private View.OnClickListener mOnPlayAllClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "mOnPlayAllClicked: " );

            YoutubeRelatedSongPlayerFragment fragment = YoutubeRelatedSongPlayerFragment.newInstance((ArrayList<SongInfo>) mSongList);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, fragment).commitAllowingStateLoss();
        }
    };

    public interface OnSongClickedListener {
        void onSongClicked(int position);
    }

    public KKboxDetailPlayListFragment() {
        // Required empty public constructor
    }


    public static KKboxDetailPlayListFragment newInstance(String playlistId, String playlistName) {
        KKboxDetailPlayListFragment fragment = new KKboxDetailPlayListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_ID, playlistId);
        args.putString(ARG_PLAYLIST_NAME, playlistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaylistId = getArguments().getString(ARG_PLAYLIST_ID);
            mPlaylistName = getArguments().getString(ARG_PLAYLIST_NAME);

            Log.e(TAG, "onCreate: mPlaylistId: " + mPlaylistId + ", mPlaylistName: " + mPlaylistName );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");

        showLoadingProgressBar();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kkbox_detail_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");

        mDetailRecycleView = view.findViewById(R.id.detail_list);
        mPlaylistNameView = view.findViewById(R.id.playlist_name);
        mPlayAll = view.findViewById(R.id.playAll);
        setActionBarTitle(getString(R.string.kkbox_detail_playlist_actionbar_title));

        if (mSongList != null) {
            initView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    public void getData() {
        if (mSongList != null) {
            return;
        }

        if (mPlaylistId == null) {
            Log.e(TAG, "getData: mPlaylistId == null");
            return;
        }

        mSongList = new ArrayList<>();

        ApiMethods.getDetailPlayList(mPlaylistId, new MyObserver<List<SongInfo>>("getDetailPlayList", new MyObserver.MyObserverNextListener<List<SongInfo>>() {
            @Override
            public void onNext(List<SongInfo> songInfos) {
                Log.e(TAG, "onNext: size: " + songInfos.size() );
                Log.e(TAG, "onNext: songInfos: " + songInfos.toString() );
                mSongList.addAll(songInfos);
            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                initView();
            }
        }));

    }

    private void initView() {
        if(!isAdded()) {
            return;
        }

        mPlaylistNameView.setText(mPlaylistName);
        mPlayAll.setVisibility(View.VISIBLE);
        mPlayAll.setOnClickListener(mOnPlayAllClicked);
        mDetailRecycleView.setAdapter(new KKboxDetailPlayListAdapter(mSongList, mOnSongClickedListener));
        mDetailRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDetailRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        hideLoadingProgressBar();
    }
}
