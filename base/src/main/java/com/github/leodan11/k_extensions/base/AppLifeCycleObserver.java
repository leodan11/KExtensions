package com.github.leodan11.k_extensions.base;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * USAGE
 * <pre>
 *  {@code
 *     val appLifeCycleObserver = AppLifeCycleObserver()
 *
 *     lifecycle.addObserver(appLifeCycleObserver)
 *
 *     appLifeCycleObserver.lifeCycleCallback = object : LifeCycleCallback {
 *         override fun appInBackground() {
 *             super.appInBackground()
 *             //Do something
 *         }
 *
 *         override fun appInForeground() {
 *             super.appInForeground()
 *             //Do something
 *         }
 *     }
 *  }
 * </pre>
 *
 * @noinspection deprecation
 */
public class AppLifeCycleObserver implements LifecycleObserver {

    public LifeCycleCallBacks lifeCycleCallback;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        if (lifeCycleCallback != null) {
            lifeCycleCallback.appInForeground();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        if (lifeCycleCallback != null) {
            lifeCycleCallback.appInBackground();
        }
    }

}
