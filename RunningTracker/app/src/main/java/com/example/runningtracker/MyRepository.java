package com.example.runningtracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository {

    private RunSessionDAO runSessionDAO;
    private LiveData<List<RunSession>> allRunSessions;

    MyRepository(Application application) {
        RunSessionDatabase db = RunSessionDatabase.getDatabase(application);
        runSessionDAO = db.runSessionDao();
        allRunSessions = runSessionDAO.getRunSession();
    }

    LiveData<List<RunSession>> getAllRunSessions() {
        return allRunSessions;
    }

    void insert(RunSession runSession) {
        RunSessionDatabase.databaseWriteExecutor.execute(() -> {
            runSessionDAO.insert(runSession);
        });
    }


}
