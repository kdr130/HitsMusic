package com.kj.kevin.hitsmusic.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;

import com.kj.kevin.hitsmusic.activity.KKboxActivity;

/**
 * Created by Kevin on 2018/6/2.
 */

public class KKboxBaseFragment extends Fragment {
    protected boolean mIsDataLoaded = false;
    protected Handler mJobHandler = null;



    protected void showLoadingProgressBar() {
        ((KKboxActivity)getActivity()).showLoadingProgressBar();
    }

    protected void hideLoadingProgressBar() {
        ((KKboxActivity)getActivity()).hideLoadingProgressBar();
    }
}
