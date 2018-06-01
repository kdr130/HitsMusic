package com.kj.kevin.hitsmusic.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kevin on 2018/5/31.
 */

public class YoutubeSearchResult {
    public class YoutubeItemId {
        private String kind;
        private String videoId;

        public String getKind() {
            return kind;
        }

        public String getVideoId() {
            return videoId;
        }
    }

    public class YoutubeThumbnail {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public class YoutubeThumbnails {
        @SerializedName("default")
        private YoutubeThumbnail defaultThumbnail;
        private YoutubeThumbnail medium;
        private YoutubeThumbnail high;

        public YoutubeThumbnail getDefaultThumbnail() {
            return defaultThumbnail;
        }

        public YoutubeThumbnail getMedium() {
            return medium;
        }

        public YoutubeThumbnail getHigh() {
            return high;
        }
    }

    public class YoutubeItemSnippet {
        private String title;
        private String description;
        private YoutubeThumbnails thumbnails;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public YoutubeThumbnails getThumbnails() {
            return thumbnails;
        }

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public class YoutubeItem {
        private YoutubeItemId id;
        private YoutubeItemSnippet snippet;
        private boolean isPlaying = false;

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public YoutubeItemId getId() {
            return id;
        }

        public YoutubeItemSnippet getSnippet() {
            return snippet;
        }

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public List<YoutubeItem> getItems() {
        return items;
    }

    private List<YoutubeItem> items;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
