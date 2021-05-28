package com.example.runningtracker;


import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RunSessionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RunSession runSession);

    @Query("DELETE FROM run_session_table")
    void deleteAll();

    @Query("SELECT * FROM run_session_table")
    LiveData<List<RunSession>> getRunSession();
}


