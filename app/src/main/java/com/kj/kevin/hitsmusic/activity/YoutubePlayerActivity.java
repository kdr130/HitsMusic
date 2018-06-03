package com.kj.kevin.hitsmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.adapter.YoutubeSearchResultAdapter;
import com.kj.kevin.hitsmusic.model.SongInfo;
import com.kj.kevin.hitsmusic.model.YoutubeSearchResult;

import java.util.ArrayList;
import java.util.List;

import static com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragmentKKbox.SONG_BUNDLE;
import static com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragmentKKbox.SONG_DATA;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "YoutubePlayerActivity";

    /*
    GET
    https://www.googleapis.com/youtube/v3/search?key=AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w&part=snippet&q=五月天&maxResults=10
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        YOUTUBE_API_KEY = getResources().getString(R.string.YOUTUBE_API_KEY);

        getInputSongData();

        mRecommendSearchResultRecyclerView = findViewById(R.id.recommend_search_result);

        initVIew();
    }

    private void getInputSongData() {
        Intent input = getIntent();

        SongInfo songData = null;
        if (input.getBundleExtra(SONG_BUNDLE).getParcelable(SONG_DATA) != null) {
            songData = input.getBundleExtra(SONG_BUNDLE).getParcelable(SONG_DATA);
        }

        if (songData != null) {
            Log.e(TAG, "onCreate: mSongData: " + songData.toString() );

            String searchKeyword1 = songData.getName();
            String searchKeyword2 = songData.getAlbum().getArtist().getName();
            String searchKeyword = searchKeyword1 + " " + searchKeyword2;

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
                    Toast.makeText(YoutubePlayerActivity.this, getString(R.string.youtube_search_no_result), Toast.LENGTH_SHORT).show();
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
        YouTubePlayerView youTubeView = findViewById(R.id.youtube_player);
        youTubeView.initialize(YOUTUBE_API_KEY, YoutubePlayerActivity.this);

        if (mSearchedResult.size() != 0) {
            mSearchedResult.get(mPlayingIndex).setPlaying(true);
        }
    }

    private void initVIew() {
        mYoutubeSearchResultAdapter = new YoutubeSearchResultAdapter(mSearchedResult, mSearchedResultListener);
        mRecommendSearchResultRecyclerView.setAdapter(mYoutubeSearchResultAdapter);
        mRecommendSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecommendSearchResultRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
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
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            Log.e(TAG, "onInitializationFailure: errorReason.isUserRecoverableError() == true");
        } else {
            String errorMessage = String.format(getString(R.string.init_youtuberplayer_error), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
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
