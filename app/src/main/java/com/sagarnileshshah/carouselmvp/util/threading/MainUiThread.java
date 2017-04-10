package com.sagarnileshshah.carouselmvp.util.threading;

import android.os.Handler;
import android.os.Looper;

/**
 * A wrapper around {@link Handler} that helps to post work to the main UI thread.
 */
public class MainUiThread {

    private Handler handler;

    public MainUiThread() {
        handler = new Handler(Looper.getMainLooper());
    }


    public void post(Runnable runnable) {
        handler.post(runnable);
    }

}
