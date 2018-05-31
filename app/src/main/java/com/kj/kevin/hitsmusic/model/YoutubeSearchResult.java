package com.kj.kevin.hitsmusic.model;

import com.google.gson.Gson;

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

    public class YoutubeItemSnippet {
        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
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
