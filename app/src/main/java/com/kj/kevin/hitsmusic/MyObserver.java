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
    private MyObserverNextListener mNextListener;
    private MyObserverCompleteListener mCompleteListener;

    public interface MyObserverNextListener<T> {
        void onNext(T t);
    }

    public interface MyObserverCompleteListener {
        void onComplete();
    }

    public MyObserver(String observerName, MyObserverNextListener nextListener) {
        this(observerName, nextListener, null);
    }

    public MyObserver(String observerName, MyObserverCompleteListener completeListener) {
        this(observerName, null, completeListener);
    }

    public MyObserver(String observerName, MyObserverNextListener nextListener, MyObserverCompleteListener completeListener) {
        mName = observerName;
        mNextListener = nextListener;
        mCompleteListener = completeListener;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        Log.e(TAG, "onNext: " + mName );
        if (mNextListener != null) {
            mNextListener.onNext(t);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.e(TAG, "onError: " +  mName + ", e: " + e.toString());
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: " );
        if (mCompleteListener != null) {
            mCompleteListener.onComplete();
        }
    }
}
