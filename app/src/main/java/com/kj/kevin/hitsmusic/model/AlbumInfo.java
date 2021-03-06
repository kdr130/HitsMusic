package com.kj.kevin.hitsmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/28.
 */

public class AlbumInfo implements Parcelable{
    private String name;
    private List<ImageInfo> images;
    private ArtistInfo artist;

    protected AlbumInfo(Parcel in) {
        name = in.readString();
        images = new ArrayList<>();
        in.readList(images, ImageInfo.class.getClassLoader());
        artist = in.readParcelable(ArtistInfo.class.getClassLoader());
    }

    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel in) {
            return new AlbumInfo(in);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    public ArtistInfo getArtist() {
        return artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(images);
        dest.writeParcelable(artist, flags);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
