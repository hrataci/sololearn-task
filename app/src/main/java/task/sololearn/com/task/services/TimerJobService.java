package task.sololearn.com.task.services;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import task.sololearn.com.task.TaskApplication;
import task.sololearn.com.task.helpers.NetworkHelper;

public class TimerJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        NetworkHelper.getInst().doRequest(false);
        TaskApplication.startTimer();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }


}