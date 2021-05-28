package com.example.runningtracker;

import android.app.Application;
import java.util.List;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RunSessionViewModel extends AndroidViewModel {

    private MyRepository repository;

    private LiveData<List<RunSession>> allRunSessions;

    public RunSessionViewModel(Application application) {
        super(application);
        repository = new MyRepository(application);
        allRunSessions = repository.getAllRunSessions();
    }

    LiveData<List<RunSession>> getAllRunSessions() { return allRunSessions; }

    public void insert(RunSession runSession) { repository.insert(runSession); }
}
