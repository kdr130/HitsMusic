package com.kj.kevin.hitsmusic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kj.kevin.hitsmusic.R;
import com.kj.kevin.hitsmusic.fragment.KKboxDetailPlayListFragment;
import com.kj.kevin.hitsmusic.model.Chart;

import java.util.List;

/**
 * Created by Kevinkj_Lin on 2018/5/7.
 */

public class KKboxPlayListAdapter extends RecyclerView.Adapter<KKboxPlayListAdapter.ViewHolder> {
    public static final String TAG = "KKboxPlayListAdapter";

    private List<Chart> data;

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

    public KKboxPlayListAdapter(List<Chart> list) {
        this.data = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");

        final Chart chart = data.get(position);

        holder.title.setText(chart.getTitle());
        holder.description.setText(chart.getDescription());

        Glide.with(holder.img.getContext()).load(chart.getImgUrl()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: title: " + chart.getTitle());
                gotoKKboxDetailPlayListFragment(v.getContext(), chart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void gotoKKboxDetailPlayListFragment(Context context, Chart chart) {
        Log.d(TAG, "gotoKKboxDetailPlayListFragment: ");

        KKboxDetailPlayListFragment fragment = KKboxDetailPlayListFragment.newInstance(chart.getId());

        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment).commit();
    }
}