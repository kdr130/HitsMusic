package com.kj.kevin.hitsmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/7.
 */

public class PlayListInfo implements Parcelable {
    private String id;
    private String title;
    private String description;
    private List<ImageInfo> images;
    private String url;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getChartUrl() {
        return url;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    protected PlayListInfo(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();

        images = new ArrayList<>();
        in.readList(images, null);

        url = in.readString();
    }

    public static final Creator<PlayListInfo> CREATOR = new Creator<PlayListInfo>() {
        @Override
        public PlayListInfo createFromParcel(Parcel in) {
            return new PlayListInfo(in);
        }

        @Override
        public PlayListInfo[] newArray(int size) {
            return new PlayListInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeList(images);
        parcel.writeString(url);
    }
}
