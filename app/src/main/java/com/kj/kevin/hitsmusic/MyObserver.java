package com.kj.kevin.hitsmusic;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Kevinkj_Lin on 2018/5/22.
 */

public class MyObserver<T> implements Observer<T> {
    private static final String TAG = "MyObserver";

    private String mName;
    private MyObserverNextListener nextListener;

    public interface MyObserverNextListener<T> {
        void onNext(T t);
    }

    public MyObserver(String observerName, MyObserverNextListener listener) {
        mName = observerName;
        nextListener = listener;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        Log.e(TAG, "onNext: " + mName );
        nextListener.onNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.e(TAG, "onError: " +  mName + ", e: " + e.toString());
    }

    @Override
    public void onComplete() {

    }
}
