package com.kj.kevin.hitsmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.KKboxPlayListFragmentKKbox;
import com.kj.kevin.hitsmusic.model.ImageInfo;
import com.kj.kevin.hitsmusic.model.PlayListInfo;

import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/7.
 */

public class KKboxPlayListAdapter extends RecyclerView.Adapter<KKboxPlayListAdapter.ViewHolder> {
    public static final String TAG = "KKboxPlayListAdapter";

    private List<PlayListInfo> mData;
    private KKboxPlayListFragmentKKbox.OnPlayListClickedListener mPlayListClickedListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView img;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            img = itemView.findViewById(R.id.img);
        }
    }

    public KKboxPlayListAdapter(List<PlayListInfo> list, KKboxPlayListFragmentKKbox.OnPlayListClickedListener listener) {
        mData = list;
        mPlayListClickedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");

        final PlayListInfo playListInfo = mData.get(position);

        holder.title.setText(playListInfo.getTitle());
        holder.description.setText(playListInfo.getDescription());

        List<ImageInfo> imageInfoList = playListInfo.getImages();

        if (imageInfoList != null && imageInfoList.get(0) != null) {
            ImageInfo imageInfo = imageInfoList.get(0);
            Glide.with(holder.img.getContext()).load(imageInfo.getUrl()).into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: title: " + playListInfo.getTitle());
                mPlayListClickedListener.onPlayListClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
