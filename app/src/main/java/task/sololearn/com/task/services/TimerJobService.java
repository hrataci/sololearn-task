package task.sololearn.com.task.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import task.sololearn.com.task.TaskApplication;

public class TimerJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d("TimerJobService","job dispatching");
        // Do some work here
        TaskApplication.startTimer();
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }


}