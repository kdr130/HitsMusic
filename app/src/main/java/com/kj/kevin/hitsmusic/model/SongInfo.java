package com.kj.kevin.hitsmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by Kevinkj_Lin on 2018/5/28.
 */

public class SongInfo implements Parcelable {
    private String name;
    private AlbumInfo album;

    protected SongInfo(Parcel in) {
        name = in.readString();
        album = in.readParcelable(AlbumInfo.class.getClassLoader());
    }

    public static final Creator<SongInfo> CREATOR = new Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public AlbumInfo getAlbum() {
        return album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(album, flags);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
