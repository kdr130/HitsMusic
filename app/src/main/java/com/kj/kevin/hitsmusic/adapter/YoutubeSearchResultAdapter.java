package com.kj.kevin.hitsmusic.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.YoutubeRelatedSongPlayerFragment;
import com.kj.kevin.hitsmusic.model.YoutubeSearchResult;

import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/6/1.
 */

public class YoutubeSearchResultAdapter extends RecyclerView.Adapter<YoutubeSearchResultAdapter.ViewHolder>{

    private List<YoutubeSearchResult.YoutubeItem> mData;
    private YoutubeRelatedSongPlayerFragment.OnSearchedResultClickedListener mSearchedResultListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title;
        private TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }

    public YoutubeSearchResultAdapter(List<YoutubeSearchResult.YoutubeItem> data, YoutubeRelatedSongPlayerFragment.OnSearchedResultClickedListener searchedResultListener) {
        mData = data;
        mSearchedResultListener = searchedResultListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube_search_result, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        YoutubeSearchResult.YoutubeItem youtubeItem = mData.get(position);

        if (youtubeItem.isPlaying()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorYouTubePlayingBg));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorYouTubeNotPlayingBg));
        }

        Glide.with(holder.itemView.getContext()).load(youtubeItem.getSnippet().getThumbnails().getDefaultThumbnail().getUrl()).into(holder.img);

        holder.title.setText(youtubeItem.getSnippet().getTitle());
        holder.description.setText(youtubeItem.getSnippet().getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchedResultListener.onSearchResultClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<YoutubeSearchResult.YoutubeItem> newData) {
        mData = newData;
        notifyDataSetChanged();
    }

}
