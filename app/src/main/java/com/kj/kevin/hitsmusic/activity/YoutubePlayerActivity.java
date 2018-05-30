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

import static com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragment.SONG_DATA;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "YoutubePlayerActivity";

    /*
    GET
    https://www.googleapis.com/youtube/v3/search?key=AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w&part=snippet&q=五月天&maxResults=10
     */
    private static final String YOUTUBE_API_KEY = "AIzaSyB91k7jLqrzqHOKVxcsbPvTor6aTW1OX2w";

    private SongInfo mSongData;
    private String mSearchedVideoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        Intent input = getIntent();
        if (input.getParcelableExtra(SONG_DATA) != null) {
            mSongData = input.getParcelableExtra(SONG_DATA);
        }

        String nameOfSong = mSongData.getName();

        ApiMethods.getYoutubeSearchResult(YOUTUBE_API_KEY, nameOfSong, new MyObserver<YoutubeSearchResult>("getYoutubeSearchResult", new MyObserver.MyObserverNextListener<YoutubeSearchResult>() {
            @Override
            public void onNext(YoutubeSearchResult result) {
                Log.e(TAG, "onNext: " + result.getItems().get(0).getId() );

                mSearchedVideoId = result.getItems().get(0).getId().getVideoId();
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            Log.e(TAG, "onInitializationSuccess: mSearchedVideoId: " + mSearchedVideoId );
            youTubePlayer.cueVideo(mSearchedVideoId);
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
