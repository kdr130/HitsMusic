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
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.YoutubeSearchResultAdapter;
import com.kj.kevin.hitsmusic.model.SongInfo;
import com.kj.kevin.hitsmusic.model.YoutubeSearchResult;

import java.util.ArrayList;
import java.util.List;

public class YoutubeRelatedSongPlayerFragment extends BaseFragment {
    private static final String TAG = "YoutubePlayerFragment";
    public static final String SONG_DATA = "song_data";

    private static String YOUTUBE_API_KEY;

    private List<String> mSearchedVideoIdList = new ArrayList<>();
    private List<YoutubeSearchResult.YoutubeItem> mSearchedResult = new ArrayList<>();
    private RecyclerView mRecommendSearchResultRecyclerView;
    private YoutubeSearchResultAdapter mYoutubeSearchResultAdapter;
    private YouTubePlayer mYouTubePlayer;
    private int mPlayingIndex = 0;

    private OnSearchedResultClickedListener mSearchedResultListener = new OnSearchedResultClickedListener() {
        @Override
        public void onSearchResultClicked(int position) {
            playYoutubeVideo(position);
        }
    };

    public interface OnSearchedResultClickedListener {
        void onSearchResultClicked(int position);
    }

    public YoutubeRelatedSongPlayerFragment() {
        // Required empty public constructor
    }

    public static YoutubeRelatedSongPlayerFragment newInstance(SongInfo songData) {
        YoutubeRelatedSongPlayerFragment fragment = new YoutubeRelatedSongPlayerFragment();
        Bundle args = new Bundle();
        args.putParcelable(SONG_DATA, songData);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YOUTUBE_API_KEY = getResources().getString(R.string.YOUTUBE_API_KEY);;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        showLoadingProgressBar();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.youtube_relatedsong_player_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e(TAG, "onViewCreated: ");

        mRecommendSearchResultRecyclerView = view.findViewById(R.id.recommend_search_result);
    }


    public void getData() {
        if(getArguments() == null && getArguments().getParcelable(SONG_DATA) == null) {
            return;
        }

        SongInfo songData = getArguments().getParcelable(SONG_DATA);

        if (songData != null) {
            Log.e(TAG, "onCreate: mSongData: " + songData.toString() );

            String songName = songData.getName();
            // remove (English Song Name) to improve search result
            int leftPara = songName.indexOf(" (");
            if (leftPara > 0) {
                songName = songName.substring(0, leftPara);
            }

            String artistName = songData.getAlbum().getArtist().getName();
            String searchKeyword = songName + " " + artistName;

            searchYoutube(searchKeyword);
        }
    }

    private void searchYoutube(String searchKeyword) {
        Log.e(TAG, "onCreate: searchKeyword: " + searchKeyword );

        ApiMethods.getYoutubeSearchResult(YOUTUBE_API_KEY, searchKeyword, new MyObserver<YoutubeSearchResult>("getYoutubeSearchResult", new MyObserver.MyObserverNextListener<YoutubeSearchResult>() {
            @Override
            public void onNext(YoutubeSearchResult result) {
                Log.e(TAG, "onNext: ");

                mSearchedResult = result.getItems();
                Log.e(TAG, "onNext: mSearchedResult.size: " + mSearchedResult.size() );
                if (mSearchedResult.size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.youtube_search_no_result), Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < mSearchedResult.size(); i++) {
                    mSearchedVideoIdList.add(mSearchedResult.get(i).getId().getVideoId());
                }
            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: " );

                initYoutubePlayerView();
                initVIew();
            }
        }));
    }

    private void initYoutubePlayerView() {
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        youTubePlayerFragment.initialize(getResources().getString(R.string.YOUTUBE_API_KEY), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

                if (!wasRestored) {
                    mYouTubePlayer = youTubePlayer;
                    youTubePlayer.cueVideos(mSearchedVideoIdList);
                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {
                            Log.e(TAG, "onLoading: ");
                        }

                        @Override
                        public void onLoaded(String s) {
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {
                            // play next video when the playing one is ended
                            playYoutubeVideo(mPlayingIndex + 1);
                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {
                            Log.e(TAG, "onError: errorReason: " + errorReason.toString() + ", videoId: " + mSearchedResult.get(mPlayingIndex).getId().getVideoId());
                            // play next video when error happened
                            playYoutubeVideo(mPlayingIndex + 1);
                        }
                    });
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    Log.e(TAG, "onInitializationFailure: errorReason.isUserRecoverableError() == true");
                } else {
                    String errorMessage = String.format(getString(R.string.init_youtubeplayer_error), youTubeInitializationResult.toString());
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }

        });
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        if (mSearchedResult.size() != 0) {
            mSearchedResult.get(mPlayingIndex).setPlaying(true);
        }
    }

    private void initVIew() {
        Log.e(TAG, "initVIew: " );

        mYoutubeSearchResultAdapter = new YoutubeSearchResultAdapter(mSearchedResult, mSearchedResultListener);
        mRecommendSearchResultRecyclerView.setAdapter(mYoutubeSearchResultAdapter);
        mRecommendSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendSearchResultRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        hideLoadingProgressBar();
    }

    private void playYoutubeVideo(int position) {
        Log.e(TAG, "playYoutubeVideo: position: " + position);

        if (position < mSearchedVideoIdList.size()) {
            Log.e(TAG, "playYoutubeVideo: mPlayingIndex: "+ mPlayingIndex + ", position: " + position);
            changeNowPlayingVideoBackground(position);
            mYouTubePlayer.cueVideos(mSearchedVideoIdList, mPlayingIndex ,0);
        }
    }

    private void changeNowPlayingVideoBackground(int position) {
        Log.e(TAG, "changeNowPlayingVideoBackground: mPlayingIndex: " + mPlayingIndex + ", position: " + position );
        mSearchedResult.get(mPlayingIndex).setPlaying(false);
        mSearchedResult.get(position).setPlaying(true);
        mYoutubeSearchResultAdapter.updateData(mSearchedResult);
        mPlayingIndex = position;
    }
}
