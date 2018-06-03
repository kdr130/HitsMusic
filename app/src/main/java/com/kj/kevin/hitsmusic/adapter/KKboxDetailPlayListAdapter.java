package com.kj.kevin.hitsmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragmentKKbox;
import com.kj.kevin.hitsmusic.model.ImageInfo;
import com.kj.kevin.hitsmusic.model.SongInfo;

import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/8.
 */

public class KKboxDetailPlayListAdapter extends RecyclerView.Adapter<KKboxDetailPlayListAdapter.ViewHolder> {

    private List<SongInfo> mSongList;
    private KKboxDetailPlayListFragmentKKbox.OnSongClickedListener mListener;

    public KKboxDetailPlayListAdapter(List<SongInfo> data, KKboxDetailPlayListFragmentKKbox.OnSongClickedListener listener) {
        mSongList = data;
        mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView artist;
        private TextView name;
        private ImageView img;

        ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            artist = itemView.findViewById(R.id.artist);
            name = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SongInfo songInfo = mSongList.get(position);

        if (songInfo == null) {
            return;
        }

        if (songInfo.getAlbum() != null
                && songInfo.getAlbum().getImages() != null
                && songInfo.getAlbum().getImages().size() != 0
                && songInfo.getAlbum().getImages().get(0) != null) {

            ImageInfo imageInfo = songInfo.getAlbum().getImages().get(0);

            Glide.with(holder.itemView.getContext()).load(imageInfo.getUrl()).into(holder.img);
        }

        holder.name.setText(songInfo.getName());
        holder.artist.setText(songInfo.getAlbum().getArtist().getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSongClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
