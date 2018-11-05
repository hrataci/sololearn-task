package task.sololearn.com.task;

import android.app.Application;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ProcessLifecycleOwner;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import task.sololearn.com.task.utils.AppLifecycleObserver;
import task.sololearn.com.task.utils.PrefManager;

public class TaskApplication extends Application implements LifecycleObserver {
    static FirebaseJobDispatcher dispatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        PrefManager.init(this);
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleObserver(new AppLifecycleObserver.Callbacks() {

            @Override
            public void onAppGoesForeground() {
                PrefManager.getInstance().setIsBackgrounded(false);
            }

            @Override
            public void onAppGoesBackground() {
                PrefManager.getInstance().setIsBackgrounded(true);
            }
        }));
    }

}
