package task.sololearn.com.task;

import android.app.Application;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import io.realm.Realm;
import task.sololearn.com.task.helpers.NetworkHelper;
import task.sololearn.com.task.services.TimerJobService;
import task.sololearn.com.task.utils.AppLifecycleObserver;
import task.sololearn.com.task.utils.Constants;
import task.sololearn.com.task.utils.PrefManager;

public class TaskApplication extends Application implements LifecycleObserver {
    private static FirebaseJobDispatcher dispatcher;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        appContext = this;
        Realm.init(this);
        PrefManager.init(this);
        NetworkHelper.init();
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
        startTimer();

    }


    public static Context getContext() {
        return appContext;
    }

    public static void startTimer() {
        Job myJob = dispatcher.newJobBuilder()
                .setService(TimerJobService.class)
                .setTrigger(Trigger.executionWindow(Constants.Timer.SECONDS, Constants.Timer.SECONDS + 1))
                .setTag("db-update-job")
                .setLifetime(Lifetime.FOREVER)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        dispatcher.mustSchedule(myJob);

    }

}
