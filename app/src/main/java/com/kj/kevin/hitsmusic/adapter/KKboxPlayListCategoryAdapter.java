package com.kj.kevin.hitsmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.KKboxPlayListCategoryFragment;

import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/25.
 */

public class KKboxPlayListCategoryAdapter extends RecyclerView.Adapter<KKboxPlayListCategoryAdapter.ViewHolder> {

    private List<Integer> mCategoryResIdList;
    private KKboxPlayListCategoryFragment.OnCategoryClickedListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    public KKboxPlayListCategoryAdapter(List<Integer> list, KKboxPlayListCategoryFragment.OnCategoryClickedListener listener) {
        mCategoryResIdList = list;
        mListener = listener;
    }

    @Override
    public KKboxPlayListCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KKboxPlayListCategoryAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCategoryClicked(position);
            }
        });
        holder.title.setText(holder.title.getContext().getText(mCategoryResIdList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mCategoryResIdList.size();
    }
}
