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
    public static final String SONG_LIST = "song_list";

    private static String YOUTUBE_API_KEY;

    private List<SongInfo> mInputSongList;
    private List<String> mSearchedVideoIdList = new ArrayList<>();
    private List<YoutubeSearchResult.YoutubeItem> mSearchedResult = new ArrayList<>();
    private RecyclerView mRecommendSearchResultRecyclerView;
    private YoutubeSearchResultAdapter mYoutubeSearchResultAdapter;
    private YouTubePlayer mYouTubePlayer;
    private int mPlayingIndex = 0;
    private boolean mIsFirst = false;
    private int mLoadedIndex = 0;
    private boolean mCanSearchMore = true;

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

    public static YoutubeRelatedSongPlayerFragment newInstance(ArrayList<SongInfo> songList) {
        YoutubeRelatedSongPlayerFragment fragment = new YoutubeRelatedSongPlayerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SONG_LIST, songList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YOUTUBE_API_KEY = getResources().getString(R.string.YOUTUBE_API_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.youtube_relatedsong_player_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");

        showLoadingProgressBar();

        mRecommendSearchResultRecyclerView = view.findViewById(R.id.recommend_search_result);

        if (mSearchedVideoIdList != null) {
            initVIew();
        }
    }


    public void getData() {
        if (mSearchedVideoIdList != null && mYouTubePlayer != null) {
            initVIew();
            return;
        }

        if(getArguments() == null && getArguments().getParcelable(SONG_LIST) == null) {
            return;
        }

        mInputSongList = getArguments().getParcelableArrayList(SONG_LIST);

        if (mInputSongList != null) {
            if (mInputSongList.size() == 1) {
                Log.e(TAG, "onCreate: mSongData: " + mInputSongList.toString() );

                String songName = mInputSongList.get(0).getName();
                // remove (English Song Name) to improve search result
                int leftPara = songName.indexOf(" (");
                if (leftPara > 0) {
                    songName = songName.substring(0, leftPara);
                }

                String artistName = mInputSongList.get(0).getAlbum().getArtist().getName();
                String searchKeyword = songName + " " + artistName;

                searchYoutube(searchKeyword);
            } else {
                searchYoutube(mInputSongList,mLoadedIndex ,mLoadedIndex+5);
            }
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

    private void searchYoutube(List<SongInfo> list, int start, int offset) {
        if (!mCanSearchMore) {
            return;
        }
        mCanSearchMore = false;

        Log.e(TAG, "searchYoutube: " );
        if (offset > list.size()) {
            offset = list.size();
        }
        List<SongInfo> subList = new ArrayList<>(list.subList(start, offset));
        mLoadedIndex = offset;

        ApiMethods.getPlaylistYoutubeSearchResult(YOUTUBE_API_KEY, subList, new MyObserver<>("getPlaylistYoutubeSearchResult", new MyObserver.MyObserverNextListener<YoutubeSearchResult>() {
            @Override
            public void onNext(YoutubeSearchResult searchResult) {
                List<YoutubeSearchResult.YoutubeItem> resultItem = searchResult.getItems();
                Log.e(TAG, "onNext: mSearchedResult.size: " + resultItem.size() );
                if (resultItem.size() == 0) {
                    Log.e(TAG, "onNext: search no related video " );
                } else {
                    mSearchedResult.addAll(searchResult.getItems());
                }

                for (int i = 0; i < resultItem.size(); i++) {
                    Log.e(TAG, "onNext: VideoId: " +  resultItem.get(i).getId().getVideoId());
                    mSearchedVideoIdList.add(resultItem.get(i).getId().getVideoId());
                }
                Log.e(TAG, "onNext: searchedVideoIdList.size: " + mSearchedVideoIdList.size());

            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: " );
                if (!mIsFirst) {
                    initYoutubePlayerView();
                    initVIew();
                    mIsFirst = true;
                } else {
                    if (isAdded()) {
                        Log.e(TAG, "onComplete: isAdded");
                        mYouTubePlayer.loadVideos(mSearchedVideoIdList);
                        mYoutubeSearchResultAdapter.notifyDataSetChanged();
                        hideLoadingProgressBar();
                    } else {
                        Log.e(TAG, "onComplete: isAdded false");
                    }
                }
                mCanSearchMore = true;
            }
        }));

    }

    private void initYoutubePlayerView() {
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if (!isAdded()) {
            Log.e(TAG, "initYoutubePlayerView: isAdd == false");
            return;
        }
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
        hideLoadingProgressBar();
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

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecommendSearchResultRecyclerView.setLayoutManager(layoutManager);
        mRecommendSearchResultRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mRecommendSearchResultRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(layoutManager.findLastVisibleItemPosition() == mSearchedResult.size() - 1){
                    Log.e(TAG, "onScrollStateChanged: bottom! mLoadedIndex: " + mLoadedIndex);
                    if (mLoadedIndex < mInputSongList.size()) {
                        showLoadingProgressBar();
                        searchYoutube(mInputSongList, mLoadedIndex, mLoadedIndex + 5);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
