package com.kj.kevin.hitsmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/28.
 */

public class ArtistInfo implements Parcelable {
    private String name;
    private List<ImageInfo> images;

    protected ArtistInfo(Parcel in) {
        name = in.readString();
        images = new ArrayList<>();
        in.readList(images, ImageInfo.class.getClassLoader());
    }

    public static final Creator<ArtistInfo> CREATOR = new Creator<ArtistInfo>() {
        @Override
        public ArtistInfo createFromParcel(Parcel in) {
            return new ArtistInfo(in);
        }

        @Override
        public ArtistInfo[] newArray(int size) {
            return new ArtistInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(images);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
