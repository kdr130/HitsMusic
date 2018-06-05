package com.kj.kevin.hitsmusic.fragment;

import android.content.Intent;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.kj.kevin.hitsmusic.HitsMusic;
import com.kj.kevin.hitsmusic.JobSchedulerService;
import com.kj.kevin.hitsmusic.activity.KKboxActivity;

import static com.kj.kevin.hitsmusic.activity.KKboxActivity.MESSENGER_INTENT_KEY;

/**
 * Created by Kevin on 2018/6/2.
 */

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    protected void showLoadingProgressBar() {
        ((KKboxActivity)getActivity()).showLoadingProgressBar();
    }

    protected void hideLoadingProgressBar() {
        ((KKboxActivity)getActivity()).hideLoadingProgressBar();
    }

    public void getData() {
        Log.e(TAG, "getData: ");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (HitsMusic.isMobileNetworkAvailable(getActivity())) {
            Log.e(TAG, "onResume: Mobile Network Is Available" );
        } else {
            Log.e(TAG, "onResume: Mobile Network Is Not Available" );
            if(isVisible()) {
//                HitsMusic.showNetworkNotAvailableDialog(getActivity());
                Toast.makeText(getContext(), "目前沒有網路，請開啟網路再重試", Toast.LENGTH_SHORT).show();
            }
        }
        ((KKboxActivity)getActivity()).scheduleJob();

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG, "onStart: ");

        Intent startJobServiceIntent = new Intent(getActivity(), JobSchedulerService.class);
        Messenger messengerIncoming = new Messenger(((KKboxActivity)getActivity()).getJobHandler());
        startJobServiceIntent.putExtra(MESSENGER_INTENT_KEY, messengerIncoming);
        getActivity().startService(startJobServiceIntent);
    }

    @Override
    public void onStop() {
        getActivity().stopService(new Intent(getActivity(), JobSchedulerService.class));
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    public void setActionBarTitle(String title) {
        Log.e(TAG, "setActionBarTitle: title: " + title );
        ((KKboxActivity)getActivity()).setActionBarTitle(title);
    }
}
