package task.sololearn.com.task.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Build;

public class AppLifecycleObserver implements LifecycleObserver {

        private Callbacks callbacks;

        public AppLifecycleObserver(Callbacks callbacks) {
            this.callbacks = callbacks;
        }

        public interface Callbacks {
            void onAppGoesForeground();

            void onAppGoesBackground();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void onAppStart(LifecycleOwner source) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                callbacks.onAppGoesForeground();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void onAppResume(LifecycleOwner source) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                callbacks.onAppGoesForeground();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void onAppPause(LifecycleOwner source) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                callbacks.onAppGoesBackground();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void onAppStop(LifecycleOwner source) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                callbacks.onAppGoesBackground();
            }
        }

    }