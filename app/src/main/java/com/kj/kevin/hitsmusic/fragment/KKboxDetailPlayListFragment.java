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

import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.KKboxDetailPlayListAdapter;
import com.kj.kevin.hitsmusic.model.SongInfo;

import java.util.ArrayList;
import java.util.List;


public class KKboxDetailPlayListFragment extends BaseFragment {
    public static final String TAG = "KKboxDetailPlayList";
    public static final String SONG_DATA = "song_data";
    public static final String SONG_BUNDLE = "song_bundle";
    private static final String ARG_PLAYLIST_ID = "playlist_id";

    private String mPlaylistId;
    private List<SongInfo> mSongList;
    private RecyclerView mDetailRecycleView;
    private OnSongClickedListener mOnSongClickedListener = new OnSongClickedListener() {
        @Override
        public void onSongClicked(int position) {
            Log.e(TAG, "onSongClicked: data: " + mSongList.get(position) );

//            Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
//            Bundle bundle = new Bundle();
//
//            bundle.putParcelable(SONG_DATA, mSongList.get(position));
//            intent.putExtra(SONG_BUNDLE, bundle);
//            startActivity(intent);

            YoutubeRelatedSongPlayerFragment fragment = YoutubeRelatedSongPlayerFragment.newInstance(mSongList.get(position));
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


    public static KKboxDetailPlayListFragment newInstance(String playlistId) {
        KKboxDetailPlayListFragment fragment = new KKboxDetailPlayListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLAYLIST_ID, playlistId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaylistId = getArguments().getString(ARG_PLAYLIST_ID);
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

        mDetailRecycleView.setAdapter(new KKboxDetailPlayListAdapter(mSongList, mOnSongClickedListener));
        mDetailRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDetailRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        hideLoadingProgressBar();
    }
}
