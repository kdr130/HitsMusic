package com.kj.kevin.hitsmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.kj.kevin.hitsmusic.ApiMethods;
import com.kj.kevin.hitsmusic.MyObserver;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.model.SongInfo;
import com.kj.kevin.hitsmusic.model.YoutubeSearchResult;

import java.util.ArrayList;
import java.util.List;

import static com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragment.SONG_BUNDLE;
import static com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragment.SONG_DATA;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "YoutubePlayerActivity";

    /*
    GET
    https://www.googleapis.com/youtube/v3/search?key=AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w&part=snippet&q=五月天&maxResults=10
     */
    private static final String YOUTUBE_API_KEY = "AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w";

    private SongInfo mSongData;
    private List<String> mSearchedVideoIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        Intent input = getIntent();


        if (input.getBundleExtra(SONG_BUNDLE).getParcelable(SONG_DATA) != null) {
            mSongData = input.getBundleExtra(SONG_BUNDLE).getParcelable(SONG_DATA);
        }

        Log.e(TAG, "onCreate: mSongData: " + mSongData.toString() );

        String searchKeyword1 = mSongData.getName();

        String searchKeyword2 = mSongData.getAlbum().getArtist().getName();

        String searchKeyword = searchKeyword1 + " " + searchKeyword2;

        Log.e(TAG, "onCreate: searchKeyword: " + searchKeyword );
        
        ApiMethods.getYoutubeSearchResult(YOUTUBE_API_KEY, searchKeyword, new MyObserver<YoutubeSearchResult>("getYoutubeSearchResult", new MyObserver.MyObserverNextListener<YoutubeSearchResult>() {
            @Override
            public void onNext(YoutubeSearchResult result) {
                Log.e(TAG, "onNext: " + result.getItems().get(0).getSnippet().toString() );

                List<YoutubeSearchResult.YoutubeItem> searchedResult = result.getItems();

                for (int i = 0; i < 5 && i < searchedResult.size(); i++) {
                    mSearchedVideoIdList.add(searchedResult.get(i).getId().getVideoId());
                }
            }
        }, new MyObserver.MyObserverCompleteListener() {
            @Override
            public void onComplete() {
                YouTubePlayerView youTubeView = findViewById(R.id.youtube_player);
                youTubeView.initialize(YOUTUBE_API_KEY, YoutubePlayerActivity.this);
            }
        }));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            final int[] playIndex = new int[1];
            playIndex[0] = 0;

            youTubePlayer.cueVideos(mSearchedVideoIdList);

            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    Log.e(TAG, "onLoading: ");
                    // avoid youtube player auto play video when onError is been triggered
                    youTubePlayer.pause();
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

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {
                    Log.e(TAG, "onError: errorReason: " + errorReason.toString() );
                    mSearchedVideoIdList.remove(0);
                    playIndex[0]++;
                    if (playIndex[0] < mSearchedVideoIdList.size()) {
                        youTubePlayer.cueVideos(mSearchedVideoIdList);

                    }

                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            Log.e(TAG, "onInitializationFailure: errorReason.isUserRecoverableError() == true");
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
